package org.api.script.impl.mission.item_management_mission.worker.impl;


import org.api.game.pricechecking.PriceCheck;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.item_management_mission.ItemManagementMission;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;

import java.io.IOException;

public class SellWorker extends Worker {

    private ItemManagementMission mission;

    public SellWorker(ItemManagementMission mission) {
        this.mission = mission;
    }

    @Override
    public boolean needsRepeat() {
        return false;
    }

    @Override
    public void work() {
        if (GrandExchange.getFirst(a -> a.getProgress() == RSGrandExchangeOffer.Progress.FINISHED) != null && GrandExchange.getView() == GrandExchange.View.OVERVIEW) {
            final int inventoryCache = Inventory.getCount(true);
            if (GrandExchange.collectAll())
                Time.sleepUntil(() -> inventoryCache != Inventory.getCount(true), 1500);
            return;
        }

        for (int itemId : mission.itemsToSell) {
            final Item itemToSell = Inventory.getFirst(itemId + 1);
            if (itemToSell == null)
                continue;

            if (GrandExchange.getView() != GrandExchange.View.SELL_OFFER) {
                if (GrandExchange.createOffer(RSGrandExchangeOffer.Type.SELL))
                    Time.sleepUntil(() -> GrandExchange.getView() == GrandExchange.View.SELL_OFFER, 1500);
                return;
            }

            if (GrandExchangeSetup.getItem() == null || GrandExchangeSetup.getItem().getId() != itemToSell.getId() - 1) {
                if (GrandExchangeSetup.setItem(itemToSell.getId()))
                    Time.sleepUntil(() -> GrandExchangeSetup.getItem() != null && GrandExchangeSetup.getItem().getId() == itemToSell.getId() - 1, 1500);
                return;
            }

            if (GrandExchangeSetup.getPricePerItem() != getItemSellPrice(itemToSell)) {
                if (GrandExchangeSetup.setPrice(getItemSellPrice(itemToSell)))
                    Time.sleepUntil(() -> GrandExchangeSetup.getPricePerItem() == getItemSellPrice(itemToSell), 1500);
                return;
            }

            if (GrandExchangeSetup.confirm())
                Time.sleepUntil(() -> GrandExchangeSetup.getItem() == null, 1500);
        }
    }

    private int getItemSellPrice(Item itemToSell) {
        try {
            return (int) (PriceCheck.getOSBuddyPrice(itemToSell.getId() - 1) * mission.getItemManagementTracker().getItemManagement().sellPriceModifier());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public String toString() {
        return "Executing item management sell worker.";
    }
}
