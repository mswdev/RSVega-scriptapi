package org.api.script.impl.worker.banking;

import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.ui.Log;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class WithdrawWorker extends Worker {

    private final Mission mission;
    private final Predicate<Item> item;
    private final Bank.WithdrawMode withdrawMode;
    private final int amount;
    private final OpenBankWorker openBankWorker;
    private final DepositWorker depositWorker;
    private boolean itemNotFound;

    public WithdrawWorker(Mission mission, Predicate<Item> item) {
        this(mission, item, 1, Bank.WithdrawMode.ITEM);
    }

    public WithdrawWorker(Mission mission, Predicate<Item> item, int amount) {
        this(mission, item, amount, Bank.WithdrawMode.ITEM);
    }

    public WithdrawWorker(Mission mission, Predicate<Item> item, Bank.WithdrawMode withdrawMode) {
        this(mission, item, 1, withdrawMode);
    }

    public WithdrawWorker(Mission mission, Predicate<Item> item, int amount, Bank.WithdrawMode withdrawMode) {
        this.mission = mission;
        this.item = item;
        this.withdrawMode = withdrawMode;
        this.amount = amount;
        openBankWorker = new OpenBankWorker(false);
        this.depositWorker = new DepositWorker();
    }

    @Override
    public void work() {
        itemNotFound = false;
        if (!Bank.isOpen()) {
            openBankWorker.work();
            return;
        }

        if (Bank.getWithdrawMode() != withdrawMode) {
            Bank.setWithdrawMode(withdrawMode);
            return;
        }

        if (Inventory.isFull()) {
            if (!Bank.getFirst(item).isStackable()) {
                depositWorker.work();
                return;
            }

            if (!Inventory.contains(item)) {
                depositWorker.work();
                return;
            }
        }

        if (!Bank.contains(item)) {
            Log.severe("You do not have the required items in your bank.");
            return;
        }

        final int withdrawCache = Inventory.getCount(true);
        BooleanSupplier booleanSupplier = () -> withdrawCache != Inventory.getCount(true);
        if (amount == 0) {
            if (Bank.withdrawAll(item)) {
                Time.sleepUntil(booleanSupplier, 2500);
            } else {
                setItemNotFound();
            }

            mission.getScript().getBankCache().update();
            return;
        }

        if (Bank.withdraw(item, amount)) {
            Time.sleepUntil(booleanSupplier, 2500);
        } else {
            setItemNotFound();
        }

        mission.getScript().getBankCache().update();
    }

    private void setItemNotFound() {

        itemNotFound = true;
    }

    public boolean itemNotFound() {
        return itemNotFound;
    }

    @Override
    public String toString() {
        return "Executing withdraw worker.";
    }
}

