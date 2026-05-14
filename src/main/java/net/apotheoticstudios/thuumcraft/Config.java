package net.apotheoticstudios.thuumcraft;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue MOB_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue CHEST_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue FISHING_DROP_CHANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PLAYER_ARROW_TRAJECTORY_TUNING;
    public static final ForgeConfigSpec.DoubleValue PLAYER_ARROW_VELOCITY_MULTIPLIER;
    public static final ForgeConfigSpec.BooleanValue PRESERVE_PLAYER_ARROW_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKILL_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STEALTH_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKYRIM_HUD_AND_STAMINA;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKYRIM_HUD;
    public static final ForgeConfigSpec.BooleanValue SHOW_SKYRIM_COMPASS;
    public static final ForgeConfigSpec.BooleanValue SHOW_TARGET_HEALTH_BAR;
    public static final ForgeConfigSpec.BooleanValue SHOW_PLAYER_HEALTH_BAR;
    public static final ForgeConfigSpec.BooleanValue SHOW_MANA_BAR;
    public static final ForgeConfigSpec.BooleanValue SHOW_STAMINA_BAR;
    public static final ForgeConfigSpec.BooleanValue SHOW_ARMOR_ICONS;
    public static final ForgeConfigSpec.BooleanValue SHOW_AIR_ICONS;
    public static final ForgeConfigSpec.BooleanValue SHOW_EXPERIENCE_BAR;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STAMINA_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STAMINA_HUNGER_OVERRIDE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STAMINA_SPRINT_LIMIT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_EPIC_FIGHT_STAMINA_REPLACEMENT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKYRIM_HEALTH_REGENERATION;
    public static final ForgeConfigSpec.IntValue STAMINA_HUNGER_FOOD_LEVEL;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_DRAIN_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_ARMOR_DRAIN_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_START_RATIO;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_RESUME_RATIO;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_START_FLOOR;
    public static final ForgeConfigSpec.DoubleValue STAMINA_SPRINT_RESUME_FLOOR;
    public static final ForgeConfigSpec.IntValue STAMINA_REGEN_DELAY_TICKS;
    public static final ForgeConfigSpec.DoubleValue HEALTH_REGEN_OUT_OF_COMBAT_PERCENT_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue HEALTH_REGEN_COMBAT_PERCENT_PER_SECOND;
    public static final ForgeConfigSpec.IntValue HEALTH_REGEN_INTERVAL_TICKS;
    public static final ForgeConfigSpec.IntValue HEALTH_REGEN_COMBAT_DURATION_TICKS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Thuumcraft Books Loot Chances").push("loot");
        MOB_DROP_CHANCE = builder.comment("Chance for the following mobs to drop a random book (default is 1.0%):").comment("zombie, skeleton, pillager, witch").defineInRange("mobDropChance", 0.01, (double)0.0F, (double)1.0F);
        CHEST_DROP_CHANCE = builder.comment("Chance for the following chests to contain a random book (default is 5%):").comment("shipwreck (treasure and supply), simple dungeon, desert pyramid, jungle temple, woodland mansion ancient city, abandoned mineshaft, nether bridge, igloo chest, bastion (bridge, other, and treasure)").defineInRange("chestDropChance", 0.05, (double)0.0F, (double)1.0F);
        FISHING_DROP_CHANCE = builder.comment("Chance for fishing junk loot to include a random book (default is 3%)").defineInRange("fishingDropChance", 0.03, (double)0.0F, (double)1.0F);
        builder.pop();
        builder.comment("Ranged combat tuning").push("rangedCombat");
        ENABLE_PLAYER_ARROW_TRAJECTORY_TUNING = builder.comment("Enable faster, flatter player-fired arrow trajectories.")
                .define("enablePlayerArrowTrajectoryTuning", true);
        PLAYER_ARROW_VELOCITY_MULTIPLIER = builder.comment("Player arrow velocity attribute multiplier. Higher values make arrows fly faster and drop less.")
                .defineInRange("playerArrowVelocityMultiplier", 1.35D, 0.1D, 10.0D);
        PRESERVE_PLAYER_ARROW_DAMAGE = builder.comment("Compensate the arrow damage attribute by the inverse velocity multiplier. Vanilla arrow damage scales with impact speed, so disabling this makes faster arrows hit harder.")
                .define("preservePlayerArrowDamage", true);
        builder.pop();
        builder.comment("Skyrim-style skill progression and perk tree settings").push("skills");
        ENABLE_SKILL_SYSTEM = builder.comment("Set to false to completely disable skill XP progression, character level, perk points, skill tree unlocking and perk ability effects.")
                .define("enableSkillSystem", true);
        builder.pop();
        builder.comment("Skyrim-style stealth awareness and sneak crosshair settings").push("stealth");
        ENABLE_STEALTH_SYSTEM = builder.comment("Set to false to completely disable Thuumcraft's stealth awareness, crosshair and sneak attack system.")
                .define("enableStealthSystem", true);
        builder.pop();
        builder.comment("Skyrim-style HUD and stamina settings").push("hudAndStamina");
        ENABLE_SKYRIM_HUD_AND_STAMINA = builder.comment("Set to false to completely disable Thuumcraft's Skyrim-style HUD, stamina replacement, hunger override and Iron's Spellbooks mana bar replacement.")
                .define("enableSkyrimHudAndStamina", true);
        ENABLE_SKYRIM_HUD = builder.comment("Render Thuumcraft's Skyrim-style HUD overlays. Individual bars can still be toggled below.")
                .define("enableSkyrimHud", true);
        SHOW_SKYRIM_COMPASS = builder.comment("Render the Skyrim-style compass at the top of the screen.")
                .define("showCompass", false);
        SHOW_TARGET_HEALTH_BAR = builder.comment("Render the targeted entity health bar.")
                .define("showTargetHealthBar", true);
        SHOW_PLAYER_HEALTH_BAR = builder.comment("Render the Skyrim-style player health bar. If false, vanilla health is shown.")
                .define("showPlayerHealthBar", true);
        SHOW_MANA_BAR = builder.comment("Render the Skyrim-style Iron's Spellbooks mana bar. If false, Iron's default mana overlay is shown.")
                .define("showManaBar", true);
        SHOW_STAMINA_BAR = builder.comment("Render the Skyrim-style stamina bar.")
                .define("showStaminaBar", true);
        SHOW_ARMOR_ICONS = builder.comment("Render the Skyrim-style armor icons. If false, vanilla armor is shown.")
                .define("showArmorIcons", true);
        SHOW_AIR_ICONS = builder.comment("Render the Skyrim-style air icons. If false, vanilla air bubbles are shown.")
                .define("showAirIcons", true);
        SHOW_EXPERIENCE_BAR = builder.comment("Render the Skyrim-style experience bar. If false, vanilla experience is shown.")
                .define("showExperienceBar", true);

        builder.comment("Skyrim-style stamina system settings").push("stamina");
        ENABLE_STAMINA_SYSTEM = builder.comment("Enable stamina draining/regeneration and stamina-based sprint control.")
                .define("enableStaminaSystem", true);
        ENABLE_STAMINA_HUNGER_OVERRIDE = builder.comment("Keep vanilla hunger stable while stamina replaces hunger. Disable for vanilla hunger behavior.")
                .define("enableHungerOverride", true);
        ENABLE_STAMINA_SPRINT_LIMIT = builder.comment("Drain stamina while sprinting and prevent sprinting when stamina is too low.")
                .define("enableSprintLimit", true);
        ENABLE_EPIC_FIGHT_STAMINA_REPLACEMENT = builder.comment("When Epic Fight is installed, make Epic Fight skills and dodges consume Thuumcraft stamina instead of Epic Fight's internal stamina.")
                .define("enableEpicFightStaminaReplacement", true);
        STAMINA_HUNGER_FOOD_LEVEL = builder.comment("Food level held by the hunger override. 17 prevents vanilla natural regeneration from running.")
                .defineInRange("hungerOverrideFoodLevel", 17, 1, 20);
        STAMINA_SPRINT_DRAIN_PER_SECOND = builder.comment("Base stamina drained per second while sprinting.")
                .defineInRange("sprintDrainPerSecond", 5.0D, 0.0D, 1000.0D);
        STAMINA_SPRINT_ARMOR_DRAIN_MULTIPLIER = builder.comment("Extra sprint stamina drain per armor point. 0.02 means 2% more drain per armor point.")
                .defineInRange("sprintArmorDrainMultiplier", 0.02D, 0.0D, 10.0D);
        STAMINA_SPRINT_START_RATIO = builder.comment("Minimum stamina ratio required to start sprinting.")
                .defineInRange("sprintStartStaminaRatio", 0.10D, 0.0D, 1.0D);
        STAMINA_SPRINT_RESUME_RATIO = builder.comment("Stamina ratio required to unlock sprinting after running out.")
                .defineInRange("sprintResumeStaminaRatio", 0.15D, 0.0D, 1.0D);
        STAMINA_SPRINT_START_FLOOR = builder.comment("Minimum absolute stamina required to start sprinting.")
                .defineInRange("sprintStartStaminaFloor", 5.0D, 0.0D, 1000000.0D);
        STAMINA_SPRINT_RESUME_FLOOR = builder.comment("Minimum absolute stamina required to unlock sprinting after running out.")
                .defineInRange("sprintResumeStaminaFloor", 10.0D, 0.0D, 1000000.0D);
        STAMINA_REGEN_DELAY_TICKS = builder.comment("Ticks before stamina starts regenerating after stamina is drained.")
                .defineInRange("staminaRegenDelayTicks", 20, 0, 1200);
        builder.pop();

        builder.comment("Skyrim-style health regeneration settings").push("healthRegeneration");
        ENABLE_SKYRIM_HEALTH_REGENERATION = builder.comment("Enable passive percentage-based Skyrim-style health regeneration.")
                .define("enableHealthRegeneration", true);
        HEALTH_REGEN_OUT_OF_COMBAT_PERCENT_PER_SECOND = builder.comment("Out-of-combat health regenerated per second as a fraction of max health. Skyrim default is 0.007 (0.70%).")
                .defineInRange("outOfCombatPercentPerSecond", 0.007D, 0.0D, 1.0D);
        HEALTH_REGEN_COMBAT_PERCENT_PER_SECOND = builder.comment("In-combat health regenerated per second as a fraction of max health. Skyrim default is 0.0049 (0.49%).")
                .defineInRange("combatPercentPerSecond", 0.0049D, 0.0D, 1.0D);
        HEALTH_REGEN_INTERVAL_TICKS = builder.comment("How often health regeneration is applied. Lower values are smoother; 20 applies once per second.")
                .defineInRange("healthRegenIntervalTicks", 20, 1, 1200);
        HEALTH_REGEN_COMBAT_DURATION_TICKS = builder.comment("Ticks a player remains in combat after dealing or taking damage.")
                .defineInRange("combatDurationTicks", 200, 0, 12000);
        builder.pop();
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
