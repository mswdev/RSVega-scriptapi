package org.api.http.data_tracking.data_tracker_factory.user;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class UserData implements RSVegaTracker {

    @Override
    public String getURL(int userId) {
        return API_URL + "/rsvega/user/id/" + userId;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/user/add";
    }

    @Override
    public String putURL(int userId) {
        return API_URL + "/rsvega/user/id/" + userId + "/update";
    }
}
