package org.api.script.impl.mission.item_management_mission.worker;

import org.api.script.framework.item_management.ItemManagementTracker;
import org.api.script.framework.worker.Worker;
import org.api.script.framework.worker.WorkerHandler;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.mission.item_management_mission.worker.impl.BuyWorker;
import org.api.script.impl.mission.item_management_mission.worker.impl.SellWorker;
import org.api.script.impl.mission.item_management_mission.worker.impl.TeleportToGrandExchangeWorker;
import org.api.script.impl.mission.item_management_mission.worker.impl.WithdrawSellablesWorker;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.banking.DepositWorker;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.api.script.impl.worker.interactables.ItemWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Tab;
import org.rspeer.runetek.api.component.tab.Tabs;

import java.util.Arrays;

public class ItemManagementWorkerHandler extends WorkerHandler {

    private final ItemManagementMission mission;
    private final BuyWorker buyWorker;
    private final SellWorker sellWorker;
    private final WithdrawSellablesWorker withdrawSellablesWorker;
    private final TeleportToGrandExchangeWorker teleportToGrandExchangeWorker;

    public ItemManagementWorkerHandler(ItemManagementMission mission) {
        this.mission = mission;
        this.buyWorker = new BuyWorker(mission);
        this.sellWorker = new SellWorker(mission);
        this.withdrawSellablesWorker = new WithdrawSellablesWorker(mission);
        this.teleportToGrandExchangeWorker = new TeleportToGrandExchangeWorker();
    }

    @Override
    public Worker decide() {
        if (Inventory.isFull())
            return new DepositWorker();

        if (Inventory.contains(a -> a.getName().equals("Ring of wealth")))
            return new DepositWorker(a -> a.getName().equals("Ring of wealth"));

        if (BankLocation.GRAND_EXCHANGE.getPosition().distance() > 20) {
            if (!Tabs.isOpen(Tab.EQUIPMENT))
                if (Tabs.open(Tab.EQUIPMENT))
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 1500);

            if (Equipment.contains(TeleportToGrandExchangeWorker.RING_OF_WEALTH))
                return teleportToGrandExchangeWorker;

            if (Inventory.contains(TeleportToGrandExchangeWorker.RING_OF_WEALTH))
                return new ItemWorker(TeleportToGrandExchangeWorker.RING_OF_WEALTH);

            if (Arrays.stream(TeleportToGrandExchangeWorker.RING_OF_WEALTH_IDS).filter(a -> mission.getScript().getBankCache().getItem(b -> b.getId() == a) != null).findFirst().isPresent())
                return new WithdrawWorker(mission, TeleportToGrandExchangeWorker.RING_OF_WEALTH);

            return new MovementWorker(BankLocation.GRAND_EXCHANGE.getPosition());
        }

        final long inventoryGold = Inventory.getCount(true, ItemManagementTracker.GOLD_ID);
        if (mission.hasPutInOffer || inventoryGold >= mission.getItemManagementEntry().getValueNeeded(mission.getItemManagementTracker().getItemManagement().buyPriceModifier())) {
            if (!GrandExchange.isOpen())
                return openGrandExchangeWorker();

            return buyWorker;
        } else if (mission.getItemManagementTracker().getTotalGold() >= mission.getItemManagementEntry().getValueNeeded(mission.getItemManagementTracker().getItemManagement().buyPriceModifier())) {
            return new WithdrawWorker(mission, a -> a.getName().equals("Coins"), 0);
        } else {
            if (!mission.hasWithdrawnSellables)
                return withdrawSellablesWorker;

            if (!GrandExchange.isOpen())
                return openGrandExchangeWorker();

            return sellWorker;
        }
    }

    private NpcWorker openGrandExchangeWorker() {
        return new NpcWorker(a -> a.getName().equals("Grand Exchange Clerk"), a -> a.equals("Exchange"));
    }
}
