package org.api.script.impl.mission.fishing_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.MissionOverride;
import org.api.script.framework.mission_override.MissionOverrideFactory;
import org.api.script.framework.mission_override.impl.mule_management.MuleManagementOverrideEntry;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.fishing_mission.worker.FishingWorkerHandler;

public class Fishingmission extends Mission implements MissionOverride {

    private final FishingWorkerHandler workerHandler;
    private boolean shouldEnd;

    public Fishingmission(SPXScript script) {
        super(script);
        workerHandler = new FishingWorkerHandler(this);
    }

    @Override
    public String getMissionName() {
        return "AIO Fishing";
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

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    @Override
    public MissionOverrideFactory[] overriddenMissionEntries() {
        return new MissionOverrideFactory[]{
                new MuleManagementOverrideEntry(this, a -> a.getName().equals("Raw shrimps"), () -> getScript().getBankCache().getItem(b -> b.getName().equals("Raw shrimps") && b.getStackSize() >= 48) != null)
        };
    }
}
