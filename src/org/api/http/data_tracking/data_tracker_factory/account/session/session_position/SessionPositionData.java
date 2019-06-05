package org.api.http.data_tracking.data_tracker_factory.account.session.session_position;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class SessionPositionData implements RSVegaTracker {

    @Override
    public String getURL(int sessionPositionId) {
        return API_URL + "/rsvega/account/session/session-position/id/" + sessionPositionId;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/account/session/session-position/add";
    }

    @Override
    public String putURL(int sessionPositionId) {
        return API_URL + "/rsvega/account/session/session-position/id/" + sessionPositionId + "/update";
    }
}
