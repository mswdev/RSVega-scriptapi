package org.api.http.data_tracking.data_tracker_factory.account;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class CreateAccountData implements RSVegaTracker {

    @Override
    public String getURL(int id) {
        return null;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/account/create";
    }

    @Override
    public String putURL(int id) {
        return null;
    }
}
