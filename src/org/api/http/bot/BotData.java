package org.api.http.bot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;
import org.rspeer.RSPeer;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.scene.Players;

import java.io.IOException;

public class BotData {

    public static boolean insertBot(RequestBody requestBody) {
        try (final Response response = Request.post(RSVegaTracker.API_URL + "/bot/add", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateBot(int botId, RequestBody requestBody) {
        try (final Response response = Request.put(RSVegaTracker.API_URL + "/bot/id/" + botId + "/update", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets a json array of the bot data.
     *
     * @param email The email of the bot.
     * @return A json array of the bot data; null otherwise.
     */
    private static JsonArray getBotByEmail(String email) {
        try (final Response response = Request.get(RSVegaTracker.API_URL + "/bot/user/" + email)) {
            if (!response.isSuccessful())
                return null;

            if (response.body() == null)
                return null;

            final Gson gson = new Gson().newBuilder().create();
            return gson.fromJson(response.body().string(), JsonArray.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the bot id associated with the bot email.
     *
     * @return The bot id associated with the bot email.
     */
    public static int getBotId() {
        JsonArray jsonArray = BotData.getBotByEmail(getEmail());
        if (jsonArray == null)
            return 0;

        if (jsonArray.size() == 0)
            return 0;

        return jsonArray.get(0).getAsJsonObject().get("id").getAsInt();
    }

    public static RequestBody getBotDataRequestBody(int accountID) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("account_id", String.valueOf(accountID));
        formBuilder.add("username", getEmail());
        formBuilder.add("display_name", getDisplayName());
        formBuilder.add("world", String.valueOf(Worlds.getCurrent()));
        formBuilder.add("last_sign_in", String.valueOf(getLastSignIn()));
        formBuilder.add("is_members", String.valueOf(isMembers()));
        formBuilder.add("is_bank_pin", String.valueOf(hasBankPin()));
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

    private static String getDisplayName() {
        if (Players.getLocal() == null)
            return "";

        final String displayName = Players.getLocal().getName();
        if (displayName == null)
            return "";

        return displayName;
    }

    private static int getLastSignIn() {
        final InterfaceComponent lastSignIn = Interfaces.getFirst(a -> a.getText().contains("You last logged in"));
        if (lastSignIn == null)
            return 0;

        if (lastSignIn.getText().contains("earlier today"))
            return 1;

        return Integer.parseInt(lastSignIn.getText().replaceAll("[^0-9]", ""));
    }

    private static int isMembers() {
        final InterfaceComponent notMembers = Interfaces.getFirst(a -> a.getText().contains("You are not a member."));
        return notMembers != null ? 0 : 1;
    }

    private static int hasBankPin() {
        final InterfaceComponent bankPinNotSet = Interfaces.getFirst(a -> a.getText().contains("You do not have a Bank PIN."));
        return bankPinNotSet != null ? 0 : 1;
    }
}
