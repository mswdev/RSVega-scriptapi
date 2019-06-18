package org.api.http.data_tracking.data_tracker_factory.impl.account;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class AccountData implements RSVegaTracker {

    @Override
    public String getURL(int accountId) {
        return API_URL + "/rsvega/account/id/" + accountId;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/account/add";
    }

    @Override
    public String putURL(int accountId) {
        return API_URL + "/rsvega/account/id/" + accountId + "/update";
    }
}
