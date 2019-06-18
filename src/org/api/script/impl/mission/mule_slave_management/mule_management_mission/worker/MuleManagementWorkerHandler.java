package org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.MuleManagementMission;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl.SwitchWorldWorker;
import org.api.script.impl.mission.mule_slave_management.mule_management_mission.worker.impl.TradeMuleWorker;
import org.api.script.impl.worker.banking.DepositWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.adapter.component.Item;
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
        this.switchToMuleWorldWorker = new SwitchWorldWorker();
        this.switchToPreviousWorldWorker = new SwitchWorldWorker();
    }

    @Override
    public Worker decide() {
        if (!getTradeMuleWorker().isFinished()) {
            if (getSwitchToPreviousWorldWorker().getWorldToSwitch() <= 0)
                getSwitchToPreviousWorldWorker().setWorldToSwitch(Worlds.getCurrent());

            int muleWorld = getMission().getMuleManagementOverrideEntry().getMuleManager().getWorld();
            if (Worlds.getCurrent() != muleWorld) {
                getSwitchToMuleWorldWorker().setWorldToSwitch(muleWorld);
                return getSwitchToMuleWorldWorker();
            }

            if (Inventory.containsAnyExcept(getMission().getMuleManagementOverrideEntry().getItem().and(Item::isNoted)))
                return new DepositWorker();

            final Item item = getMission().getScript().getBankCache().getItem(getMission().getMuleManagementOverrideEntry().getItem());
            if (((item != null && item.getStackSize() > 0) || !Inventory.contains(getMission().getMuleManagementOverrideEntry().getItem())) && !Trade.isOpen())
                return new WithdrawWorker(getMission(), getMission().getMuleManagementOverrideEntry().getItem(), 0, Bank.WithdrawMode.NOTE);

            return getTradeMuleWorker();
        }

        if (Worlds.getCurrent() != getSwitchToMuleWorldWorker().getWorldToSwitch())
            return getSwitchToMuleWorldWorker();

        getMission().setShouldEnd(true);
        return null;
    }

    public MuleManagementMission getMission() {
        return mission;
    }

    private TradeMuleWorker getTradeMuleWorker() {
        return tradeMuleWorker;
    }

    private SwitchWorldWorker getSwitchToMuleWorldWorker() {
        return switchToMuleWorldWorker;
    }

    private SwitchWorldWorker getSwitchToPreviousWorldWorker() {
        return switchToPreviousWorldWorker;
    }
}
