package org.api.http.data_tracking.data_tracker_factory.impl.account;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.rspeer.RSPeer;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.scene.Players;

import java.util.HashMap;
import java.util.Map;

public class AccountDataTracker extends RSVegaTrackerFactory {

    public static RequestBody getAccountData(int userId, boolean isMule) {
        final Map<String, String> accountData = new HashMap<>();
        accountData.put("user_id", String.valueOf(userId));
        accountData.put("email", getEmail());
        accountData.put("world", String.valueOf(Worlds.getCurrent()));
        if (Players.getLocal() != null) {
            accountData.put("display_name", Players.getLocal().getName());
            accountData.put("position_x", String.valueOf(Players.getLocal().getX()));
            accountData.put("position_y", String.valueOf(Players.getLocal().getY()));
            accountData.put("position_z", String.valueOf(Players.getLocal().getFloorLevel()));
        }
        accountData.put("is_mule", String.valueOf(isMule ? 1 : 0));
        return getAccountData(accountData);
    }

    public static RequestBody getAccountData(Map<String, String> accountData) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        if (accountData.containsKey("user_id"))
            formBuilder.add("user_id", accountData.get("user_id"));

        if (accountData.containsKey("email"))
            formBuilder.add("email", accountData.get("email"));

        if (accountData.containsKey("display_name"))
            formBuilder.add("display_name", accountData.get("display_name"));

        if (accountData.containsKey("login_response"))
            formBuilder.add("login_response", accountData.get("login_response"));

        if (accountData.containsKey("disabled_date"))
            formBuilder.add("disabled_date", accountData.get("disabled_date"));

        if (accountData.containsKey("world"))
            formBuilder.add("world", accountData.get("world"));

        if (accountData.containsKey("position_x"))
            formBuilder.add("position_x", accountData.get("position_x"));

        if (accountData.containsKey("position_y"))
            formBuilder.add("position_y", accountData.get("position_y"));

        if (accountData.containsKey("position_z"))
            formBuilder.add("position_z", accountData.get("position_z"));

        if (accountData.containsKey("last_sign_in"))
            formBuilder.add("last_sign_in", accountData.get("last_sign_in"));

        if (accountData.containsKey("hans_age"))
            formBuilder.add("hans_age", accountData.get("hans_age"));

        if (accountData.containsKey("is_mule"))
            formBuilder.add("is_mule", accountData.get("is_mule"));

        if (accountData.containsKey("is_members"))
            formBuilder.add("is_members", accountData.get("is_members"));

        if (accountData.containsKey("is_bank_pin"))
            formBuilder.add("is_bank_pin", accountData.get("is_bank_pin"));

        return formBuilder.build();
    }

    private static String getEmail() {
        if (RSPeer.getGameAccount() == null)
            return "";

        final String email = RSPeer.getGameAccount().getUsername();
        if (email == null)
            return "";

        return email;
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new AccountData();
    }
}
