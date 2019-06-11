package org.api.game;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.ItemTables;
import org.rspeer.runetek.event.listeners.ItemTableListener;
import org.rspeer.runetek.event.types.ItemTableEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BankCache implements ItemTableListener {

    private final List<Item> cache = new ArrayList<>();

    /**
     * Clears the bank cache then updates it if the bank items are not null.
     */
    public void update() {
        final Item[] items = Bank.getItems();
        if (items == null || items.length <= 0)
            return;

        getCache().clear();
        for (Item item : items)
            getCache().add(item);
    }

    /**
     * Gets the item from the cache filtered by the specified predicate.
     *
     * @param predicate The predicate to filter.
     * @return The item; null otherwise.
     */
    public Item getItem(Predicate<Item> predicate) {
        return getCache().stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * Gets the bank cache;
     *
     * @return The bank cache;
     */
    public List<Item> getCache() {
        return cache;
    }

    @Override
    public void notify(ItemTableEvent itemTableEvent) {
        if (itemTableEvent.getChangeType() != ItemTableEvent.ChangeType.ITEM_ADDED && itemTableEvent.getChangeType() != ItemTableEvent.ChangeType.ITEM_REMOVED)
            return;

        if (itemTableEvent.getTableKey() != ItemTables.BANK)
            return;

        update();
    }
}

