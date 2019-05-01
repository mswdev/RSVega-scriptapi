package org.api.game;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.input.Mouse;

import java.awt.*;

@SuppressWarnings("deprecation")
public class ClientSettings {

    private static final int INTER_OPTION_MASTER = 261;

    private static final int INTER_ADVANCED_MASTER = 60;
    private static final int INTER_ADVANCED_BUTTON = 35;
    private static final int INTER_TOGGLE_ROOFS = 14;

    private static final int INTER_ZOOM = 15;
    private static final int INTER_BRIGHTNESS = 17;
    private static final int INTER_MUSIC_AUDIO = 44;
    private static final int INTER_EFFECT_AUDIO = 50;
    private static final int INTER_AREA_AUDIO = 56;

    /**
     * Gets the zoom level.
     *
     * @return The zoom level between 1-5.
     */
    public static int getZoomLevel() {
        final InterfaceComponent zoomOption = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_ZOOM);
        if (zoomOption == null)
            return 0;

        final InterfaceComponent[] zoomIndexes = Interfaces.get(INTER_OPTION_MASTER, a -> a.getIndex() >= 9 && a.getIndex() <= 13);
        if (zoomIndexes.length < 5)
            return 0;

        for (InterfaceComponent zoomLvl : zoomIndexes) {
            if (!zoomLvl.getBounds().contains(zoomOption.getBounds()))
                continue;

            return zoomLvl.getIndex() - 8;
        }

        return 0;
    }

    /**
     * Sets the zoom level.
     *
     * @param level The zoom level to set; must be 1-5.
     * @return True if the zoom level was set; false otherwise.
     */
    public static boolean setZoomLevel(int level) {
        final InterfaceComponent zoomIndexes = Interfaces.getComponent(INTER_OPTION_MASTER, level + 8);
        if (zoomIndexes == null)
            return false;

        final Point point = Random.nextPoint(zoomIndexes.getBounds());
        Mouse.click(true, point.x, point.y);
        return true;
    }

    /**
     * Sets the brightness level.
     *
     * @param level The brightness level to set; must be 1-4.
     * @return True if the brightness level was set; false otherwise.
     */
    public static boolean setBrightnessLevel(int level) {
        final InterfaceComponent brightnessIndex = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_BRIGHTNESS + level);
        return brightnessIndex != null && brightnessIndex.click();
    }

    /**
     * Sets the music audio level.
     *
     * @param level The music audio level to set; must be 1-5.
     * @return True if the music audio level was set; false otherwise.
     */
    public static boolean setMusicAudioLevel(int level) {
        final InterfaceComponent musicAudioIndex = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_MUSIC_AUDIO + level);
        return musicAudioIndex != null && musicAudioIndex.click();
    }

    /**
     * Sets the effect audio level.
     *
     * @param level The effect audio level to set; must be 1-5.
     * @return True if the effect audio level was set; false otherwise.
     */
    public static boolean setEffectAudioLevel(int level) {
        final InterfaceComponent effectAudioIndex = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_EFFECT_AUDIO + level);
        return effectAudioIndex != null && effectAudioIndex.click();
    }

    /**
     * Sets the area audio level.
     *
     * @param level The area audio level to set; must be 1-5.
     * @return True if the area audio level was set; false otherwise.
     */
    public static boolean setAreaAudioLevel(int level) {
        final InterfaceComponent areaAudioIndex = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_AREA_AUDIO + level);
        return areaAudioIndex != null && areaAudioIndex.click();
    }

    /**
     * Toggles roof visibility on or off.
     *
     * @return True if the roof visibility was successfully toggled.
     */
    public static boolean toggleRoofs() {
        final InterfaceComponent advancedButton = Interfaces.getComponent(INTER_OPTION_MASTER, INTER_ADVANCED_BUTTON);
        if (advancedButton != null)
            advancedButton.click();

        final InterfaceComponent toggleRoofs = Interfaces.getComponent(INTER_ADVANCED_MASTER, INTER_TOGGLE_ROOFS);
        return toggleRoofs != null && toggleRoofs.click();
    }
}

