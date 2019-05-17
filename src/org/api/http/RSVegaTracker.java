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

    public static void insertBot(int accountID) {
        if (!BotData.insertBot(BotData.getBotDataRequestBody(accountID)))
            Log.severe("Bot data insert HTTP request failed.");
    }

    public static void updateBot(int accountID, int botID) {
        if (!BotData.updateBot(botID, BotData.getBotDataRequestBody(accountID)))
            Log.severe("Bot data update HTTP request failed.");
    }

    public static void updateStatsOSRS(int botID) {
        if (!StatsOSRS.updateStatsOSRS(botID, StatsOSRS.getStatsOSRSDataRequestBody()))
            Log.severe("Stats OSRS data update HTTP request failed.");
    }

    public static void insertSession(int botID, String scriptName, Date timeStarted) {
        if (!SessionData.insertSession(SessionData.getSessionDataRequestBody(botID, scriptName, timeStarted, null)))
            Log.severe("Session data insert HTTP request failed.");
    }

    public static void updateSession(int botID, int sessionID, Date timeEnded) {
        if (!SessionData.updateSession(sessionID, SessionData.getSessionDataRequestBody(botID, null, null, timeEnded)))
            Log.severe("Session data update HTTP request failed.");
    }
}
