package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Thuumcraft.MOD_ID);

    public static final RegistryObject<Item> REFINED_MALACHITE = ITEMS.register("refined_malachite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_METAL_INGOT = ITEMS.register("dwarven_metal_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REFINED_MOONSTONE = ITEMS.register("refined_moonstone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EBONY_INGOT = ITEMS.register("ebony_ingot",
            () -> new Item(new Item.Properties()));




    public static final RegistryObject<Item> MALACHITE_ORE = ITEMS.register("malachite_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORUNDUM_ORE = ITEMS.register("corundum_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORUNDUM_INGOT = ITEMS.register("corundum_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOONSTONE_ORE = ITEMS.register("moonstone_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCUM_ORE = ITEMS.register("orichalcum_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EBONY_ORE = ITEMS.register("ebony_ore",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LEATHER_STRIPS = ITEMS.register("leather_strips",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HANDLE = ITEMS.register("handle",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> JUNIPER_BERRIES = ITEMS.register("juniper_berries",
            () -> new Item(new Item.Properties().food(ModFoods.JUNIPER_BERRIES)));

    public static final RegistryObject<Item> GLASS_SWORD = ITEMS.register("glass_sword",
            () -> new SwordItem(ModToolTiers.GLASS, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> GLASS_PICKAXE = ITEMS.register("glass_pickaxe",
            () -> new PickaxeItem(ModToolTiers.GLASS, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> GLASS_AXE = ITEMS.register("glass_axe",
            () -> new AxeItem(ModToolTiers.GLASS, 5, -3, new Item.Properties()));

    public static final RegistryObject<Item> DWARVEN_SWORD = ITEMS.register("dwarven_sword",
            () -> new SwordItem(ModToolTiers.DWARVEN, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_PICKAXE = ITEMS.register("dwarven_pickaxe",
            () -> new PickaxeItem(ModToolTiers.DWARVEN, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_AXE = ITEMS.register("dwarven_axe",
            () -> new AxeItem(ModToolTiers.DWARVEN, 5, -3, new Item.Properties()));

    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword",
            () -> new SwordItem(ModToolTiers.STEEL, 3, -2.4f, new Item.Properties()));






    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
