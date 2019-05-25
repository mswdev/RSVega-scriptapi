package org.api.game;

import org.api.script.SPXScript;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BankCache implements Runnable {

    private final Map<Integer, Integer> bankCache = new HashMap<>();

    public BankCache(SPXScript spxScript) {
        spxScript.getScheduledThreadPoolExecutor().scheduleAtFixedRate(this, 0, 150, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (!Bank.isOpen())
            return;

        getBankCache().clear();
        update();
    }

    /**
     * Updates the bank cache.
     */
    public void update() {
        final Item[] items = Bank.getItems();
        if (items.length == 0)
            return;

        for (Item item : items)
            getBankCache().put(item.getId(), item.getStackSize());
    }

    /**
     * Gets the bank cache;
     *
     * @return The bank cache;
     */
    public Map<Integer, Integer> getBankCache() {
        return bankCache;
    }
}

