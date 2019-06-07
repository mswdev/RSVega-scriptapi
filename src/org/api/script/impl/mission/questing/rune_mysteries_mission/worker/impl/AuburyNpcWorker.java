package org.api.script.impl.mission.questing.rune_mysteries_mission.worker.impl;

import org.api.script.impl.worker.DialogueWorker;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;

import java.util.function.Predicate;

public class AuburyNpcWorker extends NpcWorker {

    private static final Position AUBURY_DOOR = new Position(3253, 3399, 0);

    public AuburyNpcWorker(Predicate<Npc> npcPredicate) {
        super(npcPredicate);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action) {
        super(npcPredicate, action);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, DialogueWorker dialogueWorker) {
        super(npcPredicate, dialogueWorker);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, MovementWorker movementWorker) {
        super(npcPredicate, movementWorker);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, DialogueWorker dialogueWorker, MovementWorker movementWorker) {
        super(npcPredicate, dialogueWorker, movementWorker);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, DialogueWorker dialogueWorker) {
        super(npcPredicate, action, dialogueWorker);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, MovementWorker movementWorker) {
        super(npcPredicate, action, movementWorker);
    }

    public AuburyNpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, DialogueWorker dialogueWorker, MovementWorker movementWorker) {
        super(npcPredicate, action, dialogueWorker, movementWorker);
    }

    @Override
    public void work() {
        final Npc mugger = Npcs.getNearest("Mugger");
        if (mugger == null) {
            super.work();
            return;
        }

        if (Players.getLocal().getHealthBar() == null || mugger.getTarget() == null || !mugger.getTarget().equals(Players.getLocal()) || AUBURY_DOOR.distance() >= 15) {
            super.work();
            return;
        }

        if (!Players.getLocal().getPosition().equals(AUBURY_DOOR)) {
            Movement.walkTo(AUBURY_DOOR);
            return;
        }

        final SceneObject auburyDoor = SceneObjects.getFirstAt(AUBURY_DOOR);
        if (auburyDoor == null)
            return;

        if (auburyDoor.containsAction("Close"))
            auburyDoor.click();

        Time.sleepUntil(() -> Players.getLocal().getTargetIndex() == -1, 3500);
    }
}
