package org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.impl;

import com.google.gson.JsonArray;
import org.api.http.bot.session.MuleOrderData;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.SlaveManagementMission;
import org.rspeer.ui.Log;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SlaveManagementTrackerThread implements Runnable {

    private final SlaveManagementMission mission;

    public SlaveManagementTrackerThread(SlaveManagementMission mission) {
        this.mission = mission;
        mission.getSpxScript().getScheduledThreadPoolExecutor().scheduleAtFixedRate(this, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        Log.log(Level.WARNING, "Info", "Executing RSVega mule order tracking.");
        final int muleAccountId = mission.getSpxScript().getRsVegaTracker().getAccountId();
        final JsonArray unassignedMuleOrder = MuleOrderData.getUnassignedMuleOrderByAccountId(muleAccountId);
        if (unassignedMuleOrder == null || unassignedMuleOrder.size() <= 0)
            return;

        final int muleBotId = mission.getSpxScript().getRsVegaTracker().getBotId();
        final int slaveAccountId = unassignedMuleOrder.get(0).getAsJsonObject().get("account_id").getAsInt();
        final int slaveBotId = unassignedMuleOrder.get(0).getAsJsonObject().get("bot_id").getAsInt();
        final int slaveSessionId = unassignedMuleOrder.get(0).getAsJsonObject().get("session_id").getAsInt();
        mission.getSpxScript().getRsVegaTracker().updateMuleOrder(slaveAccountId, slaveBotId, slaveSessionId, muleBotId);
    }
}
