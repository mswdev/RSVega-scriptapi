package org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.magic_instructor;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Magic;
import org.rspeer.runetek.api.component.tab.Spell;
import org.rspeer.runetek.api.scene.Players;


public class CastHomeTeleport extends Worker {

    @Override
    public void work() {
        if (Players.getLocal().getAnimation() != -1) {
            return;
        }

        if (Magic.cast(Spell.Modern.HOME_TELEPORT)) {
            Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1, 1200);
        }
    }

    @Override
    public String toString() {
        return "Casting home teleport.";
    }
}
