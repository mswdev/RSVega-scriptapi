package org.api.http.data_tracking;

import com.google.gson.JsonArray;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.api.http.data_tracking.data_tracker_factory.account.AccountDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.CreateAccountDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.session.SessionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.session.mule_order.MuleOrderDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.session.session_position.SessionPositionDataTracker;
import org.api.http.data_tracking.data_tracker_factory.account.stats.StatsOSRSDataTracker;
import org.api.http.data_tracking.data_tracker_factory.user.UserDataTracker;
import org.api.http.data_tracking.data_tracker_factory.user.system_info.SystemInfoDataTracker;
import org.api.script.SPXScript;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RSVegaTrackerWrapper {

    private final RSVegaTrackerFactory userDataTracker = new UserDataTracker();
    private final RSVegaTrackerFactory systemInfoDataTracker = new SystemInfoDataTracker();
    private final RSVegaTrackerFactory accountDataTracker = new AccountDataTracker();
    private final RSVegaTrackerFactory statsOSRSDataTracker = new StatsOSRSDataTracker();
    private final RSVegaTrackerFactory sessionDataTracker = new SessionDataTracker();
    private final RSVegaTrackerFactory sessionPositionDataTracker = new SessionPositionDataTracker();
    private final RSVegaTrackerFactory muleOrderDataTracker = new MuleOrderDataTracker();
    private final RSVegaTrackerFactory createAccountDataTracker = new CreateAccountDataTracker();

    private final SPXScript spxScript;
    private String email;

    public RSVegaTrackerWrapper(SPXScript spxScript) {
        this.spxScript = spxScript;
        getSpxScript().getScheduledThreadPoolExecutor().scheduleAtFixedRate(new RSVegaTrackerThread(this), 0, 5, TimeUnit.SECONDS);
    }

    public JsonArray getRandomAccountActiveIsMule(int userId) {
        try {
            return getMuleOrderDataTracker().get("https://api.sphiinx.me/rsvega/account/user-id/" + userId + "/is-mule/active/random");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RSVegaTrackerFactory getUserDataTracker() {
        return userDataTracker;
    }

    public RSVegaTrackerFactory getSystemInfoDataTracker() {
        return systemInfoDataTracker;
    }

    public RSVegaTrackerFactory getAccountDataTracker() {
        return accountDataTracker;
    }

    public RSVegaTrackerFactory getStatsOSRSDataTracker() {
        return statsOSRSDataTracker;
    }

    public RSVegaTrackerFactory getSessionDataTracker() {
        return sessionDataTracker;
    }

    public RSVegaTrackerFactory getSessionPositionDataTracker() {
        return sessionPositionDataTracker;
    }

    public RSVegaTrackerFactory getMuleOrderDataTracker() {
        return muleOrderDataTracker;
    }

    public RSVegaTrackerFactory getCreateAccountDataTracker() {
        return createAccountDataTracker;
    }

    public SPXScript getSpxScript() {
        return spxScript;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
