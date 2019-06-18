package org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.SlaveManagementMission;
import org.rspeer.runetek.adapter.Interactable;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.scene.Players;

public class TradeSlaveWorker extends Worker {

    private final SlaveManagementMission mission;

    public TradeSlaveWorker(SlaveManagementMission mission) {
        this.mission = mission;
    }

    @Override
    public void work() {
        final String playerTradingDisplayName = getMission().getTradeMessageListener().getPlayerTradingDisplayName();
        if (playerTradingDisplayName == null)
            return;

        final Interactable playerTrading = Players.getNearest(playerTradingDisplayName);
        if (playerTrading == null) {
            getMission().getTradeMessageListener().setPlayerTradingDisplayName(null);
            return;
        }

        if (!Trade.isOpen()) {
            if (playerTrading.interact("Trade with"))
                Time.sleepUntil(Trade::isOpen, 1500);

            return;
        }

        if (Trade.getTheirItems().length <= 0 && !Trade.isOpen(true))
            return;

        if (Trade.isOpen(true)) {
            if (Trade.accept())
                getMission().getTradeMessageListener().setPlayerTradingDisplayName(null);

            return;
        }

        if (Trade.accept())
            Time.sleepUntil(() -> Trade.isOpen(true), 1500);
    }

    @Override
    public String toString() {
        return "Executing trade slave worker.";
    }

    public SlaveManagementMission getMission() {
        return mission;
    }
}
