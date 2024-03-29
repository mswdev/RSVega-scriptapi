package org.api.script.impl.mission.blast_furnace_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.blast_furnace_mission.BlastFurnaceMission;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;

import java.util.function.Predicate;

public class WithdrawCoalBag extends Worker {

    public static final Predicate<Item> COAL_BAG = a -> a.getName().equals("Coal bag");
    private final WithdrawWorker withdrawWorker;
    private final BlastFurnaceMission mission;

    public WithdrawCoalBag(BlastFurnaceMission mission) {
        this.mission = mission;
        withdrawWorker = new WithdrawWorker(mission, COAL_BAG, Bank.WithdrawMode.ITEM);
    }

    @Override
    public void work() {
        withdrawWorker.work();
        mission.shouldEnd = withdrawWorker.itemNotFound();
    }

    @Override
    public String toString() {
        return "Getting coal bag.";
    }
}

