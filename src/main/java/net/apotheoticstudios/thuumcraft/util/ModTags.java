package net.apotheoticstudios.thuumcraft.util;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_MALACHITE_TOOL = tag("needs_malachite_tool");

        public static final TagKey<Block> NEEDS_DWARVEN_TOOL = tag("needs_dwarven_tool");

        public static final TagKey<Block> NEEDS_STEEL_TOOL = tag("needs_dwarven_tool");

        public static final TagKey<Block> NEEDS_EBONY_TOOL = tag("needs_ebony_tool");


        private static TagKey<Block> tag(String name){
            return BlockTags.create(ResourceLocation.tryBuild(Thuumcraft.MOD_ID,name));
        }
    }

    public static class Items {

        public static final TagKey<Item> INGREDIENT = tag("ingredient");
        public static final TagKey<Item> LIGHT_ARMOR = tag("light_armor");
        public static final TagKey<Item> HEAVY_ARMOR = tag("heavy_armor");
        public static final TagKey<Item> RANGED_WEAPONS = tag("ranged_weapons");
        public static final TagKey<Item> ONE_HANDED_WEAPONS = tag("one_handed_weapons");
        public static final TagKey<Item> TWO_HANDED_WEAPONS = tag("two_handed_weapons");



        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.tryBuild(Thuumcraft.MOD_ID,name));
        }
    }


}
