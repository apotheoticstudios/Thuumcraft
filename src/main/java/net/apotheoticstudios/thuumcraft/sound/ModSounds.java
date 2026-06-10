package net.apotheoticstudios.thuumcraft.sound;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
    public static final RegistryObject<SoundEvent> SKEEVER_IDLE = registerSoundEvents("skeever_idle");
    public static final RegistryObject<SoundEvent> SKEEVER_HURT = registerSoundEvents("skeever_hurt");
    public static final RegistryObject<SoundEvent> SKEEVER_DEATH = registerSoundEvents("skeever_death");
    public static final RegistryObject<SoundEvent> SKEEVER_ATTACK = registerSoundEvents("skeever_attack");

    public static final RegistryObject<SoundEvent> DYNAMIC_FOREST_BIRDS_DAY = registerSoundEvents("dynamic.forest_birds_day");
    public static final RegistryObject<SoundEvent> DYNAMIC_FOREST_BIRDS_NIGHT = registerSoundEvents("dynamic.forest_birds_night");
    public static final RegistryObject<SoundEvent> DYNAMIC_FOREST_RUSTLE = registerSoundEvents("dynamic.forest_rustle");
    public static final RegistryObject<SoundEvent> DYNAMIC_FOREST_INSECTS = registerSoundEvents("dynamic.forest_insects");
    public static final RegistryObject<SoundEvent> DYNAMIC_PLAINS_BIRDS = registerSoundEvents("dynamic.plains_birds");
    public static final RegistryObject<SoundEvent> DYNAMIC_PLAINS_WIND = registerSoundEvents("dynamic.plains_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_MOUNTAIN_WIND = registerSoundEvents("dynamic.mountain_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_MOUNTAIN_EAGLE = registerSoundEvents("dynamic.mountain_eagle");
    public static final RegistryObject<SoundEvent> DYNAMIC_SNOW_WIND = registerSoundEvents("dynamic.snow_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_TUNDRA_WIND = registerSoundEvents("dynamic.tundra_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_SWAMP_FROGS = registerSoundEvents("dynamic.swamp_frogs");
    public static final RegistryObject<SoundEvent> DYNAMIC_SWAMP_INSECTS = registerSoundEvents("dynamic.swamp_insects");
    public static final RegistryObject<SoundEvent> DYNAMIC_RIVER_WATER = registerSoundEvents("dynamic.river_water");
    public static final RegistryObject<SoundEvent> DYNAMIC_SHORE_WAVES = registerSoundEvents("dynamic.shore_waves");
    public static final RegistryObject<SoundEvent> DYNAMIC_RAIN_DRIPS = registerSoundEvents("dynamic.rain_drips");
    public static final RegistryObject<SoundEvent> DYNAMIC_THUNDER_DISTANCE = registerSoundEvents("dynamic.thunder_distance");
    public static final RegistryObject<SoundEvent> DYNAMIC_NIGHT_OWL = registerSoundEvents("dynamic.night_owl");
    public static final RegistryObject<SoundEvent> DYNAMIC_NIGHT_WOLF = registerSoundEvents("dynamic.night_wolf");
    public static final RegistryObject<SoundEvent> DYNAMIC_CAVE_RUMBLE = registerSoundEvents("dynamic.cave_rumble");
    public static final RegistryObject<SoundEvent> DYNAMIC_CAVE_DRIPS = registerSoundEvents("dynamic.cave_drips");
    public static final RegistryObject<SoundEvent> DYNAMIC_CAVE_WIND = registerSoundEvents("dynamic.cave_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_ICE_CAVE = registerSoundEvents("dynamic.ice_cave");
    public static final RegistryObject<SoundEvent> DYNAMIC_DUNGEON_CHAIN = registerSoundEvents("dynamic.dungeon_chain");
    public static final RegistryObject<SoundEvent> DYNAMIC_DUNGEON_WHISPER = registerSoundEvents("dynamic.dungeon_whisper");
    public static final RegistryObject<SoundEvent> DYNAMIC_RUIN_STONE = registerSoundEvents("dynamic.ruin_stone");
    public static final RegistryObject<SoundEvent> DYNAMIC_DESERT_WIND = registerSoundEvents("dynamic.desert_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_JUNGLE_BIRDS = registerSoundEvents("dynamic.jungle_birds");
    public static final RegistryObject<SoundEvent> DYNAMIC_BADLANDS_WIND = registerSoundEvents("dynamic.badlands_wind");
    public static final RegistryObject<SoundEvent> DYNAMIC_VILLAGE_DISTANT = registerSoundEvents("dynamic.village_distant");
    public static final RegistryObject<SoundEvent> DYNAMIC_FARM_ANIMALS = registerSoundEvents("dynamic.farm_animals");
    public static final RegistryObject<SoundEvent> DYNAMIC_CITY_CROWD = registerSoundEvents("dynamic.city_crowd");
    public static final RegistryObject<SoundEvent> DYNAMIC_INTERIOR_CREAK = registerSoundEvents("dynamic.interior_creak");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Thuumcraft.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
