package net.apotheoticstudios.thuumcraft.stealth;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.network.ClientboundSneakAwarenessPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SneakAwarenessEvents {
    private static final int UPDATE_INTERVAL_TICKS = 15;
    private static final int STALE_OBSERVER_TICKS = 120;
    private static final int FORCE_SYNC_TICKS = 20;
    private static final double MAX_SCAN_RANGE = 36.0D;
    private static final double FACING_DOT_THRESHOLD = 0.35D;
    private static final Map<UUID, PlayerAwarenessData> PLAYER_AWARENESS = new HashMap<>();

    private SneakAwarenessEvents() {
    }

    @SubscribeEvent
    public static void tickPlayerAwareness(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        PlayerAwarenessData data = PLAYER_AWARENESS.computeIfAbsent(player.getUUID(), uuid -> new PlayerAwarenessData());
        if (player.tickCount % UPDATE_INTERVAL_TICKS != 0) {
            return;
        }

        if (player.isSpectator() || player.isCreative() || !player.isAlive()) {
            data.clear(player);
            return;
        }

        updatePlayerAwareness(player, data);
    }

    @SubscribeEvent
    public static void recordPlayerSound(PlayLevelSoundEvent.AtEntity event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player) || event.getLevel().isClientSide()) {
            return;
        }

        float noise = event.getOriginalVolume();
        if (event.getSource() == SoundSource.PLAYERS) {
            noise *= 1.35F;
        }
        if (noise <= 0.0F) {
            return;
        }

        PLAYER_AWARENESS.computeIfAbsent(player.getUUID(), uuid -> new PlayerAwarenessData())
                .recordNoise(Mth.clamp(noise, 0.0F, 2.0F));
    }

    @SubscribeEvent
    public static void clearPlayer(PlayerEvent.PlayerLoggedOutEvent event) {
        PLAYER_AWARENESS.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void clearServer(ServerStoppingEvent event) {
        PLAYER_AWARENESS.clear();
    }

    private static void updatePlayerAwareness(ServerPlayer player, PlayerAwarenessData data) {
        int tick = player.tickCount;
        data.decayRecentNoise();

        ServerLevel level = player.serverLevel();
        AABB scanArea = player.getBoundingBox().inflate(MAX_SCAN_RANGE);
        for (Mob observer : level.getEntitiesOfClass(Mob.class, scanArea, observer -> canObserve(observer, player))) {
            ObserverAwareness observerAwareness = data.observer(observer.getId());
            observerAwareness.lastCheckedTick = tick;

            if (observer.getTarget() == player || observer.getLastHurtByMob() == player) {
                observerAwareness.progress = 1.0F;
                observerAwareness.lastSensedTick = tick;
                continue;
            }

            double signal = getDetectionSignal(player, observer, data.recentNoise);
            if (signal > 0.0D) {
                observerAwareness.lastSensedTick = tick;
                observerAwareness.progress += (float) (signal * 0.17D);
            } else {
                observerAwareness.progress -= 0.04F;
            }
            observerAwareness.progress = Mth.clamp(observerAwareness.progress, 0.0F, 1.0F);
        }

        data.pruneObservers(tick);
        data.sync(player, tick);
    }

    private static boolean canObserve(Mob observer, Player player) {
        return observer.isAlive()
                && !observer.isSpectator()
                && observer.getId() != player.getId()
                && (observer instanceof Enemy || observer.getTarget() == player || observer.getLastHurtByMob() == player);
    }

    private static double getDetectionSignal(ServerPlayer player, Mob observer, float recentNoise) {
        double followRange = Math.max(16.0D, observer.getAttributeValue(Attributes.FOLLOW_RANGE));
        double scanRange = Math.min(MAX_SCAN_RANGE, followRange);
        double distanceSqr = observer.distanceToSqr(player);
        if (distanceSqr > scanRange * scanRange) {
            return -0.2D;
        }

        double distance = Math.sqrt(distanceSqr);
        double distanceScore = 1.0D - Mth.clamp(distance / scanRange, 0.0D, 1.0D);
        boolean hasLineOfSight = observer.hasLineOfSight(player);
        double visualSignal = hasLineOfSight ? getVisualSignal(player, observer, distanceScore) : 0.0D;
        double soundSignal = getSoundSignal(player, distance, scanRange, hasLineOfSight, recentNoise);
        double signal = visualSignal + soundSignal;

        if (player.isCrouching() || player.isShiftKeyDown()) {
            signal *= 0.78D;
        } else {
            signal *= 1.25D;
        }

        signal *= getSneakAttributeMultiplier(player);

        if (player.hasEffect(MobEffects.GLOWING) || player.getRemainingFireTicks() > 0) {
            signal = Math.max(signal, 0.86D);
        }
        if (observer.hasEffect(MobEffects.BLINDNESS)) {
            signal *= 0.12D;
        }
        if (!hasLineOfSight && soundSignal <= 0.04D) {
            signal = -0.08D;
        }

        return Mth.clamp(signal, -0.2D, 1.0D);
    }

    private static double getVisualSignal(ServerPlayer player, Mob observer, double distanceScore) {
        double facingScore = getFacingScore(observer, player);
        if (facingScore <= 0.0D && observer.distanceToSqr(player) > 9.0D) {
            return 0.0D;
        }

        int playerLight = getActualLightLevel(player.level(), player.blockPosition());
        int observerLight = getActualLightLevel(observer.level(), observer.blockPosition());
        double flatLight = playerLight / 15.0D;
        double lightAdvantage = Mth.clamp((playerLight - observerLight + 8.0D) / 16.0D, 0.0D, 1.0D);
        double visibilityPercent = player.getVisibilityPercent(observer);

        if (player.hasEffect(MobEffects.INVISIBILITY) || player.isInvisible()) {
            visibilityPercent *= 0.25D;
        }

        return distanceScore * (0.2D + facingScore * 0.8D)
                * (0.25D + flatLight * 0.55D + lightAdvantage * 0.2D)
                * visibilityPercent;
    }

    private static double getSoundSignal(ServerPlayer player, double distance, double scanRange, boolean hasLineOfSight, float recentNoise) {
        double movementSpeed = Math.sqrt(player.getDeltaMovement().horizontalDistanceSqr());
        double movementNoise = movementSpeed * 5.0D;
        if (player.isSprinting()) {
            movementNoise += 0.55D;
        }
        if (player.isCrouching() || player.isShiftKeyDown()) {
            movementNoise *= 0.45D;
        }

        double armorNoise = player.getArmorValue() / 40.0D;
        double noise = Mth.clamp(movementNoise + armorNoise + recentNoise * 0.55D, 0.0D, 1.2D);
        if (noise <= 0.0D) {
            return 0.0D;
        }

        double soundRange = Math.max(8.0D, scanRange * 0.65D);
        double falloff = 1.0D - Mth.clamp(distance / soundRange, 0.0D, 1.0D);
        double lineOfSightMultiplier = hasLineOfSight ? 1.0D : 0.35D;
        return noise * falloff * lineOfSightMultiplier;
    }

    private static double getSneakAttributeMultiplier(ServerPlayer player) {
        double sneak = player.getAttributeValue(ModAttributes.SNEAK.get());
        double protection = Mth.clamp(sneak / 100.0D, -0.5D, 0.8D);
        return Mth.clamp(1.0D - protection, 0.2D, 1.5D);
    }

    private static double getFacingScore(LivingEntity watcher, Player player) {
        Vec3 toPlayer = player.getEyePosition().subtract(watcher.getEyePosition());
        if (toPlayer.lengthSqr() <= 0.0001D) {
            return 1.0D;
        }

        double dot = watcher.getLookAngle().normalize().dot(toPlayer.normalize());
        return Mth.clamp((dot - FACING_DOT_THRESHOLD) / (1.0D - FACING_DOT_THRESHOLD), 0.0D, 1.0D);
    }

    private static int getActualLightLevel(net.minecraft.world.level.Level level, BlockPos pos) {
        int skyLight = 0;
        if (level.dimensionType().hasSkyLight()) {
            skyLight = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        }

        return Mth.clamp(Math.max(level.getBrightness(LightLayer.BLOCK, pos), skyLight), 0, 15);
    }

    private static final class PlayerAwarenessData {
        private final Map<Integer, ObserverAwareness> observers = new HashMap<>();
        private SneakAwareness lastSentAwareness = SneakAwareness.HIDDEN;
        private float lastSentProgress;
        private int lastSentObserverId = -1;
        private int lastSyncTick;
        private float recentNoise;

        private ObserverAwareness observer(int id) {
            return observers.computeIfAbsent(id, observerId -> new ObserverAwareness());
        }

        private void recordNoise(float noise) {
            recentNoise = Math.max(recentNoise, noise);
        }

        private void decayRecentNoise() {
            recentNoise = Math.max(0.0F, recentNoise - 0.22F);
        }

        private void pruneObservers(int tick) {
            for (Iterator<Map.Entry<Integer, ObserverAwareness>> iterator = observers.entrySet().iterator(); iterator.hasNext(); ) {
                ObserverAwareness awareness = iterator.next().getValue();
                if (awareness.lastCheckedTick + STALE_OBSERVER_TICKS < tick) {
                    awareness.progress -= 0.08F;
                } else if (awareness.lastSensedTick + UPDATE_INTERVAL_TICKS < tick) {
                    awareness.progress -= 0.04F;
                }

                if (awareness.progress <= 0.0F) {
                    iterator.remove();
                } else {
                    awareness.progress = Mth.clamp(awareness.progress, 0.0F, 1.0F);
                }
            }
        }

        private void sync(ServerPlayer player, int tick) {
            float highestProgress = 0.0F;
            int observerId = -1;
            for (Map.Entry<Integer, ObserverAwareness> entry : observers.entrySet()) {
                if (entry.getValue().progress > highestProgress) {
                    highestProgress = entry.getValue().progress;
                    observerId = entry.getKey();
                }
            }

            SneakAwareness awareness = SneakAwareness.fromProgress(highestProgress);
            boolean shouldSync = awareness != lastSentAwareness
                    || observerId != lastSentObserverId
                    || Math.abs(highestProgress - lastSentProgress) >= 0.04F
                    || tick - lastSyncTick >= FORCE_SYNC_TICKS;
            if (!shouldSync) {
                return;
            }

            lastSentAwareness = awareness;
            lastSentProgress = highestProgress;
            lastSentObserverId = observerId;
            lastSyncTick = tick;
            ModMessages.sendToPlayer(new ClientboundSneakAwarenessPacket(awareness, highestProgress, observerId), player);
        }

        private void clear(ServerPlayer player) {
            observers.clear();
            recentNoise = 0.0F;
            if (lastSentAwareness != SneakAwareness.HIDDEN || lastSentProgress != 0.0F || lastSentObserverId != -1) {
                lastSentAwareness = SneakAwareness.HIDDEN;
                lastSentProgress = 0.0F;
                lastSentObserverId = -1;
                lastSyncTick = player.tickCount;
                ModMessages.sendToPlayer(new ClientboundSneakAwarenessPacket(SneakAwareness.HIDDEN, 0.0F, -1), player);
            }
        }
    }

    private static final class ObserverAwareness {
        private float progress;
        private int lastCheckedTick;
        private int lastSensedTick;
    }
}
