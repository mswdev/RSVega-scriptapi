package org.api.http.data_tracking.data_tracker_factory.impl.user.system_info;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class SystemInfoData implements RSVegaTracker {

    @Override
    public String getURL(int systemInfoId) {
        return API_URL + "/rsvega/user/system-info/id/" + systemInfoId;
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/user/system-info/add";
    }

    @Override
    public String putURL(int systemInfoId) {
        return API_URL + "/rsvega/user/system-info/id/" + systemInfoId + "/update";
    }
}
