package org.api.script.impl.mission.item_management_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.worker.banking.OpenBankWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.ui.Log;

public class WithdrawSellablesWorker extends Worker {

    private ItemManagementMission mission;
    private OpenBankWorker openBankWorker = new OpenBankWorker();
    private WithdrawWorker withdrawWorker;


    public WithdrawSellablesWorker(ItemManagementMission mission) {
        this.mission = mission;
    }

    @Override
    public void work() {
        if (!Bank.isOpen()) {
            openBankWorker.work();
            return;
        }

        if (Bank.getWithdrawMode() != Bank.WithdrawMode.NOTE) {
            Bank.setWithdrawMode(Bank.WithdrawMode.NOTE);
            return;
        }

        int withdrawCount = 0;
        for (int itemId : mission.itemsToSell) {
            withdrawWorker = new WithdrawWorker(mission, a -> a.getId() == itemId, 0, Bank.WithdrawMode.NOTE);
            withdrawWorker.work();

            if (withdrawWorker.itemNotFound())
                withdrawCount++;
        }

        if (withdrawCount == mission.itemsToSell.length) {
            Log.severe("You do not have any items to sell in your bank.");
            mission.shouldEnd = true;
            return;
        }

        mission.hasWithdrawnSellables = true;
    }

    @Override
    public String toString() {
        return "Executing item management withdraw sellables worker.";
    }
}
