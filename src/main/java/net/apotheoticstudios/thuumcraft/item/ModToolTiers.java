package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier GLASS = TierSortingRegistry.registerTier(
            new ForgeTier(3, 1500, 7, 3f, 12,
                    ModTags.Blocks.NEEDS_MALACHITE_TOOL, () -> Ingredient.of(ModItems.REFINED_MALACHITE.get())),
                    new ResourceLocation(Thuumcraft.MOD_ID,"glass"), List.of(Tiers.IRON), List.of());

    public static final Tier DWARVEN = TierSortingRegistry.registerTier(
            new ForgeTier(2, 1000, 5, 2f, 12,
                    ModTags.Blocks.NEEDS_DWARVEN_TOOL, () -> Ingredient.of(ModItems.DWARVEN_METAL_INGOT.get())),
                    new ResourceLocation(Thuumcraft.MOD_ID,"dwarven"), List.of(Tiers.STONE), List.of());

    public static final Tier STEEL = TierSortingRegistry.registerTier(
            new ForgeTier(2, 800, 5, 2f, 8,
                    ModTags.Blocks.NEEDS_STEEL_TOOL, () -> Ingredient.of(ModItems.STEEL_INGOT.get())),
            new ResourceLocation(Thuumcraft.MOD_ID,"steel"), List.of(Tiers.STONE), List.of());

    public static final Tier EBONY = TierSortingRegistry.registerTier(
            new ForgeTier(4, 2000, 7, 4f, 20,
                    ModTags.Blocks.NEEDS_EBONY_TOOL, () -> Ingredient.of(ModItems.EBONY_INGOT.get())),
            new ResourceLocation(Thuumcraft.MOD_ID,"ebony"), List.of(Tiers.DIAMOND), List.of());



}
