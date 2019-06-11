package org.api.script.impl.worker.interactables;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;

import java.util.function.Predicate;

public class ItemWorker extends Worker {

    private final Predicate<Item> itemPredicate;
    private final Predicate<String> actionPredicate;
    private final WithdrawWorker withdrawWorker;

    public ItemWorker(Predicate<Item> itemPredicate) {
        this(itemPredicate, a -> true, null);
    }

    public ItemWorker(Predicate<Item> itemPredicate, Predicate<String> actionPredicate) {
        this(itemPredicate, actionPredicate, null);
    }

    public ItemWorker(Predicate<Item> itemPredicate, WithdrawWorker withdrawWorker) {
        this(itemPredicate, a -> true, withdrawWorker);
    }

    public ItemWorker(Predicate<Item> itemPredicate, Predicate<String> actionPredicate, WithdrawWorker withdrawWorker) {
        this.itemPredicate = itemPredicate;
        this.actionPredicate = actionPredicate;
        this.withdrawWorker = withdrawWorker;
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1)
            return;

        final Item item = Inventory.getFirst(itemPredicate);
        if (item == null) {
            if (withdrawWorker == null)
                return;

            withdrawWorker.work();
            return;
        }

        final int equipmentCache = Equipment.getItems().length;
        if (item.interact(actionPredicate))
            Time.sleepUntil(() -> equipmentCache != Equipment.getItems().length || Players.getLocal().getAnimation() != -1 || Inventory.getSelectedItem() != null, 1500);
    }

    public boolean itemNotFound() {
        return withdrawWorker.itemNotFound();
    }

    @Override
    public String toString() {
        return "Executing item worker.";
    }
}

