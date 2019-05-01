package org.api.script.impl.mission.tutorial_island_mission.data;

public enum DesignOption {

    HEAD(106, 113, 24),
    JAW(107, 114, 15),
    TORSO(108, 115, 15),
    ARMS(109, 116, 12),
    HANDS(110, 117, 2),
    LEGS(111, 118, 11),
    FEET(112, 119, 2),
    HAIR(105, 121, 26),
    TORSO_COLOR(123, 127, 30),
    LEGS_COLOR(122, 129, 16),
    FEET_COLOR(124, 130, 7),
    SKIN(125, 131, 9),
    GENDER(136, 137, 2);

    private final int componentIndexLeft;
    private final int componentIndexRight;

    private final int totalOptions;

    DesignOption(int componentIndexLeft, int componentIndexRight, int totalOptions) {
        this.componentIndexLeft = componentIndexLeft;
        this.componentIndexRight = componentIndexRight;
        this.totalOptions = totalOptions;
    }

    public int getComponentIndexLeft() {
        return componentIndexLeft;
    }

    public int getComponentIndexRight() {
        return componentIndexRight;
    }

    public int getTotalOptions() {
        return totalOptions;
    }
}

