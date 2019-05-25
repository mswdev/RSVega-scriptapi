package org.api.http;

import org.api.http.bot.BotData;
import org.api.http.bot.session.MuleOrderData;
import org.api.http.bot.session.SessionData;
import org.api.http.bot.stats.StatsOSRS;
import org.api.script.SPXScript;
import org.rspeer.ui.Log;

import java.util.Date;

public class RSVegaTracker {

    public static final String API_URL = "https://api.sphiinx.me/rsvega";
    private final SPXScript spxScript;
    private final RSVegaTrackerThread rsVegaTrackerThread;
    private final String scriptName;
    private String email;
    private int accountId;
    private int botId;
    private int sessionId;

    public RSVegaTracker(SPXScript spxScript, String scriptName) {
        this.spxScript = spxScript;
        this.scriptName = scriptName;
        this.rsVegaTrackerThread = new RSVegaTrackerThread(this);
    }

    public void insertAccount() {
        if (!AccountData.insertAccount(AccountData.getAccountDataRequestBody()))
            Log.severe("Account data insert HTTP request failed.");
    }
    public void insertBot(int accountId) {
        if (!BotData.insertBot(BotData.getBotDataRequestBody(accountId)))
            Log.severe("Bot data insert HTTP request failed.");
    }

    public void updateBot(int accountId, int botId) {
        if (!BotData.updateBot(botId, BotData.getBotDataRequestBody(accountId)))
            Log.severe("Bot data update HTTP request failed.");
    }

    public void updateStatsOSRS(int botId) {
        if (!StatsOSRS.updateStatsOSRS(botId, StatsOSRS.getStatsOSRSDataRequestBody()))
            Log.severe("Stats OSRS data update HTTP request failed.");
    }

    public void insertSession(int accountId, int botId, String scriptName, Date timeStarted) {
        if (!SessionData.insertSession(SessionData.getSessionDataRequestBody(accountId, botId, scriptName, timeStarted, null)))
            Log.severe("Session data insert HTTP request failed.");
    }

    public void updateSession(int accountId, int botId, int sessionId, Date timeEnded) {
        if (!SessionData.updateSession(sessionId, SessionData.getSessionDataRequestBody(accountId, botId, null, null, timeEnded)))
            Log.severe("Session data update HTTP request failed.");
    }

    public void insertMuleOrder(int accountId, int botId, int sessionId) {
        if (!MuleOrderData.insertMuleOrder(MuleOrderData.getMuleOrderDataRequestBody(accountId, botId, sessionId, 0)))
            Log.severe("Mule Order data insert HTTP request failed.");
    }

    public void updateMuleOrder(int accountId, int botId, int sessionId, int muleBotId) {
        if (!MuleOrderData.updateMuleOrder(sessionId, MuleOrderData.getMuleOrderDataRequestBody(accountId, botId, sessionId, muleBotId)))
            Log.severe("Mule Order data update HTTP request failed.");
    }

    public SPXScript getSpxScript() {
        return spxScript;
    }

    public RSVegaTrackerThread getRsVegaTrackerThread() {
        return rsVegaTrackerThread;
    }

    public String getScriptName() {
        return scriptName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBotId() {
        return botId;
    }

    public void setBotId(int botId) {
        this.botId = botId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
