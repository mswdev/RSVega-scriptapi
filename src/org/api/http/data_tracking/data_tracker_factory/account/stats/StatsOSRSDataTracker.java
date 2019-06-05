package org.api.http.data_tracking.data_tracker_factory.account.stats;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.game.player.Player;
import org.api.game.questing.Questing;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Players;

public class StatsOSRSDataTracker extends RSVegaTrackerFactory {

    public static RequestBody getStatsOSRSData() {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("is_tutorial", String.valueOf(Player.isTutorial() ? 1 : 0));
        formBuilder.add("ironman_state", String.valueOf(Player.getIronManState().getState()));
        formBuilder.add("level_total", String.valueOf(Skills.getTotalLevel()));
        formBuilder.add("level_combat", String.valueOf(Players.getLocal().getCombatLevel()));
        formBuilder.add("level_attack", String.valueOf(Skills.getLevel(Skill.ATTACK)));
        formBuilder.add("level_defence", String.valueOf(Skills.getLevel(Skill.DEFENCE)));
        formBuilder.add("level_strength", String.valueOf(Skills.getLevel(Skill.STRENGTH)));
        formBuilder.add("level_hitpoints", String.valueOf(Skills.getLevel(Skill.HITPOINTS)));
        formBuilder.add("level_ranged", String.valueOf(Skills.getLevel(Skill.RANGED)));
        formBuilder.add("level_prayer", String.valueOf(Skills.getLevel(Skill.PRAYER)));
        formBuilder.add("level_magic", String.valueOf(Skills.getLevel(Skill.MAGIC)));
        formBuilder.add("level_cooking", String.valueOf(Skills.getLevel(Skill.COOKING)));
        formBuilder.add("level_woodcutting", String.valueOf(Skills.getLevel(Skill.WOODCUTTING)));
        formBuilder.add("level_fletching", String.valueOf(Skills.getLevel(Skill.FLETCHING)));
        formBuilder.add("level_fishing", String.valueOf(Skills.getLevel(Skill.FISHING)));
        formBuilder.add("level_firemaking", String.valueOf(Skills.getLevel(Skill.FIREMAKING)));
        formBuilder.add("level_crafting", String.valueOf(Skills.getLevel(Skill.CRAFTING)));
        formBuilder.add("level_smithing", String.valueOf(Skills.getLevel(Skill.SMITHING)));
        formBuilder.add("level_mining", String.valueOf(Skills.getLevel(Skill.MINING)));
        formBuilder.add("level_herblore", String.valueOf(Skills.getLevel(Skill.HERBLORE)));
        formBuilder.add("level_agility", String.valueOf(Skills.getLevel(Skill.AGILITY)));
        formBuilder.add("level_thieving", String.valueOf(Skills.getLevel(Skill.THIEVING)));
        formBuilder.add("level_slayer", String.valueOf(Skills.getLevel(Skill.SLAYER)));
        formBuilder.add("level_farming", String.valueOf(Skills.getLevel(Skill.FARMING)));
        formBuilder.add("level_runecrafting", String.valueOf(Skills.getLevel(Skill.RUNECRAFTING)));
        formBuilder.add("level_hunter", String.valueOf(Skills.getLevel(Skill.HUNTER)));
        formBuilder.add("level_construction", String.valueOf(Skills.getLevel(Skill.CONSTRUCTION)));
        formBuilder.add("quest_points", String.valueOf(Questing.getPoints()));
        formBuilder.add("quests_complete", Questing.getCompletedQuests());
        return formBuilder.build();
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new StatsOSRSData();
    }
}
