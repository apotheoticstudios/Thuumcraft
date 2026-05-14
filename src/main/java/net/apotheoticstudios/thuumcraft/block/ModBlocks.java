package net.apotheoticstudios.thuumcraft.block;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.custom.ModFlammableRotatedPillarBlock;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.apotheoticstudios.thuumcraft.worldgen.tree.PineTreeGrower;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Thuumcraft.MOD_ID);

    public static final RegistryObject<Block> REFINED_MALACHITE_BLOCK = registerBlock("refined_malachite_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(6f).sound(SoundType.AMETHYST)));


    public static final RegistryObject<Block> MALACHITE_ORE_VEIN = registerBlock("malachite_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_MALACHITE_ORE_VEIN = registerBlock("deepslate_malachite_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> CORUNDUM_ORE_VEIN = registerBlock("corundum_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.COPPER), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_CORUNDUM_ORE_VEIN = registerBlock("deepslate_corundum_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.COPPER), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> MOONSTONE_ORE_VEIN = registerBlock("moonstone_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_MOONSTONE_ORE_VEIN = registerBlock("deepslate_moonstone_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> QUICKSILVER_ORE_VEIN = registerBlock("quicksilver_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_QUICKSILVER_ORE_VEIN = registerBlock("deepslate_quicksilver_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> SILVER_ORE_VEIN = registerBlock("silver_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE_VEIN = registerBlock("deepslate_silver_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> ORICHALCUM_ORE_VEIN = registerBlock("orichalcum_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.COPPER), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_ORICHALCUM_ORE_VEIN = registerBlock("deepslate_orichalcum_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.COPPER), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> EBONY_ORE_VEIN = registerBlock("ebony_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 3)));
    public static final RegistryObject<Block> DEEPSLATE_EBONY_ORE_VEIN = registerBlock("deepslate_ebony_ore_vein",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL), UniformInt.of(1, 4)));

    public static final RegistryObject<Block> PINE_SAPLING = registerBlock("pine_sapling",
            () -> new SaplingBlock(new PineTreeGrower(), BlockBehaviour.Properties.copy(Blocks.SPRUCE_SAPLING)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
