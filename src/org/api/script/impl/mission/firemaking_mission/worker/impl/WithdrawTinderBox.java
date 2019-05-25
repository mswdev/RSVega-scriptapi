package org.api.script.impl.mission.firemaking_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.firemaking_mission.FireMakingMission;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.Item;

import java.util.function.Predicate;

public class WithdrawTinderBox extends Worker {

    public static final Predicate<Item> TINDERBOX = a -> a.getName().equals("Tinderbox");
    private final WithdrawWorker withdrawWorker;

    public WithdrawTinderBox(FireMakingMission mission) {
        this.withdrawWorker = new WithdrawWorker(mission, TINDERBOX);
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        withdrawWorker.work();
    }

    @Override
    public String toString() {
        return "Withdrawing Tinderbox.";
    }
}

