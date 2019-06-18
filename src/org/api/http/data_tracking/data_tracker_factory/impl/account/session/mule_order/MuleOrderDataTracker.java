package org.api.http.data_tracking.data_tracker_factory.impl.account.session.mule_order;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;

public class MuleOrderDataTracker extends RSVegaTrackerFactory {

    public static RequestBody getMuleOrderData(int userId, int accountId, int sessionId, int muleBotId) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("user_id", String.valueOf(userId));
        formBuilder.add("account_id", String.valueOf(accountId));
        formBuilder.add("session_id", String.valueOf(sessionId));
        formBuilder.add("mule_account_id", String.valueOf(muleBotId));

        return formBuilder.build();
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new MuleOrderData();
    }
}
