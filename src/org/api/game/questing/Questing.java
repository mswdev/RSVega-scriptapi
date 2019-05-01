package org.api.game.questing;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;

import java.util.Arrays;

public class Questing {

    private static final int QUEST_INTERFACE_SUB_TAB = 629;
    private static final int QUEST_INTERFACE = 399;
    private static final int QUEST_FREE_INTERFACE = 6;
    private static final int QUEST_MEMBER_INTERFACE = 7;
    private static final int QUEST_MINI_INTERFACE = 8;
    private static final int COMPLETED_QUEST_COLOR = 901389;

    /**
     * Gets the players total quest points.
     *
     * @return The players total quest points.
     */
    public static int getPoints() {
        return Varps.get(101);
    }


    /**
     * Gets a string of the players completed quests.
     *
     * @return A string of the players completed quests.
     */
    public static String getCompletedQuests() {
        final InterfaceComponent[] questList = Interfaces.get(QUEST_INTERFACE);
        if (questList == null || questList.length <= 0) {
            final InterfaceComponent questTab = Interfaces.getFirst(QUEST_INTERFACE_SUB_TAB, a -> a.containsAction("Quest List"));
            if (questTab == null)
                return null;

            if (questTab.click())
                Time.sleepUntil(() -> Interfaces.get(QUEST_INTERFACE).length > 0, 2500);
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final InterfaceComponent[] freeQuests = Interfaces.getComponent(QUEST_INTERFACE, QUEST_FREE_INTERFACE).getComponents(a -> a.getTextColor() == COMPLETED_QUEST_COLOR);
        Arrays.stream(freeQuests).forEach(a -> stringBuilder.append(a.getText()).append(", "));

        final InterfaceComponent[] memberQuests = Interfaces.getComponent(QUEST_INTERFACE, QUEST_MEMBER_INTERFACE).getComponents(a -> a.getTextColor() == COMPLETED_QUEST_COLOR);
        Arrays.stream(memberQuests).forEach(a -> stringBuilder.append(a.getText()).append(", "));

        final InterfaceComponent[] miniQuests = Interfaces.getComponent(QUEST_INTERFACE, QUEST_MINI_INTERFACE).getComponents(a -> a.getTextColor() == COMPLETED_QUEST_COLOR);
        Arrays.stream(miniQuests).forEach(a -> stringBuilder.append(a.getText()).append(", "));

        return stringBuilder.toString().replaceAll(", $", "");
    }
}

