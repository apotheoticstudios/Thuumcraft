package net.apotheoticstudios.thuumcraft.datagen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Thuumcraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.REFINED_MALACHITE_BLOCK);
        blockWithItem(ModBlocks.MALACHITE_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN);
        blockWithItem(ModBlocks.CORUNDUM_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_CORUNDUM_ORE_VEIN);
        blockWithItem(ModBlocks.MOONSTONE_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_MOONSTONE_ORE_VEIN);
        blockWithItem(ModBlocks.QUICKSILVER_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_QUICKSILVER_ORE_VEIN);
        blockWithItem(ModBlocks.SILVER_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_SILVER_ORE_VEIN);
        blockWithItem(ModBlocks.ORICHALCUM_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_ORICHALCUM_ORE_VEIN);
        blockWithItem(ModBlocks.EBONY_ORE_VEIN);
        blockWithItem(ModBlocks.DEEPSLATE_EBONY_ORE_VEIN);

        saplingBlock(ModBlocks.PINE_SAPLING);
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
