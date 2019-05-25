package org.api.script.framework.mule_management;

import com.google.gson.JsonArray;
import org.api.http.bot.session.MuleOrderData;
import org.rspeer.ui.Log;

import java.util.logging.Level;

public class MuleManagementTrackerThread implements Runnable {

    private final MuleManagementTracker muleManagementTracker;
    private int muleBotID;

    public MuleManagementTrackerThread(MuleManagementTracker muleManagementTracker) {
        this.muleManagementTracker = muleManagementTracker;
    }

    @Override
    public void run() {
        Log.log(Level.WARNING, "Info", "Executing RSVega mule order tracking.");
        final JsonArray muleOrderJson = MuleOrderData.getNewestMuleOrderByBotId(muleManagementTracker.getSpxScript().getRsVegaTracker().getBotId());
        if (muleOrderJson == null || muleOrderJson.size() <= 0)
            return;

        final int muleBotId = muleOrderJson.get(0).getAsJsonObject().get("mule_bot_id").getAsInt();
        if (muleBotId <= 0)
            return;

        setMuleBotID(muleBotId);
    }

    public int getMuleBotID() {
        return muleBotID;
    }

    public void setMuleBotID(int muleBotID) {
        this.muleBotID = muleBotID;
    }
}
