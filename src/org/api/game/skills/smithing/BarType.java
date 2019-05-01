package org.api.game.skills.smithing;

import org.api.game.skills.mining.OreType;

public enum BarType {

    BRONZE("Bronze bar", 2349, 1, false, OreType.TIN, OreType.COPPER, 1, 1),
    BLURITE("Blurite bar", 9467, 8, true, OreType.BLURITE, null, 1, -1),
    IRON("Iron bar", 2351, 15, false, OreType.IRON, null, 1, -1),
    SILVER("Sliver bar", 2355, 20, false, OreType.SILVER, null, 1, -1),
    STEEL("Steel bar", 2353, 30, false, OreType.COAL, OreType.IRON, 2, 1),
    GOLD("Gold bar", 2357, 40, false, OreType.GOLD, null, 1, -1),
    LOVAKITE("Lovakite bar", 13354, 45, true, OreType.COAL, OreType.LOVAKITE, 2, 1),
    MITHRIL("Mithril bar", 2359, 50, false, OreType.COAL, OreType.MITHRIL, 4, 1),
    ADAMANT("Adamantite bar", 2361, 70, false, OreType.COAL, OreType.ADAMANTITE, 6, 1),
    RUNITE("Rune bar", 2363, 85, false, OreType.COAL, OreType.RUNITE, 8, 1);

    private final String name;
    private final int itemId;
    private final int requiredSmithingLevel;
    private final boolean isMembers;
    private final OreType ingredientOne;
    private final OreType ingredientTwo;
    private final int ingredientOneAmount;
    private final int ingredientTwoAmount;

    BarType(String name, int itemId, int requiredSmithingLevel, boolean isMembers, OreType ingredientOne, OreType ingredientTwo, int ingredientOneAmount, int ingredientTwoAmount) {
        this.name = name;
        this.itemId = itemId;
        this.requiredSmithingLevel = requiredSmithingLevel;
        this.isMembers = isMembers;
        this.ingredientOne = ingredientOne;
        this.ingredientTwo = ingredientTwo;
        this.ingredientOneAmount = ingredientOneAmount;
        this.ingredientTwoAmount = ingredientTwoAmount;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequiredSmithingLevel() {
        return requiredSmithingLevel;
    }

    public boolean isMembers() {
        return isMembers;
    }

    public OreType getIngredientOne() {
        return ingredientOne;
    }

    public OreType getIngredientTwo() {
        return ingredientTwo;
    }

    public int getIngredientOneAmount() {
        return ingredientOneAmount;
    }

    public int getIngredientTwoAmount() {
        return ingredientTwoAmount;
    }
}
