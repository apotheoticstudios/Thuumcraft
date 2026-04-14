package net.apotheoticstudios.thuumcraft.sound;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Thuumcraft.MOD_ID);

    public static final RegistryObject<SoundEvent> GIANT_IDLE = registerSoundEvents("giant_idle");
    public static final RegistryObject<SoundEvent> GIANT_DAMAGED = registerSoundEvents("giant_damaged");
    public static final RegistryObject<SoundEvent> GIANT_DEATH = registerSoundEvents("giant_death");



    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Thuumcraft.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
