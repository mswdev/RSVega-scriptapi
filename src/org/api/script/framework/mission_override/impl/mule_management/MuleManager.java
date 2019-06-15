package org.api.script.framework.mission_override.impl.mule_management;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.rspeer.runetek.api.movement.position.Position;

public class MuleManager {

    private JsonObject accountData;

    public MuleManager(JsonArray accountData) {
        if (accountData == null || accountData.size() <= 0)
            return;

        this.accountData = accountData.get(0).getAsJsonObject();
    }

    public JsonObject getAccountData() {
        return accountData;
    }

    public String getDisplayName() {
        return getAccountData().get("display_name").getAsString();
    }

    public int getWorld() {
        return getAccountData().get("world").getAsInt();
    }

    public Position getPosition() {
        final int x = getAccountData().get("position_x").getAsInt();
        final int y = getAccountData().get("position_y").getAsInt();
        final int z = getAccountData().get("position_z").getAsInt();
        return new Position(x, y, z);
    }
}
