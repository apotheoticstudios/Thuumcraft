package net.apotheoticstudios.thuumcraft.worldgen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MALACHITE_ORE_VEIN = registerKey("add_malachite_ore_vein");
    public static final ResourceKey<BiomeModifier> ADD_CORUNDUM_ORE_VEIN = registerKey("add_corundum_ore_vein");
    public static final ResourceKey<BiomeModifier> ADD_MOONSTONE_ORE_VEIN = registerKey("add_moonstone_ore_vein");
    public static final ResourceKey<BiomeModifier> ADD_SILVER_ORE_VEIN = registerKey("add_silver_ore_vein");
    public static final ResourceKey<BiomeModifier> ADD_ORICHALCUM_ORE_VEIN = registerKey("add_orichalcum_ore_vein");
    public static final ResourceKey<BiomeModifier> ADD_EBONY_ORE_VEIN = registerKey("add_ebony_ore_vein");
    private static final TagKey<Biome> MALACHITE_ORE_BIOMES = biomeTag("malachite_ore_biomes");
    private static final TagKey<Biome> CORUNDUM_ORE_BIOMES = biomeTag("corundum_ore_biomes");
    private static final TagKey<Biome> MOONSTONE_ORE_BIOMES = biomeTag("moonstone_ore_biomes");
    private static final TagKey<Biome> SILVER_ORE_BIOMES = biomeTag("silver_ore_biomes");
    private static final TagKey<Biome> ORICHALCUM_ORE_BIOMES = biomeTag("orichalcum_ore_biomes");
    private static final TagKey<Biome> EBONY_ORE_BIOMES = biomeTag("ebony_ore_biomes");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_MALACHITE_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(MALACHITE_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MALACHITE_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_CORUNDUM_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(CORUNDUM_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CORUNDUM_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_MOONSTONE_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(MOONSTONE_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MOONSTONE_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_SILVER_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(SILVER_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SILVER_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_ORICHALCUM_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ORICHALCUM_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORICHALCUM_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_EBONY_ORE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(EBONY_ORE_BIOMES),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EBONY_ORE_VEIN_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));



    }


    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Thuumcraft.MOD_ID, name));
    }

    private static TagKey<Biome> biomeTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(Thuumcraft.MOD_ID, name));
    }
}
