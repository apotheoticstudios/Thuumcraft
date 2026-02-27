package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Thuumcraft.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("thuumcraft_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.REFINED_MALACHITE_BLOCK.get()))
                    .title(Component.translatable("creativetab.thuumcraft_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.REFINED_MALACHITE_BLOCK.get());

                        pOutput.accept(Items.DIAMOND);


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}