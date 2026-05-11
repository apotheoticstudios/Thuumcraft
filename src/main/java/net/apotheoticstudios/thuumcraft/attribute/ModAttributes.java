package net.apotheoticstudios.thuumcraft.attribute;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Thuumcraft.MOD_ID);

    private static final List<RegistryObject<Attribute>> CUSTOM_ATTRIBUTES = new ArrayList<>();

    public static final RegistryObject<Attribute> ARCHERY = registerAttribute("archery");
    public static final RegistryObject<Attribute> BARTER = registerAttribute("barter");
    public static final RegistryObject<Attribute> BLOCK = registerAttribute("block");
    public static final RegistryObject<Attribute> CARRY_WEIGHT = registerAttribute("carry_weight");
    public static final RegistryObject<Attribute> ENCHANTING = registerAttribute("enchanting");
    public static final RegistryObject<Attribute> HEAVY_ARMOR = registerAttribute("heavy_armor");
    public static final RegistryObject<Attribute> LIGHT_ARMOR = registerAttribute("light_armor");
    public static final RegistryObject<Attribute> LOCKPICKING = registerAttribute("lockpicking");
    public static final RegistryObject<Attribute> ONE_HANDED = registerAttribute("one_handed");
    public static final RegistryObject<Attribute> PICKPOCKET = registerAttribute("pickpocket");
    public static final RegistryObject<Attribute> SMITHING = registerAttribute("smithing");
    public static final RegistryObject<Attribute> SNEAK = registerAttribute("sneak");
    public static final RegistryObject<Attribute> STAMINA = registerAttribute("stamina");
    public static final RegistryObject<Attribute> STAMINA_REGENERATION = registerAttribute("stamina_regeneration");
    public static final RegistryObject<Attribute> TWO_HANDED = registerAttribute("two_handed");

    private static RegistryObject<Attribute> registerAttribute(String name) {
        RegistryObject<Attribute> attribute = ATTRIBUTES.register(name,
                () -> new RangedAttribute("attribute.name." + Thuumcraft.MOD_ID + "." + name,
                        0.0D, -1024.0D, 1024.0D).setSyncable(true));
        CUSTOM_ATTRIBUTES.add(attribute);
        return attribute;
    }

    public static List<RegistryObject<Attribute>> customAttributes() {
        return Collections.unmodifiableList(CUSTOM_ATTRIBUTES);
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
