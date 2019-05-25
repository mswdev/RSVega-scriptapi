package org.api.http.bot.session;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;
import org.rspeer.ui.Log;

import java.io.IOException;

public class MuleOrderData {

    public static boolean insertMuleOrder(RequestBody requestBody) {
        try (final Response response = Request.post(RSVegaTracker.API_URL + "/bot/session/mule-order/add", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateMuleOrder(int sessionId, RequestBody requestBody) {
        try (final Response response = Request.put(RSVegaTracker.API_URL + "/bot/session/session-id/" + sessionId + "/mule-order/update", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static RequestBody getMuleOrderDataRequestBody(int accountId, int botId, int sessionId, int muleBotId) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("account_id", String.valueOf(accountId));
        formBuilder.add("bot_id", String.valueOf(botId));
        formBuilder.add("session_id", String.valueOf(sessionId));
        formBuilder.add("mule_bot_id", String.valueOf(muleBotId));

        return formBuilder.build();
    }

    /**
     * Gets a json array of an unassigned mule order by account id.
     *
     * @param accountId The account id;
     * @return A json array of an unassigned mule order by account id; null otherwise.
     */
    public static JsonArray getUnassignedMuleOrderByAccountId(int accountId) {
        try (final Response response = Request.get(RSVegaTracker.API_URL + "/bot/session/account-id/" + accountId + "/mule-order/unassigned")) {
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
     * Gets a json array of the newest mule order by bot id.
     *
     * @param botId The bot id;
     * @return A json array of the newest mule order by bot id; null otherwise.
     */
    public static JsonArray getNewestMuleOrderByBotId(int botId) {
        try (final Response response = Request.get(RSVegaTracker.API_URL + "/bot/session/bot-id/" + botId + "/mule-order/newest")) {
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
     * Gets an unassigned mule order id associated with the account id.
     *
     * @param accountId The account id;
     * @return An unassigned mule order id associated with the account id.
     */
    public static int getUnassignedMuleOrderId(int accountId) {
        JsonArray jsonArray = getUnassignedMuleOrderByAccountId(accountId);
        if (jsonArray == null)
            return 0;

        if (jsonArray.size() == 0)
            return 0;

        return jsonArray.get(0).getAsJsonObject().get("id").getAsInt();
    }

    /**
     * Gets the newest mule order id associated with the bot id.
     *
     * @param botId The bot id;
     * @return The newest mule order id associated with the bot id.
     */
    public static int getNewestMuleOrderId(int botId) {
        JsonArray jsonArray = getNewestMuleOrderByBotId(botId);
        if (jsonArray == null)
            return 0;

        if (jsonArray.size() == 0)
            return 0;

        return jsonArray.get(0).getAsJsonObject().get("id").getAsInt();
    }
}
