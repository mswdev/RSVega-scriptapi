package org.api.game.skills.mining;

import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;

import java.util.Arrays;

public class MiningUtil {

    /**
     * Gets the most appropriate pickaxe the player has in their equipment, inventory, or bank.
     *
     * @return The most appropriate pickaxe.
     */
    public static PickaxeType getAppropriateOwnedPickaxe() {
        return Arrays.stream(PickaxeType.values())
                .filter(pickaxeType -> Skills.getLevel(Skill.MINING) >= pickaxeType.getRequiredMiningLevel() && Equipment.contains(pickaxeType.getItemId()) || Inventory.contains(pickaxeType.getItemId()) || Bank.contains(pickaxeType.getItemId()))
                .reduce((first, second) -> second)
                .orElse(PickaxeType.IRON);
    }

    /**
     * Gets the most appropriate pickaxe the player can use.
     *
     * @return The most appropriate pickaxe.
     */
    public static PickaxeType getAppropriatePickaxe() {
        return Arrays.stream(PickaxeType.values())
                .filter(pickaxeType -> Skills.getLevel(Skill.MINING) >= pickaxeType.getRequiredMiningLevel())
                .reduce((first, second) -> second)
                .orElse(PickaxeType.IRON);
    }

    /**
     * Gets the most appropriate progressive ore the player can mine.
     *
     * @return The most appropriate progressive ore.
     */
    public static OreType getAppropriateOre() {
        return Arrays.stream(OreType.values())
                .filter(oreType -> Skills.getLevel(Skill.MINING) >= oreType.getRequiredMiningLevel() && oreType.isProgressive())
                .reduce((first, second) -> second)
                .orElse(OreType.CLAY);
    }
}

