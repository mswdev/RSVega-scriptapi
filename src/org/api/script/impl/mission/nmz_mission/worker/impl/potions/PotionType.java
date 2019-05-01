package org.api.script.impl.mission.nmz_mission.worker.impl.potions;

import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.Arrays;

public enum PotionType {

    SUPER_RANGING("Super ranging", "Super ranging potion", 0, 3951, -1),
    SUPER_MAGIC("Super magic", "Super magic potion", 3, 3952, -1),
    OVERLOAD("Overload", "Overload potion", 6, 3953, 3955),
    ABSORPTION("Absorption", "Absorption potion", 9, 3954, 3956);

    private final String name;
    private final String barrelName;
    private final int shopInterfaceId;
    private final int amountOwnedVarpbit;
    private final int activeVarpbit;

    PotionType(String name, String barrelName, int shopInterfaceId, int amountOwnedVarpbit, int activeVarpbit) {
        this.name = name;
        this.barrelName = barrelName;
        this.shopInterfaceId = shopInterfaceId;
        this.amountOwnedVarpbit = amountOwnedVarpbit;
        this.activeVarpbit = activeVarpbit;
    }

    public static int getCount(PotionType potionType) {
        return Arrays.stream(Inventory.getItems()).filter(a -> a.getName().contains(potionType.getName())).mapToInt(a -> Integer.parseInt(a.getName().replaceAll("[^0-9]+", ""))).sum();
    }

    public String getName() {
        return name;
    }

    public String getBarrelName() {
        return barrelName;
    }

    public int getShopInterfaceId() {
        return shopInterfaceId;
    }

    public int getAmountOwnedVarpbit() {
        return amountOwnedVarpbit;
    }

    public int getActiveVarpbit() {
        return activeVarpbit;
    }
}

