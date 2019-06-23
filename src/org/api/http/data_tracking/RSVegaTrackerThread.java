package org.api.http.data_tracking;

import com.google.gson.JsonArray;
import org.api.http.data_tracking.data_tracker_factory.impl.account.AccountDataTracker;
import org.api.http.data_tracking.data_tracker_factory.impl.account.session.SessionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.impl.account.session.session_position.SessionPositionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.impl.account.stats.StatsOSRSDataTracker;
import org.api.http.data_tracking.data_tracker_factory.impl.user.UserDataTracker;
import org.api.http.data_tracking.data_tracker_factory.impl.user.system_info.SystemInfoDataTracker;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.SlaveManagementMission;
import org.rspeer.RSPeer;
import org.rspeer.runetek.api.Game;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

public class RSVegaTrackerThread implements Runnable {

    private final RSVegaTrackerWrapper rsVegaTrackerWrapper;
    private boolean isMule;

    public RSVegaTrackerThread(RSVegaTrackerWrapper rsVegaTrackerWrapper) {
        this.rsVegaTrackerWrapper = rsVegaTrackerWrapper;
    }

    @Override
    public void run() {
        if (Script.getRSPeerUser().getUsername().equalsIgnoreCase("sphiinx"))
            Log.log(Level.WARNING, "Info", "Executing RSVega data tracking.");

        if (getRsVegaTrackerWrapper().getUserDataTracker().getId() <= 0) {
            insertSystemInfo(getRsVegaTrackerWrapper().getUserDataTracker().getId());
        }

        if (getRsVegaTrackerWrapper().getUserDataTracker().getId() <= 0)
            return;

        final String currentEmail = getEmail();
        if (currentEmail == null || currentEmail.length() <= 0)
            return;

        if (getRsVegaTrackerWrapper().getEmail() == null)
            getRsVegaTrackerWrapper().setEmail(currentEmail);

        if (getRsVegaTrackerWrapper().getAccountDataTracker().getId() <= 0 || getRsVegaTrackerWrapper().getSessionDataTracker().getId() <= 0 || !currentEmail.equals(getRsVegaTrackerWrapper().getEmail())) {
            getRsVegaTrackerWrapper().setEmail(currentEmail);
            if (getRsVegaTrackerWrapper().getUserDataTracker().getId() > 0)
                insertAccount(getRsVegaTrackerWrapper().getUserDataTracker().getId(), isMule());

            if (getRsVegaTrackerWrapper().getAccountDataTracker().getId() > 0)
                insertSession(getRsVegaTrackerWrapper().getUserDataTracker().getId(), getRsVegaTrackerWrapper().getAccountDataTracker().getId(), getRsVegaTrackerWrapper().getSpxScript().getMeta().name(), new Date());
        }

        if (getRsVegaTrackerWrapper().getAccountDataTracker().getId() <= 0 || getRsVegaTrackerWrapper().getSessionDataTracker().getId() <= 0)
            return;

        if (getRsVegaTrackerWrapper().getSpxScript().getMissionHandler() != null && getRsVegaTrackerWrapper().getSpxScript().getMissionHandler().getCurrent() != null)
            setMule(getRsVegaTrackerWrapper().getSpxScript().getMissionHandler().getCurrent() instanceof SlaveManagementMission);

        if (getRsVegaTrackerWrapper().getAccountDataTracker().getId() > 0) {
            updateAccount(getRsVegaTrackerWrapper().getUserDataTracker().getId(), isMule());

            if (Game.isLoggedIn())
                updateStatsOSRS();
        }

        if (getRsVegaTrackerWrapper().getSessionDataTracker().getId() > 0)
            updateSession(getRsVegaTrackerWrapper().getUserDataTracker().getId(), getRsVegaTrackerWrapper().getAccountDataTracker().getId(), new Date());

        insertSessionPosition(getRsVegaTrackerWrapper().getUserDataTracker().getId(), getRsVegaTrackerWrapper().getAccountDataTracker().getId(), getRsVegaTrackerWrapper().getSessionDataTracker().getId());
    }

    private void insertUser() {
        try {
            final JsonArray jsonArray = getRsVegaTrackerWrapper().getUserDataTracker().post(UserDataTracker.getUserData());
            getRsVegaTrackerWrapper().getUserDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSystemInfo(int userId) {
        try {
            final JsonArray jsonArray = getRsVegaTrackerWrapper().getSystemInfoDataTracker().post(SystemInfoDataTracker.getUserData(userId));
            getRsVegaTrackerWrapper().getSystemInfoDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertAccount(int userId, boolean isMule) {
        try {
            final JsonArray jsonArray = getRsVegaTrackerWrapper().getAccountDataTracker().post(AccountDataTracker.getAccountData(userId, isMule));
            final int insertId = getInsertId(jsonArray);
            getRsVegaTrackerWrapper().getAccountDataTracker().setId(insertId);
            getRsVegaTrackerWrapper().getStatsOSRSDataTracker().setId(insertId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAccount(int userId, boolean isMule) {
        try {
            getRsVegaTrackerWrapper().getAccountDataTracker().put(AccountDataTracker.getAccountData(userId, isMule));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStatsOSRS() {
        try {
            getRsVegaTrackerWrapper().getStatsOSRSDataTracker().put(StatsOSRSDataTracker.getStatsOSRSData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSession(int userId, int accountId, String scriptName, Date timeStarted) {
        try {
            final JsonArray jsonArray = getRsVegaTrackerWrapper().getSessionDataTracker().post(SessionDataTracker.getSessionData(userId, accountId, scriptName, timeStarted, null));
            getRsVegaTrackerWrapper().getSessionDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSession(int userId, int accountId, Date timeEnded) {
        try {
            getRsVegaTrackerWrapper().getSessionDataTracker().put(SessionDataTracker.getSessionData(userId, accountId, null, null, timeEnded));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSessionPosition(int userId, int accountId, int sessionId) {
        try {
            final JsonArray jsonArray = getRsVegaTrackerWrapper().getSessionPositionDataTracker().post(SessionPositionDataTracker.getSessionPositionData(userId, accountId, sessionId));
            getRsVegaTrackerWrapper().getSessionPositionDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getInsertId(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() <= 0)
            return 0;

        final int insertId = jsonArray.get(0).getAsJsonObject().get("insertId").getAsInt();
        if (insertId < 0)
            return 0;

        return insertId;
    }

    private String getEmail() {
        if (RSPeer.getGameAccount() == null)
            return null;

        return RSPeer.getGameAccount().getUsername();
    }

    private RSVegaTrackerWrapper getRsVegaTrackerWrapper() {
        return rsVegaTrackerWrapper;
    }

    private boolean isMule() {
        return isMule;
    }

    private void setMule(boolean mule) {
        isMule = mule;
    }
}
