package org.api.script.impl.mission.account_creation_mission;

import org.api.http.HTTPUtil;
import org.api.script.SPXScript;
import org.api.script.framework.goal.GoalList;
import org.api.script.framework.goal.impl.InfiniteGoal;
import org.api.script.framework.mission.Mission;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.account_creation_mission.data.Args;
import org.api.script.impl.mission.account_creation_mission.worker.AccountCreationWorkerHandler;

import java.io.IOException;
import java.util.Map;

public class AccountCreationMission extends Mission {

    private final AccountCreationWorkerHandler workerHandler;
    private final Args args;
    private final Map<String, String> accountData;
    private boolean shouldEnd;

    public AccountCreationMission(SPXScript script, Args args, Map<String, String> accountData) {
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
        if (getAccountData().containsKey("socks_ip") && getAccountData().containsKey(("socks_port"))) {
            try {
                if (!HTTPUtil.setProxy(getAccountData().get("socks_ip"), getAccountData().get("socks_port"), getAccountData().get("socks_username"), getAccountData().get("socks_password")))
                    setShouldEnd(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getAccountData().put("two_captcha_api_key", getArgs().twoCaptchaApiKey);
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    public Args getArgs() {
        return args;
    }

    public Map<String, String> getAccountData() {
        return accountData;
    }
}
