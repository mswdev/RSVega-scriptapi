package org.api.game.skills.contruction;

public enum PlankType {

    PLANK("Plank", 960, 100),
    OAK("Oak plank", 8778, 250),
    TEAK("Teak plank", 8780, 500),
    MAHOGANY("Mahogany plank", 8782, 1500);

    private final String name;
    private final int itemId;
    private final int sawmillCost;

    PlankType(String name, int itemId, int sawmillCost) {
        this.name = name;
        this.itemId = itemId;
        this.sawmillCost = sawmillCost;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSawmillCost() {
        return sawmillCost;
    }
}
