package net.apotheoticstudios.thuumcraft.magic;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModSpellSchools {
    public static final DeferredRegister<SchoolType> SPELL_SCHOOLS =
            DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, Thuumcraft.MOD_ID);

    public static final RegistryObject<SchoolType> ALTERATION = registerSchool("alteration", ChatFormatting.AQUA);
    public static final RegistryObject<SchoolType> CONJURATION = registerSchool("conjuration", ChatFormatting.DARK_PURPLE);
    public static final RegistryObject<SchoolType> DESTRUCTION = registerSchool("destruction", ChatFormatting.RED);
    public static final RegistryObject<SchoolType> ILLUSION = registerSchool("illusion", ChatFormatting.LIGHT_PURPLE);
    public static final RegistryObject<SchoolType> RESTORATION = registerSchool("restoration", ChatFormatting.GREEN);

    private ModSpellSchools() {
    }

    public static void register(IEventBus eventBus) {
        SPELL_SCHOOLS.register(eventBus);
    }

    private static RegistryObject<SchoolType> registerSchool(String name, ChatFormatting color) {
        ResourceLocation id = new ResourceLocation(Thuumcraft.MOD_ID, name);
        TagKey<Item> focusTag = TagKey.create(Registries.ITEM,
                new ResourceLocation(Thuumcraft.MOD_ID, "spell_focus/" + name));
        return SPELL_SCHOOLS.register(name, () -> new SchoolType(id,
                focusTag,
                Component.translatable("school.thuumcraft." + name).withStyle(color),
                LazyOptional.<Attribute>empty(),
                LazyOptional.<Attribute>empty(),
                LazyOptional.<SoundEvent>of(() -> SoundEvents.AMETHYST_BLOCK_CHIME),
                DamageTypes.MAGIC));
    }
}
