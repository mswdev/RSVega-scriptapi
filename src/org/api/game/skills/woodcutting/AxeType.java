package org.api.game.skills.woodcutting;

import org.api.game.skills.mining.PickaxeType;

import java.util.Arrays;

public enum AxeType {

    BRONZE("Bronze axe", 1351, 1, 1, false),
    IRON("Iron axe", 1349, 1, 1, false),
    STEEL("Steel axe", 1353, 6, 5, false),
    BLACK("Black axe", 1361, 11, 10, false),
    MITHRIL("Mithril axe", 1355, 21, 20, false),
    ADAMANT("Adamant axe", 1357, 31, 30, false),
    RUNE("Rune axe", 1359, 41, 40, false),
    DRAGON("Dragon axe", 6739, 61, 60, true),
    THIRD_AGE("3rd age axe", 20011, 61, 65, true),
    INFERNAL("Infernal axe", 13241, 61, -1, true);

    private final String name;
    private final int itemId;
    private final int requiredWoodcuttingLevel;
    private final int requiredAttackLevel;
    private final boolean members;

    AxeType(String name, int itemId, int requiredWoodcuttingLevel, int requiredAttackLevel, boolean members) {
        this.name = name;
        this.itemId = itemId;
        this.requiredWoodcuttingLevel = requiredWoodcuttingLevel;
        this.requiredAttackLevel = requiredAttackLevel;
        this.members = members;
    }

    /**
     * Gets all of the axe item ids in the enum.
     *
     * @return An array containing all of the axe item ids in the enum.
     */
    public static int[] getItemIds() {
        return Arrays.stream(PickaxeType.values()).mapToInt(PickaxeType::getItemId).toArray();
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequiredWoodcuttingLevel() {
        return requiredWoodcuttingLevel;
    }

    public int getRequiredAttackLevel() {
        return requiredAttackLevel;
    }

    public boolean isMembers() {
        return members;
    }
}

