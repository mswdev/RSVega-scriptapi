package org.api.script.impl.mission.item_management_mission.worker;

import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.mission.item_management_mission.worker.impl.BuyWorker;
import org.api.script.impl.mission.item_management_mission.worker.impl.SellWorker;
import org.api.script.impl.mission.item_management_mission.worker.impl.TeleportToGrandExchangeWorker;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.banking.DepositWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.Arrays;

public class ItemManagementWorkerHandler extends WorkerHandler {

    private final ItemManagementMission mission;
    private final BuyWorker buyWorker;
    private final SellWorker sellWorker;
    private final TeleportToGrandExchangeWorker teleportToGrandExchangeWorker;

    public ItemManagementWorkerHandler(ItemManagementMission mission) {
        this.mission = mission;
        this.buyWorker = new BuyWorker(mission);
        this.sellWorker = new SellWorker(mission);
        this.teleportToGrandExchangeWorker = new TeleportToGrandExchangeWorker();
    }

    @Override
    public Worker decide() {
        if (Inventory.isFull())
            return new DepositWorker();

        if (Inventory.contains(a -> a.getName().equals("Ring of wealth")))
            return new DepositWorker(a -> a.getName().equals("Ring of wealth"));

        if (BankLocation.GRAND_EXCHANGE.getPosition().distance() > 20) {
            if (Equipment.contains(TeleportToGrandExchangeWorker.RING_OF_WEALTH))
                return getTeleportToGrandExchangeWorker();

            if (Arrays.stream(TeleportToGrandExchangeWorker.RING_OF_WEALTH_IDS).filter(a -> getMission().getScript().getBankCache().getItem(b -> b.getId() == a) != null).findFirst().isPresent())
                return new WithdrawWorker(mission, TeleportToGrandExchangeWorker.RING_OF_WEALTH);

            return new MovementWorker(BankLocation.GRAND_EXCHANGE.getPosition());
        }

        if (getMission().getItemManagementBuyOverrideEntry() != null) {
            return getBuyWorker();
        }

        return getSellWorker();
    }

    private ItemManagementMission getMission() {
        return mission;
    }

    private BuyWorker getBuyWorker() {
        return buyWorker;
    }

    private SellWorker getSellWorker() {
        return sellWorker;
    }

    private TeleportToGrandExchangeWorker getTeleportToGrandExchangeWorker() {
        return teleportToGrandExchangeWorker;
    }
}
