package org.api.script.impl.worker.interactables;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.DialogueWorker;
import org.api.script.impl.worker.MovementWorker;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;

import java.util.function.Predicate;

public class NpcWorker extends Worker {

    private final Predicate<Npc> npcPredicate;
    private final Predicate<String> action;
    private final DialogueWorker dialogueWorker;
    private final MovementWorker movementWorker;

    public NpcWorker(Predicate<Npc> npcPredicate) {
        this(npcPredicate, a -> true, new DialogueWorker(), null);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action) {
        this(npcPredicate, action, new DialogueWorker(), null);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, DialogueWorker dialogueWorker) {
        this(npcPredicate, a -> true, dialogueWorker, null);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, MovementWorker movementWorker) {
        this(npcPredicate, a -> true, new DialogueWorker(), movementWorker);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, DialogueWorker dialogueWorker, MovementWorker movementWorker) {
        this(npcPredicate, a -> true, dialogueWorker, movementWorker);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, DialogueWorker dialogueWorker) {
        this(npcPredicate, action, dialogueWorker, null);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, MovementWorker movementWorker) {
        this(npcPredicate, action, new DialogueWorker(), movementWorker);
    }

    public NpcWorker(Predicate<Npc> npcPredicate, Predicate<String> action, DialogueWorker dialogueWorker, MovementWorker movementWorker) {
        this.npcPredicate = npcPredicate;
        this.action = action;
        this.dialogueWorker = dialogueWorker;
        this.movementWorker = movementWorker;
    }

    @Override
    public boolean needsRepeat() {
        return dialogueWorker.needsRepeat() || (movementWorker != null && movementWorker.needsRepeat());
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1)
            return;

        if (Dialog.isOpen() || Game.isInCutscene() || Game.isLoadingRegion() || Dialog.isProcessing()) {
            dialogueWorker.work();
            return;
        }

        final Npc npc = Npcs.getNearest(npcPredicate);
        if (npc == null) {
            if (movementWorker == null)
                return;

            movementWorker.work();
            return;
        }

        if (!npc.isPositionInteractable()) {
            Movement.walkTo(npc);
            return;
        }

        npc.interact(action);
        Time.sleepWhile(() -> Players.getLocal().isMoving(), 2500);
    }

    @Override
    public String toString() {
        return "Executing npc worker.";
    }
}

