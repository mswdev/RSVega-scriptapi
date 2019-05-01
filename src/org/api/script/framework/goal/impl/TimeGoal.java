package org.api.script.framework.goal.impl;

import org.api.script.framework.goal.Goal;

public class TimeGoal implements Goal {

    private long startTime;
    private long timeAmount;

    public TimeGoal(long timeAmount) {
        this.startTime = System.currentTimeMillis();
        this.timeAmount = timeAmount;
    }

    @Override
    public boolean hasReached() {
        return System.currentTimeMillis() - startTime >= timeAmount;
    }

    @Override
    public String getCompletionMessage() {
        return "[Time Goal]: Complete: Ran for" + (timeAmount / 1000) / 60 + " minutes.";
    }

    @Override
    public String getName() {
        return "[Time Goal:] Set: " + (timeAmount / 1000) / 60 + " minutes.";
    }

    @Override
    public String toString() {
        return "[Time Goal:] Left: " + ((System.currentTimeMillis() - startTime) / 1000) / 60 + "minutes.";
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }
}

