package net.apotheoticstudios.thuumcraft.effect;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Thuumcraft.MOD_ID);

    public static final RegistryObject<MobEffect> DAMAGE_MAGICKA = register("damage_magicka", MobEffectCategory.HARMFUL, 0x3752A3);
    public static final RegistryObject<MobEffect> DAMAGE_MAGICKA_REGENERATION = register("damage_magicka_regeneration", MobEffectCategory.HARMFUL, 0x26316E);
    public static final RegistryObject<MobEffect> FEAR = register("fear", MobEffectCategory.HARMFUL, 0x5D4D6E);
    public static final RegistryObject<MobEffect> FIRE_WEAKNESS = register("fire_weakness", MobEffectCategory.HARMFUL, 0xD15F2A);
    public static final RegistryObject<MobEffect> FRENZY = register("frenzy", MobEffectCategory.HARMFUL, 0x8F2A2A);
    public static final RegistryObject<MobEffect> FROST_RESISTANCE = register("frost_resistance", MobEffectCategory.BENEFICIAL, 0x9ED9FF);
    public static final RegistryObject<MobEffect> FROST_WEAKNESS = register("frost_weakness", MobEffectCategory.HARMFUL, 0x5FA4CC);
    public static final RegistryObject<MobEffect> FORTIFY_MAGICKA = register("fortify_magicka", MobEffectCategory.BENEFICIAL, 0x5D77D7);
    public static final RegistryObject<MobEffect> LIGHT = register("light", MobEffectCategory.BENEFICIAL, 0xFFE7A3);
    public static final RegistryObject<MobEffect> MAGIC_RESISTANCE = register("magic_resistance", MobEffectCategory.BENEFICIAL, 0xA48DCE);
    public static final RegistryObject<MobEffect> MAGIC_WEAKNESS = register("magic_weakness", MobEffectCategory.HARMFUL, 0x6D5191);
    public static final RegistryObject<MobEffect> PARALYSIS = MOB_EFFECTS.register("paralysis",
            () -> new ParalysisMobEffect()
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "C7406443-1445-4E73-8B41-9E5F274C24BB",
                            -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> POISON_RESISTANCE = register("poison_resistance", MobEffectCategory.BENEFICIAL, 0x4B8E37);
    public static final RegistryObject<MobEffect> POISON_WEAKNESS = register("poison_weakness", MobEffectCategory.HARMFUL, 0x756D2F);
    public static final RegistryObject<MobEffect> REGENERATE_MAGICKA = register("regenerate_magicka", MobEffectCategory.BENEFICIAL, 0x5369BD);
    public static final RegistryObject<MobEffect> RESTORE_MAGICKA = register("restore_magicka", MobEffectCategory.BENEFICIAL, 0x637BF0);
    public static final RegistryObject<MobEffect> SHOCK_RESISTANCE = register("shock_resistance", MobEffectCategory.BENEFICIAL, 0xD3CCFF);
    public static final RegistryObject<MobEffect> SHOCK_WEAKNESS = register("shock_weakness", MobEffectCategory.HARMFUL, 0xA79CF1);
    public static final RegistryObject<MobEffect> SPELL_ABSORPTION = register("spell_absorption", MobEffectCategory.BENEFICIAL, 0xC0A4FF);

    private static RegistryObject<MobEffect> register(String name, MobEffectCategory category, int color) {
        return MOB_EFFECTS.register(name, () -> new MobEffect(category, color) {});
    }

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
