package org.api.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.http.wrappers.Request;
import org.rspeer.script.Script;

import java.io.IOException;

public class AccountData {

    private static int ACCOUNT_ID;

    public static boolean insertAccount(RequestBody requestBody) {
        try (final Response response = Request.post(RSVegaTracker.API_URL + "/account/add", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static RequestBody getAccountDataRequestBody() {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", Script.getRSPeerUser().getUsername());
        return formBuilder.build();
    }

    /**
     * Gets a json object of the rspeer users account data.
     *
     * @param username The username of the rspeer user.
     * @return A json object of the rspeer users account data; null otherwise.
     */
    private static JsonObject getAccountByUsername(String username) {
        try (final Response response = Request.get(RSVegaTracker.API_URL + "/account/user/" + username)) {
            if (!response.isSuccessful())
                return null;

            if (response.body() == null)
                return null;

            final Gson gson = new Gson().newBuilder().create();
            return gson.fromJson(response.body().string(), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the account id associated with the rspeer user.
     *
     * @return The account id associated with the rspeer user.
     */
    public static int getAccountId() {
        if (ACCOUNT_ID == 0) {
            JsonObject jsonObject = AccountData.getAccountByUsername(Script.getRSPeerUser().getUsername());
            if (jsonObject == null)
                return 0;

            if (jsonObject.size() == 0)
                return 0;

            ACCOUNT_ID = jsonObject.get("id").getAsInt();
        }

        return ACCOUNT_ID;
    }
}
