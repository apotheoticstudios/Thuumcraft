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
            ModBlocks.MALACHITE_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get());

    private static final List<ItemLike> CORUNDUM_SMELTABLES = List.of(ModItems.CORUNDUM_ORE.get(),
            ModBlocks.CORUNDUM_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_CORUNDUM_ORE_VEIN.get());

    private static final List<ItemLike> MOONSTONE_SMELTABLES = List.of(ModItems.MOONSTONE_ORE.get(),
            ModBlocks.MOONSTONE_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_MOONSTONE_ORE_VEIN.get());

    private static final List<ItemLike> QUICKSILVER_SMELTABLES = List.of(ModItems.QUICKSILVER_ORE.get(),
            ModBlocks.QUICKSILVER_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_QUICKSILVER_ORE_VEIN.get());

    private static final List<ItemLike> SILVER_SMELTABLES = List.of(ModItems.SILVER_ORE.get(),
            ModBlocks.SILVER_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_SILVER_ORE_VEIN.get());

    private static final List<ItemLike> ORICHALCUM_SMELTABLES = List.of(ModItems.ORICHALCUM_ORE.get(),
            ModBlocks.ORICHALCUM_ORE_VEIN.get(),
            ModBlocks.DEEPSLATE_ORICHALCUM_ORE_VEIN.get());


    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        //REFINED_MALACHITE
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

        //MOONSTONE
        oreSmelting(pWriter, MOONSTONE_SMELTABLES, RecipeCategory.MISC, ModItems.REFINED_MOONSTONE.get(), 0.7f, 400, "refined_moonstone");
        oreBlasting(pWriter, MOONSTONE_SMELTABLES, RecipeCategory.MISC, ModItems.REFINED_MOONSTONE.get(), 0.7f, 200, "refined_moonstone");

        //QUICKSILVER
        oreSmelting(pWriter, QUICKSILVER_SMELTABLES, RecipeCategory.MISC, ModItems.QUICKSILVER_INGOT.get(), 0.7f, 400, "quicksilver_ingot");
        oreBlasting(pWriter, QUICKSILVER_SMELTABLES, RecipeCategory.MISC, ModItems.QUICKSILVER_INGOT.get(), 0.7f, 200, "quicksilver_ingot");

        //ORICHALCUM
        oreSmelting(pWriter, ORICHALCUM_SMELTABLES, RecipeCategory.MISC, ModItems.ORICHALCUM_INGOT.get(), 0.7f, 400, "orichalcum_ingot");
        oreBlasting(pWriter, ORICHALCUM_SMELTABLES, RecipeCategory.MISC, ModItems.ORICHALCUM_INGOT.get(), 0.7f, 200, "orichalcum_ingot");

        //SILVER
        oreSmelting(pWriter, SILVER_SMELTABLES, RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.6f, 300, "silver_ingot");
        oreBlasting(pWriter, SILVER_SMELTABLES, RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.6f, 150, "silver_ingot");

        //CORUNDUM
        oreSmelting(pWriter, CORUNDUM_SMELTABLES, RecipeCategory.MISC, ModItems.CORUNDUM_INGOT.get(), 0.6f, 300, "corundum_ingot");
        oreBlasting(pWriter, CORUNDUM_SMELTABLES, RecipeCategory.MISC, ModItems.CORUNDUM_INGOT.get(), 0.6f, 150, "corundum_ingot");

        //STEEL
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_INGOT.get())
                .requires(ModItems.CORUNDUM_INGOT.get())
                .requires(Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.CORUNDUM_INGOT.get()), has(ModItems.CORUNDUM_INGOT.get()))
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

        //SWORDS
        swordRecipe(pWriter, ModItems.SILVER_SWORD.get(), ModItems.SILVER_INGOT.get());
        swordRecipe(pWriter, ModItems.STEEL_SWORD.get(), ModItems.STEEL_INGOT.get());
        swordRecipe(pWriter, ModItems.DWARVEN_SWORD.get(), ModItems.DWARVEN_METAL_INGOT.get());
        swordRecipe(pWriter, ModItems.ORCISH_SWORD.get(), ModItems.ORICHALCUM_INGOT.get());
        elvenSwordRecipe(pWriter, ModItems.ELVEN_SWORD.get());
        glassSwordRecipe(pWriter, ModItems.GLASS_SWORD.get());
        swordRecipe(pWriter, ModItems.EBONY_SWORD.get(), ModItems.EBONY_INGOT.get());

        //WAR AXES
        warAxeRecipe(pWriter, ModItems.SILVER_WAR_AXE.get(), ModItems.SILVER_INGOT.get());
        warAxeRecipe(pWriter, ModItems.STEEL_WAR_AXE.get(), ModItems.STEEL_INGOT.get());
        warAxeRecipe(pWriter, ModItems.DWARVEN_WAR_AXE.get(), ModItems.DWARVEN_METAL_INGOT.get());
        warAxeRecipe(pWriter, ModItems.ORCISH_WAR_AXE.get(), ModItems.ORICHALCUM_INGOT.get());
        elvenWarAxeRecipe(pWriter, ModItems.ELVEN_WAR_AXE.get());
        glassWarAxeRecipe(pWriter, ModItems.GLASS_WAR_AXE.get());
        warAxeRecipe(pWriter, ModItems.EBONY_WAR_AXE.get(), ModItems.EBONY_INGOT.get());

        // MISC

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LEATHER_STRIPS.get(), 2)
                .requires(Items.LEATHER)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(pWriter);


    }

    private void swordRecipe(Consumer<FinishedRecipe> writer, ItemLike result, ItemLike material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("M")
                .pattern("M")
                .pattern("H")
                .define('M', material)
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    private void elvenSwordRecipe(Consumer<FinishedRecipe> writer, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("M")
                .pattern("Q")
                .pattern("H")
                .define('M', ModItems.REFINED_MOONSTONE.get())
                .define('Q', ModItems.QUICKSILVER_INGOT.get())
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MOONSTONE.get()), has(ModItems.REFINED_MOONSTONE.get()))
                .save(writer);
    }

    private void glassSwordRecipe(Consumer<FinishedRecipe> writer, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("R")
                .pattern("M")
                .pattern("H")
                .define('R', ModItems.REFINED_MALACHITE.get())
                .define('M', ModItems.REFINED_MOONSTONE.get())
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MALACHITE.get()), has(ModItems.REFINED_MALACHITE.get()))
                .save(writer);
    }

    private void warAxeRecipe(Consumer<FinishedRecipe> writer, ItemLike result, ItemLike material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("MM")
                .pattern("MS")
                .pattern(" H")
                .define('M', material)
                .define('S', Items.STICK)
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    private void elvenWarAxeRecipe(Consumer<FinishedRecipe> writer, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("MQ")
                .pattern("MS")
                .pattern(" H")
                .define('M', ModItems.REFINED_MOONSTONE.get())
                .define('Q', ModItems.QUICKSILVER_INGOT.get())
                .define('S', Items.STICK)
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MOONSTONE.get()), has(ModItems.REFINED_MOONSTONE.get()))
                .save(writer);
    }

    private void glassWarAxeRecipe(Consumer<FinishedRecipe> writer, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("RR")
                .pattern("MS")
                .pattern(" H")
                .define('R', ModItems.REFINED_MALACHITE.get())
                .define('M', ModItems.REFINED_MOONSTONE.get())
                .define('S', Items.STICK)
                .define('H', ModItems.HANDLE.get())
                .unlockedBy(getHasName(ModItems.REFINED_MALACHITE.get()), has(ModItems.REFINED_MALACHITE.get()))
                .save(writer);
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
