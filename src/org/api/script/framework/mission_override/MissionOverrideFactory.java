package org.api.script.framework.mission_override;

import org.api.script.framework.goal.GoalList;
import org.api.script.framework.mission.Mission;

import java.util.function.BooleanSupplier;

public abstract class MissionOverrideFactory {

    private final Mission mission;
    private final GoalList goalList;
    private final BooleanSupplier goalOverride;

    public MissionOverrideFactory(Mission mission, GoalList goalList) {
        this(mission, goalList, null);
    }

    public MissionOverrideFactory(Mission mission, BooleanSupplier goalOverride) {
        this(mission, null, goalOverride);
    }

    public MissionOverrideFactory(Mission mission, GoalList goalList, BooleanSupplier goalOverride) {
        this.mission = mission;
        this.goalList = goalList;
        this.goalOverride = goalOverride;
    }

    public abstract Mission getOverrideMission();

    public abstract boolean entryIsReady();

    public boolean canOverride() {
        return getGoalOverride() != null ? getGoalOverride().getAsBoolean() : getGoalList() != null && getGoalList().hasReachedGoals();
    }

    public Mission getMission() {
        return mission;
    }

    private GoalList getGoalList() {
        return goalList;
    }

    private BooleanSupplier getGoalOverride() {
        return goalOverride;
    }
}
