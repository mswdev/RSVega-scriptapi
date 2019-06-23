package org.api.script.impl.mission.tutorial_island_mission.worker;

import org.api.game.questing.QuestType;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.tutorial_island_mission.TutorialIslandMission;
import org.api.script.impl.mission.tutorial_island_mission.data.TutorialState;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.at_end.*;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.at_start.CharacterSetupWorker;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.SceneObjects;

public class TutorialIslandWorkerHandler extends WorkerHandler {

    private final TutorialIslandMission mission;
    private final CharacterSetupWorker characterSetupWorker;
    private final HideRoofs hideRoofs;
    private final SetAudio setAudio;
    private final SetBrightness setBrightness;
    private final DropItems dropItems;
    private final DepositItems bankItems;
    private final StayLoggedIn stayLoggedIn;
    private final WalkToPosition walkToPosition;
    private final Logout logout;

    public TutorialIslandWorkerHandler(TutorialIslandMission mission) {
        this.mission = mission;
        characterSetupWorker = new CharacterSetupWorker(mission);
        hideRoofs = new HideRoofs();
        setAudio = new SetAudio(mission);
        setBrightness = new SetBrightness(mission);
        dropItems = new DropItems();
        bankItems = new DepositItems();
        stayLoggedIn = new StayLoggedIn();
        walkToPosition = new WalkToPosition(mission);
        logout = new Logout(mission);
    }

    @Override
    public Worker decide() {
        if (TutorialState.isInVarp(TutorialState.CHARACTER_DESIGN))
            return characterSetupWorker;

        if (QuestType.TUTORIAL_ISLAND.isComplete()) {
            if (Dialog.canContinue())
                Dialog.processContinue();

            if (mission.getArgs().hideRoofs && !InterfaceOptions.Display.isRoofsHidden())
                return hideRoofs;

            if (mission.getArgs().setAudio > 0 && (5 - InterfaceOptions.Audio.getMusicVolume() != mission.getArgs().setAudio || 5 - InterfaceOptions.Audio.getSoundEffectVolume() != mission.getArgs().setAudio || 5 - InterfaceOptions.Audio.getAreaSoundVolume() != mission.getArgs().setAudio))
                return setAudio;

            if (mission.getArgs().setBrightness > 0 && InterfaceOptions.Display.getBrightness() != mission.getArgs().setBrightness)
                return setBrightness;

            if (mission.getArgs().dropItems && Inventory.getCount() > 0)
                return dropItems;

            if (mission.getArgs().bankItems && Inventory.getCount() > 0)
                return bankItems;

            if (mission.getArgs().walkPosition != null && mission.getArgs().walkPosition.distance() > 10)
                return walkToPosition;

            if (mission.getArgs().stayLoggedIn)
                return stayLoggedIn;

            if (mission.getArgs().stayLoggedInAndEnd) {
                mission.setShouldEnd(true);
                return null;
            }

            return logout;
        }

        return TutorialState.getValidState().getWorker();
    }
}

