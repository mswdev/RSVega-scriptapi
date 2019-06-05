package org.api.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class HTTPUtil {

    /**
     * Gets the current ip address.
     *
     * @return The current ip address.
     */
    public static String getIP() throws IOException {
        final Request request = new Request.Builder()
                .url("https://checkip.amazonaws.com/")
                .get()
                .build();

        final Response response = new OkHttpClient().newCall(request).execute();
        if (response.body() == null)
            return null;

        return response.body().string().trim();
    }

    /**
     * Sets the socks5 proxy.
     *
     * @param socksIP       The proxy ip;
     * @param socksPort     The proxy port;
     * @param socksUsername The proxy username; null otherwise;
     * @param socksPassword The proxy password; null otherwise;
     * @return True if the proxy was successfully set; false otherwise.
     */
    public static boolean setProxy(String socksIP, String socksPort, String socksUsername, String socksPassword) throws IOException {
        if (socksIP != null && socksPort != null) {
            Log.fine("Setting Proxy: IP: " + socksIP + " | Port: " + socksPort);
            System.setProperty("socksProxyHost", socksIP);
            System.setProperty("socksProxyPort", socksPort);
        }

        if (socksUsername != null && socksPassword != null) {
            Log.fine("Setting Proxy: Username: " + socksUsername + " | Password: " + socksPassword);
            System.setProperty("java.net.socks.username", socksUsername);
            System.setProperty("java.net.socks.password", socksPassword);

            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(socksUsername, socksPassword.toCharArray());
                }
            });
        }

        final String ip = HTTPUtil.getIP();
        if (socksIP != null && socksIP.equals(ip)) {
            Log.fine("Successfully set proxy; confirmed with AWS.");
            return true;
        } else {
            Log.severe("Failed to set proxy; confirmed with AWS.");
            return false;
        }
    }
}
