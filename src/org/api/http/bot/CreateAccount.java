package org.api.http.bot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.FormBody;
import okhttp3.Response;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.util.HashMap;

public class CreateAccount {

    /**
     * Sends a post request to the RSVega-restapi to create an account.
     *
     * @param account_data The HashMap containing the account data to use when creating the account.
     *                     <p>
     *                     Key: two_captcha_api_key - The 2captcha api key; this is required.
     *
     *                     <p>
     *                     Key: email - The email to use when creating the account; If no email is provided it will be
     *                     generated.
     *                     <p>
     *                     Key: password - The password to use when creating the account; If no password is provided it
     *                     will be generated.
     *                     <p>
     *                     Key: socks_ip - The socks5 proxy ip address to use when creating the account; If the proxy ip
     *                     and port are not specified no proxy will be used.
     *                     <p>
     *                     Key: socks_port - The socks5 proxy port to use when creating the account; If the proxy ip and
     *                     port are not specified no proxy will be used.
     *                     <p>
     *                     Key: socks_username - The socks5 proxy username; this is not needed if the proxy does not
     *                     have a username or password.
     *                     <p>
     *                     Key: socks_password - The socks5 proxy password; this is not needed if the proxy does not
     *                     have a username or password.
     * @return A json object of the account data; null otherwise. The json object includes the success, email, password
     * and proxy used when creating the account. If the success returned false then there was an error creating the
     * account; this can be due to a bad captcha or the email is taken.
     */
    public static JsonObject post(HashMap<String, String> account_data) {
        final FormBody.Builder form_builder = new FormBody.Builder();
        form_builder.add("two_captcha_api_key", account_data.getOrDefault("two_captcha_api_key", ""));
        form_builder.add("email", account_data.getOrDefault("email", ""));
        form_builder.add("password", account_data.getOrDefault("password", ""));
        form_builder.add("socks_ip", account_data.getOrDefault("socks_ip", ""));
        form_builder.add("socks_port", account_data.getOrDefault("socks_port", ""));
        form_builder.add("socks_username", account_data.getOrDefault("socks_username", ""));
        form_builder.add("socks_password", account_data.getOrDefault("socks_password", ""));

        try (final Response response = Request.post(RSVegaTracker.API_URL + "/account/create", form_builder.build())) {
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
}

