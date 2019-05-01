package org.api.script.impl.mission.item_management_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.item_management.ItemManagementEntry;
import org.api.script.framework.item_management.ItemManagementTracker;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.item_management_mission.worker.ItemManagementWorkerHandler;

public class ItemManagementMission extends Mission {

    public final int[] itemsToSell;
    private final ItemManagementWorkerHandler workerHandler;
    private final ItemManagementEntry itemManagementEntry;
    private final ItemManagementTracker itemManagementTracker;
    public boolean hasPutInOffer;
    public boolean hasWithdrawnSellables;
    public boolean shouldEnd;

    public ItemManagementMission(SPXScript script, ItemManagementEntry itemManagementEntry, ItemManagementTracker itemManagementTracker, int[] itemsToSell) {
        super(script);
        this.itemManagementEntry = itemManagementEntry;
        this.itemManagementTracker = itemManagementTracker;
        this.itemsToSell = itemsToSell;
        workerHandler = new ItemManagementWorkerHandler(this);
    }

    @Override
    public String getMissionName() {
        return "Item Management";
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

    public ItemManagementTracker getItemManagementTracker() {
        return itemManagementTracker;
    }

    public ItemManagementEntry getItemManagementEntry() {
        return itemManagementEntry;
    }
}
