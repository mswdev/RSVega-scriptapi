package org.api.script.impl.mission.tutorial_island_mission;

import com.google.gson.JsonObject;
import org.api.http.bot.CreateAccount;
import org.api.script.SPXScript;
import org.api.script.SPXScriptUtil;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.tutorial_island_mission.data.args.Args;
import org.api.script.impl.mission.tutorial_island_mission.worker.TutorialIslandWorkerHandler;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.GameAccount;
import org.rspeer.ui.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;

public class TutorialIslandMission extends Mission {

    public static final int TUTORIAL_ISLAND_VARP = 281;
    private final Path createdAccountsPath;
    private final Args args;
    private final HashMap<String, String> accountData;
    private final boolean create_account;
    private final TutorialIslandWorkerHandler worker_handler;
    private int create_account_tries;
    private boolean should_stop;

    public TutorialIslandMission(SPXScript script, Args args, HashMap<String, String> accountData, boolean create_account) {
        super(script);
        this.args = args;
        this.accountData = accountData;
        this.create_account = create_account;
        worker_handler = new TutorialIslandWorkerHandler(this);
        createdAccountsPath = Paths.get(SPXScriptUtil.getDataPath(script.getMeta().name()) + File.separator + "created_accounts.txt");
    }


    @Override
    public String getMissionName() {
        return "Tutorial Island";
    }

    @Override
    public String getWorkerName() {
        Worker c = worker_handler.getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        Worker c = worker_handler.getCurrent();
        return c == null ? "Loading next available worker" : c.toString();
    }

    @Override
    public boolean shouldPrintWorkerString() {
        return true;
    }

    @Override
    public boolean shouldEnd() {
        return should_stop;
    }

    @Override
    public GoalList getGoals() {
        return new GoalList(new InfiniteGoal());
    }

    @Override
    public int execute() {
        if (!Game.isLoggedIn())
            return 100;

        //Temporary until rspeer forces fixed mode upon login.
        if (Game.getClientPreferences().getResizable() == 2) {
            final InterfaceComponent fixed_mode_component = Interfaces.getFirst(a -> a.isVisible() && a.containsAction("Fixed mode"));
            if (fixed_mode_component != null)
                if (fixed_mode_component.click())
                    Time.sleepUntil(() -> Game.getClientPreferences().getResizable() == 1, 2500);
        }

        //Temporary until the rspeer continue dialog is fixed.
        if (Dialog.canContinue()) {
            Dialog.processContinue();
            Game.getClient().fireScriptEvent(299, 1, 1);
        }


        worker_handler.work();
        return 100;
    }

    @Override
    public void onMissionStart() {
        setProxy(getAccountData().get("socks_ip"), getAccountData().get("socks_port"), getAccountData().get("socks_username"), getAccountData().get("socks_password"));

        if (!shouldCreateAccount()) {
            script.setAccount(new GameAccount(getAccountData().get("email"), getAccountData().get("password")));
            return;
        }

        final JsonObject accountData = createAccount();
        if (accountData == null)
            return;

        try {
            Files.write(getCreatedAccountsPath(), getFormattedAccountData(accountData).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String email = accountData.get("email").getAsString();
        final String password = accountData.get("password").getAsString();
        Log.log(Level.WARNING, "Info", "[Account Creation]: Email: " + email + " | Password: " + password);
        script.setAccount(new GameAccount(email, password));
    }

    public void setShouldEnd(boolean should_stop) {
        this.should_stop = should_stop;
    }

    public Path getCreatedAccountsPath() {
        return createdAccountsPath;
    }

    public Args getArgs() {
        return args;
    }

    public HashMap<String, String> getAccountData() {
        return accountData;
    }

    public boolean shouldCreateAccount() {
        return create_account;
    }

    public JsonObject createAccount() {
        if (create_account_tries >= 3) {
            setShouldEnd(true);
            return null;
        }

        getAccountData().put("two_captcha_api_key", getArgs().two_captcha_api_key);
        Log.log(Level.WARNING, "Info", "[Account Creation]: Attempting to create account...");
        final JsonObject accountData = CreateAccount.post(getAccountData());

        if (accountData == null || !accountData.get("response").getAsString().equals("ACCOUNT_CREATED")) {
            Log.severe("[Account Creation]: Failed to create account; trying up to 3 more times...");
            create_account_tries++;
            return createAccount();
        }

        return accountData;
    }

    private String getFormattedAccountData(JsonObject accountData) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (accountData.has("email") && accountData.has("password"))
            stringBuilder.append(accountData.get("email").getAsString()).append(":").append(accountData.get("password").getAsString());

        if (accountData.has("proxy"))
            stringBuilder.append(":").append(accountData.get("proxy").getAsString());

        Log.fine(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private void setProxy(String socksIP, String socksPort, String socksUsername, String socksPassword) {
        if (socksIP != null && socksPort != null) {
            Log.fine("Setting Proxy: IP: " + socksIP + " | Port: " + socksPort);
            System.setProperty("socksProxyHost", socksIP);
            System.setProperty("socksProxyPort", socksPort);
        }

        if (socksUsername != null && socksPassword != null) {
            Log.fine("Setting Proxy: Username: " + socksUsername + " | Password: " + socksPassword);
            System.setProperty("java.net.socks.username", socksUsername);
            System.setProperty("java.net.socks.password", socksPassword);
        }
    }
}

