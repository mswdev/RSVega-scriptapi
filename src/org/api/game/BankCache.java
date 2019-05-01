package org.api.game;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.script.Script;

import java.util.HashMap;
import java.util.Map;

public class BankCache extends Thread {

    private final Map<Integer, Integer> bankCache = new HashMap<>();
    private final Script script;

    public BankCache(Script script) {
        this.script = script;
    }

    @Override
    public void run() {
        while (!script.isStopping()) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!Bank.isOpen())
                continue;

            bankCache.clear();
            update();
        }
    }

    /**
     * Updates the bank cache.
     */
    private void update() {
        final Item[] items = Bank.getItems();
        if (items.length == 0)
            return;

        for (Item item : items)
            bankCache.put(item.getId(), item.getStackSize());
    }

    /**
     * Gets the bank cache;
     *
     * @return The bank cache;
     */
    public Map<Integer, Integer> get() {
        return bankCache;
    }
}

