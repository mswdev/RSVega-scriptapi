package org.api.http.bot.session;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionData {

    public static boolean insertSession(RequestBody requestBody) {
        try (final Response response = Request.post(RSVegaTracker.API_URL + "/bot/session/add", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateSession(int sessionId, RequestBody requestBody) {
        try (final Response response = Request.put(RSVegaTracker.API_URL + "/bot/id/" + sessionId + "/session/update", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static RequestBody getSessionDataRequestBody(int accountId, int botId, String scriptName, Date timeStarted, Date timeEnded) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("account_id", String.valueOf(accountId));
        formBuilder.add("bot_id", String.valueOf(botId));

        if (scriptName != null)
            formBuilder.add("script", scriptName);

        if (timeStarted != null)
            formBuilder.add("time_started", new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(timeStarted));

        if (timeEnded != null)
            formBuilder.add("time_ended", new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(timeEnded));
        return formBuilder.build();
    }

    /**
     * Gets a json array of the newest bot session by account id.
     *
     * @param accountId The account id;
     * @return A json array of the newest bot session by account id; null otherwise.
     */
    public static JsonArray getNewestSessionByAccountId(int accountId) {
        try (final Response response = Request.get(RSVegaTracker.API_URL + "/bot/session/account-id/" + accountId + "/newest")) {
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
     * Gets the session id associated with the account id.
     *
     * @param accountId The account id;
     * @return The session id associated with the account id.
     */
    public static int getSessionId(int accountId) {
        JsonArray jsonArray = getNewestSessionByAccountId(accountId);
        if (jsonArray == null)
            return 0;

        if (jsonArray.size() == 0)
            return 0;

        return jsonArray.get(0).getAsJsonObject().get("id").getAsInt();
    }
}
