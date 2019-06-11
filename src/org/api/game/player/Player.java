package org.api.game.player;

import org.rspeer.runetek.api.Varps;

import java.util.Arrays;

public class Player {

    private static final int IRONMAN_VARP = 1777;

    /**
     * Gets the players ironman state.
     *
     * @return The players ironman state.
     */
    public static IronmanState getIronManState() {
        final int ironmanState = Varps.getBitValue(IRONMAN_VARP);
        return Arrays.stream(IronmanState.values()).filter(a -> a.getState() == ironmanState).findFirst().orElse(IronmanState.NONE);
    }

}
