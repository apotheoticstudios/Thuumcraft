package net.apotheoticstudios.thuumcraft.worldgen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MALACHITE_ORE_KEY = registerKey("malachite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_CORUNDUM_ORE_KEY = registerKey("corundum_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MOONSTONE_ORE_KEY = registerKey("moonstone_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SILVER_ORE_KEY = registerKey("silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORICHALCUM_ORE_KEY = registerKey("orichalcum_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_EBONY_ORE_KEY = registerKey("ebony_ore");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldMalachiteOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.MALACHITE_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_MALACHITE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMalachiteOreVeins, 3));

        List<OreConfiguration.TargetBlockState> overworldCorundumOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.CORUNDUM_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_CORUNDUM_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_CORUNDUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldCorundumOreVeins, 7));

        List<OreConfiguration.TargetBlockState> overworldMoonstoneOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.MOONSTONE_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_MOONSTONE_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_MOONSTONE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMoonstoneOreVeins, 5));

        List<OreConfiguration.TargetBlockState> overworldSilverOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.SILVER_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_SILVER_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSilverOreVeins, 4));

        List<OreConfiguration.TargetBlockState> overworldOrichalcumOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.ORICHALCUM_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_ORICHALCUM_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_ORICHALCUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldOrichalcumOreVeins, 6));

        List<OreConfiguration.TargetBlockState> overworldEbonyOreVeins = List.of(OreConfiguration.target(stoneReplaceables,
                        ModBlocks.EBONY_ORE_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_EBONY_ORE_VEIN.get().defaultBlockState()));
        register(context, OVERWORLD_EBONY_ORE_KEY, Feature.ORE, new OreConfiguration(overworldEbonyOreVeins, 3));


    }



    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Thuumcraft.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
