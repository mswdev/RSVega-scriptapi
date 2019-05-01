package org.api.script.impl.mission.firemaking_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.firemaking_mission.data.Args;
import org.api.script.impl.mission.firemaking_mission.worker.FireMakingWorkerHandler;
import org.rspeer.runetek.api.movement.position.Position;

import java.util.LinkedList;

public class FireMakingMission extends Mission {

    public static final int SEARCH_DISTANCE = 30;
    public static final int LANE_LENGTH = 15;
    public static final int MINIMUM_SCORE = 0;
    private final Args args;
    private final FireMakingWorkerHandler workerHandler;
    private final LinkedList<Position> ignoredTiles;
    private Position currentLaneStartPosition;
    private Position searchPosition;
    private boolean isStuckInLane;
    private boolean shouldEnd;

    public FireMakingMission(SPXScript script, Args args) {
        super(script);
        this.args = args;
        workerHandler = new FireMakingWorkerHandler(this);
        ignoredTiles = new LinkedList<>();
    }

    @Override
    public String getMissionName() {
        return "AIO Firemaking";
    }

    @Override
    public String getWorkerName() {
        Worker c = workerHandler.getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        Worker c = workerHandler.getCurrent();
        return c == null ? "Waiting for worker." : c.toString();
    }

    @Override
    public boolean shouldPrintWorkerString() {
        return true;
    }

    @Override
    public boolean shouldEnd() {
        return shouldEnd;
    }

    @Override
    public GoalList getGoals() {
        return new GoalList(new InfiniteGoal());
    }

    @Override
    public int execute() {
        workerHandler.work();
        return 100;
    }

    public Position getCurrentLaneStartPosition() {
        return currentLaneStartPosition;
    }

    public void setCurrentLaneStartPosition(Position currentLaneStartPosition) {
        this.currentLaneStartPosition = currentLaneStartPosition;
    }

    public boolean isStuckInLane() {
        return isStuckInLane;
    }

    public void setIsStuckInLane(boolean isStuckInLane) {
        this.isStuckInLane = isStuckInLane;
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    public LinkedList<Position> getIgnoredTiles() {
        return ignoredTiles;
    }

    public Position getSearchPosition() {
        return searchPosition;
    }

    public void setSearchPosition(Position searchPosition) {
        this.searchPosition = searchPosition;
    }

    public Args getArgs() {
        return args;
    }
}

