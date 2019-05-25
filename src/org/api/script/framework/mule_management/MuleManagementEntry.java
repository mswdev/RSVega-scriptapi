package org.api.script.framework.mule_management;

import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.BooleanSupplier;

public class MuleManagementEntry {

    private final int id;
    private GoalList goals;
    private BooleanSupplier goalOverride;
    private Mission mission;

    private MuleManager muleManager;

    public MuleManagementEntry(Mission mission, int id, GoalList goals) {
        this(mission, id, goals, null);
    }

    public MuleManagementEntry(Mission mission, int id, BooleanSupplier goalOverride) {
        this(mission, id, null, goalOverride);
    }

    public MuleManagementEntry(Mission mission, int id, GoalList goals, BooleanSupplier goalOverride) {
        this.mission = mission;
        this.id = id;
        this.goals = goals;
        this.goalOverride = goalOverride;
    }

    /**
     * Determines whether the player can mule.
     *
     * @return True if the player can mule; false otherwise.
     * */
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
        final int inventoryAmount = Inventory.getCount(true, id) + Inventory.getCount(true, id + 1);
        final int bankAmount = mission.getScript().getBankCache().getBankCache().getOrDefault(id, 0);

        return inventoryAmount + bankAmount > 0;
    }

    /**
     * Gets the item id.
     *
     * @return The item id.
     */
    public int getId() {
        return id;
    }

    public MuleManager getMuleManager() {
        return muleManager;
    }

    public void setMuleManager(MuleManager muleManager) {
        this.muleManager = muleManager;
    }
}
