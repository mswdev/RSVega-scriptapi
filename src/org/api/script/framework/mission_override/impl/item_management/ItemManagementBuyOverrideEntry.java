package org.api.script.framework.mission_override.impl.item_management;

import org.api.game.pricechecking.PriceCheck;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.MissionOverrideFactory;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class ItemManagementBuyOverrideEntry extends MissionOverrideFactory {

    public static final String GOLD_COINS_NAME = "Coins";
    private final String name;
    private final int quantity;
    private final double buyPriceModifier;
    private final ItemManagementSellOverrideEntry[] itemManagementSellOverrideEntries;

    public ItemManagementBuyOverrideEntry(Mission mission, String name, int quantity, double buyPriceModifier, GoalList goalList, ItemManagementSellOverrideEntry[] itemManagementSellOverrideEntries) {
        this(mission, name, quantity, buyPriceModifier, goalList, null, itemManagementSellOverrideEntries);
    }

    public ItemManagementBuyOverrideEntry(Mission mission, String name, int quantity, double buyPriceModifier, BooleanSupplier goalOverride, ItemManagementSellOverrideEntry[] itemManagementSellOverrideEntries) {
        this(mission, name, quantity, buyPriceModifier, null, goalOverride, itemManagementSellOverrideEntries);
    }

    public ItemManagementBuyOverrideEntry(Mission mission, String name, int quantity, double buyPriceModifier, GoalList goalList, BooleanSupplier goalOverride, ItemManagementSellOverrideEntry[] itemManagementSellOverrideEntries) {
        super(mission, goalList, goalOverride);
        this.name = name;
        this.quantity = quantity;
        this.buyPriceModifier = buyPriceModifier;
        this.itemManagementSellOverrideEntries = itemManagementSellOverrideEntries;
    }

    @Override
    public Mission getOverrideMission() {
        return determineBuyOrSellMission();
    }

    @Override
    public boolean entryIsReady() {
        final boolean inInventory = Inventory.contains(getName());
        final boolean inEquipment = Equipment.contains(getName());
        final boolean inBank = getMission().getScript().getBankCache().getItem(a -> a.getName().equals(getName())) != null;
        if (inInventory || inEquipment || inBank)
            return false;

        return determineBuyOrSellMission() != null;
    }

    private Mission determineBuyOrSellMission() {
        if (getCurrentGold() + getSellableGold() < getTotalGoldNeeded())
            return null;

        if (getCurrentGold() >= getTotalGoldNeeded())
            return new ItemManagementMission(getMission().getScript(), this);

        final ItemManagementSellOverrideEntry itemManagementSellOverrideEntry = Arrays.stream(getItemManagementSellOverrideEntries()).filter(a -> a.entryIsReady() && a.canOverride()).findFirst().orElse(null);
        if (itemManagementSellOverrideEntry == null)
            return null;

        return new ItemManagementMission(getMission().getScript(), itemManagementSellOverrideEntry);
    }

    private int getCurrentGold() {
        final int inInventory = Inventory.getCount(true, GOLD_COINS_NAME);
        final Item item = getMission().getScript().getBankCache().getItem(a -> a.getName().equals(ItemManagementBuyOverrideEntry.GOLD_COINS_NAME));
        int inBank = 0;
        if (item != null)
            inBank += item.getStackSize();

        return inInventory + inBank;
    }

    private int getSellableGold() {
        return Arrays.stream(getItemManagementSellOverrideEntries()).filter(a -> a.entryIsReady() && a.canOverride()).mapToInt(ItemManagementSellOverrideEntry::getTotalSellableGold).sum();
    }

    /**
     * Gets the total gold needed to buy the item taking into consideration the price, quantity, and buy price
     * modifier.
     *
     * @return The total gold needed to buy the item; 0 otherwise.
     */
    public int getTotalGoldNeeded() {
        if (getName().equals(ItemManagementBuyOverrideEntry.GOLD_COINS_NAME))
            return getQuantity();

        return getItemBuyPrice() * getQuantity();
    }

    public int getItemBuyPrice() {
        try {
            return (int) (PriceCheck.getOSBuddyPrice(getName()) * getBuyPriceModifier());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    private double getBuyPriceModifier() {
        return buyPriceModifier;
    }

    private ItemManagementSellOverrideEntry[] getItemManagementSellOverrideEntries() {
        return itemManagementSellOverrideEntries;
    }
}
