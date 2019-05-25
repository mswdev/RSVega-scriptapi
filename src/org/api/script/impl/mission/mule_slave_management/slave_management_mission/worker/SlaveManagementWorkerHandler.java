package org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.SlaveManagementMission;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.impl.SlaveManagementTrackerThread;
import org.api.script.impl.mission.mule_slave_management.slave_management_mission.worker.impl.TradeSlaveWorker;

public class SlaveManagementWorkerHandler extends WorkerHandler {

    private final SlaveManagementTrackerThread slaveManagementTrackerThread;
    private final Worker tradeSlaveWorker;

    public SlaveManagementWorkerHandler(SlaveManagementMission mission) {
        this.tradeSlaveWorker = new TradeSlaveWorker(mission);
        this.slaveManagementTrackerThread = new SlaveManagementTrackerThread(mission);
    }

    @Override
    public Worker decide() {
            return tradeSlaveWorker;
    }
}
