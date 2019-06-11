package org.api.script.framework.item_management;

import org.api.game.pricechecking.PriceCheck;
import org.api.script.SPXScript;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.io.IOException;

public class ItemManagementTracker {

    public static final int GOLD_ID = 995;
    public final SPXScript script;
    private final ItemManagement itemManagement;
    private long totalGold;
    private long totalSellableGold;

    public ItemManagementTracker(SPXScript script, ItemManagement itemManagement) {
        this.script = script;
        this.itemManagement = itemManagement;
    }

    /**
     * Updates the item management tracker. Updates the total gold the player has in their inventory and bank in
     * addition to the total sellable gold in terms of the sellable items.
     * <p>
     * This also takes into account the buy price modifier and sell price modifier.
     */
    public void update() {
        final long inventoryGold = Inventory.getCount(true, GOLD_ID);
        final Item bankGold = script.getBankCache().getItem(a -> a.getId() == GOLD_ID);
        if (bankGold == null)
            return;

        totalGold = inventoryGold + bankGold.getStackSize();

        totalSellableGold = 0;
        for (int id : itemManagement.itemsToSell()) {
            final long inventoryAmount = Inventory.getCount(id);
            final Item bankAmount = script.getBankCache().getItem(a -> a.getId() == id);
            if (bankAmount == null)
                return;

            try {
                totalSellableGold += (inventoryAmount + bankAmount.getStackSize()) * (PriceCheck.getOSBuddyPrice(id) * itemManagement.sellPriceModifier());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determines which item management entry should be bought based on the total value we have and the buy price
     * modifier.
     *
     * @return The item management entry that should be bought.
     */
    public ItemManagementEntry shouldBuy() {
        for (ItemManagementEntry itemManagementEntry : itemManagement.itemsToBuy()) {
            if (itemManagementEntry.canBuy(getTotalValue(), itemManagement.buyPriceModifier()))
                return itemManagementEntry;
        }

        return null;
    }

    /**
     * Gets the total value the player has. This is the total gold combined with the total sellable gold the player
     * has.
     *
     * @return The total value the player has.
     */
    private long getTotalValue() {
        return totalGold + totalSellableGold;
    }

    /**
     * Gets the total gold the player has in their inventory and bank.
     *
     * @return The total gold.
     */
    public long getTotalGold() {
        return totalGold;
    }

    /**
     * Gets the item management.
     *
     * @return The item management.
     */
    public ItemManagement getItemManagement() {
        return itemManagement;
    }
}
