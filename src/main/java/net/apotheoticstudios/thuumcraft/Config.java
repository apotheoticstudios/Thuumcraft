package net.apotheoticstudios.thuumcraft;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue MOB_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue CHEST_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue FISHING_DROP_CHANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STEALTH_SYSTEM;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Thuumcraft Books Loot Chances").push("loot");
        MOB_DROP_CHANCE = builder.comment("Chance for the following mobs to drop a random book (default is 1.0%):").comment("zombie, skeleton, pillager, witch").defineInRange("mobDropChance", 0.01, (double)0.0F, (double)1.0F);
        CHEST_DROP_CHANCE = builder.comment("Chance for the following chests to contain a random book (default is 5%):").comment("shipwreck (treasure and supply), simple dungeon, desert pyramid, jungle temple, woodland mansion ancient city, abandoned mineshaft, nether bridge, igloo chest, bastion (bridge, other, and treasure)").defineInRange("chestDropChance", 0.05, (double)0.0F, (double)1.0F);
        FISHING_DROP_CHANCE = builder.comment("Chance for fishing junk loot to include a random book (default is 3%)").defineInRange("fishingDropChance", 0.03, (double)0.0F, (double)1.0F);
        builder.pop();
        builder.comment("Skyrim-style stealth awareness and sneak crosshair settings").push("stealth");
        ENABLE_STEALTH_SYSTEM = builder.comment("Set to false to completely disable Thuumcraft's stealth awareness system and stealth crosshair indicator.")
                .define("enableStealthSystem", true);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
