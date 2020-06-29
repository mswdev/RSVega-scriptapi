package org.api.game.questing;

import org.api.script.framework.mission.Mission;
import org.api.script.impl.mission.questing.restless_ghost_mission.RestlessGhostMission;
import org.api.script.impl.mission.questing.romeo_and_juliet.RomeoAndJulietMission;
import org.api.script.impl.mission.questing.rune_mysteries_mission.RuneMysteriesMission;
import org.api.script.impl.mission.questing.sheep_shearer_mission.SheepShearerMission;
import org.rspeer.runetek.api.Varps;

public enum QuestType {

    //Free
    BLACK_KNIGHTS_FORTRESS(null, 0, 0, 0, false),
    COOKS_ASSISTANT(null, 0, 0, 0, false),
    THE_CORSAIR_CURSE(null, 0, 0, 0, false),
    DEMON_SLAYER(null, 0, 0, 0, false),
    DORICS_QUEST(null, 0, 0, 0, false),
    DRAGON_SLAYER(null, 0, 0, 0, false),
    ERNEST_THE_CHICKEN(null, 0, 0, 0, false),
    GOBLIN_DIPLOMACY(null, 0, 0, 0, false),
    IMP_CATCHER(null, 0, 0, 0, false),
    THE_KNIGHTS_SWORD(null, 0, 0, 0, false),
    MISTHALIN_MYSTERY(null, 0, 0, 0, false),
    PIRATES_TREASURE(null, 0, 0, 0, false),
    PRINCE_ALI_RESCUE(null, 0, 0, 0, false),
    THE_RESTLESS_GHOST(new RestlessGhostMission(null), 1, 107, 5, false),
    ROMEO_AND_JULIET(new RomeoAndJulietMission(null), 5, 144, 100, false),
    RUNE_MYSTERIES(new RuneMysteriesMission(null), 1, 63, 6, false),
    SHEEP_SHEARER(new SheepShearerMission(null), 1, 179, 21, false),
    SHIELD_OF_ARRAV(null, 0, 0, 0, false),
    VAMPIRE_SLAYER(null, 0, 0, 0, false),
    WITCHS_POTION(null, 0, 0, 0, false),
    X_MARKS_THE_SPOT(null, 0, 0, 0, false),

    //Members
    ANIMAL_MAGNETISM(null, 0, 0, 0, true),
    ANOTHER_SLICE_OF_HAM(null, 0, 0, 0, true),
    THE_ASCENT_OF_ARCEUUS(null, 0, 0, 0, true),
    BETWEEN_A_ROCK(null, 0, 0, 0, true),
    BIG_CHOMPY_BIRD_HUNTING(null, 0, 0, 0, true),
    BIOHAZARD(null, 0, 0, 0, true),
    BONE_VOYAGE(null, 0, 0, 0, true),
    CABIN_FEVER(null, 0, 0, 0, true),
    CLIENT_OF_KOUREND(null, 0, 0, 0, true),
    CLOCK_TOWER(null, 0, 0, 0, true),
    COLD_WAR(null, 0, 0, 0, true),
    CONTACT(null, 0, 0, 0, true),
    CREATURE_OF_FENKENSTRAIN(null, 0, 0, 0, true),
    DARKNESS_OF_HALLOWVALE(null, 0, 0, 0, true),
    DEATH_PLATEAU(null, 0, 0, 0, true),
    DEATH_TO_THE_DORGESHUUN(null, 0, 0, 0, true),
    THE_DEPTHS_OF_DESPAIR(null, 0, 0, 0, true),
    DESERT_TREASURE(null, 0, 0, 0, true),
    DEVIOUS_MINDS(null, 0, 0, 0, true),
    THE_DIG_SITE(null, 0, 0, 0, true),
    DRAGON_SLAYER_II(null, 0, 0, 0, true),
    DREAM_MENTOR(null, 0, 0, 0, true),
    DRUIDIC_RITUAL(null, 0, 0, 0, true),
    DWARF_CANNON(null, 0, 0, 0, true),
    EADGARS_RUSE(null, 0, 0, 0, true),
    EAGLES_PEAK(null, 0, 0, 0, true),
    ELEMENTAL_WORKSHOP_I(null, 0, 0, 0, true),
    ELEMENTAL_WORKSHOP_II(null, 0, 0, 0, true),
    ENAKHRAS_LAMENT(null, 0, 0, 0, true),
    ENLIGHTENED_JOURNEY(null, 0, 0, 0, true),
    THE_EYES_OF_GLOUPHRIE(null, 0, 0, 0, true),
    FAIRYTALE_I_GROWING_PAINTS(null, 0, 0, 0, true),
    FAIRYTALE_II_CURE_A_QUEEN(null, 0, 0, 0, true),
    FAMILY_CREST(null, 0, 0, 0, true),
    THE_FEUD(null, 0, 0, 0, true),
    FIGHT_AREA(null, 0, 0, 0, true),
    FISHING_CONTEST(null, 0, 0, 0, true),
    FORGETTABLE_TALE(null, 0, 0, 0, true),
    THE_FORSAKEN_TOWER(null, 0, 0, 0, true),
    THE_FREMENNIK_ISLES(null, 0, 0, 0, true),
    THE_FREMENNIK_TRIALS(null, 0, 0, 0, true),
    GARDEN_OF_TRANQUILITY(null, 0, 0, 0, true),
    GERTRUDES_CAT(null, 0, 0, 0, true),
    GHOSTS_AHOY(null, 0, 0, 0, true),
    THE_GIANT_DWARF(null, 0, 0, 0, true),
    THE_GOLEM(null, 0, 0, 0, true),
    THE_GRAND_TREE(null, 0, 0, 0, true),
    THE_GREAT_BRAIN_ROBBERY(null, 0, 0, 0, true),
    GRIM_TALES(null, 0, 0, 0, true),
    THE_HAND_IN_THE_SAND(null, 0, 0, 0, true),
    HAUNTED_MINE(null, 0, 0, 0, true),
    HAZEEL_CULT(null, 0, 0, 0, true),
    HEROES_QUEST(null, 0, 0, 0, true),
    HOLY_GRAIL(null, 0, 0, 0, true),
    HORROR_FROM_THE_DEEP(null, 0, 0, 0, true),
    ICTHLARINS_LITTLE_HELPER(null, 0, 0, 0, true),
    IN_AID_OF_THE_MYREQUE(null, 0, 0, 0, true),
    IN_SEARCH_OF_THE_MYREQUE(null, 0, 0, 0, true),
    JUNGLE_POTION(null, 0, 0, 0, true),
    KINGS_RANSOM(null, 0, 0, 0, true),
    LEGENDS_QUEST(null, 0, 0, 0, true),
    LOST_CITY(null, 0, 0, 0, true),
    THE_LOST_TRIBE(null, 0, 0, 0, true),
    LUNAR_DIPLOMACY(null, 0, 0, 0, true),
    MAKING_FRIENDS_WITH_MY_ARM(null, 0, 0, 0, true),
    MAKING_HISTORY(null, 0, 0, 0, true),
    MERLINS_CRYSTAL(null, 0, 0, 0, true),
    MONKS_FRIEND(null, 0, 0, 0, true),
    MONKEY_MADNESS_I(null, 0, 0, 0, true),
    MONKEY_MADNESS_II(null, 0, 0, 0, true),
    MOUNTAIN_DAUGHTER(null, 0, 0, 0, true),
    MOURNINGS_END_PART_I(null, 0, 0, 0, true),
    MOURNINGS_END_PART_II(null, 0, 0, 0, true),
    MURDER_MYSTERY(null, 0, 0, 0, true),
    MY_ARMS_BIG_ADVENTURE(null, 0, 0, 0, true),
    NATURE_SPIRIT(null, 0, 0, 0, true),
    OBSERVATORY_QUEST(null, 0, 0, 0, true),
    OLAFS_QUEST(null, 0, 0, 0, true),
    ONE_SMALL_FAVOUR(null, 0, 0, 0, true),
    PLAGUE_CITY(null, 0, 0, 0, true),
    PRIEST_IN_PERIL(null, 0, 0, 0, true),
    THE_QUEEN_OF_THIEVES(null, 0, 0, 0, true),
    RAG_AND_BONE_MAN(null, 0, 0, 0, true),
    RAG_AND_BONE_MAN_II(null, 0, 0, 0, true),
    RATCATCHERS(null, 0, 0, 0, true),
    RECIPE_FOR_DISASTER(null, 0, 0, 0, true),
    RECRUITMENT_DRIVE(null, 0, 0, 0, true),
    REGICIDE(null, 0, 0, 0, true),
    ROVING_ELVES(null, 0, 0, 0, true),
    ROYAL_TROUBLE(null, 0, 0, 0, true),
    RUM_DEAL(null, 0, 0, 0, true),
    SCORPION_CATCHER(null, 0, 0, 0, true),
    SEA_SLUG(null, 0, 0, 0, true),
    SHADES_OF_MORTTON(null, 0, 0, 0, true),
    SHADOW_OF_THE_STORM(null, 0, 0, 0, true),
    SHEEP_HERDER(null, 0, 0, 0, true),
    SHILO_VILLAGE(null, 0, 0, 0, true),
    THE_SLUG_MENACE(null, 0, 0, 0, true),
    A_SOULS_BANE(null, 0, 0, 0, true),
    SPIRITS_OF_THE_ELID(null, 0, 0, 0, true),
    SWAN_SONG(null, 0, 0, 0, true),
    TAI_BWO_WANNAI_TRIO(null, 0, 0, 0, true),
    A_TAIL_OF_TWO_CATS(null, 0, 0, 0, true),
    TEARS_OF_GUTHIX(null, 0, 0, 0, true),
    TEMPLE_OF_IKOV(null, 0, 0, 0, true),
    THRONE_OF_MISCELLANIA(null, 0, 0, 0, true),
    THE_TOURIST_TRAP(null, 0, 0, 0, true),
    TOWER_OF_LIFE(null, 0, 0, 0, true),
    TREE_GNOME_VILLAGE(null, 0, 0, 9, true),
    TRIBAL_TOTEM(null, 0, 0, 0, true),
    TROLL_ROMANCE(null, 0, 0, 0, true),
    TROLL_STRONGHOLD(null, 0, 0, 0, true),
    UNDERGROUND_PASS(null, 0, 0, 0, true),
    WANTED(null, 0, 0, 0, true),
    WATCHTOWER(null, 0, 0, 0, true),
    WATERFALL_QUEST(null, 0, 0, 0, true),
    WHAT_LIES_BELOW(null, 0, 0, 0, true),
    WITCHES_HOUSE(null, 0, 0, 0, true),
    ZORGE_FLESH_EATERS(null, 0, 0, 0, true),

    //Miniquests
    TUTORIAL_ISLAND_V1(null, 0, 281, 1000, false),
    TUTORIAL_ISLAND_V2(null, 0, 2686, 1000, false),
    ALFRED_GRIMHANDS_BARCRAWL(null, 0, 0, 0, true),
    ARCHITECTURAL_ALLIANCE(null, 0, 0, 0, true),
    BEAR_YOUR_SOUL(null, 0, 0, 0, true),
    CURSE_OF_THE_EMPTY_LORD(null, 0, 0, 0, true),
    ENCHANTED_KEY(null, 0, 0, 0, true),
    ENTER_THE_ABYSS(null, 0, 0, 0, true),
    FAMILY_PEST(null, 0, 0, 0, true),
    THE_GENERALS_SHADOW(null, 0, 0, 0, true),
    LAIR_OF_TARN_RAZORLOR(null, 0, 0, 0, true),
    THE_MAGE_ARENA(null, 0, 0, 0, true),
    THE_MAGE_ARENA_II(null, 0, 0, 0, true),
    SKIPPY_AND_THE_MOGRES(null, 0, 0, 0, true);

    private Mission mission;
    private int points;
    private int varp;
    private int complete;
    private boolean members;

    QuestType(Mission mission, int points, int varp, int isComplete, boolean isMembers) {
        this.mission = mission;
        this.points = points;
        this.varp = varp;
        this.complete = isComplete;
        this.members = isMembers;
    }

    public Mission getMission() {
        return mission;
    }

    public int getPoints() {
        return points;
    }

    public int getVarp() {
        return varp;
    }

    public int getComplete() {
        return complete;
    }

    public boolean isComplete() {
        return Varps.get(varp) == complete;
    }

    public boolean isMembers() {
        return members;
    }

    public boolean hasStarted() {
        return Varps.get(varp) > 0 && !isComplete();
    }
}
