package org.api.game.player;

import org.rspeer.runetek.api.Varps;

public class Player {

    private static final int TUTORIAL_ISLAND_VARP = 281;
    private static final int TUTORIAL_ISLAND_FINISHED = 1000;
    private static final int IRONMAN_VARP = 1777;

    /**
     * Determines whether the player has completed tutorial island.
     *
     * @return True if the player has not completed tutorial island; false otherwise.
     */
    public static boolean isTutorial() {
        return Varps.get(TUTORIAL_ISLAND_VARP) < TUTORIAL_ISLAND_FINISHED;
    }

    /**
     * Gets the players ironman state.
     *
     * @return The players ironman state.
     */
    public static IronmanState getIronManState() {
        final int ironmanState = Varps.getBitValue(IRONMAN_VARP);
        if (ironmanState == IronmanState.NONE.getState())
            return IronmanState.NONE;

        if (ironmanState == IronmanState.IRONMAN.getState())
            return IronmanState.IRONMAN;

        if (ironmanState == IronmanState.ULTIMATE.getState())
            return IronmanState.ULTIMATE;

        if (ironmanState == IronmanState.HARDCORE.getState())
            return IronmanState.HARDCORE;

        return IronmanState.NONE;
    }

}
