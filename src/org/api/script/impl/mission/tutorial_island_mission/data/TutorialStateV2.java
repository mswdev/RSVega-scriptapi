package org.api.script.impl.mission.tutorial_island_mission.data;

import org.api.game.questing.QuestType;
import org.api.script.framework.worker.Worker;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.HintWorker;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.OpenTab;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.account_guide.PollBooth;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.combat_instructor.FightRat;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.combat_instructor.OpenEquipmentStats;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.magic_instructor.CastAirStrike;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.magic_instructor.CastHomeTeleport;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.master_navigator.OpenWorldMap;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.mining_instructor.SmithDagger;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.prayer_instructor.ActivatePrayer;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.survival_expert.CatchShrimp;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.survival_expert.ChopTree;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.survival_expert.CookShrimp;
import org.api.script.impl.mission.tutorial_island_mission.worker.impl.stage.survival_expert.LightFire;
import org.api.script.impl.worker.DialogueWorker;
import org.api.script.impl.worker.MovementWorker;
import org.api.script.impl.worker.interactables.ItemWorker;
import org.api.script.impl.worker.interactables.NpcWorker;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.component.tab.Tab;
import org.rspeer.runetek.api.movement.position.Position;

import java.util.Arrays;

public enum TutorialStateV2 {

    //Gielinor Guide
    CHARACTER_DESIGN(null, 1),
    GIELINOR_GUIDE_DIALOGUE(new NpcWorker(a -> a.getName().equals("Gielinor Guide")), 2),

    //Survival Expert
    ENTER_SURVIVAL_EXPERT(new HintWorker(), 10),
    SURVIVAL_EXPERT_DIALOGUE(new NpcWorker(a -> a.getName().equals("Survival Expert")), 20, 60),
    OPEN_INVENTORY_TAB(new OpenTab(Tab.INVENTORY), 30),
    CATCH_SHRIMP(new CatchShrimp(), 40),
    OPEN_SKILL_TAB(new OpenTab(Tab.SKILLS), 50),
    CHOP_TREE(new ChopTree(), 70),
    LIGHT_FIRE(new LightFire(), 80),
    COOK_SHRIMP(new CookShrimp(), 90),

    //Master Navigator
    ENTER_MASTER_NAVIGATOR(new HintWorker(), 120, 130),
    MASTER_CHEF_DIALOGUE(new NpcWorker(a -> a.getName().equals("Master Navigator")), 140, 150, 155, 160, 162, 164, 166),
    OPEN_WORLD_MAP(new OpenWorldMap(), 145),

    //Quest Guide
    ENTER_QUEST_GUIDE(new HintWorker(), 170, 210),
    QUEST_GUIDE_DIALOGUE(new NpcWorker(a -> a.getName().equals("Quest Guide"), new MovementWorker(new Position(1678, 6131, 0))), 220, 240),
    OPEN_QUEST_TAB(new OpenTab(Tab.QUEST_LIST), 230),

    //Mining Instructor
    ENTER_MINING_INSTRUCTOR(new HintWorker(), 250),
    MINING_INSTRUCTOR_DIALOGUE(new NpcWorker(a -> a.getName().equals("Mining Instructor"), new MovementWorker(new Position(1673, 12515, 0))), 260, 270),
    MINE_TIN(new HintWorker(), 300),
    MINE_COPPER(new HintWorker(), 310),
    SMELT_ORE(new HintWorker(), 320),
    OPEN_ANVIL(new HintWorker(), 330),
    SMITH_DAGGER(new SmithDagger(), 340),

    //Combat Instructor
    ENTER_COMBAT_INSTRUCTOR(new HintWorker(), 350),
    COMBAT_INSTRUCTOR_DIALOGUE(new NpcWorker(a -> a.getName().equals("Combat Instructor")), 360, 410, 470),
    OPEN_EQUIPMENT_TAB(new OpenTab(Tab.EQUIPMENT), 390),
    OPEN_EQUIPMENT_STATS(new OpenEquipmentStats(), 400),
    EQUIP_DAGGER(new ItemWorker(a -> a.getName().equals("Bronze dagger")), 405),
    EQUIP_SWORD_AND_SHEILD(new ItemWorker(a -> a.getName().equals("Bronze sword") || a.getName().equals("Wooden shield")), 420),
    OPEN_COMBAT_TAB(new OpenTab(Tab.COMBAT), 430),
    MELEE_RAT(new FightRat(), 440, 450),
    LOOT_BONES(new HintWorker(), 465),
    RANGE_RAT(new FightRat(), 480, 490),

    //Brother Brace
    ENTER_BROTHER_BRACE(new HintWorker(), 500),
    BROTHER_BRACE_DIALOGUE(new NpcWorker(a -> a.getName().equals("Brother Brace"), new MovementWorker(new Position(1715, 6115, 0))), 510, 525, 535, 545, 555),
    OPEN_PRAYER_TAB(new OpenTab(Tab.PRAYER), 520),
    ACTIVATE_PRAYER(new ActivatePrayer(), 530),
    BURY_BONES(new ItemWorker(a -> a.getName().equals("Bones")), 540),
    OPEN_FRIEND_TAB(new OpenTab(Tab.FRIENDS_LIST), 550),

    //Bank Guide
    ENTER_BANK_GUIDE(new HintWorker(), 560),
    OPEN_BANK(new HintWorker(), 565),
    POLL_BOOTH(new PollBooth(), 570),

    //Magic Instructor
    ENTER_MAGIC_INSTRUCTOR(new HintWorker(), 580),
    MAGIC_INSTRUCTOR_DIALOGUE(new NpcWorker(a -> a.getName().equals("Magic Instructor"), new DialogueWorker(a -> a.equals("Yes.") || a.equals("No, I'm not planning to do that.")), new MovementWorker(new Position(1733, 6095, 0))), 620, 640, 670),
    OPEN_MAGIC_TAB(new OpenTab(Tab.MAGIC), 630),
    CAST_AIR_STRIKE(new CastAirStrike(), 650),
    CAST_HOME_TELEPORT(new CastHomeTeleport(), 680),

    //Tutorial Island Complete
    COMPLETE(null, 1000);

    private final Worker worker;
    private final int[] varps;

    TutorialStateV2(Worker worker, int... varps) {
        this.varps = varps;
        this.worker = worker;
    }

    public static TutorialStateV2 getValidState() {
        return Arrays.stream(values()).filter(TutorialStateV2::isInVarp).findFirst().orElse(null);
    }

    public static boolean isInVarp(TutorialStateV2 state) {
        return Arrays.stream(state.getVarps()).anyMatch(a -> a == Varps.get(QuestType.TUTORIAL_ISLAND_V2.getVarp()));
    }

    public Worker getWorker() {
        return worker;
    }

    public int[] getVarps() {
        return varps;
    }
}
