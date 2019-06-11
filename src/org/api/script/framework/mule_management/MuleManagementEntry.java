package org.api.script.framework.mule_management;

import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class MuleManagementEntry {

    private final Predicate<Item> item;
    private GoalList goals;
    private BooleanSupplier goalOverride;
    private Mission mission;

    private MuleManager muleManager;

    public MuleManagementEntry(Mission mission, Predicate<Item> item, GoalList goals) {
        this(mission, item, goals, null);
    }

    public MuleManagementEntry(Mission mission, Predicate<Item> item, BooleanSupplier goalOverride) {
        this(mission, item, null, goalOverride);
    }

    public MuleManagementEntry(Mission mission, Predicate<Item> item, GoalList goals, BooleanSupplier goalOverride) {
        this.mission = mission;
        this.item = item;
        this.goals = goals;
        this.goalOverride = goalOverride;
    }

    /**
     * Determines whether the player can mule.
     *
     * @return True if the player can mule; false otherwise.
     */
    public boolean canMule() {
        return shouldOverride();
    }

    /**
     * Determines whether the goal should be overridden by the goal override boolean supplier.
     *
     * @return True if the goal should be overridden; false otherwise.
     */
    private boolean shouldOverride() {
        return goalOverride != null ? goalOverride.getAsBoolean() && playerHasEntry() : goals != null && (goals.hasReachedGoals() && playerHasEntry());
    }

    /**
     * Determines whether the player already has the entry in their inventory or bank.
     *
     * @return True if the player has the entry; false otherwise.
     */
    private boolean playerHasEntry() {
        final int inventoryAmount = Inventory.getCount(true, item);
        final Item bankItem = mission.getScript().getBankCache().getItem(item);
        if (bankItem == null)
            return false;

        return inventoryAmount + bankItem.getStackSize() > 0;
    }

    /**
     * Gets the item predicate.
     *
     * @return The item predicate.
     */
    public Predicate<Item> getItem() {
        return item;
    }

    public MuleManager getMuleManager() {
        return muleManager;
    }

    public void setMuleManager(MuleManager muleManager) {
        this.muleManager = muleManager;
    }
}
