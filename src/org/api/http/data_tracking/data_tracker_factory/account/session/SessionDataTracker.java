package org.api.http.data_tracking.data_tracker_factory.account.session;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionDataTracker extends RSVegaTrackerFactory {

    public static RequestBody getSessionData(int userId, int accountId, String scriptName, Date timeStarted, Date timeEnded) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("user_id", String.valueOf(userId));
        formBuilder.add("account_id", String.valueOf(accountId));

        if (scriptName != null)
            formBuilder.add("script", scriptName);

        if (timeStarted != null)
            formBuilder.add("time_started", new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(timeStarted));

        if (timeEnded != null)
            formBuilder.add("time_ended", new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(timeEnded));

        return formBuilder.build();
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new SessionData();
    }
}
