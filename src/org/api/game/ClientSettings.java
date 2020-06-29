package org.api.game;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.ui.Log;

import java.util.Comparator;

public class ClientSettings {

    /**
     * Sets the brightness level.
     *
     * @param level The brightness level to set; must be 0-3.
     * @return True if the brightness level was set; false otherwise.
     */
    public static boolean setBrightnessLevel(int level) {
        final Object[] brightnessIndexes = Interfaces.newQuery().actions(a -> a.equals("Adjust Screen Brightness")).results().stream().sorted(Comparator.comparingInt(InterfaceComponent::getComponentIndex)).toArray();
        if (brightnessIndexes.length <= level)
            return false;

        final Object screenBrightnessComponent = brightnessIndexes[level];
        if (screenBrightnessComponent == null)
            return false;

        final String screenBrightnessComponentString = screenBrightnessComponent.toString();
        if (screenBrightnessComponentString == null)
            return false;

        final String[] screenBrightnessComponentsString = screenBrightnessComponentString.split(",");
        if (screenBrightnessComponentsString.length <= 1)
            return false;

        final InterfaceComponent screenBrightnessIndex = Interfaces.getComponent(Integer.parseInt(screenBrightnessComponentsString[0].trim()), Integer.parseInt(screenBrightnessComponentsString[1].trim()));
        return screenBrightnessIndex != null && screenBrightnessIndex.click();
    }

    /**
     * Sets the music audio level.
     *
     * @param level The music audio level to set; must be 0-4.
     * @return True if the music audio level was set; false otherwise.
     */
    public static boolean setMusicAudioLevel(int level) {
        final Object[] musicAudioIndexes = Interfaces.newQuery().actions(a -> a.equals("Adjust Music Volume")).results().stream().sorted(Comparator.comparingInt(InterfaceComponent::getComponentIndex)).toArray();
        if (musicAudioIndexes.length <= level)
            return false;

        final Object musicComponent = musicAudioIndexes[level];
        if (musicComponent == null)
            return false;

        final String musicComponentString = musicComponent.toString();
        if (musicComponentString == null)
            return false;

        final String[] musicComponentsString = musicComponentString.split(",");
        if (musicComponentsString.length <= 1)
            return false;

        final InterfaceComponent musicAudioIndex = Interfaces.getComponent(Integer.parseInt(musicComponentsString[0].trim()), Integer.parseInt(musicComponentsString[1].trim()));
        return musicAudioIndex != null && musicAudioIndex.click();
    }

    /**
     * Sets the effect audio level.
     *
     * @param level The effect audio level to set; must be 0-4.
     * @return True if the effect audio level was set; false otherwise.
     */
    public static boolean setEffectAudioLevel(int level) {
        final Object[] audioEffectIndexes = Interfaces.newQuery().actions(a -> a.equals("Adjust Sound Effect Volume")).results().stream().sorted(Comparator.comparingInt(InterfaceComponent::getComponentIndex)).toArray();
        if (audioEffectIndexes.length  <= level)
            return false;

        final Object audioEffectComponent = audioEffectIndexes[level];
        if (audioEffectComponent == null)
            return false;

        final String audioEffectComponentString = audioEffectComponent.toString();
        if (audioEffectComponentString == null)
            return false;

        final String[] audioEffectComponentsString = audioEffectComponentString.split(",");
        if (audioEffectComponentsString.length <= 1)
            return false;

        final InterfaceComponent audioEffectIndex = Interfaces.getComponent(Integer.parseInt(audioEffectComponentsString[0].trim()), Integer.parseInt(audioEffectComponentsString[1].trim()));
        return audioEffectIndex != null && audioEffectIndex.click();
    }

    /**
     * Sets the area audio level.
     *
     * @param level The area audio level to set; must be 0-4.
     * @return True if the area audio level was set; false otherwise.
     */
    public static boolean setAreaAudioLevel(int level) {
        final Object[] areaAudioIndexes = Interfaces.newQuery().actions(a -> a.equals("Adjust Area Sound Effect Volume")).results().stream().sorted(Comparator.comparingInt(InterfaceComponent::getComponentIndex)).toArray();
        if (areaAudioIndexes.length <= level)
            return false;

        final Object areaAudioComponent = areaAudioIndexes[level];
        if (areaAudioComponent == null)
            return false;

        final String areaAudioComponentString = areaAudioComponent.toString();
        if (areaAudioComponentString == null)
            return false;

        final String[] areaAudioComponentsString = areaAudioComponentString.split(",");
        if (areaAudioComponentsString.length <= 1)
            return false;

        final InterfaceComponent areaAudioIndex = Interfaces.getComponent(Integer.parseInt(areaAudioComponentsString[0].trim()), Integer.parseInt(areaAudioComponentsString[1].trim()));
        return areaAudioIndex != null && areaAudioIndex.click();
    }

    /**
     * Toggles roof visibility on or off.
     *
     * @return True if the roof visibility was successfully toggled.
     */
    public static boolean toggleRoofs() {
        final InterfaceComponent advancedOptionsButton = Interfaces.newQuery().names(a -> a.contains("Advanced options")).results().first();
        if (advancedOptionsButton == null)
            return false;

        final InterfaceComponent toggleRoofsButton = Interfaces.newQuery().actions(a -> a.equals("Roof-removal")).results().first();
        if (toggleRoofsButton == null)
            advancedOptionsButton.click();

        return toggleRoofsButton != null && toggleRoofsButton.click();
    }
}

