package org.api.game.skills.firemaking;

public enum LogType {

    LOGS("Logs", 1511, 1512, 1, false),
    ACHEY("Achey logs", 2862, 2863, 1, true),
    OAK("Oak logs", 1521, 1522, 15, false),
    WILLOW("Willow logs", 1519, 1520, 30, false),
    TEAK("Teak logs", 6333, 6334, 35, true),
    ARCTIC_PINE("Arctic pine logs", 10810, 10811, 42, true),
    MAPLE("Maple logs", 1517, 1518, 45, false),
    MAHOGANY("Mahogany logs", 6332, 8836, 50, true),
    YEW("Yew logs", 1515, 1516, 60, false),
    MAGIC("Magic logs", 1513, 1514, 75, true),
    REDWOOD("Redwood logs", 19669, 19670, 90, true);

    private final String name;
    private final int itemId;
    private final int notedItemId;
    private final int requiredFiremakingLevel;
    private final boolean members;

    LogType(String name, int itemId, int notedItemId, int requiredFiremakingLevel, boolean members) {
        this.name = name;
        this.itemId = itemId;
        this.notedItemId = notedItemId;
        this.requiredFiremakingLevel = requiredFiremakingLevel;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getNotedItemId() {
        return notedItemId;
    }

    public int getRequiredFiremakingLevel() {
        return requiredFiremakingLevel;
    }

    public boolean isMembers() {
        return members;
    }
}
