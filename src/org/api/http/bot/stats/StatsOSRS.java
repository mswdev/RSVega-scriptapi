package org.api.http.bot.stats;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.game.player.Player;
import org.api.game.questing.Questing;
import org.api.http.RSVegaTracker;
import org.api.http.wrappers.Request;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

import java.io.IOException;

public class StatsOSRS {

    public static boolean updateStatsOSRS(int botId, RequestBody requestBody) {
        try (final Response response = Request.put(RSVegaTracker.API_URL + "/bot/id/" + botId + "/stats/osrs/update", requestBody)) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static RequestBody getStatsOSRSDataRequestBody() {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("is_tutorial", String.valueOf(Player.isTutorial()));
        formBuilder.add("ironman_state", String.valueOf(Player.getIronManState().getState()));
        formBuilder.add("position_x", String.valueOf(Players.getLocal().getX()));
        formBuilder.add("position_y", String.valueOf(Players.getLocal().getY()));
        formBuilder.add("position_z", String.valueOf(Players.getLocal().getFloorLevel()));
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
}
