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
    private static final double SPELL_POWER_ATTRIBUTE_MIN = -100.0D;
    private static final double SPELL_POWER_ATTRIBUTE_MAX = 100.0D;
    private static final double BASE_STAMINA = 100.0D;
    private static final double BASE_STAMINA_REGENERATION = 10.0D;

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Thuumcraft.MOD_ID);

    private static final List<RegistryObject<Attribute>> CUSTOM_ATTRIBUTES = new ArrayList<>();

    public static final RegistryObject<Attribute> ALCHEMY = registerSkillAttribute("alchemy");
    public static final RegistryObject<Attribute> ALTERATION = registerSkillAttribute("alteration");
    public static final RegistryObject<Attribute> ARCHERY = registerSkillAttribute("archery");
    public static final RegistryObject<Attribute> BARTER = registerSkillAttribute("barter");
    public static final RegistryObject<Attribute> BLOCK = registerSkillAttribute("block");
    public static final RegistryObject<Attribute> CONJURATION = registerSkillAttribute("conjuration");
    public static final RegistryObject<Attribute> DESTRUCTION = registerSkillAttribute("destruction");
    public static final RegistryObject<Attribute> ENCHANTING = registerSkillAttribute("enchanting");
    public static final RegistryObject<Attribute> ALTERATION_SPELL_POWER = registerSpellPowerAttribute("alteration_spell_power");
    public static final RegistryObject<Attribute> CONJURATION_SPELL_POWER = registerSpellPowerAttribute("conjuration_spell_power");
    public static final RegistryObject<Attribute> DESTRUCTION_SPELL_POWER = registerSpellPowerAttribute("destruction_spell_power");
    public static final RegistryObject<Attribute> FIRE_SPELL_POWER = registerSpellPowerAttribute("fire_spell_power");
    public static final RegistryObject<Attribute> FROST_SPELL_POWER = registerSpellPowerAttribute("frost_spell_power");
    public static final RegistryObject<Attribute> SHOCK_SPELL_POWER = registerSpellPowerAttribute("shock_spell_power");
    public static final RegistryObject<Attribute> HEAVY_ARMOR = registerSkillAttribute("heavy_armor");
    public static final RegistryObject<Attribute> ILLUSION = registerSkillAttribute("illusion");
    public static final RegistryObject<Attribute> ILLUSION_SPELL_POWER = registerSpellPowerAttribute("illusion_spell_power");
    public static final RegistryObject<Attribute> LIGHT_ARMOR = registerSkillAttribute("light_armor");
    public static final RegistryObject<Attribute> LOCKPICKING = registerSkillAttribute("lockpicking");
    public static final RegistryObject<Attribute> ONE_HANDED = registerSkillAttribute("one_handed");
    public static final RegistryObject<Attribute> PICKPOCKET = registerSkillAttribute("pickpocket");
    public static final RegistryObject<Attribute> RESTORATION = registerSkillAttribute("restoration");
    public static final RegistryObject<Attribute> RESTORATION_SPELL_POWER = registerSpellPowerAttribute("restoration_spell_power");
    public static final RegistryObject<Attribute> SMITHING = registerSkillAttribute("smithing");
    public static final RegistryObject<Attribute> STAMINA = registerPositiveAttribute("stamina", BASE_STAMINA);
    public static final RegistryObject<Attribute> STAMINA_REGENERATION = registerPositiveAttribute("stamina_regeneration", BASE_STAMINA_REGENERATION);
    public static final RegistryObject<Attribute> TWO_HANDED = registerSkillAttribute("two_handed");

    private static RegistryObject<Attribute> registerAttribute(String name) {
        return registerAttribute(name, DEFAULT_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerSkillAttribute(String name) {
        return registerAttribute(name, SKYRIM_SKILL_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerPositiveAttribute(String name, double defaultValue) {
        return registerAttribute(name, defaultValue, 0.0D, DEFAULT_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerSpellPowerAttribute(String name) {
        return registerAttribute(name, 1.0D, SPELL_POWER_ATTRIBUTE_MIN, SPELL_POWER_ATTRIBUTE_MAX);
    }

    private static RegistryObject<Attribute> registerAttribute(String name, double maxValue) {
        return registerAttribute(name, 0.0D, DEFAULT_ATTRIBUTE_MIN, maxValue);
    }

    private static RegistryObject<Attribute> registerAttribute(String name, double defaultValue, double minValue, double maxValue) {
        RegistryObject<Attribute> attribute = ATTRIBUTES.register(name,
                () -> new RangedAttribute("attribute.name." + Thuumcraft.MOD_ID + "." + name,
                        defaultValue, minValue, maxValue).setSyncable(true));
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
