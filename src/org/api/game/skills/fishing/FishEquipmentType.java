package org.api.game.skills.fishing;

public enum FishEquipmentType {

    SMALL_FISHING_NET("Small fishing net", 303, 1, false),
    FISHING_ROD("Fishing rod", 307, 5, false),
    FISHING_BAIT("Fishing bait", 313, 5, false),
    BIG_FISHING_NET("Big fishing net", 305, 16, true),
    FLY_FISHING_ROD("Fly fishing rod", 309, 20, false),
    FEATHER("Feather", 314, 20, false),
    HARPOON("Harpoon", 311, 35, false),
    LOBSTER_POT("Lobster pot", 301, 40, false),
    BARBARIAN_ROD("Barbarian rod", 11323, 48, true),
    DRAGON_HARPOON("Dragon harpoon", 21028, 61, true),
    KARAMBWAN_VESSEL("Karambwan vessel", 3157, 65, true),
    INFERNAL_HARPOON("Infernal harpoon", 21031, 75, true),
    SANDWORMS("Sandworms", 13431, 82, true),
    DARK_FISHING_BAIT("Dark fishing bait", 11940, 85, true);

    private final String name;
    private final int itemId;
    private final int requiredFishingLevel;
    private final boolean isMembers;

    FishEquipmentType(String name, int itemId, int requiredFishingLevel, boolean isMembers) {
        this.name = name;
        this.itemId = itemId;
        this.requiredFishingLevel = requiredFishingLevel;
        this.isMembers = isMembers;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequiredFishingLevel() {
        return requiredFishingLevel;
    }

    public boolean isMembers() {
        return isMembers;
    }
}
