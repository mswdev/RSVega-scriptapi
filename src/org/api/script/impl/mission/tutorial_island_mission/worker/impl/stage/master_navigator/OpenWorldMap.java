package org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.master_navigator;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;

public class OpenWorldMap extends Worker {

    @Override
    public void work() {
        final InterfaceComponent worldMapComponent = Interfaces.newQuery().actions(a -> a.equals("Floating World Map")).results().first();
        if (worldMapComponent == null)
            return;

        if (worldMapComponent.interact(a -> true))
            Time.sleepUntil(() -> Interfaces.newQuery().actions(a -> a.equals("Close Floating panel")).results().first() != null, 1500);
    }

    @Override
    public String toString() {
        return "Opening world map.";
    }
}
