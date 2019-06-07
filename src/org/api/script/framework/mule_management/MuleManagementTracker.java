package org.api.script.framework.mule_management;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.api.script.SPXScript;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.rspeer.runetek.api.movement.position.Position;

public class MuleManagementTracker {

    private final SPXScript spxScript;
    private MuleManagementEntry muleManagementEntry;

    public MuleManagementTracker(SPXScript spxScript) {
        this.spxScript = spxScript;
    }

    public MuleManagementEntry getReadyMuleManagementEntry() {
        if (!(getSpxScript().getMissionHandler().getCurrent() instanceof MuleManagement))
            return null;

        if (getSpxScript().getMissionHandler().getCurrent().getClass().isAssignableFrom(MuleManagementMission.class))
            return null;

        if (muleManagementEntry == null) {
            muleManagementEntry = getMuleManagementEntry();
            return null;
        }

        final int userId = getSpxScript().getRsVegaTrackerWrapper().getUserDataTracker().getId();
        if (userId <= 0)
            return null;

        final JsonArray accountActiveIsMuleData = getSpxScript().getRsVegaTrackerWrapper().getRandomAccountActiveIsMule(userId);
        if (accountActiveIsMuleData == null || accountActiveIsMuleData.size() <= 0)
            return null;

        final MuleManager muleManager = createMuleManager(accountActiveIsMuleData);
        if (muleManager == null)
            return null;

        muleManagementEntry.setMuleManager(muleManager);
        return muleManagementEntry;
    }

    private MuleManagementEntry getMuleManagementEntry() {
        for (MuleManagementEntry muleManagementEntry : ((MuleManagement) getSpxScript().getMissionHandler().getCurrent()).itemsToMule()) {
            if (!muleManagementEntry.canMule())
                continue;

            return muleManagementEntry;
        }

        return null;
    }

    public void setMuleManagementEntry(MuleManagementEntry muleManagementEntry) {
        this.muleManagementEntry = muleManagementEntry;
    }

    private MuleManager createMuleManager(JsonArray accountActiveIsMuleData) {
        final JsonObject accountActiveIsMuleObject = accountActiveIsMuleData.get(0).getAsJsonObject();
        final String displayName = accountActiveIsMuleObject.get("display_name").getAsString();
        if (displayName == null)
            return null;

        final int world = accountActiveIsMuleObject.get("world").getAsInt();
        if (world <= 0)
            return null;

        final int x = accountActiveIsMuleObject.get("position_x").getAsInt();
        final int y = accountActiveIsMuleObject.get("position_y").getAsInt();
        final int z = accountActiveIsMuleObject.get("position_z").getAsInt();
        if (x <= 0 || y <= 0)
            return null;

        final Position position = new Position(x, y, z);
        return new MuleManager(displayName, world, position);
    }

    private SPXScript getSpxScript() {
        return spxScript;
    }

}
