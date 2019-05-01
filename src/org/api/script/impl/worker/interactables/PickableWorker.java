package org.api.script.impl.worker.interactables;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.MovementWorker;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;

import java.util.function.Predicate;

public class PickableWorker extends Worker {

    private final Predicate<Pickable> pickablePredicate;
    private final Predicate<String> action;
    private final MovementWorker movementWorker;

    public PickableWorker(Predicate<Pickable> pickablePredicate) {
        this(pickablePredicate, a -> true, null);
    }

    public PickableWorker(Predicate<Pickable> pickablePredicate, Predicate<String> action) {
        this(pickablePredicate, action, null);
    }

    public PickableWorker(Predicate<Pickable> pickablePredicate, MovementWorker movementWorker) {
        this(pickablePredicate, a -> true, movementWorker);
    }

    public PickableWorker(Predicate<Pickable> pickablePredicate, Predicate<String> action, MovementWorker movementWorker) {
        this.pickablePredicate = pickablePredicate;
        this.action = action;
        this.movementWorker = movementWorker;
    }

    @Override
    public boolean needsRepeat() {
        return movementWorker != null && movementWorker.needsRepeat();
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1)
            return;

        final Pickable pickable = Pickables.getNearest(pickablePredicate);
        if (pickable == null) {
            if (movementWorker == null)
                return;

            movementWorker.work();
            return;
        }

        if (!pickable.isPositionInteractable()) {
            Movement.walkTo(pickable);
            return;
        }

        pickable.interact(action);
        Time.sleepWhile(() -> Players.getLocal().isMoving(), 2500);
    }

    @Override
    public String toString() {
        return "Executing pickable worker.";
    }
}

