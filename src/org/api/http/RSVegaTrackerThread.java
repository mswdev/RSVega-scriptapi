package org.api.http;

import org.api.http.bot.BotData;
import org.api.http.bot.session.SessionData;
import org.rspeer.RSPeer;
import org.rspeer.runetek.api.Game;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.Date;
import java.util.logging.Level;

public class RSVegaTrackerThread implements Runnable {

    private final String scriptName;
    private boolean hasInsertedAccount;
    private String username;
    private int accountID;
    private int botID;
    private int sessionID;

    public RSVegaTrackerThread(String scriptName) {
        this.scriptName = scriptName;
    }

    @Override
    public void run() {
        Log.log(Level.WARNING, "Info", "Executing RSVega data tracking.");
        if (!hasInsertedAccount) {
            RSVegaTracker.insertAccount();
            hasInsertedAccount = true;
        }

        if (accountID == 0)
            accountID = AccountData.getAccountId(Script.getRSPeerUser().getUsername());

        if (username == null) {
            username = getUsername();
            return;
        }

        if (botID == 0 || !getUsername().equals(username)) {
            botID = BotData.getBotId();
            sessionID = SessionData.getSessionId(botID);
            RSVegaTracker.insertBot(accountID);
            RSVegaTracker.insertSession(botID, scriptName, new Date());
        }

        RSVegaTracker.updateBot(accountID, botID);
        RSVegaTracker.updateSession(botID, sessionID, new Date());

        if (Game.isLoggedIn())
            RSVegaTracker.updateStatsOSRS(botID);
    }

    private static String getUsername() {
        if (RSPeer.getGameAccount() == null)
            return null;

        return RSPeer.getGameAccount().getUsername();
    }
}
