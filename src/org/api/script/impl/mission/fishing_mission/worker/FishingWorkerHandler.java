package org.api.script.impl.mission.fishing_mission.worker;

import org.api.game.skills.fishing.FishType;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.fishing_mission.Fishingmission;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;

public class FishingWorkerHandler extends WorkerHandler {

    private final Fishingmission mission;
    private boolean tempBool;

    public FishingWorkerHandler(Fishingmission mission) {
        this.mission = mission;
    }

    @Override
    public Worker decide() {
        final FishType fishType = FishType.SHRIMP;
        final Item shrimpItem = Inventory.getFirst(fishType.getName());
        final Item anchoviesItem = Inventory.getFirst("Raw anchovies");
        if (Inventory.containsOnly(fishType.getRequiredEquipmentIds()))
            tempBool = false;

        if ((shrimpItem != null || anchoviesItem != null) && (tempBool || Inventory.isFull())) {
            tempBool = true;

            if (shrimpItem != null)
                shrimpItem.interact(a -> a.equals("Drop"));

            if (anchoviesItem != null)
                anchoviesItem.interact(a -> a.equals("Drop"));
            return null;
        }

        if (!Inventory.contains(fishType.getRequiredEquipmentIds())) {
            for (int id : fishType.getRequiredEquipmentIds()) {
                if (Inventory.contains(id))
                    continue;

                return new WithdrawWorker(mission, a -> a.getId() == id);
            }
        }

        return new NpcWorker(a -> a.getName().equals("Fishing spot"), a -> a.contains(fishType.getAction()), new MovementWorker(fishType.getFishLocation()[0].getPosition()));
    }
}
