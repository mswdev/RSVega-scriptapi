package org.api.script.impl.mission.blast_furnace_mission.worker.impl.smelt;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.blast_furnace_mission.BlastFurnaceMission;
import org.api.script.impl.mission.blast_furnace_mission.worker.impl.WithdrawCoalBag;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.SceneObjects;

import java.util.function.Predicate;

public class SmeltBars extends Worker {

    private static final Predicate<SceneObject> CONVEYOR_BELT = a -> a.getName().equals("Conveyor belt") && a.containsAction("Put-ore-on");
    private static final Predicate<String> EMPTY_COAL_BAG = a -> a.equals("Empty");
    private final BlastFurnaceMission mission;

    public SmeltBars(BlastFurnaceMission mission) {
        this.mission = mission;
    }

    @Override
    public void work() {
        final SceneObject conveyorBelt = SceneObjects.getNearest(CONVEYOR_BELT);
        if (conveyorBelt == null)
            return;

        if (Inventory.isFull()) {
            if (conveyorBelt.click())
                Time.sleepUntil(() -> !Inventory.isFull(), 1500);

            if (mission.isCoalBagEmpty)
                mission.isSmelting = false;
        } else {
            final Item coalBag = Inventory.getFirst(WithdrawCoalBag.COAL_BAG);
            if (coalBag == null)
                return;

            if (coalBag.interact(EMPTY_COAL_BAG))
                if (Time.sleepUntil(Inventory::isFull, 1500))
                    mission.isCoalBagEmpty = true;
        }
    }

    @Override
    public String toString() {
        return "Smelting bars.";
    }
}

