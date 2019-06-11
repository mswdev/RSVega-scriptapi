package org.api.http.data_tracking;

import com.google.gson.JsonArray;
import org.api.http.data_tracking.data_tracker_factory.account.AccountDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.session.SessionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.session.session_position.SessionPositionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.stats.StatsOSRSDataTracker;
import org.api.http.data_tracking.data_tracker_factory.user.UserDataTracker;
import org.api.http.data_tracking.data_tracker_factory.user.system_info.SystemInfoDataTracker;
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
    private boolean insertedUser;
    private boolean isMule;


    public RSVegaTrackerThread(RSVegaTrackerWrapper rsVegaTrackerWrapper) {
        this.rsVegaTrackerWrapper = rsVegaTrackerWrapper;
    }

    @Override
    public void run() {
        if (Script.getRSPeerUser().getUsername().equalsIgnoreCase("sphiinx"))
            Log.log(Level.WARNING, "Info", "Executing RSVega data tracking.");

        if (!insertedUser()) {
            insertUser();
            insertSystemInfo(rsVegaTrackerWrapper.getUserDataTracker().getId());
            setInsertedUser(true);
        }

        final String currentEmail = getEmail();
        if (currentEmail == null)
            return;

        if (rsVegaTrackerWrapper.getEmail() == null)
            rsVegaTrackerWrapper.setEmail(currentEmail);

        if (rsVegaTrackerWrapper.getAccountDataTracker().getId() == 0 || !currentEmail.equals(rsVegaTrackerWrapper.getEmail())) {
            rsVegaTrackerWrapper.setEmail(currentEmail);
            if (rsVegaTrackerWrapper.getUserDataTracker().getId() > 0)
                insertAccount(rsVegaTrackerWrapper.getUserDataTracker().getId(), isMule());

            if (rsVegaTrackerWrapper.getAccountDataTracker().getId() > 0)
                insertSession(rsVegaTrackerWrapper.getUserDataTracker().getId(), rsVegaTrackerWrapper.getAccountDataTracker().getId(), rsVegaTrackerWrapper.getSpxScript().getMeta().name(), new Date());
        }

        if (rsVegaTrackerWrapper.getSpxScript().getMissionHandler() != null && rsVegaTrackerWrapper.getSpxScript().getMissionHandler().getCurrent() != null)
            setMule(rsVegaTrackerWrapper.getSpxScript().getMissionHandler().getCurrent() instanceof SlaveManagementMission);

        if (rsVegaTrackerWrapper.getAccountDataTracker().getId() > 0) {
            updateAccount(rsVegaTrackerWrapper.getUserDataTracker().getId(), isMule());

            if (Game.isLoggedIn())
                updateStatsOSRS();
        }

        if (rsVegaTrackerWrapper.getSessionDataTracker().getId() > 0)
            updateSession(rsVegaTrackerWrapper.getUserDataTracker().getId(), rsVegaTrackerWrapper.getAccountDataTracker().getId(), new Date());

        insertSessionPosition(rsVegaTrackerWrapper.getUserDataTracker().getId(), rsVegaTrackerWrapper.getAccountDataTracker().getId(), rsVegaTrackerWrapper.getSessionDataTracker().getId());
    }

    private void insertUser() {
        try {
            final JsonArray jsonArray = rsVegaTrackerWrapper.getUserDataTracker().post(UserDataTracker.getUserData());
            rsVegaTrackerWrapper.getUserDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSystemInfo(int userId) {
        try {
            final JsonArray jsonArray = rsVegaTrackerWrapper.getSystemInfoDataTracker().post(SystemInfoDataTracker.getUserData(userId));
            rsVegaTrackerWrapper.getSystemInfoDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertAccount(int userId, boolean isMule) {
        try {
            final JsonArray jsonArray = rsVegaTrackerWrapper.getAccountDataTracker().post(AccountDataTracker.getAccountData(userId, isMule));
            final int insertId = getInsertId(jsonArray);
            rsVegaTrackerWrapper.getAccountDataTracker().setId(insertId);
            rsVegaTrackerWrapper.getStatsOSRSDataTracker().setId(insertId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAccount(int userId, boolean isMule) {
        try {
            rsVegaTrackerWrapper.getAccountDataTracker().put(AccountDataTracker.getAccountData(userId, isMule));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStatsOSRS() {
        try {
            rsVegaTrackerWrapper.getStatsOSRSDataTracker().put(StatsOSRSDataTracker.getStatsOSRSData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSession(int userId, int accountId, String scriptName, Date timeStarted) {
        try {
            final JsonArray jsonArray = rsVegaTrackerWrapper.getSessionDataTracker().post(SessionDataTracker.getSessionData(userId, accountId, scriptName, timeStarted, null));
            rsVegaTrackerWrapper.getSessionDataTracker().setId(getInsertId(jsonArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSession(int userId, int accountId, Date timeEnded) {
        try {
            rsVegaTrackerWrapper.getSessionDataTracker().put(SessionDataTracker.getSessionData(userId, accountId, null, null, timeEnded));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSessionPosition(int userId, int accountId, int sessionId) {
        try {
            final JsonArray jsonArray = rsVegaTrackerWrapper.getSessionPositionDataTracker().post(SessionPositionDataTracker.getSessionPositionData(userId, accountId, sessionId));
            rsVegaTrackerWrapper.getSessionPositionDataTracker().setId(getInsertId(jsonArray));
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

    private boolean insertedUser() {
        return insertedUser;
    }

    private void setInsertedUser(boolean insertedAccount) {
        this.insertedUser = insertedAccount;
    }

    private boolean isMule() {
        return isMule;
    }

    private void setMule(boolean mule) {
        isMule = mule;
    }
}
