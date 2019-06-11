package org.api.script.impl.mission.questing.restless_ghost_mission.worker.impl;

import org.api.script.impl.worker.MovementWorker;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.SceneObjects;

public class TempMovementWorker extends MovementWorker {

    private static final Position WIZARD_TOWER_FIRST_DOOR = new Position(3109, 3167, 0);
    private static final Position WIZARD_TOWER_SECOND_DOOR = new Position(3107, 3162, 0);
    private static final Position WIZARD_TOWER_BASEMENT_DOOR = new Position(3108, 9570, 0);


    public TempMovementWorker(Position position) {
        super(position);
    }

    public TempMovementWorker(Position position, int offSet) {
        super(position, offSet);
    }

    @Override
    public void work() {
        final SceneObject wizardTowerDoor = SceneObjects.getNearest(a -> a.getPosition().equals(WIZARD_TOWER_FIRST_DOOR) || a.getPosition().equals(WIZARD_TOWER_SECOND_DOOR) || a.getPosition().equals(WIZARD_TOWER_BASEMENT_DOOR));
        if (wizardTowerDoor != null) {
            wizardTowerDoor.click();
            return;
        }

        super.work();
    }
}
