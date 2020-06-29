package org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.prayer_instructor;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Prayer;
import org.rspeer.runetek.api.component.tab.Prayers;

public class ActivatePrayer extends Worker {

    @Override
    public void work() {
        if (Prayers.isActive(Prayer.THICK_SKIN)) {
            return;
        }

        if (Prayers.toggle(true, Prayer.THICK_SKIN)) {
            Time.sleepUntil(() -> Prayers.isActive(Prayer.THICK_SKIN), 1200);
        }
    }

    @Override
    public String toString() {
        return "Activating prayer.";
    }
}
