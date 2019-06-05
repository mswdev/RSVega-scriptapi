package org.api.http.data_tracking.data_tracker_factory.account.session.mule_order;

import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;

public class MuleOrderData implements RSVegaTracker {

    @Override
    public String getURL(int userId) {
        return API_URL + "/rsvega/account/user-id/" + userId + "/is-mule";
    }

    @Override
    public String postURL() {
        return API_URL + "/rsvega/account/session/mule-order/add";
    }

    @Override
    public String putURL(int muleOrderId) {
        return API_URL + "/rsvega/account/session/mule-order/id/" + muleOrderId + "/update";
    }
}
