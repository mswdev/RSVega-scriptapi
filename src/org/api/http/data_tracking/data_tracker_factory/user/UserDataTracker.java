package org.api.http.data_tracking.data_tracker_factory.user;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.rspeer.script.Script;

public class UserDataTracker extends RSVegaTrackerFactory {

    public static RequestBody getUserData() {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", Script.getRSPeerUser().getUsername());
        return formBuilder.build();
    }

    @Override
    public RSVegaTracker getDataTracker() {
        return new UserData();
    }
}
