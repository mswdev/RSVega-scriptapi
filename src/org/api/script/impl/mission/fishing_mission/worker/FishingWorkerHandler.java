package org.api.script.impl.mission.fishing_mission.worker;

import org.api.game.skills.fishing.FishType;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.fishing_mission.Fishingmission;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.banking.DepositWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.api.component.tab.Inventory;

public class FishingWorkerHandler extends WorkerHandler {

    private final Fishingmission mission;

    public FishingWorkerHandler(Fishingmission mission) {
        this.mission = mission;
    }

    @Override
    public Worker decide() {
        final FishType fishType = FishType.SHRIMP;
        if (Inventory.isFull())
            return new DepositWorker(a -> a.getId() != fishType.getRequiredEquipmentIds()[0]);

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
