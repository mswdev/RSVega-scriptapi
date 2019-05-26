package org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl.SwitchWorldWorker;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl.TradeMuleWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;

public class MuleManagementWorkerHandler extends WorkerHandler {

    private final MuleManagementMission mission;
    private final TradeMuleWorker tradeMuleWorker;
    private final SwitchWorldWorker switchToMuleWorldWorker;
    private final SwitchWorldWorker switchToPreviousWorldWorker;

    public MuleManagementWorkerHandler(MuleManagementMission mission) {
        this.mission = mission;
        this.tradeMuleWorker = new TradeMuleWorker(mission);
        this.switchToMuleWorldWorker = new SwitchWorldWorker(mission);
        this.switchToPreviousWorldWorker = new SwitchWorldWorker(mission);
    }

    @Override
    public Worker decide() {
        if (!tradeMuleWorker.isFinished()) {
            if (switchToPreviousWorldWorker.getWorldToSwitch() <= 0)
                switchToPreviousWorldWorker.setWorldToSwitch(Worlds.getCurrent());

            int muleWorld = mission.getMuleManagementEntry().getMuleManager().getWorld();
            if (Worlds.getCurrent() != muleWorld) {
                switchToMuleWorldWorker.setWorldToSwitch(muleWorld);
                return switchToMuleWorldWorker;
            }

            if (!Inventory.contains(mission.getMuleManagementEntry().getId() + 1) && !Trade.isOpen())
                return new WithdrawWorker(mission, a -> a.getId() == mission.getMuleManagementEntry().getId(), 0, Bank.WithdrawMode.NOTE);

            return tradeMuleWorker;
        }

        if (Worlds.getCurrent() != switchToPreviousWorldWorker.getWorldToSwitch())
            return switchToPreviousWorldWorker;

        mission.setShouldEnd(true);
        return null;
    }
}
