package org.api.script.impl.mission.account_creation_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.account_creation_mission.AccountCreationMission;
import org.api.script.impl.mission.account_creation_mission.worker.impl.CreateAccountWorker;

public class AccountCreationWorkerHandler extends WorkerHandler {

    private final Worker createAccountWorker;

    public AccountCreationWorkerHandler(AccountCreationMission mission) {
        this.createAccountWorker = new CreateAccountWorker(mission);
    }

    @Override
    public Worker decide() {
        return createAccountWorker;
    }
}
