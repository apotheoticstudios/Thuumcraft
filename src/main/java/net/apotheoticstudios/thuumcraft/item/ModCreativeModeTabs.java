package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.apotheoticstudios.thuumcraft.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Thuumcraft.MOD_ID);

    public static final RegistryObject<CreativeModeTab> THUUMCRAFT_TAB = CREATIVE_MODE_TABS.register("thuumcraft_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MALACHITE_ORE.get()))
                    .title(Component.translatable("creativetab.thuumcraft_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModItems.JUNIPER_BERRIES.get());

                        pOutput.accept(ModBlocks.MALACHITE_ORE_VEIN.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_MALACHITE_ORE_VEIN.get());
                        pOutput.accept(ModBlocks.REFINED_MALACHITE_BLOCK.get());
                        pOutput.accept(ModItems.MALACHITE_ORE.get());
                        pOutput.accept(ModItems.REFINED_MALACHITE.get());

                        pOutput.accept(ModItems.GLASS_SWORD.get());
                        pOutput.accept(ModItems.GLASS_WAR_AXE.get());

                        pOutput.accept(ModItems.DWARVEN_METAL_INGOT.get());
                        pOutput.accept(ModItems.DWARVEN_SWORD.get());
                        pOutput.accept(ModItems.DWARVEN_WAR_AXE.get());

                        pOutput.accept(ModBlocks.CORUNDUM_ORE_VEIN.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_CORUNDUM_ORE_VEIN.get());
                        pOutput.accept(ModItems.CORUNDUM_ORE.get());
                        pOutput.accept(ModItems.CORUNDUM_INGOT.get());

                        pOutput.accept(ModItems.STEEL_INGOT.get());
                        pOutput.accept(ModItems.STEEL_SWORD.get());

                        pOutput.accept(ModItems.MOONSTONE_ORE.get());
                        pOutput.accept(ModItems.SILVER_ORE.get());
                        pOutput.accept(ModItems.ORICHALCUM_ORE.get());
                        pOutput.accept(ModItems.EBONY_ORE.get());
                        pOutput.accept(ModItems.REFINED_MOONSTONE.get());
                        pOutput.accept(ModItems.SILVER_INGOT.get());
                        pOutput.accept(ModItems.EBONY_INGOT.get());

                        pOutput.accept(ModItems.LEATHER_STRIPS.get());
                        pOutput.accept(ModItems.HANDLE.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}