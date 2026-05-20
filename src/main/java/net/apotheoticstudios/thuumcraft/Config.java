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
    public static final ForgeConfigSpec.BooleanValue ENABLE_KILL_CAM;
    public static final ForgeConfigSpec.DoubleValue KILL_CAM_CHANCE;
    public static final ForgeConfigSpec.IntValue KILL_CAM_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue KILL_CAM_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.BooleanValue KILL_CAM_REQUIRE_LAST_THREAT;
    public static final ForgeConfigSpec.BooleanValue KILL_CAM_HOSTILE_ONLY;
    public static final ForgeConfigSpec.DoubleValue KILL_CAM_THREAT_RADIUS;
    public static final ForgeConfigSpec.DoubleValue KILL_CAM_MAX_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue KILL_CAM_FOV;
    public static final ForgeConfigSpec.BooleanValue KILL_CAM_HIDE_HUD;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKILL_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RESTED_SKILL_XP_BONUS;
    public static final ForgeConfigSpec.DoubleValue RESTED_SKILL_XP_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue RESTED_SKILL_XP_DURATION_TICKS;
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
    public static final ForgeConfigSpec.BooleanValue ENABLE_SKYRIM_FOOD_EFFECTS;
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
    public static final ForgeConfigSpec.DoubleValue FOOD_HEALTH_RESTORE_PER_NUTRITION;
    public static final ForgeConfigSpec.DoubleValue FOOD_STAMINA_RESTORE_PER_NUTRITION;
    public static final ForgeConfigSpec.DoubleValue FOOD_MAGICKA_RESTORE_PER_NUTRITION;
    public static final ForgeConfigSpec.DoubleValue FOOD_RAW_RESTORE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_COOKED_RESTORE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_MEAL_RESTORE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue FOOD_MEAL_REGEN_DURATION_TICKS;
    public static final ForgeConfigSpec.DoubleValue FOOD_MEAL_HEALTH_REGEN_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue FOOD_MEAL_STAMINA_REGEN_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue FOOD_MEAL_MAGICKA_REGEN_PER_SECOND;

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
        PLAYER_ARROW_VELOCITY_MULTIPLIER = builder.comment("Player arrow velocity attribute multiplier. Higher values make arrows fly faster and drop less. Default 2.25 gives a much flatter Skyrim-like shot.")
                .defineInRange("playerArrowVelocityMultiplier", 3.0D, 0.1D, 10.0D);
        PRESERVE_PLAYER_ARROW_DAMAGE = builder.comment("Compensate the arrow damage attribute by the inverse velocity multiplier. At the default 2.25 velocity multiplier, arrow damage is multiplied by 0.4444 before other ranged bonuses so the faster arrow does not gain free damage from speed.")
                .define("preservePlayerArrowDamage", true);
        builder.pop();
        builder.comment("Skyrim-style kill camera settings").push("killCam");
        ENABLE_KILL_CAM = builder.comment("Enable client-side cinematic kill cameras after the player lands a killing blow.")
                .define("enableKillCam", true);
        KILL_CAM_CHANCE = builder.comment("Chance for a valid killing blow to trigger a kill camera.")
                .defineInRange("chance", 0.25D, 0.0D, 1.0D);
        KILL_CAM_DURATION_TICKS = builder.comment("Length of the client-side kill camera in ticks. This does not slow server or world time.")
                .defineInRange("durationTicks", 48, 1, 200);
        KILL_CAM_COOLDOWN_TICKS = builder.comment("Minimum ticks between kill cameras for the same player.")
                .defineInRange("cooldownTicks", 100, 0, 12000);
        KILL_CAM_REQUIRE_LAST_THREAT = builder.comment("Only trigger when the kill leaves no nearby hostile mob actively targeting or recently hurt by the player. This approximates Skyrim's combat-ending killmove rule while still allowing undetected stealth kills.")
                .define("requireLastThreat", true);
        KILL_CAM_HOSTILE_ONLY = builder.comment("Only trigger kill cameras when killing hostile mobs.")
                .define("hostileOnly", true);
        KILL_CAM_THREAT_RADIUS = builder.comment("Radius used when checking for other nearby active threats.")
                .defineInRange("threatRadius", 24.0D, 0.0D, 128.0D);
        KILL_CAM_MAX_DISTANCE = builder.comment("Maximum distance between player and target for a kill camera.")
                .defineInRange("maxDistance", 128.0D, 1.0D, 256.0D);
        KILL_CAM_FOV = builder.comment("Camera FOV during the kill camera. Lower values feel more cinematic.")
                .defineInRange("fov", 54.0D, 10.0D, 120.0D);
        KILL_CAM_HIDE_HUD = builder.comment("Hide HUD overlays during the kill camera.")
                .define("hideHud", true);
        builder.pop();
        builder.comment("Skyrim-style skill progression and perk tree settings").push("skills");
        ENABLE_SKILL_SYSTEM = builder.comment("Set to false to completely disable skill XP progression, character level, perk points, skill tree unlocking and perk ability effects.")
                .define("enableSkillSystem", true);
        ENABLE_RESTED_SKILL_XP_BONUS = builder.comment("Grant a Skyrim-style Well Rested bonus to skill XP after sleeping.")
                .define("enableRestedSkillXpBonus", true);
        RESTED_SKILL_XP_MULTIPLIER = builder.comment("Skill XP multiplier while Well Rested. Skyrim's Well Rested bonus is 10% faster skill improvement.")
                .defineInRange("restedSkillXpMultiplier", 1.10D, 1.0D, 10.0D);
        RESTED_SKILL_XP_DURATION_TICKS = builder.comment("Ticks the Well Rested skill XP bonus lasts after sleeping.")
                .defineInRange("restedSkillXpDurationTicks", 24000, 0, 240000);
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

        builder.comment("Skyrim-style food effects settings").push("food");
        ENABLE_SKYRIM_FOOD_EFFECTS = builder.comment("Enable food restoring Skyrim-style health, stamina and occasional magicka, with soups/stews granting timed regeneration.")
                .define("enableFoodEffects", true);
        FOOD_HEALTH_RESTORE_PER_NUTRITION = builder.comment("Health restored per Minecraft nutrition point when food is eaten.")
                .defineInRange("healthRestorePerNutrition", 0.05D, 0.0D, 1000.0D);
        FOOD_STAMINA_RESTORE_PER_NUTRITION = builder.comment("Stamina restored per Minecraft nutrition point when food is eaten.")
                .defineInRange("staminaRestorePerNutrition", 0.06D, 0.0D, 1000.0D);
        FOOD_MAGICKA_RESTORE_PER_NUTRITION = builder.comment("Magicka restored per Minecraft nutrition point for magicka-themed foods.")
                .defineInRange("magickaRestorePerNutrition", 0.03D, 0.0D, 1000.0D);
        FOOD_RAW_RESTORE_MULTIPLIER = builder.comment("Multiplier for direct food recovery from raw food.")
                .defineInRange("rawRestoreMultiplier", 0.5D, 0.0D, 10.0D);
        FOOD_COOKED_RESTORE_MULTIPLIER = builder.comment("Multiplier for direct food recovery from cooked food.")
                .defineInRange("cookedRestoreMultiplier", 1.25D, 0.0D, 10.0D);
        FOOD_MEAL_RESTORE_MULTIPLIER = builder.comment("Multiplier for direct food recovery from soups, stews and full meals.")
                .defineInRange("mealRestoreMultiplier", 1.5D, 0.0D, 10.0D);
        FOOD_MEAL_REGEN_DURATION_TICKS = builder.comment("Ticks soups, stews and full meals grant regeneration. Default 60 seconds (1200 ticks)")
                .defineInRange("mealRegenDurationTicks", 1200, 0, 24000);
        FOOD_MEAL_HEALTH_REGEN_PER_SECOND = builder.comment("Health restored per second by soup/stew/full-meal regeneration.")
                .defineInRange("mealHealthRegenPerSecond", 0.5D, 0.0D, 1000.0D);
        FOOD_MEAL_STAMINA_REGEN_PER_SECOND = builder.comment("Stamina restored per second by soup/stew/full-meal regeneration.")
                .defineInRange("mealStaminaRegenPerSecond", 0.5D, 0.0D, 1000.0D);
        FOOD_MEAL_MAGICKA_REGEN_PER_SECOND = builder.comment("Magicka restored per second by magicka-themed meal regeneration.")
                .defineInRange("mealMagickaRegenPerSecond", 0.3D, 0.0D, 1000.0D);
        builder.pop();

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
