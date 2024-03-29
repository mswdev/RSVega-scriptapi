package org.api.script.impl.mission.firemaking_mission.worker.impl;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.firemaking_mission.FireMakingMission;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

public class LightFire extends Worker {

    private final FireMakingMission mission;

    public LightFire(FireMakingMission mission) {
        this.mission = mission;
    }

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1 || Players.getLocal().isMoving())
            return;

        final Item tinderbox = Inventory.getFirst(WithdrawTinderBox.TINDERBOX);
        final Item logs = Inventory.getFirst(mission.getArgs().logType.getName());
        if (tinderbox == null || logs == null)
            return;

        if (Inventory.use(WithdrawTinderBox.TINDERBOX, logs))
            if (!Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1 || Players.getLocal().isMoving(), 2500)) {
                Log.severe("[STUCK]: Cannot light fire; moving to next lane");
                mission.getIgnoredTiles().add(Players.getLocal().getPosition());
                mission.setIsStuckInLane(true);
            }
    }

    @Override
    public String toString() {
        return "Lighting fire.";
    }

}

