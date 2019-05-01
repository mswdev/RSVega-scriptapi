package org.api.script.impl.mission.tutorial_island_mission.data;

public enum DisplayNameType {

    UNKNOWN(0),
    NOT_AVAILABLE(1),
    SEARCHING(2),
    AVAILABLE(4),
    SET(5);

    public final int varpbitValue;

    DisplayNameType(int varpbitValue) {
        this.varpbitValue = varpbitValue;
    }

    public int getVarpbitValue() {
        return varpbitValue;
    }
}
