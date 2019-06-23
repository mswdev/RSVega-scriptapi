package org.api.script.impl.mission.tutorial_island_mission;

import com.google.gson.JsonObject;
import org.api.http.HTTPUtil;
import org.api.http.data_tracking.data_tracker_factory.impl.account.CreateAccountDataTracker;
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
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.logging.Level;

public class TutorialIslandMission extends Mission {
    
    private final Path createdAccountsPath;
    private final Args args;
    private final Map<String, String> accountData;
    private final boolean createAccount;
    private final TutorialIslandWorkerHandler workerHandler;
    private int createAccountTries;
    private boolean shouldEnd;

    public TutorialIslandMission(SPXScript script, Args args, Map<String, String> accountData, boolean createAccount) {
        super(script);
        this.args = args;
        this.accountData = accountData;
        this.createAccount = createAccount;
        workerHandler = new TutorialIslandWorkerHandler(this);
        createdAccountsPath = Paths.get(SPXScriptUtil.getScriptDataPath(script.getMeta().name()) + File.separator + "created_accounts.txt");
    }


    @Override
    public String getMissionName() {
        return "Tutorial Island";
    }

    @Override
    public String getWorkerName() {
        Worker c = workerHandler.getCurrent();
        return c == null ? "WORKER" : c.getClass().getSimpleName();
    }

    @Override
    public String getWorkerString() {
        Worker c = workerHandler.getCurrent();
        return c == null ? "Loading next available worker" : c.toString();
    }

    @Override
    public boolean shouldPrintWorkerString() {
        return true;
    }

    @Override
    public boolean shouldEnd() {
        return shouldEnd;
    }

    @Override
    public GoalList getGoals() {
        return new GoalList(new InfiniteGoal());
    }

    @Override
    public int execute() {
        if (!Game.isLoggedIn())
            return 100;

        //Temporary until the rspeer continue dialog is fixed.
        if (Dialog.canContinue()) {
            Dialog.processContinue();
            Game.getClient().fireScriptEvent(299, 1, 1);
        }

        workerHandler.work();
        return 100;
    }

    @Override
    public void onMissionStart() {
        if (getAccountData().containsKey("socks_ip") && getAccountData().containsKey(("socks_port"))) {
            try {
                if (!HTTPUtil.setProxy(getAccountData().get("socks_ip"), getAccountData().get("socks_port"), getAccountData().get("socks_username"), getAccountData().get("socks_password")))
                    setShouldEnd(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!shouldCreateAccount()) {
            script.setAccount(new GameAccount(getAccountData().get("email"), getAccountData().get("password")));
            return;
        }

        final JsonObject accountData = createAccount();
        if (accountData == null)
            return;

        try {
            Files.write(getCreatedAccountsPath(), (getFormattedAccountData(accountData) + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String email = accountData.get("email").getAsString();
        final String password = accountData.get("password").getAsString();
        Log.log(Level.WARNING, "Info", "[Account Creation]: Email: " + email + " | Password: " + password);
        script.setAccount(new GameAccount(email, password));
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    private Path getCreatedAccountsPath() {
        return createdAccountsPath;
    }

    public Args getArgs() {
        return args;
    }

    private Map<String, String> getAccountData() {
        return accountData;
    }

    private boolean shouldCreateAccount() {
        return createAccount;
    }

    private JsonObject createAccount() {
        if (createAccountTries >= 3) {
            setShouldEnd(true);
            return null;
        }

        getAccountData().put("two_captcha_api_key", getArgs().twoCaptchaApiKey);
        Log.log(Level.WARNING, "Info", "[Account Creation]: Attempting to create account...");
        JsonObject accountData = null;
        try {
            accountData = getScript().getRsVegaTrackerWrapper().getCreateAccountDataTracker().post(CreateAccountDataTracker.getCreateAccountData(getAccountData())).get(0).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (accountData == null || accountData.get("error") != null) {
            Log.severe("[Account Creation]: Failed to create account; trying up to 3 more times...");
            if (accountData != null && accountData.get("error") != null)
                Log.severe(accountData.get("error"));

            createAccountTries++;
            return createAccount();
        }

        return accountData.getAsJsonObject("credentials");
    }

    private String getFormattedAccountData(JsonObject accountData) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (accountData.has("email") && accountData.has("password"))
            stringBuilder.append(accountData.get("email").getAsString()).append(":").append(accountData.get("password").getAsString());

        if (accountData.has("socks_ip") && accountData.has("socks_port"))
            stringBuilder.append(accountData.get("socks_ip").getAsString()).append(":").append(accountData.get("socks_port").getAsString());

        if (accountData.has("socks_username") && accountData.has("socks_password"))
            stringBuilder.append(accountData.get("socks_username").getAsString()).append(":").append(accountData.get("socks_password").getAsString());

        return stringBuilder.toString();
    }
}

