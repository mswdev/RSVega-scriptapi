package org.api.script.impl.mission.tutorial_island_mission.worker.impl.at_start;

import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.tutorial_island_mission.data.DesignOption;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CharacterDesignWorker extends Worker {

    private static final int INTER_MASTER = 269;
    private static final int INTER_ACCEPT_COMPONENT = 99;

    private boolean shouldExecute() {
        final InterfaceComponent CHARACTER_DESIGN = Interfaces.getFirst(INTER_MASTER, a -> a.getIndex() == INTER_ACCEPT_COMPONENT);
        return CHARACTER_DESIGN != null && CHARACTER_DESIGN.isVisible();
    }

    @Override
    public void work() {
        final List<DesignOption> designOptions = new ArrayList<>(Arrays.asList(DesignOption.values()));
        Collections.shuffle(designOptions);
        designOptions.remove(DesignOption.GENDER);
        designOptions.add(0, DesignOption.GENDER);

        for (DesignOption option : designOptions) {
            final InterfaceComponent leftOption = Interfaces.getFirst(INTER_MASTER, a -> a.getIndex() == option.getComponentIndexLeft());
            final InterfaceComponent rightOption = Interfaces.getFirst(INTER_MASTER, a -> a.getIndex() == option.getComponentIndexRight());
            if (leftOption == null || rightOption == null)
                continue;

            int options = Random.nextInt(0, option.getTotalOptions());
            for (int i = 0; i < options; i++) {
                final boolean rightClick = Random.nextInt(0, 1) == 0;
                if (rightClick)
                    rightOption.interact(a -> true);
                else
                    leftOption.interact(a -> true);
            }
        }

        final InterfaceComponent ACCEPT_BUTTON = Interfaces.getFirst(INTER_MASTER, a -> a.getIndex() == INTER_ACCEPT_COMPONENT);
        if (ACCEPT_BUTTON == null)
            return;

        if (ACCEPT_BUTTON.interact(a -> true))
            Time.sleepUntil(() -> !shouldExecute(), 1500);
    }

    @Override
    public String toString() {
        return "Executing character design worker.";
    }
}

