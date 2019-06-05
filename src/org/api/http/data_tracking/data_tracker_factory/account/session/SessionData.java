package org.api.http.data_tracking.data_tracker_factory.account.session;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class SessionData implements RSVegaTracker {

    @Override
    public String getURL(int sessionId) {
        return API_URL + "/rsvega/account/session/id/" + sessionId;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/account/session/add";
    }

    @Override
    public String putURL(int sessionId) {
        return API_URL + "/rsvega/account/session/id/" + sessionId + "/update";
    }
}
