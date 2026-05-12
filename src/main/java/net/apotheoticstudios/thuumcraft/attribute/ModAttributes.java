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
    private static final double DEFAULT_ATTRIBUTE_MIN = -1024.0D;
    private static final double DEFAULT_ATTRIBUTE_MAX = 1024.0D;
    private static final double SKYRIM_SKILL_ATTRIBUTE_MAX = 100.0D;

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Thuumcraft.MOD_ID);

    private static final List<RegistryObject<Attribute>> CUSTOM_ATTRIBUTES = new ArrayList<>();

    public static final RegistryObject<Attribute> ARCHERY = registerSkillAttribute("archery");
    public static final RegistryObject<Attribute> BARTER = registerSkillAttribute("barter");
    public static final RegistryObject<Attribute> BLOCK = registerSkillAttribute("block");
    public static final RegistryObject<Attribute> CARRY_WEIGHT = registerAttribute("carry_weight");
    public static final RegistryObject<Attribute> ENCHANTING = registerSkillAttribute("enchanting");
    public static final RegistryObject<Attribute> HEAVY_ARMOR = registerSkillAttribute("heavy_armor");
    public static final RegistryObject<Attribute> LIGHT_ARMOR = registerSkillAttribute("light_armor");
    public static final RegistryObject<Attribute> LOCKPICKING = registerSkillAttribute("lockpicking");
    public static final RegistryObject<Attribute> ONE_HANDED = registerSkillAttribute("one_handed");
    public static final RegistryObject<Attribute> PICKPOCKET = registerSkillAttribute("pickpocket");
    public static final RegistryObject<Attribute> SMITHING = registerSkillAttribute("smithing");
    public static final RegistryObject<Attribute> SNEAK = registerSkillAttribute("sneak");
    public static final RegistryObject<Attribute> STAMINA = registerAttribute("stamina");
    public static final RegistryObject<Attribute> STAMINA_REGENERATION = registerAttribute("stamina_regeneration");
    public static final RegistryObject<Attribute> TWO_HANDED = registerSkillAttribute("two_handed");

    private static RegistryObject<Attribute> registerAttribute(String name) {
        return registerAttribute(name, DEFAULT_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerSkillAttribute(String name) {
        return registerAttribute(name, SKYRIM_SKILL_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerAttribute(String name, double maxValue) {
        RegistryObject<Attribute> attribute = ATTRIBUTES.register(name,
                () -> new RangedAttribute("attribute.name." + Thuumcraft.MOD_ID + "." + name,
                        0.0D, DEFAULT_ATTRIBUTE_MIN, maxValue).setSyncable(true));
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
