package org.api.script.impl.mission.nmz_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.DialogueWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.adapter.scene.Npc;

import java.util.function.Predicate;

public class BuyDream extends Worker {

    public static final int DREAM_POTION_VARBIT = 3946;
    private final Predicate<Npc> dominicOnionPredicate = a -> a.getName().equals("Dominic Onion");
    private final DialogueWorker dialogueWorker = new DialogueWorker(a -> a.contains("Previous") || a.contains("Yes"));
    private final NpcWorker npcWorker = new NpcWorker(dominicOnionPredicate, a -> a.contains("Dream"), dialogueWorker);

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        npcWorker.work();
    }

    @Override
    public String toString() {
        return "Buying dream";
    }
}

