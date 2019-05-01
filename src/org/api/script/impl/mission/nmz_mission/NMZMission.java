package org.api.script.impl.mission.nmz_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.nmz_mission.worker.NMZWorkerHandler;
import org.rspeer.runetek.api.movement.Movement;

public class NMZMission extends Mission {

    private final NMZWorkerHandler workerHandler = new NMZWorkerHandler();
    public boolean shouldEnd;

    public NMZMission(SPXScript script) {
        super(script);
    }

    @Override
    public String getMissionName() {
        return "NMZ";
    }

    @Override
    public String getWorkerName() {
        final Worker c = workerHandler.getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        final Worker c = workerHandler.getCurrent();
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
        if (!Movement.isRunEnabled() && Movement.getRunEnergy() > 10)
            Movement.toggleRun(true);

        workerHandler.work();
        return 100;
    }
}

