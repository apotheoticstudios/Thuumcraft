package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Thuumcraft.MOD_ID);

    public static final RegistryObject<Item> REFINED_MALACHITE = ITEMS.register("refined_malachite",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MALACHITE_ORE = ITEMS.register("malachite_ore",
            () -> new Item(new Item.Properties().food(ModFoods.JUNIPER_BERRIES)));

    public static final RegistryObject<Item> JUNIPER_BERRIES = ITEMS.register("juniper_berries",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
