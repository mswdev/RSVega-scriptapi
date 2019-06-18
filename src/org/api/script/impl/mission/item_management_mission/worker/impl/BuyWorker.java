package org.api.script.impl.mission.item_management_mission.worker.impl;

import org.api.script.framework.mission_override.impl.item_management.ItemManagementBuyOverrideEntry;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;

public class BuyWorker extends Worker {

    private final Worker openGrandExchangeWorker = new NpcWorker(a -> a.getName().equals("Grand Exchange Clerk"), a -> a.equals("Exchange"));
    private final Worker withdrawCoinsWorker;
    private ItemManagementMission mission;
    private boolean placedOffer;

    public BuyWorker(ItemManagementMission mission) {
        this.mission = mission;
        this.withdrawCoinsWorker = new WithdrawWorker(mission, a -> a.getName().equals(ItemManagementBuyOverrideEntry.GOLD_COINS_NAME), 0);
    }

    @Override
    public void work() {
        if (Inventory.getCount(true, ItemManagementBuyOverrideEntry.GOLD_COINS_NAME) < getMission().getItemManagementBuyOverrideEntry().getTotalGoldNeeded() && !placedOffer()) {
            getWithdrawCoinsWorker().work();
            return;
        }

        if (!GrandExchange.isOpen()) {
            getOpenGrandExchangeWorker().work();
            return;
        }

        final RSGrandExchangeOffer buyOffer = GrandExchange.getFirst(a -> a.getItemDefinition().getName().equals(getMission().getItemManagementBuyOverrideEntry().getName()));
        if (buyOffer == null) {
            if (GrandExchange.getView() != GrandExchange.View.BUY_OFFER) {
                if (GrandExchange.createOffer(RSGrandExchangeOffer.Type.BUY))
                    Time.sleepUntil(() -> GrandExchange.getView() == GrandExchange.View.BUY_OFFER, 1500);

                return;
            }

            if (GrandExchangeSetup.getItem() == null || !GrandExchangeSetup.getItem().getName().equals(getMission().getItemManagementBuyOverrideEntry().getName())) {
                if (GrandExchangeSetup.setItem(getMission().getItemManagementBuyOverrideEntry().getName()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getItem() != null && GrandExchangeSetup.getItem().getName().equals(getMission().getItemManagementBuyOverrideEntry().getName()), 1500);

                return;
            }

            if (GrandExchangeSetup.getPricePerItem() != getMission().getItemManagementBuyOverrideEntry().getItemBuyPrice()) {
                if (GrandExchangeSetup.setPrice(getMission().getItemManagementBuyOverrideEntry().getItemBuyPrice()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getPricePerItem() == getMission().getItemManagementBuyOverrideEntry().getItemBuyPrice(), 1500);

                return;
            }

            if (GrandExchangeSetup.getQuantity() != getMission().getItemManagementBuyOverrideEntry().getQuantity()) {
                if (GrandExchangeSetup.setQuantity(getMission().getItemManagementBuyOverrideEntry().getQuantity()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getQuantity() == getMission().getItemManagementBuyOverrideEntry().getQuantity(), 1500);

                return;
            }

            if (GrandExchangeSetup.confirm())
                if (Time.sleepUntil(() -> GrandExchangeSetup.getItem() == null, 1500))
                    setPlacedOffer(true);

            return;
        }

        if (GrandExchange.getFirst(a -> a.getProgress() == RSGrandExchangeOffer.Progress.FINISHED) == null || GrandExchange.getView() != GrandExchange.View.OVERVIEW)
            return;

        final int inventoryCache = Inventory.getCount(true);
        if (GrandExchange.collectAll())
            if (Time.sleepUntil(() -> Inventory.getCount(true) != inventoryCache, 1500))
                getMission().setShouldEnd(true);
    }

    @Override
    public String toString() {
        return "Executing item management buy worker.";
    }

    private ItemManagementMission getMission() {
        return mission;
    }

    private Worker getOpenGrandExchangeWorker() {
        return openGrandExchangeWorker;
    }

    public boolean placedOffer() {
        return placedOffer;
    }

    public void setPlacedOffer(boolean placedOffer) {
        this.placedOffer = placedOffer;
    }

    public Worker getWithdrawCoinsWorker() {
        return withdrawCoinsWorker;
    }
}
