package org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.api.script.impl.worker.MovementWorker;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.scene.Players;

public class TradeMuleWorker extends Worker {

    private final Worker movementWorker;
    private MuleManagementMission mission;
    private boolean isFinished;

    public TradeMuleWorker(MuleManagementMission mission) {
        this.mission = mission;
        movementWorker = new MovementWorker(mission.getMuleManagementOverrideEntry().getMuleManager().getPosition());
    }

    @Override
    public void work() {
        final Player mule = Players.getNearest(getMission().getMuleManagementOverrideEntry().getMuleManager().getDisplayName());
        if (mule == null) {
            getMovementWorker().work();
            return;
        }

        if (!Trade.isOpen()) {
            if (mule.interact("Trade with")) {
                Time.sleepUntil(Trade::isOpen, 6500);
                return;
            }
        }

        if (!Trade.contains(true, getMission().getMuleManagementOverrideEntry().getItem()) && !Trade.isOpen(true)) {
            if (Trade.offerAll(getMission().getMuleManagementOverrideEntry().getItem())) {
                Time.sleepUntil(() -> Trade.contains(true, getMission().getMuleManagementOverrideEntry().getItem()), 1500);
            }
            return;
        }

        if (!Trade.hasOtherAccepted())
            return;

        if (Trade.isOpen(true)) {
            if (Trade.accept())
                setFinished(true);

            return;
        }

        if (Trade.accept())
            Time.sleepUntil(() -> Trade.isOpen(true), 1500);
    }

    @Override
    public String toString() {
        return "Executing trade mule worker.";
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public Worker getMovementWorker() {
        return movementWorker;
    }

    public MuleManagementMission getMission() {
        return mission;
    }
}
