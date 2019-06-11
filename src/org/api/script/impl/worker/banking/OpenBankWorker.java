package org.api.script.impl.worker.banking;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;

public class OpenBankWorker extends Worker {

    private final boolean depositBox;

    public OpenBankWorker() {
        this(false);
    }

    public OpenBankWorker(boolean depositBox) {
        this.depositBox = depositBox;
    }

    @Override
    public void work() {
        if (Players.getLocal().isMoving() && Movement.getDestinationDistance() > 10)
            return;

        if (depositBox) {
            DepositBox.open(BankLocation.getNearestDepositBox());
            return;
        }

        Bank.open(BankLocation.getNearestWithdrawable());
    }

    @Override
    public String toString() {
        return "Executing open bank worker.";
    }
}

