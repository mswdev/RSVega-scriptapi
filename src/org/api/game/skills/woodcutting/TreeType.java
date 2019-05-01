package org.api.game.skills.woodcutting;

import org.api.game.skills.firemaking.LogType;

public enum TreeType {

    TREE("Tree", 1, false, true, LogType.LOGS),
    ACHEY("Achey", 1, true, false, LogType.ACHEY),
    OAK("Oak", 15, false, true, LogType.OAK),
    WILLOW("Willow", 30, false, true, LogType.WILLOW),
    TEAK("Teak", 35, true, false, LogType.TEAK),
    MAPLE("Maple", 45, false, true, LogType.MAPLE),
    MAHOGANY("Mahogany", 50, true, false, LogType.MAHOGANY),
    ARCTIC_PINE("Arctic pine", 54, true, false, LogType.ARCTIC_PINE),
    YEW("Yew", 60, false, false, LogType.YEW),
    MAGIC("Magic", 75, true, false, LogType.MAGIC),
    REDWOOD("Redwood", 90, true, false, LogType.REDWOOD);

    private final String name;
    private final int requiredWoodcuttingLevel;
    private final boolean members;
    private final boolean progressive;
    private final LogType logType;

    TreeType(String name, int requiredWoodcuttingLevel, boolean members, boolean progressive, LogType logType) {
        this.name = name;
        this.requiredWoodcuttingLevel = requiredWoodcuttingLevel;
        this.members = members;
        this.progressive = progressive;
        this.logType = logType;
    }

    public String getName() {
        return name;
    }

    public int getRequiredWoodcuttingLevel() {
        return requiredWoodcuttingLevel;
    }

    public boolean isMembers() {
        return members;
    }

    public boolean isProgressive() {
        return progressive;
    }

    public LogType getLogType() {
        return logType;
    }
}

