package org.api.script.impl.mission.tutorial_island_mission.worker.impl.at_start;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.tutorial_island_mission.TutorialIslandMission;
import org.api.script.impl.mission.tutorial_island_mission.data.DisplayNameType;
import org.rspeer.runetek.api.Varps;

public class CharacterSetupWorker extends Worker {

    private final CharacterDesignWorker characterDesignWorker;
    private final CharacterDisplayNameWorker characterDisplayNameWorker;

    public CharacterSetupWorker(TutorialIslandMission mission) {
        this.characterDesignWorker = new CharacterDesignWorker();
        this.characterDisplayNameWorker = new CharacterDisplayNameWorker(mission);
    }

    @Override
    public void work() {
        if (Varps.getBitValue(CharacterDisplayNameWorker.DISPLAY_NAME_VARPBIT) == DisplayNameType.UNKNOWN.getVarpbitValue() || Varps.getBitValue(CharacterDisplayNameWorker.DISPLAY_NAME_VARPBIT) == DisplayNameType.SET.getVarpbitValue())
            characterDesignWorker.work();

        characterDisplayNameWorker.work();
    }

    @Override
    public String toString() {
        return "Executing character setup worker.";
    }
}

