package org.api.script.framework.goal.impl;

import org.api.script.framework.goal.Goal;

public class ResourceGoal implements Goal {

    private int[] ids;
    private int currentAmount;
    private int goalAmount;
    private String itemString;

    public ResourceGoal(int goalAmount, int... ids) {
        this.ids = ids;
        this.goalAmount = goalAmount;
        itemString = getItemString();
    }

    @Override
    public boolean hasReached() {
        return currentAmount >= goalAmount;
    }

    public void update(int count) {
        currentAmount += count;
    }

    public int[] getIds() {
        return ids;
    }

    @Override
    public String getCompletionMessage() {
        return "[Resource Goal]: Collected " + goalAmount + " of " + itemString + ".";
    }

    @Override
    public String getName() {
        return "[Resource Goal]: Set: Collecting " + goalAmount + " of " + itemString + ".";
    }

    public String toString() {
        return "[Resource Goal]: Left: Collected " + currentAmount + " of " + goalAmount + ".";
    }

    private String getItemString() {
        StringBuilder str = new StringBuilder(ids.length == 1 ? "Item[" : "Items[");

        for (int i = 0; i < ids.length; i++)
            str.append(i == ids.length - 1 ? ids[i] + "]" : ids[i] + ",");

        return str.toString();
    }

    public boolean containsId(int id) {
        for (int i : ids)
            if (i == id)
                return true;

        return false;
    }
}

