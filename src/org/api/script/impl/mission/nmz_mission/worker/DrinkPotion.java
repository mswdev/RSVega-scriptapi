package org.api.script.impl.mission.nmz_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.nmz_mission.worker.impl.potions.PotionType;
import org.api.script.impl.worker.interactables.ItemWorker;

public class DrinkPotion extends Worker {

    private final ItemWorker itemWorker = new ItemWorker(a -> a.getName().contains(PotionType.ABSORPTION.getName()));

    @Override
    public void work() {
        itemWorker.work();
    }

    @Override
    public String toString() {
        return "Drinking absorption potion.";
    }
}

