package org.api.http;

import java.util.Date;

public class RSVegaTrackerThread implements Runnable {

    private final String scriptName;

    public RSVegaTrackerThread(String scriptName) {
        this.scriptName = scriptName;
    }

    @Override
    public void run() {
        RSVegaTracker.updateBot(scriptName);
        RSVegaTracker.updateStatsOSRS(scriptName);
        RSVegaTracker.updateSession(new Date(), scriptName);
    }
}
