package org.api.script.impl.mission.mule_slave_management.mule_management_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.impl.mule_management.MuleManagementOverrideEntry;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.MuleManagementWorkerHandler;

public class MuleManagementMission extends Mission {

    private final WorkerHandler workerHandler;
    private final MuleManagementOverrideEntry muleManagementOverrideEntry;
    private boolean shouldEnd;

    public MuleManagementMission(SPXScript script, MuleManagementOverrideEntry muleManagementOverrideEntry) {
        super(script);
        this.muleManagementOverrideEntry = muleManagementOverrideEntry;
        this.workerHandler = new MuleManagementWorkerHandler(this);
    }

    @Override
    public String getMissionName() {
        return "Mule management";
    }

    @Override
    public String getWorkerName() {
        final Worker c = getWorkerHandler().getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        final Worker c = getWorkerHandler().getCurrent();
        return c == null ? "Waiting for worker." : c.toString();
    }

    @Override
    public boolean shouldPrintWorkerString() {
        return true;
    }

    @Override
    public boolean shouldEnd() {
        return getShouldEnd();
    }

    @Override
    public GoalList getGoals() {
        return new GoalList(new InfiniteGoal());
    }

    @Override
    public int execute() {
        getWorkerHandler().work();
        return 150;
    }

    public MuleManagementOverrideEntry getMuleManagementOverrideEntry() {
        return muleManagementOverrideEntry;
    }

    public boolean getShouldEnd() {
        return shouldEnd;
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    public WorkerHandler getWorkerHandler() {
        return workerHandler;
    }
}
