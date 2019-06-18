package org.api.script.impl.mission.mule_slave_management.slave_management_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.SlaveManagementWorkerHandler;

public class SlaveManagementMission extends Mission {

    private final TradeMessageListener tradeMessageListener;
    private final WorkerHandler workerHandler;
    private boolean shouldEnd;

    public SlaveManagementMission(SPXScript spxScript, TradeMessageListener tradeMessageListener) {
        super(spxScript);
        this.tradeMessageListener = tradeMessageListener;
        this.workerHandler = new SlaveManagementWorkerHandler(this);
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

    public TradeMessageListener getTradeMessageListener() {
        return tradeMessageListener;
    }

    public WorkerHandler getWorkerHandler() {
        return workerHandler;
    }

    public boolean getShouldEnd() {
        return shouldEnd;
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }
}
