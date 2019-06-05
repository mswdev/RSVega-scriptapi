package org.api.http.data_tracking.data_tracker_factory.account;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;

import java.util.Map;

public class CreateAccountDataTracker extends RSVegaTrackerFactory {

    /**
     * Gets the request body for creating an account.
     *
     * @param accountData The Map containing the account data to use when creating the account.
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
     * @return A request body of the account data.
     */
    public static RequestBody getCreateAccountData(Map<String, String> accountData) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("two_captcha_api_key", accountData.getOrDefault("two_captcha_api_key", ""));
        formBuilder.add("email", accountData.getOrDefault("email", ""));
        formBuilder.add("password", accountData.getOrDefault("password", ""));
        formBuilder.add("socks_ip", accountData.getOrDefault("socks_ip", ""));
        formBuilder.add("socks_port", accountData.getOrDefault("socks_port", ""));
        formBuilder.add("socks_username", accountData.getOrDefault("socks_username", ""));
        formBuilder.add("socks_password", accountData.getOrDefault("socks_password", ""));
        return formBuilder.build();
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new CreateAccountData();
    }
}
