package org.api.script.framework.goal.impl;

import org.api.script.framework.goal.Goal;

public class SkillGoal implements Goal {

    private int currentLevel;
    private int endLevel;

    public SkillGoal(int currentLevel, int endLevel) {
        this.currentLevel = currentLevel;
        this.endLevel = endLevel;
    }

    @Override
    public boolean hasReached() {
        return currentLevel >= endLevel;
    }

    @Override
    public String getCompletionMessage() {
        return "[Skill Goal]: Complete: Achieved level " + endLevel + ".";
    }

    @Override
    public String getName() {
        return "[Skill Goal:] Set: [Start level: " + currentLevel + "| End level: " + endLevel + "].";
    }

    public String toString() {
        return "[Skill Goal:] Left: [Current level: " + currentLevel + "| End level: " + endLevel + "].";
    }

    public void update(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getGoal() {
        return endLevel;
    }
}

