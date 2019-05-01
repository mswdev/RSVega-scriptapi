package org.api.script.impl.mission.blast_furnace_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.banking.DepositWorker;
import org.api.script.impl.worker.banking.OpenBankWorker;
import org.api.script.impl.worker.interactables.ItemWorker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;

import java.util.function.Predicate;

public class DrinkStamina extends Worker {

    public static final Predicate<Item> stamina = a -> a.getName().contains("Stamina");
    public static final Predicate<Item> vial = a -> a.getName().equals("Vial");
    private final ItemWorker itemWorker = new ItemWorker(stamina);
    private final OpenBankWorker openBankWorker = new OpenBankWorker(false);
    private final DepositWorker depositWorker = new DepositWorker();
    public boolean outOfStamina;

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        if (Movement.isStaminaEnhancementActive()) {
            if (Bank.isOpen()) {
                if (Bank.depositAll(vial))
                    Time.sleepUntil(() -> !Inventory.contains(vial), 1500);

                if (Bank.depositAll(stamina))
                    Time.sleepUntil(() -> !Inventory.contains(stamina), 1500);
            } else {
                openBankWorker.work();
            }
        } else {
            if (Inventory.isFull())
                depositWorker.work();

            itemWorker.work();
            outOfStamina = itemWorker.itemNotFound();
        }
    }

    @Override
    public String toString() {
        return "Drinking Stamina potion.";
    }
}

