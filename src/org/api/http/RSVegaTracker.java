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

    public static void updateBot(String scriptName) {
        if (!BotData.updateBot(BotData.getBotId(scriptName), BotData.getBotDataRequestBody()))
            Log.severe("Bot data update HTTP request failed.");
    }

    public static void updateStatsOSRS(String scriptName) {
        if (!StatsOSRS.updateStatsOSRS(BotData.getBotId(scriptName), StatsOSRS.getStatsOSRSDataRequestBody()))
            Log.severe("Stats OSRS data update HTTP request failed.");
    }

    public static void insertSession(String scriptName, Date timeStarted) {
        if (!SessionData.insertSession(SessionData.getSessionDataRequestBody(scriptName, timeStarted, null)))
            Log.severe("Session data insert HTTP request failed.");
    }

    public static void updateSession(Date timeEnded, String scriptName) {
        if (!SessionData.updateSession(SessionData.getSessionId(scriptName), SessionData.getSessionDataRequestBody(scriptName, null, timeEnded)))
            Log.severe("Session data update HTTP request failed.");
    }
}
