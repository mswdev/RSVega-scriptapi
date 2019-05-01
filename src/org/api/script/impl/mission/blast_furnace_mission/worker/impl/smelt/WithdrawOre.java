package org.api.script.impl.mission.blast_furnace_mission.worker.impl.smelt;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.blast_furnace_mission.BlastFurnaceMission;
import org.api.script.impl.mission.blast_furnace_mission.worker.impl.WithdrawCoalBag;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.Predicate;

public class WithdrawOre extends Worker {

    private static final int COAL_VARP = 547;
    private final int ingredientOneMinimum = (BlastFurnaceMission.barType.getIngredientOneAmount() / 2) * 27;
    private final Predicate<Item> ingredientOne = a -> a.getName().equals(BlastFurnaceMission.barType.getIngredientOne().getName());
    private final Predicate<Item> ingredientTwo = a -> a.getName().equals(BlastFurnaceMission.barType.getIngredientTwo().getName());
    private final WithdrawWorker withdrawWorkerIngredientOne = new WithdrawWorker(ingredientOne, 0, Bank.WithdrawMode.ITEM);
    private final WithdrawWorker withdrawWorkerIngredientTwo = new WithdrawWorker(ingredientTwo, 0, Bank.WithdrawMode.ITEM);
    private final BlastFurnaceMission mission;

    public WithdrawOre(BlastFurnaceMission mission) {
        this.mission = mission;
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        if (mission.isCoalBagEmpty) {
            if (Inventory.contains(ingredientOne)) {
                fillCoalBag();
            } else {
                withdrawIngrediantOne();
            }
        } else if (Varps.get(COAL_VARP) <= ingredientOneMinimum - 27) {
            if (Inventory.contains(ingredientOne)) {
                mission.isSmelting = true;
            } else {
                withdrawIngrediantOne();
            }
        } else {
            if (Inventory.contains(ingredientTwo)) {
                mission.isSmelting = true;
            } else {
                withdrawIngrediantTwo();
            }
        }
    }

    private void fillCoalBag() {
        final Item coalBag = Inventory.getFirst(WithdrawCoalBag.COAL_BAG);
        if (coalBag.click())
            if (Time.sleepUntil(Dialog::isOpen, 1500))
                mission.isCoalBagEmpty = false;
    }

    private void withdrawIngrediantOne() {
        withdrawWorkerIngredientOne.work();
        mission.shouldEnd = withdrawWorkerIngredientOne.itemNotFound();
    }

    private void withdrawIngrediantTwo() {
        withdrawWorkerIngredientTwo.work();
        mission.shouldEnd = withdrawWorkerIngredientTwo.itemNotFound();
    }

    @Override
    public String toString() {
        return "Withdrawing ore.";
    }
}

