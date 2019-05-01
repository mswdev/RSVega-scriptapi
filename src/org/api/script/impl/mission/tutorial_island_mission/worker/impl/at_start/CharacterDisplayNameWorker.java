package org.api.script.impl.mission.tutorial_island_mission.worker.impl.at_start;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.tutorial_island_mission.TutorialIslandMission;
import org.api.script.impl.mission.tutorial_island_mission.data.DisplayNameType;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.Interfaces;

public class CharacterDisplayNameWorker extends Worker {

    static final int DISPLAY_NAME_VARPBIT = 5605;
    private static final int CHOOSE_DISPLAY_NAME_INTER = 558;
    private final TutorialIslandMission mission;

    CharacterDisplayNameWorker(TutorialIslandMission mission) {
        this.mission = mission;
    }

    @Override
    public boolean needsRepeat() {
        return Varps.getBitValue(DISPLAY_NAME_VARPBIT) != 0;
    }

    @Override
    public void work() {
        if (Varps.getBitValue(DISPLAY_NAME_VARPBIT) == DisplayNameType.SEARCHING.getVarpbitValue())
            return;

        if (Varps.getBitValue(DISPLAY_NAME_VARPBIT) == DisplayNameType.NOT_AVAILABLE.getVarpbitValue()) {
            final InterfaceComponent displayNameSuggestion = Interfaces.getFirst(CHOOSE_DISPLAY_NAME_INTER, a -> a.getIndex() == Random.nextInt(14, 16));
            if (displayNameSuggestion != null && displayNameSuggestion.isVisible()) {
                displayNameSuggestion.click();
                return;
            }

            final InterfaceComponent lookupDisplayName = Interfaces.getFirst(CHOOSE_DISPLAY_NAME_INTER, a -> a.containsAction("Look up name"));
            if (lookupDisplayName == null)
                return;

            if (!EnterInput.isOpen()) {
                lookupDisplayName.click();
                Time.sleepUntil(EnterInput::isOpen, 1500);
            }

            String displayName = mission.getScript().getAccount().getUsername().split("@")[0];
            EnterInput.initiate(displayName);
        }

        final InterfaceComponent setDisplayName = Interfaces.getFirst(CHOOSE_DISPLAY_NAME_INTER, a -> a.containsAction("Set name") && a.isVisible() && a.getText().isEmpty());
        if (setDisplayName == null)
            return;

        setDisplayName.click();
        Time.sleepUntil(() -> Varps.getBitValue(DISPLAY_NAME_VARPBIT) == DisplayNameType.SET.getVarpbitValue(), 1500);
    }

    @Override
    public String toString() {
        return "Executing character display name worker.";
    }
}

