package net.apotheoticstudios.thuumcraft.skill;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.compat.EpicFightCompat;
import net.apotheoticstudios.thuumcraft.network.ClientboundSkillPerksPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum SkillPerk {
    ARCHERY_OVERDRAW(SkillProgression.Skill.ARCHERY, "Overdraw", 0, 20, 40, 60, 80),
    ARCHERY_CRITICAL_SHOT(SkillProgression.Skill.ARCHERY, "Critical Shot", req(30, 60, 90),
            "archery_overdraw"),
    ARCHERY_EAGLE_EYE(SkillProgression.Skill.ARCHERY, "Eagle Eye", req(30), "archery_overdraw"),
    ARCHERY_STEADY_HAND(SkillProgression.Skill.ARCHERY, "Steady Hand", req(40, 60),
            "archery_eagle_eye"),
    ARCHERY_POWER_SHOT(SkillProgression.Skill.ARCHERY, "Power Shot", req(50), "archery_eagle_eye"),
    ARCHERY_HUNTERS_DISCIPLINE(SkillProgression.Skill.ARCHERY, "Hunter's Discipline", req(50),
            "archery_critical_shot"),
    ARCHERY_RANGER(SkillProgression.Skill.ARCHERY, "Ranger", req(60), "archery_hunters_discipline"),
    ARCHERY_QUICK_SHOT(SkillProgression.Skill.ARCHERY, "Quick Shot", req(70), "archery_power_shot"),
    ARCHERY_BULLSEYE(SkillProgression.Skill.ARCHERY, "Bullseye", req(100), "archery_ranger",
            "archery_quick_shot"),

    BLOCK_SHIELD_WALL(SkillProgression.Skill.BLOCK, "Shield Wall", 0, 20, 40, 60, 80),
    BLOCK_DEFLECT_ARROWS(SkillProgression.Skill.BLOCK, "Deflect Arrows", req(30), "block_shield_wall"),
    BLOCK_BLOCK_RUNNER(SkillProgression.Skill.BLOCK, "Block Runner", req(70), "block_elemental_protection"),
    BLOCK_ELEMENTAL_PROTECTION(SkillProgression.Skill.BLOCK, "Elemental Protection", req(50),
            "block_deflect_arrows"),
    BLOCK_QUICK_REFLEXES(SkillProgression.Skill.BLOCK, "Quick Reflexes", req(30), "block_shield_wall"),
    BLOCK_POWER_BASH(SkillProgression.Skill.BLOCK, "Power Bash", req(30), "block_shield_wall"),
    BLOCK_DEADLY_BASH(SkillProgression.Skill.BLOCK, "Deadly Bash", req(50), "block_power_bash"),
    BLOCK_DISARMING_BASH(SkillProgression.Skill.BLOCK, "Disarming Bash", req(70), "block_deadly_bash"),
    BLOCK_SHIELD_CHARGE(SkillProgression.Skill.BLOCK, "Shield Charge", req(100), "block_disarming_bash",
            "block_block_runner"),

    HEAVY_ARMOR_JUGGERNAUT(SkillProgression.Skill.HEAVY_ARMOR, "Juggernaut", 0, 20, 40, 60, 80),
    HEAVY_ARMOR_FISTS_OF_STEEL(SkillProgression.Skill.HEAVY_ARMOR, "Fists of Steel", req(30),
            "heavy_armor_juggernaut"),
    HEAVY_ARMOR_CUSHIONED(SkillProgression.Skill.HEAVY_ARMOR, "Cushioned", req(50),
            "heavy_armor_fists_of_steel"),
    HEAVY_ARMOR_CONDITIONING(SkillProgression.Skill.HEAVY_ARMOR, "Conditioning", req(70),
            "heavy_armor_cushioned"),
    HEAVY_ARMOR_WELL_FITTED(SkillProgression.Skill.HEAVY_ARMOR, "Well Fitted", req(30),
            "heavy_armor_juggernaut"),
    HEAVY_ARMOR_TOWER_OF_STRENGTH(SkillProgression.Skill.HEAVY_ARMOR, "Tower of Strength", req(50),
            "heavy_armor_well_fitted"),
    HEAVY_ARMOR_MATCHING_SET(SkillProgression.Skill.HEAVY_ARMOR, "Matching Set", req(70),
            "heavy_armor_tower_of_strength"),
    HEAVY_ARMOR_REFLECT_BLOWS(SkillProgression.Skill.HEAVY_ARMOR, "Reflect Blows", req(100),
            "heavy_armor_matching_set"),

    ONE_HANDED_ARMSMAN(SkillProgression.Skill.ONE_HANDED, "Armsman", 0, 20, 40, 60, 80),
    ONE_HANDED_BLADESMAN(SkillProgression.Skill.ONE_HANDED, "Bladesman", req(30, 60, 90),
            "one_handed_armsman"),
    ONE_HANDED_BONE_BREAKER(SkillProgression.Skill.ONE_HANDED, "Bone Breaker", req(30, 60, 90),
            "one_handed_armsman"),
    ONE_HANDED_DUAL_FLURRY(SkillProgression.Skill.ONE_HANDED, "Dual Flurry", req(30, 50),
            "one_handed_armsman"),
    ONE_HANDED_DUAL_SAVAGERY(SkillProgression.Skill.ONE_HANDED, "Dual Savagery", req(70),
            "one_handed_dual_flurry"),
    ONE_HANDED_FIGHTING_STANCE(SkillProgression.Skill.ONE_HANDED, "Fighting Stance", req(20),
            "one_handed_armsman"),
    ONE_HANDED_CRITICAL_CHARGE(SkillProgression.Skill.ONE_HANDED, "Critical Charge", req(50),
            "one_handed_fighting_stance"),
    ONE_HANDED_SAVAGE_STRIKE(SkillProgression.Skill.ONE_HANDED, "Savage Strike", req(50),
            "one_handed_fighting_stance"),
    ONE_HANDED_PARALYZING_STRIKE(SkillProgression.Skill.ONE_HANDED, "Paralyzing Strike", req(100),
            "one_handed_critical_charge", "one_handed_savage_strike"),
    ONE_HANDED_HACK_AND_SLASH(SkillProgression.Skill.ONE_HANDED, "Hack and Slash", req(30, 60, 90),
            "one_handed_armsman"),

    SMITHING_STEEL_SMITHING(SkillProgression.Skill.SMITHING, "Steel Smithing", req(0)),
    SMITHING_ARCANE_BLACKSMITH(SkillProgression.Skill.SMITHING, "Arcane Blacksmith", req(60),
            "smithing_steel_smithing"),
    SMITHING_ELVEN_SMITHING(SkillProgression.Skill.SMITHING, "Elven Smithing", req(30),
            "smithing_steel_smithing"),
    SMITHING_ADVANCED_ARMORS(SkillProgression.Skill.SMITHING, "Advanced Armors", req(50),
            "smithing_elven_smithing"),
    SMITHING_GLASS_SMITHING(SkillProgression.Skill.SMITHING, "Glass Smithing", req(70),
            "smithing_advanced_armors"),
    SMITHING_DRAGON_ARMOR(SkillProgression.Skill.SMITHING, "Dragon Armor", req(100),
            "smithing_daedric_smithing", "smithing_glass_smithing"),
    SMITHING_DWARVEN_SMITHING(SkillProgression.Skill.SMITHING, "Dwarven Smithing", req(30),
            "smithing_steel_smithing"),
    SMITHING_ORCISH_SMITHING(SkillProgression.Skill.SMITHING, "Orcish Smithing", req(50),
            "smithing_dwarven_smithing"),
    SMITHING_EBONY_SMITHING(SkillProgression.Skill.SMITHING, "Ebony Smithing", req(80),
            "smithing_orcish_smithing"),
    SMITHING_DAEDRIC_SMITHING(SkillProgression.Skill.SMITHING, "Daedric Smithing", req(90),
            "smithing_ebony_smithing"),

    TWO_HANDED_BARBARIAN(SkillProgression.Skill.TWO_HANDED, "Barbarian", 0, 20, 40, 60, 80),
    TWO_HANDED_LIMBSPLITTER(SkillProgression.Skill.TWO_HANDED, "Limbsplitter", req(30, 60, 90),
            "two_handed_barbarian"),
    TWO_HANDED_CHAMPIONS_STANCE(SkillProgression.Skill.TWO_HANDED, "Champion's Stance", req(20),
            "two_handed_barbarian"),
    TWO_HANDED_DEVASTATING_BLOW(SkillProgression.Skill.TWO_HANDED, "Devastating Blow", req(50),
            "two_handed_champions_stance"),
    TWO_HANDED_GREAT_CRITICAL_CHARGE(SkillProgression.Skill.TWO_HANDED, "Great Critical Charge", req(50),
            "two_handed_champions_stance"),
    TWO_HANDED_SWEEP(SkillProgression.Skill.TWO_HANDED, "Sweep", req(70),
            "two_handed_great_critical_charge", "two_handed_devastating_blow"),
    TWO_HANDED_WARMASTER(SkillProgression.Skill.TWO_HANDED, "Warmaster", req(100), "two_handed_sweep"),
    TWO_HANDED_DEEP_WOUNDS(SkillProgression.Skill.TWO_HANDED, "Deep Wounds", req(30, 60, 90),
            "two_handed_barbarian"),
    TWO_HANDED_SKULLCRUSHER(SkillProgression.Skill.TWO_HANDED, "Skullcrusher", req(30, 60, 90),
            "two_handed_barbarian"),

    ALCHEMY_ALCHEMIST(SkillProgression.Skill.ALCHEMY, "Alchemist", 0, 20, 40, 60, 80),
    ALCHEMY_PHYSICIAN(SkillProgression.Skill.ALCHEMY, "Physician", req(20), "alchemy_alchemist"),
    ALCHEMY_BENEFACTOR(SkillProgression.Skill.ALCHEMY, "Benefactor", req(30), "alchemy_physician"),
    ALCHEMY_EXPERIMENTER(SkillProgression.Skill.ALCHEMY, "Experimenter", req(50, 70, 90),
            "alchemy_benefactor"),
    ALCHEMY_PURITY(SkillProgression.Skill.ALCHEMY, "Purity", req(100), "alchemy_snakeblood"),
    ALCHEMY_POISONER(SkillProgression.Skill.ALCHEMY, "Poisoner", req(30), "alchemy_physician"),
    ALCHEMY_CONCENTRATED_POISON(SkillProgression.Skill.ALCHEMY, "Concentrated Poison", req(60),
            "alchemy_poisoner"),
    ALCHEMY_GREEN_THUMB(SkillProgression.Skill.ALCHEMY, "Green Thumb", req(70),
            "alchemy_concentrated_poison"),
    ALCHEMY_SNAKEBLOOD(SkillProgression.Skill.ALCHEMY, "Snakeblood", req(80),
            "alchemy_experimenter", "alchemy_concentrated_poison"),

    LIGHT_ARMOR_AGILE_DEFENDER(SkillProgression.Skill.LIGHT_ARMOR, "Agile Defender", 0, 20, 40, 60, 80),
    LIGHT_ARMOR_CUSTOM_FIT(SkillProgression.Skill.LIGHT_ARMOR, "Custom Fit", req(30),
            "light_armor_agile_defender"),
    LIGHT_ARMOR_MATCHING_SET(SkillProgression.Skill.LIGHT_ARMOR, "Matching Set", req(70),
            "light_armor_custom_fit"),
    LIGHT_ARMOR_UNHINDERED(SkillProgression.Skill.LIGHT_ARMOR, "Unhindered", req(50),
            "light_armor_custom_fit"),
    LIGHT_ARMOR_WIND_WALKER(SkillProgression.Skill.LIGHT_ARMOR, "Wind Walker", req(60),
            "light_armor_unhindered"),
    LIGHT_ARMOR_DEFT_MOVEMENT(SkillProgression.Skill.LIGHT_ARMOR, "Deft Movement", req(100),
            "light_armor_wind_walker", "light_armor_matching_set"),

    SNEAK_STEALTH(SkillProgression.Skill.SNEAK, "Stealth", 0, 20, 40, 60, 80),
    SNEAK_BACKSTAB(SkillProgression.Skill.SNEAK, "Backstab", req(30), "sneak_stealth"),
    SNEAK_DEADLY_AIM(SkillProgression.Skill.SNEAK, "Deadly Aim", req(40), "sneak_backstab"),
    SNEAK_ASSASSINS_BLADE(SkillProgression.Skill.SNEAK, "Assassin's Blade", req(50), "sneak_deadly_aim"),
    SNEAK_MUFFLED_MOVEMENT(SkillProgression.Skill.SNEAK, "Muffled Movement", req(30), "sneak_stealth"),
    SNEAK_LIGHT_FOOT(SkillProgression.Skill.SNEAK, "Light Foot", req(40), "sneak_muffled_movement"),
    SNEAK_SILENT_ROLL(SkillProgression.Skill.SNEAK, "Silent Roll", req(50), "sneak_light_foot"),
    SNEAK_SILENCE(SkillProgression.Skill.SNEAK, "Silence", req(70), "sneak_silent_roll"),
    SNEAK_SHADOW_WARRIOR(SkillProgression.Skill.SNEAK, "Shadow Warrior", req(100), "sneak_silence"),

    BARTER_HAGGLING(SkillProgression.Skill.BARTER, "Haggling", 0, 20, 40, 60, 80),
    BARTER_ALLURE(SkillProgression.Skill.BARTER, "Allure", req(30), "barter_haggling"),
    BARTER_MERCHANT(SkillProgression.Skill.BARTER, "Merchant", req(50), "barter_allure"),
    BARTER_INVESTOR(SkillProgression.Skill.BARTER, "Investor", req(70), "barter_merchant"),
    BARTER_FENCE(SkillProgression.Skill.BARTER, "Fence", req(90), "barter_investor"),
    BARTER_MASTER_TRADER(SkillProgression.Skill.BARTER, "Master Trader", req(100), "barter_fence"),
    BARTER_BRIBERY(SkillProgression.Skill.BARTER, "Bribery", req(30), "barter_haggling"),
    BARTER_PERSUASION(SkillProgression.Skill.BARTER, "Persuasion", req(50), "barter_bribery"),
    BARTER_INTIMIDATION(SkillProgression.Skill.BARTER, "Intimidation", req(70), "barter_persuasion");

    private static final String PERKS_TAG = "ThuumcraftSkillPerks";
    private static final String PERK_POINTS_TAG = "ThuumcraftSkillPerkPoints";
    private static final String EARNED_PERK_POINTS_TAG = "ThuumcraftSkillPerkPointsEarned";
    private static final String BONUS_PLAYER_LEVELS_TAG = "ThuumcraftSkillBonusPlayerLevels";
    private static final String FORFEITED_PERK_POINTS_TAG = "ThuumcraftSkillPerkPointsForfeited";
    private static final int STARTING_PLAYER_LEVEL = 1;
    private static final int SKILL_LEVELS_PER_PLAYER_LEVEL = 10;
    private static final int MAX_BONUS_PLAYER_LEVELS = 1_000_000;
    private static final Map<String, SkillPerk> BY_ID = new HashMap<>();

    static {
        for (SkillPerk perk : values()) {
            BY_ID.put(perk.id, perk);
        }
    }

    private final SkillProgression.Skill skill;
    private final String displayName;
    private final String id;
    private final int[] skillRequirements;
    private final String[] prerequisiteIds;

    SkillPerk(SkillProgression.Skill skill, String displayName, int... skillRequirements) {
        this(skill, displayName, skillRequirements, new String[0]);
    }

    SkillPerk(SkillProgression.Skill skill, String displayName, int[] skillRequirements, String... prerequisiteIds) {
        this.skill = skill;
        this.displayName = displayName;
        this.id = normalize(skill.id() + "_" + displayName);
        this.skillRequirements = skillRequirements;
        this.prerequisiteIds = prerequisiteIds;
    }

    public String id() {
        return id;
    }

    public SkillProgression.Skill skill() {
        return skill;
    }

    public String displayName() {
        return displayName;
    }

    public int maximumRank() {
        return skillRequirements.length;
    }

    public int requiredSkillForRank(int rank) {
        int clampedRank = Mth.clamp(rank, 0, skillRequirements.length - 1);
        return skillRequirements[clampedRank];
    }

    public static SkillPerk byId(String id) {
        return BY_ID.get(id);
    }

    public static boolean isSystemEnabled() {
        return Config.ENABLE_SKILL_SYSTEM.get();
    }

    public static boolean isSkillEnabled(SkillProgression.Skill skill) {
        if (skill == null) {
            return false;
        }
        return switch (skill) {
            case ONE_HANDED, TWO_HANDED -> EpicFightCompat.isLoaded();
            default -> true;
        };
    }

    public static int rank(Player player, SkillPerk perk) {
        if (!isSystemEnabled() || player == null || perk == null || !isSkillEnabled(perk.skill)) {
            return 0;
        }
        return Mth.clamp(player.getPersistentData().getCompound(PERKS_TAG).getInt(perk.id), 0,
                perk.maximumRank());
    }

    public static boolean has(Player player, SkillPerk perk) {
        return rank(player, perk) > 0;
    }

    public static boolean canUnlock(ServerPlayer player, SkillPerk perk) {
        if (!isSystemEnabled() || perk == null || !isSkillEnabled(perk.skill)) {
            return false;
        }
        int rank = rank(player, perk);
        if (rank >= perk.maximumRank()) {
            return false;
        }
        if (perkPoints(player) <= 0) {
            return false;
        }
        if (SkillProgression.getLevel(player, perk.skill) < perk.requiredSkillForRank(rank)) {
            return false;
        }
        if (perk.prerequisiteIds.length == 0) {
            return true;
        }
        for (String prerequisiteId : perk.prerequisiteIds) {
            SkillPerk prerequisite = byId(prerequisiteId);
            if (prerequisite != null && rank(player, prerequisite) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean unlock(ServerPlayer player, String perkId) {
        SkillPerk perk = byId(perkId);
        if (!isSystemEnabled() || perk == null || !canUnlock(player, perk)) {
            sync(player);
            return false;
        }

        int rank = rank(player, perk) + 1;
        CompoundTag perks = player.getPersistentData().getCompound(PERKS_TAG);
        perks.putInt(perk.id, rank);
        player.getPersistentData().put(PERKS_TAG, perks);
        setPerkPoints(player, Math.max(0, perkPoints(player) - 1));
        player.displayClientMessage(Component.literal(perk.displayName + " unlocked")
                .withStyle(Style.EMPTY.withBold(true)), true);
        sync(player);
        return true;
    }

    public static int perkPoints(Player player) {
        if (!isSystemEnabled()) {
            return 0;
        }
        return Math.max(0, player.getPersistentData().getInt(PERK_POINTS_TAG));
    }

    public static void refreshPerkPoints(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return;
        }
        int earnedPoints = getEarnedPerkPoints(player);
        CompoundTag data = player.getPersistentData();
        int recordedEarnedPoints = Math.max(0, data.getInt(EARNED_PERK_POINTS_TAG));
        int targetPerkPoints = Math.max(0,
                earnedPoints - countUnlockedRanks(player) - getForfeitedPerkPoints(player, earnedPoints));
        boolean leveledUp = earnedPoints > recordedEarnedPoints;
        boolean pointsChanged = perkPoints(player) != targetPerkPoints;
        if (!leveledUp && !pointsChanged && recordedEarnedPoints == earnedPoints) {
            return;
        }

        data.putInt(EARNED_PERK_POINTS_TAG, earnedPoints);
        setPerkPoints(player, targetPerkPoints);
        if (leveledUp) {
            player.displayClientMessage(Component.literal("Level increased to ")
                    .append(Component.literal(Integer.toString(playerLevel(player))).withStyle(Style.EMPTY.withBold(true)))
                    .append(Component.literal(" - Perk points: "))
                    .append(Component.literal(Integer.toString(perkPoints(player))).withStyle(Style.EMPTY.withBold(true)))
                    .append(Component.literal(" available")), true);
        }
    }

    public static int addPlayerLevels(ServerPlayer player, int levels) {
        if (!isSystemEnabled() || levels <= 0) {
            return 0;
        }

        refreshPerkPoints(player);
        int currentLevel = playerLevel(player);
        int currentBonusLevels = getBonusPlayerLevels(player);
        int nextBonusLevels = (int) Math.min(MAX_BONUS_PLAYER_LEVELS, (long) currentBonusLevels + levels);
        if (nextBonusLevels == currentBonusLevels) {
            sendSync(player);
            return 0;
        }

        player.getPersistentData().putInt(BONUS_PLAYER_LEVELS_TAG, nextBonusLevels);
        sync(player);
        return playerLevel(player) - currentLevel;
    }

    public static int addPerkPoints(ServerPlayer player, int points) {
        return addPlayerLevels(player, points);
    }

    public static int resetSkillTrees(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return 0;
        }

        CompoundTag data = player.getPersistentData();
        CompoundTag perks = data.getCompound(PERKS_TAG);
        int removedRanks = 0;
        for (SkillPerk perk : values()) {
            removedRanks += Math.max(0, perks.getInt(perk.id));
        }
        data.remove(PERKS_TAG);
        sync(player);
        return removedRanks;
    }

    public static int resetPerkPoints(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return 0;
        }

        int previousPoints = perkPoints(player);
        int earnedPoints = getEarnedPerkPoints(player);
        setForfeitedPerkPoints(player, getForfeitedPerkPoints(player, earnedPoints) + previousPoints, earnedPoints);
        setPerkPoints(player, 0);
        player.getPersistentData().putInt(EARNED_PERK_POINTS_TAG, earnedPoints);
        sendSync(player);
        return previousPoints;
    }

    public static int resetAllSkillProgress(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return 0;
        }

        int removed = countUnlockedRanks(player) + perkPoints(player) + getBonusPlayerLevels(player)
                + getForfeitedPerkPoints(player, getEarnedPerkPoints(player));
        removed += SkillProgression.resetAll(player);
        CompoundTag data = player.getPersistentData();
        data.remove(PERKS_TAG);
        data.remove(PERK_POINTS_TAG);
        data.remove(EARNED_PERK_POINTS_TAG);
        data.remove(BONUS_PLAYER_LEVELS_TAG);
        data.remove(FORFEITED_PERK_POINTS_TAG);
        sendSync(player);
        return removed;
    }

    public static void copy(Player original, ServerPlayer clone) {
        CompoundTag originalPerks = original.getPersistentData().getCompound(PERKS_TAG);
        if (!originalPerks.isEmpty()) {
            clone.getPersistentData().put(PERKS_TAG, originalPerks.copy());
        }
        CompoundTag originalData = original.getPersistentData();
        clone.getPersistentData().putInt(PERK_POINTS_TAG, originalData.getInt(PERK_POINTS_TAG));
        clone.getPersistentData().putInt(EARNED_PERK_POINTS_TAG, originalData.getInt(EARNED_PERK_POINTS_TAG));
        clone.getPersistentData().putInt(BONUS_PLAYER_LEVELS_TAG, originalData.getInt(BONUS_PLAYER_LEVELS_TAG));
        clone.getPersistentData().putInt(FORFEITED_PERK_POINTS_TAG, originalData.getInt(FORFEITED_PERK_POINTS_TAG));
        sync(clone);
    }

    public static int[] snapshot(Player player) {
        int[] ranks = new int[values().length];
        for (SkillPerk perk : values()) {
            ranks[perk.ordinal()] = rank(player, perk);
        }
        return ranks;
    }

    public static void sync(ServerPlayer player) {
        refreshPerkPoints(player);
        sendSync(player);
    }

    public static void syncCommandGrantedSkillLevels(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return;
        }
        sync(player);
    }

    private static void sendSync(ServerPlayer player) {
        ModMessages.sendToPlayer(new ClientboundSkillPerksPacket(snapshot(player), perkPoints(player),
                playerLevel(player), EpicFightCompat.isLoaded()), player);
    }

    public static int playerLevel(ServerPlayer player) {
        if (!isSystemEnabled()) {
            return STARTING_PLAYER_LEVEL;
        }
        return STARTING_PLAYER_LEVEL + getEarnedPerkPoints(player);
    }

    private static int getEarnedPerkPoints(ServerPlayer player) {
        int totalSkillLevels = 0;
        for (SkillProgression.Skill skill : SkillProgression.Skill.values()) {
            if (isSkillEnabled(skill)) {
                totalSkillLevels += SkillProgression.getLevel(player, skill);
            }
        }
        return totalSkillLevels / SKILL_LEVELS_PER_PLAYER_LEVEL + getBonusPlayerLevels(player);
    }

    private static int getBonusPlayerLevels(Player player) {
        return Mth.clamp(player.getPersistentData().getInt(BONUS_PLAYER_LEVELS_TAG), 0,
                MAX_BONUS_PLAYER_LEVELS);
    }

    private static void setPerkPoints(Player player, int points) {
        player.getPersistentData().putInt(PERK_POINTS_TAG, Math.max(0, points));
    }

    private static int getForfeitedPerkPoints(Player player, int earnedPoints) {
        return Mth.clamp(player.getPersistentData().getInt(FORFEITED_PERK_POINTS_TAG), 0,
                Math.max(0, earnedPoints));
    }

    private static void setForfeitedPerkPoints(Player player, int points, int earnedPoints) {
        player.getPersistentData().putInt(FORFEITED_PERK_POINTS_TAG,
                Mth.clamp(points, 0, Math.max(0, earnedPoints)));
    }

    private static int countUnlockedRanks(Player player) {
        CompoundTag perks = player.getPersistentData().getCompound(PERKS_TAG);
        int ranks = 0;
        for (SkillPerk perk : values()) {
            ranks += Math.max(0, perks.getInt(perk.id));
        }
        return ranks;
    }

    private static int[] req(int... values) {
        return values;
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replace("'", "")
                .replace("-", "_")
                .replace(" ", "_");
    }
}
