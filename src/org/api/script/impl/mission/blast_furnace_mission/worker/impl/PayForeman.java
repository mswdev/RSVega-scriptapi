package org.api.script.impl.mission.blast_furnace_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.blast_furnace_mission.BlastFurnaceMission;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Npcs;

import java.util.function.Predicate;

public class PayForeman extends Worker {

    private static final int DEPOSIT_AMOUNT = 2500;
    private static final Predicate<InterfaceComponent> PAY_FOREMAN = a -> a.getText().contains("You must ask the foreman");
    private static final Predicate<Npc> FOREMAN = a -> a.getName().equals("Blast Furnace Foreman");
    private static final Predicate<String> PAY = a -> a.equals("Pay");
    private final WithdrawWorker withdrawWorker;
    private final BlastFurnaceMission mission;
    public boolean paidForeman;

    public PayForeman(BlastFurnaceMission mission) {
        this.mission = mission;
        withdrawWorker = new WithdrawWorker(mission, BlastFurnaceMission.COINS, 2500, Bank.WithdrawMode.ITEM);
    }

    public static boolean shouldPayForeman() {
        final InterfaceComponent component = Interfaces.getFirst(PAY_FOREMAN);
        if (component == null)
            return false;

        return component.isVisible();
    }

    @Override
    public void work() {
        paidForeman = false;
        if (Inventory.getCount(true, BlastFurnaceMission.COINS) < DEPOSIT_AMOUNT) {
            withdrawWorker.work();
            mission.shouldEnd = withdrawWorker.itemNotFound();
        } else {
            final Npc npc = Npcs.getFirst(FOREMAN);
            if (npc == null)
                return;

            if (!Dialog.isOpen()) {
                if (npc.interact(PAY))
                    Time.sleepUntil(Dialog::isOpen, 1500);
            } else {
                if (Dialog.process(0))
                    if (Time.sleepUntil(Dialog::isProcessing, 1500))
                        paidForeman = true;
            }
        }
    }

    @Override
    public String toString() {
        return "Paying foreman";
    }
}

