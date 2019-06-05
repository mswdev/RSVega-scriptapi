package org.api.script.impl.mission.account_creation_mission.worker.impl;

import com.google.gson.JsonObject;
import org.api.http.data_tracking.data_tracker_factory.account.CreateAccountDataTracker;
import org.api.script.SPXScriptUtil;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.account_creation_mission.AccountCreationMission;
import org.rspeer.script.GameAccount;
import org.rspeer.ui.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public class CreateAccountWorker extends Worker {

    private final AccountCreationMission mission;
    private final Path createdAccountsPath;

    private int createAccountTries;

    public CreateAccountWorker(AccountCreationMission mission) {
        this.mission = mission;
        this.createdAccountsPath = Paths.get(SPXScriptUtil.getScriptDataPath(mission.getScript().getMeta().name()) + File.separator + "created_accounts.txt");
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
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
        Log.log(Level.WARNING, "Info", "[Account Creation]: Username: " + email + " | Password: " + password);
        mission.getScript().setAccount(new GameAccount(email, password));
        mission.setShouldEnd(true);
    }

    private JsonObject createAccount() {
        if (createAccountTries >= 3) {
            mission.setShouldEnd(true);
            return null;
        }


        Log.log(Level.WARNING, "Info", "[Account Creation]: Attempting to create account...");
        JsonObject accountData = null;
        try {
            accountData = mission.getScript().getRsVegaTrackerWrapper().getCreateAccountDataTracker().post(CreateAccountDataTracker.getCreateAccountData(mission.getAccountData())).getAsJsonObject();
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

    public Path getCreatedAccountsPath() {
        return createdAccountsPath;
    }

    @Override
    public String toString() {
        return "Executing account creation worker.";
    }
}
