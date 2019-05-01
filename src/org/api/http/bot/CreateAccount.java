package org.api.http.bot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.FormBody;
import okhttp3.Response;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;

import java.io.IOException;
import java.util.HashMap;

public class CreateAccount {

    /**
     * Sends a post request to the RSVega-restapi to create an account.
     *
     * @param accountData The HashMap containing the account data to use when creating the account.
     *                    <p>
     *                    Key: twoCaptchaApiKey - The 2captcha api key; this is required.
     *
     *                    <p>
     *                    Key: email - The email to use when creating the account; If no email is provided it will be
     *                    generated.
     *                    <p>
     *                    Key: password - The password to use when creating the account; If no password is provided it
     *                    will be generated.
     *                    <p>
     *                    Key: socks_ip - The socks5 proxy ip address to use when creating the account; If the proxy ip
     *                    and port are not specified no proxy will be used.
     *                    <p>
     *                    Key: socks_port - The socks5 proxy port to use when creating the account; If the proxy ip and
     *                    port are not specified no proxy will be used.
     *                    <p>
     *                    Key: socks_username - The socks5 proxy username; this is not needed if the proxy does not have
     *                    a username or password.
     *                    <p>
     *                    Key: socks_password - The socks5 proxy password; this is not needed if the proxy does not have
     *                    a username or password.
     * @return A json object of the account data; null otherwise. The json object includes the success, email, password
     * and proxy used when creating the account. If the success returned false then there was an error creating the
     * account; this can be due to a bad captcha or the email is taken.
     */
    public static JsonObject post(HashMap<String, String> accountData) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("twoCaptchaApiKey", accountData.getOrDefault("twoCaptchaApiKey", ""));
        formBuilder.add("email", accountData.getOrDefault("email", ""));
        formBuilder.add("password", accountData.getOrDefault("password", ""));
        formBuilder.add("socks_ip", accountData.getOrDefault("socks_ip", ""));
        formBuilder.add("socks_port", accountData.getOrDefault("socks_port", ""));
        formBuilder.add("socks_username", accountData.getOrDefault("socks_username", ""));
        formBuilder.add("socks_password", accountData.getOrDefault("socks_password", ""));

        try (final Response response = Request.post(RSVegaTracker.API_URL + "/account/create", formBuilder.build())) {
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

