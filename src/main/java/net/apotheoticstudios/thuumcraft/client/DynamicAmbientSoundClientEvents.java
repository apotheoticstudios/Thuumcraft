package net.apotheoticstudios.thuumcraft.client;

import com.mojang.logging.LogUtils;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.sound.ModSounds;
import net.apotheoticstudios.thuumcraft.sound.StructureSoundCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class DynamicAmbientSoundClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int MIN_INTERVAL_TICKS = 90;
    private static final int MAX_INTERVAL_TICKS = 260;
    private static final int WATER_SCAN_HORIZONTAL_RADIUS = 8;
    private static final int WATER_SCAN_VERTICAL_RADIUS = 4;
    private static int nextAmbientSoundTick;

    private DynamicAmbientSoundClientEvents() {
    }

    @SubscribeEvent
    public static void tickDynamicAmbientSounds(TickEvent.ClientTickEvent event) {
        try {
            if (event.phase != TickEvent.Phase.END || !Config.ENABLE_DYNAMIC_AMBIENT_SOUNDS.get()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null || minecraft.player == null) {
                nextAmbientSoundTick = 0;
                ClientStructureSoundState.clear();
                return;
            }
            if (minecraft.isPaused()) {
                nextAmbientSoundTick = 0;
                return;
            }

            double frequency = Config.DYNAMIC_AMBIENT_SOUND_FREQUENCY.get();
            if (frequency <= 0.0D || minecraft.player.isSpectator()) {
                return;
            }

            if (minecraft.player.tickCount < nextAmbientSoundTick) {
                return;
            }

            RandomSource random = minecraft.player.getRandom();
            nextAmbientSoundTick = minecraft.player.tickCount + nextInterval(random, frequency);
            AmbientCandidate selected = chooseAvailableCandidate(minecraft, gatherCandidates(minecraft.level, minecraft.player), random);
            if (selected != null) {
                playCandidate(minecraft, selected, random);
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to tick dynamic ambient sounds", exception);
            nextAmbientSoundTick = 0;
        }
    }

    private static int nextInterval(RandomSource random, double frequency) {
        int base = MIN_INTERVAL_TICKS + random.nextInt(MAX_INTERVAL_TICKS - MIN_INTERVAL_TICKS + 1);
        return Math.max(40, (int) Math.round(base / Math.max(0.05D, frequency)));
    }

    private static List<AmbientCandidate> gatherCandidates(ClientLevel level, Player player) {
        List<AmbientCandidate> candidates = new ArrayList<>();
        BlockPos origin = player.blockPosition();
        boolean seesSky = level.canSeeSky(origin);
        boolean night = isNight(level);
        boolean cave = !seesSky && origin.getY() < level.getSeaLevel() + 10;
        boolean interior = !seesSky && !cave;
        String biomePath = biomePath(level, origin);
        boolean waterNearby = scanWaterNearby(level, origin);
        int structureMask = ClientStructureSoundState.categoryMask();
        boolean dungeonStructure = StructureSoundCategory.hasAny(structureMask,
                StructureSoundCategory.DUNGEON, StructureSoundCategory.RUIN, StructureSoundCategory.FORT,
                StructureSoundCategory.MINE, StructureSoundCategory.CAVE, StructureSoundCategory.TEMPLE,
                StructureSoundCategory.TOMB, StructureSoundCategory.NETHER, StructureSoundCategory.END);
        boolean ruinStructure = StructureSoundCategory.hasAny(structureMask,
                StructureSoundCategory.RUIN, StructureSoundCategory.FORT, StructureSoundCategory.TEMPLE,
                StructureSoundCategory.TOMB, StructureSoundCategory.NETHER, StructureSoundCategory.END);
        boolean settlementStructure = StructureSoundCategory.hasAny(structureMask,
                StructureSoundCategory.SETTLEMENT, StructureSoundCategory.CITY,
                StructureSoundCategory.VILLAGE, StructureSoundCategory.FARM);
        boolean cityStructure = StructureSoundCategory.has(structureMask, StructureSoundCategory.CITY);
        boolean villageStructure = StructureSoundCategory.hasAny(structureMask,
                StructureSoundCategory.VILLAGE, StructureSoundCategory.SETTLEMENT);
        boolean farmStructure = StructureSoundCategory.has(structureMask, StructureSoundCategory.FARM);
        boolean mineOrCaveStructure = StructureSoundCategory.hasAny(structureMask,
                StructureSoundCategory.MINE, StructureSoundCategory.CAVE);
        boolean waterStructure = StructureSoundCategory.has(structureMask, StructureSoundCategory.WATER);
        boolean wildStructure = StructureSoundCategory.has(structureMask, StructureSoundCategory.WILD);

        if (level.isRaining()) {
            add(candidates, ModSounds.DYNAMIC_RAIN_DRIPS, 10, 0.58F, 0.92F, 1.06F, 6.0D, 18.0D, 4.0D);
        }
        if (level.isThundering()) {
            add(candidates, ModSounds.DYNAMIC_THUNDER_DISTANCE, 3, 0.82F, 0.84F, 1.02F, 18.0D, 34.0D, 8.0D);
        }

        if (cave) {
            add(candidates, ModSounds.DYNAMIC_CAVE_RUMBLE, 8, 0.58F, 0.82F, 1.02F, 10.0D, 26.0D, 8.0D);
            add(candidates, ModSounds.DYNAMIC_CAVE_DRIPS, 10, 0.48F, 0.88F, 1.10F, 5.0D, 18.0D, 6.0D);
            add(candidates, ModSounds.DYNAMIC_CAVE_WIND, 6, 0.52F, 0.86F, 1.04F, 12.0D, 30.0D, 8.0D);
            if (dungeonStructure) {
                add(candidates, ModSounds.DYNAMIC_DUNGEON_CHAIN, 7, 0.48F, 0.88F, 1.06F, 8.0D, 24.0D, 6.0D);
                add(candidates, ModSounds.DYNAMIC_DUNGEON_WHISPER, 4, 0.42F, 0.82F, 1.02F, 10.0D, 28.0D, 8.0D);
            }
            if (ruinStructure) {
                add(candidates, ModSounds.DYNAMIC_RUIN_STONE, 6, 0.45F, 0.88F, 1.08F, 8.0D, 24.0D, 6.0D);
            }
            if (containsAny(biomePath, "frozen", "ice", "snow", "cold")) {
                add(candidates, ModSounds.DYNAMIC_ICE_CAVE, 8, 0.58F, 0.86F, 1.02F, 12.0D, 30.0D, 8.0D);
            }
            return candidates;
        }

        if (interior) {
            add(candidates, ModSounds.DYNAMIC_INTERIOR_CREAK, 10, 0.42F, 0.90F, 1.08F, 4.0D, 14.0D, 4.0D);
            if (settlementStructure) {
                add(candidates, ModSounds.DYNAMIC_VILLAGE_DISTANT, 6, 0.44F, 0.90F, 1.05F, 10.0D, 24.0D, 5.0D);
            }
            if (dungeonStructure) {
                add(candidates, ModSounds.DYNAMIC_DUNGEON_CHAIN, 5, 0.42F, 0.88F, 1.06F, 8.0D, 22.0D, 5.0D);
                add(candidates, ModSounds.DYNAMIC_DUNGEON_WHISPER, 4, 0.38F, 0.82F, 1.02F, 10.0D, 26.0D, 6.0D);
            }
            if (ruinStructure) {
                add(candidates, ModSounds.DYNAMIC_RUIN_STONE, 5, 0.40F, 0.88F, 1.08F, 8.0D, 22.0D, 5.0D);
            }
        }

        if (waterNearby || waterStructure) {
            if (waterStructure || containsAny(biomePath, "ocean", "beach", "shore", "stony_shore")) {
                add(candidates, ModSounds.DYNAMIC_SHORE_WAVES, 12, 0.62F, 0.92F, 1.05F, 6.0D, 22.0D, 3.0D);
            } else {
                add(candidates, ModSounds.DYNAMIC_RIVER_WATER, 10, 0.54F, 0.92F, 1.08F, 5.0D, 18.0D, 3.0D);
            }
        }

        if (settlementStructure) {
            if (villageStructure) {
                add(candidates, ModSounds.DYNAMIC_VILLAGE_DISTANT, 6, 0.44F, 0.90F, 1.05F, 12.0D, 28.0D, 5.0D);
            }
            if (farmStructure) {
                add(candidates, ModSounds.DYNAMIC_FARM_ANIMALS, 5, 0.46F, 0.92F, 1.08F, 10.0D, 26.0D, 4.0D);
            }
            if (cityStructure && !night) {
                add(candidates, ModSounds.DYNAMIC_CITY_CROWD, 5, 0.40F, 0.92F, 1.04F, 14.0D, 30.0D, 4.0D);
            }
        }

        if (dungeonStructure) {
            add(candidates, ModSounds.DYNAMIC_DUNGEON_WHISPER, 4, 0.38F, 0.82F, 1.02F, 12.0D, 32.0D, 8.0D);
            if (ruinStructure) {
                add(candidates, ModSounds.DYNAMIC_RUIN_STONE, 5, 0.40F, 0.88F, 1.08F, 10.0D, 28.0D, 6.0D);
            }
            if (mineOrCaveStructure) {
                add(candidates, ModSounds.DYNAMIC_CAVE_WIND, 4, 0.42F, 0.86F, 1.04F, 12.0D, 30.0D, 8.0D);
            }
        }

        if (wildStructure && !settlementStructure && !dungeonStructure) {
            add(candidates, ModSounds.DYNAMIC_FOREST_RUSTLE, 3, 0.36F, 0.90F, 1.08F, 6.0D, 20.0D, 4.0D);
        }

        if (night) {
            add(candidates, ModSounds.DYNAMIC_NIGHT_OWL, 7, 0.48F, 0.88F, 1.06F, 12.0D, 32.0D, 6.0D);
            add(candidates, ModSounds.DYNAMIC_NIGHT_WOLF, 3, 0.55F, 0.84F, 1.00F, 20.0D, 42.0D, 8.0D);
        }

        if (containsAny(biomePath, "forest", "taiga", "grove")) {
            add(candidates, night ? ModSounds.DYNAMIC_FOREST_BIRDS_NIGHT : ModSounds.DYNAMIC_FOREST_BIRDS_DAY,
                    10, 0.48F, 0.90F, 1.10F, 8.0D, 24.0D, 8.0D);
            add(candidates, ModSounds.DYNAMIC_FOREST_RUSTLE, 7, 0.42F, 0.90F, 1.08F, 5.0D, 18.0D, 4.0D);
            if (!level.isRaining()) {
                add(candidates, ModSounds.DYNAMIC_FOREST_INSECTS, night ? 8 : 4, 0.36F, 0.92F, 1.10F, 6.0D, 18.0D, 4.0D);
            }
        } else if (containsAny(biomePath, "jungle", "bamboo")) {
            add(candidates, ModSounds.DYNAMIC_JUNGLE_BIRDS, 12, 0.55F, 0.90F, 1.08F, 8.0D, 26.0D, 8.0D);
            add(candidates, ModSounds.DYNAMIC_FOREST_INSECTS, 8, 0.42F, 0.92F, 1.10F, 6.0D, 20.0D, 4.0D);
        } else if (containsAny(biomePath, "swamp", "mangrove")) {
            add(candidates, ModSounds.DYNAMIC_SWAMP_FROGS, night ? 12 : 7, 0.52F, 0.88F, 1.08F, 6.0D, 22.0D, 4.0D);
            add(candidates, ModSounds.DYNAMIC_SWAMP_INSECTS, 9, 0.42F, 0.92F, 1.10F, 6.0D, 20.0D, 4.0D);
        } else if (containsAny(biomePath, "mountain", "peak", "slope", "stony", "windswept")) {
            add(candidates, ModSounds.DYNAMIC_MOUNTAIN_WIND, 12, 0.62F, 0.86F, 1.04F, 10.0D, 30.0D, 8.0D);
            add(candidates, ModSounds.DYNAMIC_MOUNTAIN_EAGLE, 4, 0.54F, 0.88F, 1.06F, 18.0D, 40.0D, 12.0D);
        } else if (containsAny(biomePath, "snow", "frozen", "ice", "cold")) {
            add(candidates, ModSounds.DYNAMIC_SNOW_WIND, 12, 0.64F, 0.84F, 1.02F, 10.0D, 30.0D, 6.0D);
            add(candidates, ModSounds.DYNAMIC_TUNDRA_WIND, 7, 0.54F, 0.86F, 1.04F, 10.0D, 30.0D, 6.0D);
        } else if (containsAny(biomePath, "desert")) {
            add(candidates, ModSounds.DYNAMIC_DESERT_WIND, 12, 0.58F, 0.86F, 1.04F, 10.0D, 30.0D, 6.0D);
        } else if (containsAny(biomePath, "badlands", "mesa")) {
            add(candidates, ModSounds.DYNAMIC_BADLANDS_WIND, 12, 0.58F, 0.86F, 1.04F, 10.0D, 30.0D, 6.0D);
        } else {
            add(candidates, ModSounds.DYNAMIC_PLAINS_BIRDS, night ? 3 : 8, 0.48F, 0.90F, 1.10F, 8.0D, 26.0D, 6.0D);
            add(candidates, ModSounds.DYNAMIC_PLAINS_WIND, 5, 0.42F, 0.88F, 1.06F, 10.0D, 28.0D, 5.0D);
        }

        return candidates;
    }

    private static AmbientCandidate chooseAvailableCandidate(Minecraft minecraft, List<AmbientCandidate> candidates, RandomSource random) {
        candidates.removeIf(candidate -> !soundAssetExists(minecraft, candidate.sound()));
        if (candidates.isEmpty()) {
            return null;
        }

        int totalWeight = 0;
        for (AmbientCandidate candidate : candidates) {
            totalWeight += candidate.weight();
        }
        int roll = random.nextInt(Math.max(1, totalWeight));
        for (AmbientCandidate candidate : candidates) {
            roll -= candidate.weight();
            if (roll < 0) {
                return candidate;
            }
        }
        return candidates.get(candidates.size() - 1);
    }

    private static boolean soundAssetExists(Minecraft minecraft, RegistryObject<SoundEvent> sound) {
        ResourceLocation soundId = sound.getId();
        if (soundId == null) {
            return false;
        }
        ResourceLocation audioFile = new ResourceLocation(soundId.getNamespace(), "sounds/" + soundId.getPath().replace('.', '/') + ".ogg");
        return minecraft.getResourceManager().getResource(audioFile).isPresent();
    }

    private static void playCandidate(Minecraft minecraft, AmbientCandidate candidate, RandomSource random) {
        double angle = random.nextDouble() * Math.PI * 2.0D;
        double distance = candidate.minDistance() + random.nextDouble() * Math.max(0.0D, candidate.maxDistance() - candidate.minDistance());
        double x = minecraft.player.getX() + Math.cos(angle) * distance;
        double y = minecraft.player.getY() + (random.nextDouble() * 2.0D - 1.0D) * candidate.yVariance();
        double z = minecraft.player.getZ() + Math.sin(angle) * distance;
        float volume = (float) Mth.clamp(candidate.volume() * Config.DYNAMIC_AMBIENT_SOUND_VOLUME.get(), 0.0D, 4.0D);
        float pitch = candidate.pitchMin() + random.nextFloat() * Math.max(0.0F, candidate.pitchMax() - candidate.pitchMin());
        minecraft.level.playLocalSound(x, y, z, candidate.sound().get(), SoundSource.AMBIENT, volume, pitch, false);
    }

    private static void add(List<AmbientCandidate> candidates, RegistryObject<SoundEvent> sound, int weight, float volume,
                            float pitchMin, float pitchMax, double minDistance, double maxDistance, double yVariance) {
        candidates.add(new AmbientCandidate(sound, Math.max(1, weight), volume, pitchMin, pitchMax, minDistance, maxDistance, yVariance));
    }

    private static boolean scanWaterNearby(ClientLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = -WATER_SCAN_HORIZONTAL_RADIUS; x <= WATER_SCAN_HORIZONTAL_RADIUS; x++) {
            for (int y = -WATER_SCAN_VERTICAL_RADIUS; y <= WATER_SCAN_VERTICAL_RADIUS; y++) {
                for (int z = -WATER_SCAN_HORIZONTAL_RADIUS; z <= WATER_SCAN_HORIZONTAL_RADIUS; z++) {
                    mutable.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    FluidState fluidState = level.getFluidState(mutable);
                    if (!fluidState.isEmpty() && fluidState.is(FluidTags.WATER)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String biomePath(ClientLevel level, BlockPos origin) {
        return level.getBiome(origin)
                .unwrapKey()
                .map(key -> key.location().getPath())
                .orElse("");
    }

    private static boolean isNight(ClientLevel level) {
        long dayTime = level.getDayTime() % 24000L;
        return dayTime >= 13000L && dayTime <= 23000L;
    }

    private static boolean containsAny(String value, String... needles) {
        for (String needle : needles) {
            if (value.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    private record AmbientCandidate(RegistryObject<SoundEvent> sound, int weight, float volume, float pitchMin,
                                    float pitchMax, double minDistance, double maxDistance, double yVariance) {
    }
}
