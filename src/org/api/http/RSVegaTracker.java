package org.api.http;

import org.api.http.bot.BotData;
import org.api.http.bot.session.SessionData;
import org.api.http.bot.stats.StatsOSRS;
import org.rspeer.ui.Log;

import java.util.Date;

public class RSVegaTracker {

    public static final String API_URL = "https://api.sphiinx.me/rsvega";

    public static void insertAccount() {
        if (!AccountData.insertAccount(AccountData.getAccountDataRequestBody()))
            Log.severe("Account data insert HTTP request failed.");
    }

    public static void insertBot() {
        if (!BotData.insertBot(BotData.getBotDataRequestBody()))
            Log.severe("Bot data insert HTTP request failed.");
    }

    public static void updateBot() {
        if (!BotData.updateBot(BotData.getBotId(), BotData.getBotDataRequestBody()))
            Log.severe("Bot data update HTTP request failed.");
    }

    public static void updateStatsOSRS() {
        if (!StatsOSRS.updateStatsOSRS(BotData.getBotId(), StatsOSRS.getStatsOSRSDataRequestBody()))
            Log.severe("Stats OSRS data update HTTP request failed.");
    }

    public static void insertSession(String scriptName, Date timeStarted) {
        if (!SessionData.insertSession(SessionData.getSessionDataRequestBody(scriptName, timeStarted, null)))
            Log.severe("Session data insert HTTP request failed.");
    }

    public static void updateSession(Date timeEnded) {
        if (!SessionData.updateSession(SessionData.getSessionId(), SessionData.getSessionDataRequestBody(null, null, timeEnded)))
            Log.severe("Session data update HTTP request failed.");
    }
}
