package org.api.script.impl.mission.item_management_mission.worker.impl;


import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.api.script.impl.worker.banking.WithdrawWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;
import org.rspeer.ui.Log;

public class SellWorker extends Worker {

    private ItemManagementMission mission;
    private final Worker openGrandExchangeWorker = new NpcWorker(a -> a.getName().equals("Grand Exchange Clerk"), a -> a.equals("Exchange"));
    private final Worker withdrawSellableItemWorker;
    private boolean placedOffer;

    public SellWorker(ItemManagementMission mission) {
        this.mission = mission;
        withdrawSellableItemWorker = new WithdrawWorker(mission, a -> a.getName().equals(mission.getItemManagementSellOverrideEntry().getName()), 0, Bank.WithdrawMode.NOTE);
    }

    @Override
    public void work() {
        if ((!Inventory.contains(getMission().getItemManagementSellOverrideEntry().getName()) || getMission().getScript().getBankCache().getItem(a -> a.getName().equals(getMission().getItemManagementSellOverrideEntry().getName())) != null) && !placedOffer()) {
            getWithdrawSellableItemWorker().work();
            return;
        }

        if (!GrandExchange.isOpen()) {
            getOpenGrandExchangeWorker().work();
            return;
        }

        final RSGrandExchangeOffer sellOffer = GrandExchange.getFirst(a -> a.getItemDefinition().getName().equals(getMission().getItemManagementSellOverrideEntry().getName()));
        if (sellOffer == null) {
            if (GrandExchange.getView() != GrandExchange.View.SELL_OFFER) {
                if (GrandExchange.createOffer(RSGrandExchangeOffer.Type.SELL))
                    Time.sleepUntil(() -> GrandExchange.getView() == GrandExchange.View.SELL_OFFER, 1500);

                return;
            }

            if (GrandExchangeSetup.getItem() == null || !GrandExchangeSetup.getItem().getName().equals(getMission().getItemManagementSellOverrideEntry().getName())) {
                if (GrandExchangeSetup.setItem(getMission().getItemManagementSellOverrideEntry().getName()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getItem() != null && GrandExchangeSetup.getItem().getName().equals(getMission().getItemManagementSellOverrideEntry().getName()), 1500);

                return;
            }

            if (GrandExchangeSetup.getPricePerItem() != getMission().getItemManagementSellOverrideEntry().getItemSellPrice()) {
                if (GrandExchangeSetup.setPrice(getMission().getItemManagementSellOverrideEntry().getItemSellPrice()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getPricePerItem() == getMission().getItemManagementSellOverrideEntry().getItemSellPrice(), 1500);

                return;
            }

            final int quantity = Inventory.getCount(true, getMission().getItemManagementSellOverrideEntry().getName());
            Log.fine(quantity);
            if (GrandExchangeSetup.getQuantity() != quantity) {
                if (GrandExchangeSetup.setQuantity(quantity))
                    Time.sleepUntil(() -> GrandExchangeSetup.getQuantity() == quantity, 1500);

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
        return "Executing item management sell worker.";
    }

    private ItemManagementMission getMission() {
        return mission;
    }

    private Worker getOpenGrandExchangeWorker() {
        return openGrandExchangeWorker;
    }

    private Worker getWithdrawSellableItemWorker() {
        return withdrawSellableItemWorker;
    }

    public boolean placedOffer() {
        return placedOffer;
    }

    public void setPlacedOffer(boolean placedOffer) {
        this.placedOffer = placedOffer;
    }
}
