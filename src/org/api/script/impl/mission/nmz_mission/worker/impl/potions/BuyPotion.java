package org.api.script.impl.mission.nmz_mission.worker.impl.potions;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.interactables.SceneObjectWorker;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.Interfaces;

import java.util.function.Predicate;

public class BuyPotion extends Worker {

    private static final int REWARDS_CHEST_COMPONENT_PREDICATE = 206;
    private static final int REWARDS_CHEST_POTIONS_COMPONENT_PREDICATE = 6;
    private final Predicate<SceneObject> rewardsChestPredicate = a -> a.getName().equals("Rewards chest");
    private final SceneObjectWorker sceneObjectWorker = new SceneObjectWorker(rewardsChestPredicate);
    private final PotionType potionType;
    private final int amount;

    public BuyPotion(PotionType potionType, int amount) {
        this.potionType = potionType;
        this.amount = amount;
    }

    @Override
    public void work() {
        final InterfaceComponent rewardsChestComponent = Interfaces.getComponent(REWARDS_CHEST_COMPONENT_PREDICATE, REWARDS_CHEST_POTIONS_COMPONENT_PREDICATE, potionType.getShopInterfaceId());
        if (rewardsChestComponent == null) {
            sceneObjectWorker.work();
            return;
        }

        if (!EnterInput.isOpen()) {
            rewardsChestComponent.interact("Buy-X");
            return;
        }

        EnterInput.initiate(amount);
    }

    @Override
    public String toString() {
        return "Buying potion";
    }
}

