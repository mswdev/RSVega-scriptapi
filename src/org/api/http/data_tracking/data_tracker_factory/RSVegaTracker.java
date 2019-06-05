package org.api.http.data_tracking.data_tracker_factory;

public interface RSVegaTracker {

    //String API_URL = "https://api.sphiinx.me";
    String API_URL = "http://localhost:8080";

    String getURL(int id);

    String postURL();

    String putURL(int id);
}
