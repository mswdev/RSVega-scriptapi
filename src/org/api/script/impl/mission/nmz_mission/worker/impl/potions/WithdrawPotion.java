package org.api.script.impl.mission.nmz_mission.worker.impl.potions;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.interactables.SceneObjectWorker;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.Predicate;

public class WithdrawPotion extends Worker {

    private final Predicate<SceneObject> potionBarrel;
    private final SceneObjectWorker sceneObjectWorker;
    private final int amount;

    public WithdrawPotion(PotionType potionType, int amount) {
        this.amount = amount;
        potionBarrel = a -> a.getName().equals(potionType.getBarrelName());
        sceneObjectWorker = new SceneObjectWorker(potionBarrel, a -> a.equals("Take"));
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        if (!EnterInput.isOpen()) {
            sceneObjectWorker.work();
            return;
        }

        final int inventoryCache = Inventory.getCount();
        EnterInput.initiate(amount);
        Time.sleepUntil(() -> inventoryCache != Inventory.getCount(), 1500);
    }

    @Override
    public String toString() {
        return "Withdrawing potion";
    }
}

