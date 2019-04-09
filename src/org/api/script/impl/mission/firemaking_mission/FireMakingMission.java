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
    private final FireMakingWorkerHandler worker_handler;
    private final LinkedList<Position> ignored_tiles;
    private Position current_lane_start_position;
    private Position search_position;
    private boolean is_stuck_in_lane;
    private boolean should_stop;

    public FireMakingMission(SPXScript script, Args args) {
        super(script);
        this.args = args;
        worker_handler = new FireMakingWorkerHandler(this);
        ignored_tiles = new LinkedList<>();
    }

    @Override
    public String getMissionName() {
        return "AIO Firemaking";
    }

    @Override
    public String getWorkerName() {
        Worker c = worker_handler.getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        Worker c = worker_handler.getCurrent();
        return c == null ? "Waiting for worker." : c.toString();
    }

    @Override
    public boolean shouldPrintWorkerString() {
        return true;
    }

    @Override
    public boolean shouldEnd() {
        return should_stop;
    }

    @Override
    public GoalList getGoals() {
        return new GoalList(new InfiniteGoal());
    }

    @Override
    public int execute() {
        worker_handler.work();
        return 100;
    }

    public Position getCurrentLaneStartPosition() {
        return current_lane_start_position;
    }

    public void setCurrentLaneStartPosition(Position current_lane_start_position) {
        this.current_lane_start_position = current_lane_start_position;
    }

    public boolean isStuckInLane() {
        return is_stuck_in_lane;
    }

    public void setIsStuckInLane(boolean is_stuck_in_lane) {
        this.is_stuck_in_lane = is_stuck_in_lane;
    }

    public void setShouldStop(boolean should_stop) {
        this.should_stop = should_stop;
    }

    public LinkedList<Position> getIgnoredTiles() {
        return ignored_tiles;
    }

    public Position getSearchPosition() {
        return search_position;
    }

    public void setSearchPosition(Position search_position) {
        this.search_position = search_position;
    }

    public Args getArgs() {
        return args;
    }
}

