package org.api.script.impl.worker.banking;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.Predicate;

public class DepositWorker extends Worker {

    private final Predicate<Item> itemPredicate;
    private final int amount;
    private final int inventoryCache;
    private final OpenBankWorker openBankWorker;

    public DepositWorker() {
        this(null, 0);
    }

    public DepositWorker(Predicate<Item> itemPredicate) {
        this(itemPredicate, 0);
    }

    public DepositWorker(Predicate<Item> itemPredicate, int amount) {
        this.itemPredicate = itemPredicate;
        this.amount = amount;
        inventoryCache = Inventory.getCount(true);
        openBankWorker = new OpenBankWorker();
    }

    @Override
    public boolean needsRepeat() {
        return Inventory.getCount(true) == inventoryCache && !Inventory.isEmpty();
    }

    @Override
    public void work() {
        if (!Bank.isOpen() && !DepositBox.isOpen()) {
            openBankWorker.work();
            return;
        }

        if (Bank.isOpen()) {
            if (itemPredicate == null) {
                Bank.depositInventory();
                return;
            }

            if (amount == 0) {
                Bank.depositAll(itemPredicate);
                return;
            }

            Bank.deposit(itemPredicate, amount);
        }

        if (itemPredicate == null) {
            DepositBox.depositInventory();
            return;
        }

        if (amount == 0) {
            DepositBox.depositAll(itemPredicate);
            return;
        }

        DepositBox.deposit(itemPredicate, true, amount);
    }

    @Override
    public String toString() {
        return "Executing deposit worker.";
    }
}

