package org.api.script.framework.mule_management;

import org.rspeer.runetek.api.movement.position.Position;

public class MuleManager {

    private String displayName;
    private int world;
    private Position position;

    public MuleManager(String displayName, int world, Position position) {
        this.displayName = displayName;
        this.world = world;
        this.position = position;
    }
    public String getDisplayName() {
        return displayName;
    }

    public int getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }
}
