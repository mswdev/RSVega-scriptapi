package org.api.http.data_tracking.data_tracker_factory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class RSVegaTrackerFactory {

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(80, TimeUnit.SECONDS)
            .writeTimeout(80, TimeUnit.SECONDS)
            .readTimeout(80, TimeUnit.SECONDS)
            .build();
    private int id;

    protected abstract RSVegaTracker getDataTracker();

    public JsonArray get() throws IOException {
        return get(getDataTracker().getURL(getId()));
    }

    public JsonArray get(String customUrl) throws IOException {
        final Request request = new Request.Builder()
                .url(customUrl)
                .get()
                .build();

        final Response response = getHttpClient().newCall(request).execute();
        return responseToJsonArray(response);
    }

    public JsonArray post(RequestBody requestBody) throws IOException {
        if (requestBody == null)
            return null;

        return post(getDataTracker().postURL(), requestBody);
    }

    public JsonArray post(String customUrl, RequestBody requestBody) throws IOException {
        if (requestBody == null)
            return null;

        final Request request = new Request.Builder()
                .url(customUrl)
                .addHeader("connection", "keep-alive")
                .post(requestBody)
                .build();

        final Response response = getHttpClient().newCall(request).execute();
        return responseToJsonArray(response);
    }

    public JsonArray put(RequestBody requestBody) throws IOException {
        if (requestBody == null)
            return null;

        return put(getDataTracker().putURL(getId()), requestBody);
    }

    public JsonArray put(String customUrl, RequestBody requestBody) throws IOException {
        if (requestBody == null)
            return null;

        final Request request = new Request.Builder()
                .url(customUrl)
                .put(requestBody)
                .build();

        final Response response = getHttpClient().newCall(request).execute();
        return responseToJsonArray(response);
    }

    private JsonArray responseToJsonArray(Response response) throws IOException {
        if (!response.isSuccessful()) {
            Log.severe("RSVega data tracking " + response.request().method() + " http request failed.");
            return null;
        }

        if (response.body() == null)
            return null;

        final Gson gson = new Gson().newBuilder().create();
        return gson.fromJson(response.body().string(), JsonArray.class);
    }

    private OkHttpClient getHttpClient() {
        return httpClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
