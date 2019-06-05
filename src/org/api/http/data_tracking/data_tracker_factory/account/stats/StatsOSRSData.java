package org.api.http.data_tracking.data_tracker_factory.account.stats;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class StatsOSRSData implements RSVegaTracker {

    @Override
    public String getURL(int accountId) {
        return API_URL + "/rsvega/account/id/" + accountId + "/stats/osrs";
    }

    @Override
    public String postURL() {
        return null;
    }

    @Override
    public String putURL(int accountId) {
        return API_URL + "/rsvega/account/id/" + accountId + "/stats/osrs/update";
    }
}
