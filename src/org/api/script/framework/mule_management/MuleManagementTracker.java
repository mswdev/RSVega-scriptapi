package org.api.script.framework.mule_management;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.api.http.bot.BotData;
import org.api.script.SPXScript;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.rspeer.runetek.api.movement.position.Position;

import java.util.concurrent.TimeUnit;

public class MuleManagementTracker {

    private final SPXScript spxScript;
    private final MuleManagementTrackerThread muleManagementTrackerThread;
    private MuleManagementEntry muleManagementEntry;
    private boolean hasInsertedRequest;

    public MuleManagementTracker(SPXScript spxScript) {
        this.spxScript = spxScript;
        this.muleManagementTrackerThread = new MuleManagementTrackerThread(this);
    }

    public MuleManagementEntry getReadyMuleManagementEntry() {
        if (!(spxScript.getMissionHandler().getCurrent() instanceof MuleManagement))
            return null;

        if (spxScript.getMissionHandler().getCurrent().getClass().isAssignableFrom(MuleManagementMission.class))
            return null;

        if (muleManagementEntry == null) {
            muleManagementEntry = getMuleManagementEntry();
            return null;
        }

        final int accountId = spxScript.getRsVegaTracker().getAccountId();
        final int botId = spxScript.getRsVegaTracker().getBotId();
        final int sessionId = spxScript.getRsVegaTracker().getSessionId();
        if (!hasInsertedRequest() && accountId > 0 && botId > 0 && sessionId > 0) {
            spxScript.getRsVegaTracker().insertMuleOrder(accountId, botId, sessionId);
            spxScript.getScheduledThreadPoolExecutor().scheduleAtFixedRate(muleManagementTrackerThread, 0, 15, TimeUnit.SECONDS);
            setHasInsertedRequest(true);
            return null;
        }

        if (getMuleManagementTrackerThread().getMuleBotID() <= 0)
            return null;

        final MuleManager muleManager = createMuleManager(getMuleManagementTrackerThread().getMuleBotID());
        if (muleManager == null)
            return null;

        muleManagementEntry.setMuleManager(muleManager);
        spxScript.getScheduledThreadPoolExecutor().remove(muleManagementTrackerThread);
        getMuleManagementTrackerThread().setMuleBotID(0);
        return muleManagementEntry;
    }

    private MuleManagementEntry getMuleManagementEntry() {
        for (MuleManagementEntry muleManagementEntry : ((MuleManagement) spxScript.getMissionHandler().getCurrent()).itemsToMule()) {
            if (!muleManagementEntry.canMule())
                continue;

            return muleManagementEntry;
        }

        return null;
    }

    private MuleManager createMuleManager(int muleBotId) {
        final JsonArray muleBotDataArray = BotData.getBotById(muleBotId);
        if (muleBotDataArray == null || muleBotDataArray.size() <= 0)
            return null;

        final JsonObject muleBotDataObject = muleBotDataArray.get(0).getAsJsonObject();

        final String displayName = muleBotDataObject.get("display_name").getAsString();
        if (displayName == null)
            return null;

        final int world = muleBotDataObject.get("world").getAsInt();
        if (world <= 0)
            return null;

        final int x = muleBotDataObject.get("position_x").getAsInt();
        final int y = muleBotDataObject.get("position_y").getAsInt();
        final int z = muleBotDataObject.get("position_z").getAsInt();
        if (x <= 0 || y <= 0)
            return null;

        final Position position = new Position(x, y, z);
        return new MuleManager(displayName, world, position);
    }

    public SPXScript getSpxScript() {
        return spxScript;
    }

    public MuleManagementTrackerThread getMuleManagementTrackerThread() {
        return muleManagementTrackerThread;
    }

    public void setMuleManagementEntry(MuleManagementEntry muleManagementEntry) {
        this.muleManagementEntry = muleManagementEntry;
    }

    private boolean hasInsertedRequest() {
        return hasInsertedRequest;
    }

    public void setHasInsertedRequest(boolean hasInsertedRequest) {
        this.hasInsertedRequest = hasInsertedRequest;
    }
}
