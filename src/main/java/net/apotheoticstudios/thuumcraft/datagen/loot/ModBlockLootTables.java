package net.apotheoticstudios.thuumcraft.datagen.loot;

import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.REFINED_MALACHITE_BLOCK.get());



        this.add(ModBlocks.MALACHITE_ORE_VEIN.get(),
                block -> createOreDrop(ModBlocks.MALACHITE_ORE_VEIN.get(), ModItems.MALACHITE_ORE.get()));
        this.add(ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get(), ModItems.MALACHITE_ORE.get()));
    }



    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}