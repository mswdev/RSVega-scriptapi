package org.api.script.impl.mission.nmz_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.worker.interactables.SceneObjectWorker;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.Interfaces;

import java.util.function.Predicate;

public class EnterDream extends Worker {


    public static final int ACTIVE_DREAM_VARPBIT = 4605;
    private static final int DREAM_COMPONENT_PREDICATE = 129;
    private static final int ACCEPT_DREAM_COMPONENT_PREDICATE = 6;
    private final Predicate<SceneObject> dreamPotionPredicate = a -> a.getName().contains("Potion");
    private final SceneObjectWorker sceneObjectWorker = new SceneObjectWorker(dreamPotionPredicate, a -> a.equals("Drink"));

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        final InterfaceComponent acceptDreamComponent = Interfaces.getComponent(DREAM_COMPONENT_PREDICATE, ACCEPT_DREAM_COMPONENT_PREDICATE);
        if (acceptDreamComponent == null) {
            sceneObjectWorker.work();
            return;
        }

        acceptDreamComponent.click();
    }

    @Override
    public String toString() {
        return "Entering dream";
    }
}

