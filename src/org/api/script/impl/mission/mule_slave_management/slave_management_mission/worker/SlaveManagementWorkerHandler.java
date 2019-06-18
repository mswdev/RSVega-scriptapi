package org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.SlaveManagementMission;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.impl.TradeSlaveWorker;

public class SlaveManagementWorkerHandler extends WorkerHandler {

    private final Worker tradeSlaveWorker;

    public SlaveManagementWorkerHandler(SlaveManagementMission mission) {
        this.tradeSlaveWorker = new TradeSlaveWorker(mission);
    }

    @Override
    public Worker decide() {
        return getTradeSlaveWorker();
    }

    public Worker getTradeSlaveWorker() {
        return tradeSlaveWorker;
    }
}
