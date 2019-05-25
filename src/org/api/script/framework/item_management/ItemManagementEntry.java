package org.api.script.framework.item_management;

import org.api.game.pricechecking.PriceCheck;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.io.IOException;
import java.util.function.BooleanSupplier;

public class ItemManagementEntry {

    private final int id;
    private final int quantity;
    private GoalList goals;
    private BooleanSupplier goalOverride;
    private Mission mission;

    public ItemManagementEntry(Mission mission, int id, int quantity, GoalList goals) {
        this(mission, id, quantity, goals, null);
    }

    public ItemManagementEntry(Mission mission, int id, int quantity, BooleanSupplier goalOverride) {
        this(mission, id, quantity, null, goalOverride);
    }

    public ItemManagementEntry(Mission mission, int id, int quantity, GoalList goals, BooleanSupplier goalOverride) {
        this.mission = mission;
        this.id = id;
        this.quantity = quantity;
        this.goals = goals;
        this.goalOverride = goalOverride;
    }

    /**
     * Determines whether the goal should be overridden by the goal override boolean supplier.
     *
     * @return True if the goal should be overridden; false otherwise.
     */
    private boolean shouldOverride() {
        if (goals != null && goalOverride != null)
            return goals.hasReachedGoals() && goalOverride.getAsBoolean();

        return goals != null ? goals.hasReachedGoals() && !playerHasEntry() : goalOverride != null && goalOverride.getAsBoolean();
    }

    /**
     * Determines whether the player can buy the specified entry.
     *
     * @param totalValue       The total value of gold and sellables the player has taking into account the sell price
     *                         modifier.
     * @param buyPriceModifier The buy price modifier.
     * @return True if the entry can be bought; false otherwise.
     */
    boolean canBuy(long totalValue, double buyPriceModifier) {
        return !mission.getScript().getBankCache().getBankCache().isEmpty() && totalValue >= getValueNeeded(buyPriceModifier) && shouldOverride();
    }

    /**
     * Determines whether the player already has the entry in their inventory, bank, or equipment.
     *
     * @return True if the player has the entry; false otherwise.
     */
    private boolean playerHasEntry() {
        final boolean inInventory = Inventory.contains(id, id + 1);
        final boolean inEquipment = Equipment.contains(id);
        final boolean inBank = mission.getScript().getBankCache().getBankCache().containsKey(id);

        return inInventory || inEquipment || inBank;
    }

    /**
     * Gets the item id.
     *
     * @return The item id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the value needed to buy the item taking into consideration the price, quantity, and buy price modifier.
     *
     * @param buyPriceModifier The buy price modifier.
     * @return The value needed to buy the item; 0 otherwise.
     */
    public int getValueNeeded(double buyPriceModifier) {
        if (id == ItemManagementTracker.GOLD_ID)
            return quantity;

        try {
            return (int) (PriceCheck.getOSBuddyPrice(id) * quantity * buyPriceModifier);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Gets the quantity of item to buy.
     *
     * @return The quantity of item to buy.
     */
    public int getQuantity() {
        return quantity;
    }
}
