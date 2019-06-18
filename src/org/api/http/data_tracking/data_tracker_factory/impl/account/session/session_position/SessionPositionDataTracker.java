package org.api.http.data_tracking.data_tracker_factory.impl.account.session.session_position;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;

public class SessionPositionDataTracker extends RSVegaTrackerFactory {

    private static Position currentPosition;

    public static RequestBody getSessionPositionData(int userId, int accountId, int sessionId) {
        if (!positionChanged())
            return null;

        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("user_id", String.valueOf(userId));
        formBuilder.add("account_id", String.valueOf(accountId));
        formBuilder.add("session_id", String.valueOf(sessionId));
        formBuilder.add("position_x", String.valueOf(currentPosition.getX()));
        formBuilder.add("position_y", String.valueOf(currentPosition.getY()));
        formBuilder.add("position_z", String.valueOf(currentPosition.getFloorLevel()));
        return formBuilder.build();
    }

    private static boolean positionChanged() {
        if (currentPosition == null || !currentPosition.equals(Players.getLocal().getPosition())) {
            if (Players.getLocal() == null)
                return false;

            currentPosition = Players.getLocal().getPosition();
            return true;
        }

        return false;
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new SessionPositionData();
    }
}
