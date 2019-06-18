package org.api.script.impl.mission.item_management_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.mission_override.impl.item_management.ItemManagementBuyOverrideEntry;
import org.api.script.framework.mission_override.impl.item_management.ItemManagementSellOverrideEntry;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.item_management_mission.worker.ItemManagementWorkerHandler;

public class ItemManagementMission extends Mission {

    private final WorkerHandler workerHandler;
    private final ItemManagementBuyOverrideEntry itemManagementBuyOverrideEntry;
    private final ItemManagementSellOverrideEntry itemManagementSellOverrideEntry;
    public boolean shouldEnd;

    public ItemManagementMission(SPXScript script, ItemManagementBuyOverrideEntry itemManagementBuyOverrideEntry) {
        super(script);
        this.itemManagementBuyOverrideEntry = itemManagementBuyOverrideEntry;
        this.itemManagementSellOverrideEntry = null;
        workerHandler = new ItemManagementWorkerHandler(this);
    }

    public ItemManagementMission(SPXScript script, ItemManagementSellOverrideEntry itemManagementSellOverrideEntry) {
        super(script);
        this.itemManagementSellOverrideEntry = itemManagementSellOverrideEntry;
        this.itemManagementBuyOverrideEntry = null;
        workerHandler = new ItemManagementWorkerHandler(this);
    }

    @Override
    public String getMissionName() {
        return "Item Management";
    }

    @Override
    public String getWorkerName() {
        Worker c = getWorkerHandler().getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        Worker c = getWorkerHandler().getCurrent();
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
        return 100;
    }

    public ItemManagementBuyOverrideEntry getItemManagementBuyOverrideEntry() {
        return itemManagementBuyOverrideEntry;
    }

    public ItemManagementSellOverrideEntry getItemManagementSellOverrideEntry() {
        return itemManagementSellOverrideEntry;
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
