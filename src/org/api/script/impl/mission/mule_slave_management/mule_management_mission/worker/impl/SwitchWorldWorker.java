package org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.WorldHopper;

public class SwitchWorldWorker extends Worker {

    private int worldToSwitch;

    @Override
    public void work() {
        if (Interfaces.getOpen().size() > 0) {
            if (Interfaces.closeAll())
                Time.sleepUntil(() -> Interfaces.getOpen().size() <= 0, 1500);

            return;
        }

        if (WorldHopper.hopTo(getWorldToSwitch()))
            Time.sleepUntil(() -> Worlds.getCurrent() == getWorldToSwitch(), 2500);
    }

    @Override
    public String toString() {
        return "Executing switch world to switch worker.";
    }

    public int getWorldToSwitch() {
        return worldToSwitch;
    }

    public void setWorldToSwitch(int worldToSwitch) {
        this.worldToSwitch = worldToSwitch;
    }
}
