package org.api.game;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Interfaces;

public class Wilderness {

    private static final int INTER_MASTER_ENTER_WILDERNESS_WARNING = 475;
    private static final int INTER_MASTER_WILDERNESS_LEVEL = 90;

    /**
     * Gets the wilderness level.
     *
     * @return The wilderness level.
     */
    public static int getLevel() {
        final InterfaceComponent level = Interfaces.getFirst(INTER_MASTER_WILDERNESS_LEVEL, a -> a.getText().contains("Level: "));
        return level == null ? 0 : Integer.parseInt(level.getText().replace("Level: ", ""));
    }

    /**
     * Checks whether the enter wilderness warning interface is present.
     *
     * @return True if the enter wilderness warning interface is present; false otherwise.
     */
    public static boolean hasWarning() {
        final InterfaceComponent enterWilderness = Interfaces.getFirst(INTER_MASTER_ENTER_WILDERNESS_WARNING, a -> a.containsAction("Enter Wilderness"));
        return enterWilderness != null;
    }

    /**
     * Clicks the enter wilderness button on the enter wilderness warning interface and checks the remember option if
     * present.
     *
     * @return True if the enter wilderness button was clicked; false otherwise.
     */
    public static boolean enter() {
        final InterfaceComponent enterWilderness = Interfaces.getFirst(INTER_MASTER_ENTER_WILDERNESS_WARNING, a -> a.containsAction("Enter Wilderness"));
        if (enterWilderness == null)
            return false;

        final InterfaceComponent enterWildernessRemember = Interfaces.getFirst(INTER_MASTER_ENTER_WILDERNESS_WARNING, a -> a.containsAction("Disable warning"));
        if (enterWildernessRemember != null)
            enterWildernessRemember.click();

        return enterWilderness.click();
    }
}

