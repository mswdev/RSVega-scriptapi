package org.api.game.skills.woodcutting;

import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;

import java.util.Arrays;

public class WoodcuttingUtil {

    /**
     * Gets the most appropriate axe the player has in their equipment, inventory, or bank.
     *
     * @return The most appropriate axe.
     */
    public static AxeType getAppropriateOwnedAxe() {
        return Arrays.stream(AxeType.values())
                .filter(axeType -> Skills.getLevel(Skill.WOODCUTTING) >= axeType.getRequiredWoodcuttingLevel() && Equipment.contains(axeType.getItemId()) || Inventory.contains(axeType.getItemId()) || Bank.contains(axeType.getItemId()))
                .reduce((first, second) -> second)
                .orElse(AxeType.IRON);
    }

    /**
     * Gets the most appropriate axe the player can use.
     *
     * @return The most appropriate axe.
     */
    public static AxeType getAppropriateAxe() {
        return Arrays.stream(AxeType.values())
                .filter(axeType -> Skills.getLevel(Skill.WOODCUTTING) >= axeType.getRequiredWoodcuttingLevel())
                .reduce((first, second) -> second)
                .orElse(AxeType.IRON);
    }

    /**
     * Gets the most appropriate progressive tree the player can woodcut.
     *
     * @return The most appropriate progressive tree.
     */
    public static TreeType getAppropriateTree() {
        return Arrays.stream(TreeType.values())
                .filter(treeType -> Skills.getLevel(Skill.WOODCUTTING) >= treeType.getRequiredWoodcuttingLevel() && treeType.isProgressive())
                .reduce((first, second) -> second)
                .orElse(TreeType.TREE);
    }
}

