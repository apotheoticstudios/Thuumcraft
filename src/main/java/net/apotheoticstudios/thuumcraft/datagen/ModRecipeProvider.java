package net.apotheoticstudios.thuumcraft.datagen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> MALACHITE_SMELTABLES = List.of(ModItems.MALACHITE_ORE.get(),
            ModBlocks.MALACHITE_ORE_VEIN.get(), ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        //REFINED_MALACHITE and REFINED_MALACHITE_BLOCK
        oreSmelting(pWriter, MALACHITE_SMELTABLES, RecipeCategory.MISC, ModItems.REFINED_MALACHITE.get(), 0.7f, 400, "refined_malachite");
        oreBlasting(pWriter, MALACHITE_SMELTABLES, RecipeCategory.MISC, ModItems.REFINED_MALACHITE.get(), 0.7f, 200, "refined_malachite");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REFINED_MALACHITE_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.REFINED_MALACHITE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MALACHITE.get()), has(ModItems.REFINED_MALACHITE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.REFINED_MALACHITE.get(), 9)
                .requires(ModBlocks.REFINED_MALACHITE_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.REFINED_MALACHITE_BLOCK.get()), has(ModBlocks.REFINED_MALACHITE_BLOCK.get()))
                .save(pWriter);

        //HANDLE
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HANDLE.get())
                .pattern("   ")
                .pattern("LIL")
                .pattern("   ")
                .define('L', ModItems.LEATHER_STRIPS.get())
                .define('I', Tags.Items.INGOTS)
                .unlockedBy(getHasName(ModItems.LEATHER_STRIPS.get()), has(ModItems.LEATHER_STRIPS.get()))
                .save(pWriter);

        //INGOTS
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_INGOT.get())
                .requires(ModItems.CORUNDUM_INGOT.get())
                .requires(Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.CORUNDUM_INGOT.get()), has(ModItems.CORUNDUM_INGOT.get()))
                .save(pWriter);


        //SWORDS
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_SWORD.get())
                .pattern(" S ")
                .pattern(" S ")
                .pattern(" H ")
                .define('S', ModItems.STEEL_INGOT.get())
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.STEEL_INGOT.get()), has(ModItems.STEEL_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GLASS_SWORD.get())
                .pattern(" R ")
                .pattern(" M ")
                .pattern(" H ")
                .define('R', ModItems.REFINED_MALACHITE.get())
                .define('M', ModItems.REFINED_MOONSTONE.get())
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MALACHITE.get()), has(ModItems.REFINED_MALACHITE.get()))
                .save(pWriter);

        //WAR AXES



    }
    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                    pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Thuumcraft.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }

}
