package net.apotheoticstudios.thuumcraft.sound;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ClientboundStructureSoundContextPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class StructureSoundSyncEvents {
    private static final int SYNC_INTERVAL_TICKS = 40;
    private static final int FULL_RESYNC_INTERVAL_TICKS = 200;
    private static final int STRUCTURE_SCAN_CHUNK_RADIUS = 4;
    private static final int STRUCTURE_NEAR_PADDING_BLOCKS = 32;

    private static final Map<UUID, SyncedStructureContext> LAST_SYNCED = new HashMap<>();

    private StructureSoundSyncEvents() {
    }

    @SubscribeEvent
    public static void syncStructureContextOnTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % SYNC_INTERVAL_TICKS != 0) {
            return;
        }

        if (!Config.ENABLE_DYNAMIC_AMBIENT_SOUNDS.get()) {
            syncMaskIfNeeded(player, 0, false);
            return;
        }

        syncMaskIfNeeded(player, detectStructureMask(player), false);
    }

    @SubscribeEvent
    public static void syncStructureContextOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncNow(player, Config.ENABLE_DYNAMIC_AMBIENT_SOUNDS.get() ? detectStructureMask(player) : 0);
        }
    }

    @SubscribeEvent
    public static void syncStructureContextOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncNow(player, Config.ENABLE_DYNAMIC_AMBIENT_SOUNDS.get() ? detectStructureMask(player) : 0);
        }
    }

    @SubscribeEvent
    public static void syncStructureContextOnDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncNow(player, Config.ENABLE_DYNAMIC_AMBIENT_SOUNDS.get() ? detectStructureMask(player) : 0);
        }
    }

    @SubscribeEvent
    public static void clearStructureContextOnLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_SYNCED.remove(event.getEntity().getUUID());
    }

    private static void syncMaskIfNeeded(ServerPlayer player, int mask, boolean force) {
        SyncedStructureContext runtime = LAST_SYNCED.computeIfAbsent(player.getUUID(), ignored -> new SyncedStructureContext());
        runtime.ticksSinceFullSync += SYNC_INTERVAL_TICKS;
        if (!force && runtime.lastMask == mask && runtime.ticksSinceFullSync < FULL_RESYNC_INTERVAL_TICKS) {
            return;
        }

        runtime.lastMask = mask;
        runtime.ticksSinceFullSync = 0;
        ModMessages.sendToPlayer(new ClientboundStructureSoundContextPacket(mask), player);
    }

    private static void syncNow(ServerPlayer player, int mask) {
        syncMaskIfNeeded(player, mask, true);
    }

    private static int detectStructureMask(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos origin = player.blockPosition();
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        int originChunkX = SectionPos.blockToSectionCoord(origin.getX());
        int originChunkZ = SectionPos.blockToSectionCoord(origin.getZ());
        int mask = 0;
        Set<SeenStructureStart> seenStarts = new HashSet<>();

        for (int chunkX = originChunkX - STRUCTURE_SCAN_CHUNK_RADIUS; chunkX <= originChunkX + STRUCTURE_SCAN_CHUNK_RADIUS; chunkX++) {
            for (int chunkZ = originChunkZ - STRUCTURE_SCAN_CHUNK_RADIUS; chunkZ <= originChunkZ + STRUCTURE_SCAN_CHUNK_RADIUS; chunkZ++) {
                ChunkAccess referenceChunk = level.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_REFERENCES, false);
                if (referenceChunk == null) {
                    continue;
                }

                for (Map.Entry<Structure, LongSet> entry : referenceChunk.getAllReferences().entrySet()) {
                    Structure structure = entry.getKey();
                    ResourceLocation structureId = structureRegistry.getKey(structure);
                    if (structureId == null) {
                        continue;
                    }

                    int structureMask = StructureSoundClassifier.classify(structureId, structureRegistry.wrapAsHolder(structure));
                    for (long packedStartChunk : entry.getValue()) {
                        ChunkPos startChunkPos = new ChunkPos(packedStartChunk);
                        if (!seenStarts.add(new SeenStructureStart(structureId, startChunkPos.toLong()))) {
                            continue;
                        }

                        ChunkAccess startChunk = level.getChunk(startChunkPos.x, startChunkPos.z, ChunkStatus.STRUCTURE_STARTS, false);
                        if (startChunk == null) {
                            continue;
                        }

                        StructureStart start = startChunk.getStartForStructure(structure);
                        if (start != null
                                && start.isValid()
                                && start.getBoundingBox().inflatedBy(STRUCTURE_NEAR_PADDING_BLOCKS).isInside(origin)) {
                            mask |= structureMask;
                        }
                    }
                }
            }
        }

        return mask;
    }

    private static final class SyncedStructureContext {
        private int lastMask = -1;
        private int ticksSinceFullSync = FULL_RESYNC_INTERVAL_TICKS;
    }

    private record SeenStructureStart(ResourceLocation structureId, long startChunkPos) {
    }
}
