package net.apotheoticstudios.thuumcraft.datagen;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Thuumcraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.REFINED_MALACHITE);
        simpleItem(ModItems.MALACHITE_ORE);
        simpleItem(ModItems.DWARVEN_METAL_INGOT);
        simpleItem(ModItems.CORUNDUM_ORE);
        simpleItem(ModItems.CORUNDUM_INGOT);
        simpleItem(ModItems.STEEL_INGOT);
        simpleItem(ModItems.MOONSTONE_ORE);
        simpleItem(ModItems.SILVER_ORE);
        simpleItem(ModItems.ORICHALCUM_ORE);
        simpleItem(ModItems.EBONY_ORE);
        simpleItem(ModItems.REFINED_MOONSTONE);
        simpleItem(ModItems.SILVER_INGOT);
        simpleItem(ModItems.EBONY_INGOT);

        simpleItem(ModItems.LEATHER_STRIPS);
        simpleItem(ModItems.HANDLE);

        simpleItem(ModItems.JUNIPER_BERRIES);

        handheldItem(ModItems.GLASS_SWORD);
        handheldItem(ModItems.GLASS_PICKAXE);
        handheldItem(ModItems.GLASS_AXE);

        handheldItem(ModItems.DWARVEN_SWORD);
        handheldItem(ModItems.DWARVEN_PICKAXE);
        handheldItem(ModItems.DWARVEN_AXE);

        handheldItem(ModItems.STEEL_SWORD);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Thuumcraft.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Thuumcraft.MOD_ID,"item/" + item.getId().getPath()));
    }
}
