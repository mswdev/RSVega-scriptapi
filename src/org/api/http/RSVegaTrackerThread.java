package org.api.http;

import org.api.http.bot.BotData;
import org.api.http.bot.session.SessionData;
import org.rspeer.RSPeer;
import org.rspeer.runetek.api.Game;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RSVegaTrackerThread implements Runnable {

    private final RSVegaTracker rsVegaTracker;

    private boolean hasInsertedAccount;


    public RSVegaTrackerThread(RSVegaTracker rsVegaTracker) {
        this.rsVegaTracker = rsVegaTracker;
        rsVegaTracker.getSpxScript().getScheduledThreadPoolExecutor().scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        Log.log(Level.WARNING, "Info", "Executing RSVega data tracking.");
        if (!hasInsertedAccount) {
            rsVegaTracker.insertAccount();
            hasInsertedAccount = true;
        }

        if (rsVegaTracker.getAccountId() == 0)
            rsVegaTracker.setAccountId(AccountData.getAccountId(Script.getRSPeerUser().getUsername()));

        if (rsVegaTracker.getEmail() == null)
            rsVegaTracker.setEmail(getEmail());

        if (rsVegaTracker.getBotId() == 0 || (getEmail() != null && !getEmail().equals(rsVegaTracker.getEmail()))) {
            rsVegaTracker.setBotId(BotData.getBotId());
            rsVegaTracker.setSessionId(SessionData.getSessionId(rsVegaTracker.getAccountId()));
            rsVegaTracker.insertBot(rsVegaTracker.getAccountId());
            rsVegaTracker.insertSession(rsVegaTracker.getAccountId(), rsVegaTracker.getBotId(), rsVegaTracker.getScriptName(), new Date());
            rsVegaTracker.setEmail(null);
        }

        rsVegaTracker.updateBot(rsVegaTracker.getAccountId(), rsVegaTracker.getBotId());
        rsVegaTracker.updateSession(rsVegaTracker.getAccountId(), rsVegaTracker.getBotId(), rsVegaTracker.getSessionId(), new Date());

        if (Game.isLoggedIn())
            rsVegaTracker.updateStatsOSRS(rsVegaTracker.getBotId());
    }

    private static String getEmail() {
        if (RSPeer.getGameAccount() == null)
            return null;

        return RSPeer.getGameAccount().getUsername();
    }
}
