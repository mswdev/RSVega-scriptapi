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

    private final OpenBankWorker open_bank_worker = new OpenBankWorker(false);
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
            if (mission.getArgs().set_progressive)
                mission.getArgs().log_type = FiremakingUtil.getAppropriateOwnedLogs();

            if (mission.getArgs().log_type != null && Bank.contains(mission.getArgs().log_type.getName())) {
                if (Bank.withdrawAll(mission.getArgs().log_type.getName()))
                    Time.sleepUntil(() -> Inventory.contains(mission.getArgs().log_type.getName()), 1500);
            } else {
                Log.severe("[FIREMAKING]: You do not have any usable logs in your bank.");
                mission.setShouldStop(true);
            }
        } else {
            open_bank_worker.work();
        }
    }

    @Override
    public String toString() {
        return "Withdrawing logs.";
    }
}
