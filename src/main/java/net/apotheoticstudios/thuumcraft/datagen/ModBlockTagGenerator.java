package net.apotheoticstudios.thuumcraft.datagen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Thuumcraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_STONE_TOOL);


        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.REFINED_MALACHITE_BLOCK.get(),
                        ModBlocks.MALACHITE_ORE_VEIN.get(),
                        ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get()


                        );



        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);


        this.tag(ModTags.Blocks.NEEDS_MALACHITE_TOOL);


        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL);


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.REFINED_MALACHITE_BLOCK.get(),
                        ModBlocks.MALACHITE_ORE_VEIN.get(),
                        ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get()


                        );
    }
}
