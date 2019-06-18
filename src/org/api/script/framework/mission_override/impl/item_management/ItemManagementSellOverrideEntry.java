package org.api.script.framework.mission_override.impl.item_management;

import org.api.game.pricechecking.PriceCheck;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.MissionOverrideFactory;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.io.IOException;
import java.util.function.BooleanSupplier;

public class ItemManagementSellOverrideEntry extends MissionOverrideFactory {

    private final String name;
    private final double sellPriceModifier;

    public ItemManagementSellOverrideEntry(Mission mission, String name, double sellPriceModifier, GoalList goalList) {
        this(mission, name, sellPriceModifier, goalList, null);
    }

    public ItemManagementSellOverrideEntry(Mission mission, String name, double sellPriceModifier, BooleanSupplier goalOverride) {
        this(mission, name, sellPriceModifier, null, goalOverride);
    }

    public ItemManagementSellOverrideEntry(Mission mission, String name, double sellPriceModifier, GoalList goalList, BooleanSupplier goalOverride) {
        super(mission, goalList, goalOverride);
        this.name = name;
        this.sellPriceModifier = sellPriceModifier;
    }

    public int getTotalSellableGold() {
        final long inventoryAmount = Inventory.getCount(true, getName());
        final Item item = getMission().getScript().getBankCache().getItem(a -> a.getName().equals(getName()));
        int bankAmount = 0;
        if (item != null)
            bankAmount += item.getStackSize();

        return inventoryAmount + bankAmount > 0 ? (int) ((inventoryAmount + bankAmount) * getItemSellPrice()) : getItemSellPrice();
    }

    public int getItemSellPrice() {
        try {
            return (int) (PriceCheck.getOSBuddyPrice(getName()) * getSellPriceModifier());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Mission getOverrideMission() {
        return null;
    }

    @Override
    public boolean entryIsReady() {
        return getTotalSellableGold() > 0;
    }

    public String getName() {
        return name;
    }

    private double getSellPriceModifier() {
        return sellPriceModifier;
    }
}
