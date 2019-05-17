package org.api.script.impl.mission.account_creation_mission;

import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.account_creation_mission.data.Args;
import org.api.script.impl.mission.account_creation_mission.worker.AccountCreationWorkerHandler;
import org.rspeer.ui.Log;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;

public class AccountCreationMission extends Mission {

    private final AccountCreationWorkerHandler workerHandler;
    private final Args args;
    private final HashMap<String, String> accountData;
    private boolean shouldEnd;

    public AccountCreationMission(SPXScript script, Args args, HashMap<String, String> accountData) {
        super(script);
        this.args = args;
        this.accountData = accountData;
        this.workerHandler = new AccountCreationWorkerHandler(this);
    }

    @Override
    public String getMissionName() {
        return "Account Creator";
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
        workerHandler.work();
        return 150;
    }

    @Override
    public void onMissionStart() {
        setProxy(getAccountData().get("socks_ip"), getAccountData().get("socks_port"), getAccountData().get("socks_username"), getAccountData().get("socks_password"));
        getAccountData().put("two_captcha_api_key", getArgs().twoCaptchaApiKey);
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    public Args getArgs() {
        return args;
    }

    public HashMap<String, String> getAccountData() {
        return accountData;
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

            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(socksUsername, socksPassword.toCharArray());
                }
            });
        }
    }
}
