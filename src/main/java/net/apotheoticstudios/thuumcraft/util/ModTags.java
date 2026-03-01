package net.apotheoticstudios.thuumcraft.util;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_MALACHITE_TOOL = tag("needs_malachite_tool");


        private static TagKey<Block> tag(String name){
            return BlockTags.create(ResourceLocation.tryBuild(Thuumcraft.MOD_ID,name));
        }
    }

    public static class Items {

        public static final TagKey<Item> INGREDIENT = tag("ingredient");



        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.tryBuild(Thuumcraft.MOD_ID,name));
        }
    }
}
