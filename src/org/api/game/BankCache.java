package org.api.game;

import org.api.script.SPXScript;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class BankCache implements Runnable {

    private final List<Item> cache = new ArrayList<>();

    public BankCache(SPXScript spxScript) {
        spxScript.getScheduledThreadPoolExecutor().scheduleAtFixedRate(this, 0, 250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (!Bank.isOpen())
            return;

        update();
    }

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
}

