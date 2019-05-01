package org.api.script.impl.mission.firemaking_mission.worker.impl;


import org.api.game.skills.firemaking.FiremakingUtil;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.firemaking_mission.FireMakingMission;
import org.api.script.impl.worker.banking.OpenBankWorker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

public class WithdrawLogs extends Worker {

    private final OpenBankWorker openBankWorker = new OpenBankWorker(false);
    private final FireMakingMission mission;

    public WithdrawLogs(FireMakingMission mission) {
        this.mission = mission;
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1)
            return;

        if (Bank.isOpen()) {
            if (mission.getArgs().setProgressive)
                mission.getArgs().logType = FiremakingUtil.getAppropriateOwnedLogs();

            if (mission.getArgs().logType != null && Bank.contains(mission.getArgs().logType.getName())) {
                if (Bank.withdrawAll(mission.getArgs().logType.getName()))
                    Time.sleepUntil(() -> Inventory.contains(mission.getArgs().logType.getName()), 1500);
            } else {
                Log.severe("[FIREMAKING]: You do not have any usable logs in your bank.");
                mission.setShouldEnd(true);
            }
        } else {
            openBankWorker.work();
        }
    }

    @Override
    public String toString() {
        return "Withdrawing logs.";
    }
}

