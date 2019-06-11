package org.api.script.impl.worker.interactables;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.MovementWorker;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;

import java.util.function.Predicate;

public class SceneObjectWorker extends Worker {

    private final Predicate<SceneObject> sceneObjectPredicate;
    private final Predicate<String> action;
    private final MovementWorker movementWorker;

    public SceneObjectWorker(Predicate<SceneObject> sceneObjectPredicate) {
        this(sceneObjectPredicate, a -> true, null);
    }

    public SceneObjectWorker(Predicate<SceneObject> sceneObjectPredicate, Predicate<String> action) {
        this(sceneObjectPredicate, action, null);
    }

    public SceneObjectWorker(Predicate<SceneObject> sceneObjectPredicate, MovementWorker movementWorker) {
        this(sceneObjectPredicate, a -> true, movementWorker);
    }

    public SceneObjectWorker(Predicate<SceneObject> sceneObjectPredicate, Predicate<String> action, MovementWorker movementWorker) {
        this.sceneObjectPredicate = sceneObjectPredicate;
        this.action = action;
        this.movementWorker = movementWorker;
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1)
            return;

        final SceneObject sceneObject = SceneObjects.getNearest(sceneObjectPredicate);
        if (sceneObject == null) {
            if (movementWorker == null)
                return;

            movementWorker.work();
            return;
        }

        if (!sceneObject.isPositionInteractable()) {
            Movement.walkTo(sceneObject);
            return;
        }

        sceneObject.interact(action);
        Time.sleepWhile(() -> Players.getLocal().isMoving(), 2500);
    }

    @Override
    public String toString() {
        return "Executing scene object worker.";
    }
}

