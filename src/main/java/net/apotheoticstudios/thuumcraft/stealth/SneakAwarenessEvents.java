package net.apotheoticstudios.thuumcraft.stealth;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.network.ClientboundSneakAwarenessPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SneakAwarenessEvents {
    private static final int UPDATE_INTERVAL_TICKS = 15;
    private static final int STALE_OBSERVER_TICKS = 120;
    private static final int FORCE_SYNC_TICKS = 20;
    private static final double MAX_SCAN_RANGE = 36.0D;
    private static final double FACING_DOT_THRESHOLD = 0.35D;
    private static final double SNEAK_ATTRIBUTE_CAP = 100.0D;
    private static final double MAX_SNEAK_DETECTION_REDUCTION = 0.8D;
    private static final double MAX_NEGATIVE_SNEAK_DETECTION_PENALTY = 0.5D;
    private static final double MIN_SNEAK_DETECTION_MULTIPLIER = 0.2D;
    private static final double MAX_SNEAK_DETECTION_MULTIPLIER = 1.5D;
    private static final double GUARANTEED_SNEAK_DETECTION_RANGE = 1.75D;
    private static final double MIN_SNEAK_TARGET_DETECTION_RANGE = 2.5D;
    private static final double SOUND_DISRUPTION_NOISE_THRESHOLD = 0.5D;
    private static final double MIN_SNEAK_ATTACK_CRIT_DAMAGE = 1.0D;
    private static final int SNEAK_ATTACK_CRIT_PARTICLES = 24;
    private static final int SNEAK_ATTACK_ENCHANTED_HIT_PARTICLES = 12;
    private static final float TARGET_ACQUISITION_PROGRESS = 0.85F;
    private static final Map<UUID, PlayerAwarenessData> PLAYER_AWARENESS = new HashMap<>();
    private static final Map<UUID, Integer> DISABLED_SYNC_TICKS = new HashMap<>();

    private SneakAwarenessEvents() {
    }

    @SubscribeEvent
    public static void tickPlayerAwareness(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        if (!isStealthSystemEnabled()) {
            disableForPlayer(player);
            return;
        }

        UUID playerId = player.getUUID();
        DISABLED_SYNC_TICKS.remove(playerId);

        PlayerAwarenessData data = PLAYER_AWARENESS.get(playerId);
        boolean undetectable = isUndetectable(player);
        if (player.isSpectator() || player.isCreative() || !player.isAlive() || undetectable) {
            if (undetectable) {
                clearNearbyObserverTargets(player);
            }
            clearAwareness(player, data);
            return;
        }

        if (!isTryingToSneak(player)) {
            clearAwareness(player, data);
            return;
        }

        if (player.tickCount % UPDATE_INTERVAL_TICKS != 0) {
            return;
        }

        data = PLAYER_AWARENESS.computeIfAbsent(playerId, uuid -> new PlayerAwarenessData());
        updatePlayerAwareness(player, data);
    }

    @SubscribeEvent
    public static void recordPlayerSound(PlayLevelSoundEvent.AtEntity event) {
        Entity entity = event.getEntity();
        if (!isStealthSystemEnabled()
                || !(entity instanceof ServerPlayer player)
                || event.getLevel().isClientSide()
                || !isTryingToSneak(player)
                || isUndetectable(player)) {
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
    public static void preventUndetectedPlayerTargeting(LivingChangeTargetEvent event) {
        if (!isStealthSystemEnabled()) {
            return;
        }

        if (!(event.getEntity() instanceof Mob observer) || !(event.getNewTarget() instanceof ServerPlayer player)) {
            return;
        }

        if (isUndetectable(player) || shouldSuppressStealthTarget(observer, player)) {
            event.setNewTarget(null);
        }
    }

    @SubscribeEvent
    public static void applySneakAttackDamage(LivingHurtEvent event) {
        if (!isStealthSystemEnabled() || event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof Mob observer)) {
            return;
        }

        SneakAttackType sneakAttackType = getSneakAttackType(event.getSource());
        if (sneakAttackType == SneakAttackType.NONE || !(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!canSneakAttack(player, observer)) {
            return;
        }

        double critDamage = getSneakAttackCritDamage(player, sneakAttackType);
        event.setAmount((float) (event.getAmount() * critDamage));
        if (critDamage > MIN_SNEAK_ATTACK_CRIT_DAMAGE) {
            spawnSneakAttackCritParticles(observer, sneakAttackType);
        }
    }

    @SubscribeEvent
    public static void clearPlayer(PlayerEvent.PlayerLoggedOutEvent event) {
        PLAYER_AWARENESS.remove(event.getEntity().getUUID());
        DISABLED_SYNC_TICKS.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void clearServer(ServerStoppingEvent event) {
        PLAYER_AWARENESS.clear();
        DISABLED_SYNC_TICKS.clear();
    }

    private static boolean isStealthSystemEnabled() {
        return Config.ENABLE_STEALTH_SYSTEM.get();
    }

    private static void disableForPlayer(ServerPlayer player) {
        UUID playerId = player.getUUID();
        PlayerAwarenessData data = PLAYER_AWARENESS.remove(playerId);
        if (data != null) {
            data.clear(player);
        }

        Integer lastSyncTick = DISABLED_SYNC_TICKS.get(playerId);
        if (lastSyncTick == null || player.tickCount - lastSyncTick >= FORCE_SYNC_TICKS) {
            DISABLED_SYNC_TICKS.put(playerId, player.tickCount);
            ModMessages.sendToPlayer(new ClientboundSneakAwarenessPacket(SneakAwareness.DISABLED, 0.0F, -1), player);
        }
    }

    private static void clearAwareness(ServerPlayer player, PlayerAwarenessData data) {
        if (data != null) {
            data.clear(player);
            PLAYER_AWARENESS.remove(player.getUUID());
        }
    }

    private static void updatePlayerAwareness(ServerPlayer player, PlayerAwarenessData data) {
        int tick = player.tickCount;
        data.decayRecentNoise();

        ServerLevel level = player.serverLevel();
        AABB scanArea = player.getBoundingBox().inflate(MAX_SCAN_RANGE);
        List<Mob> observers = level.getEntitiesOfClass(Mob.class, scanArea, observer -> canObserve(observer, player));
        if (!observers.isEmpty()) {
            double normalizedSneak = getNormalizedSneak(player);
            double playerNoise = getSneakingPlayerNoise(player, data.recentNoise);
            int playerLight = getActualLightLevel(player.level(), player.blockPosition());

            for (Mob observer : observers) {
                ObserverAwareness observerAwareness = data.observer(observer.getId());
                observerAwareness.lastCheckedTick = tick;

                if (observer.getTarget() == player || observer.getLastHurtByMob() == player) {
                    observerAwareness.progress = 1.0F;
                    observerAwareness.lastSensedTick = tick;
                    continue;
                }

                double distanceSqr = observer.distanceToSqr(player);
                if (canDetectSneakingTarget(observer, player, distanceSqr, playerNoise, normalizedSneak, playerLight)) {
                    observerAwareness.progress = Math.max(observerAwareness.progress, TARGET_ACQUISITION_PROGRESS);
                    observerAwareness.lastSensedTick = tick;
                    continue;
                }

                double signal = getDetectionSignal(player, observer, distanceSqr, playerNoise, normalizedSneak, playerLight);
                if (signal > 0.0D) {
                    observerAwareness.lastSensedTick = tick;
                    observerAwareness.progress += (float) (signal * 0.17D);
                } else {
                    observerAwareness.progress -= 0.04F;
                }
                observerAwareness.progress = Mth.clamp(observerAwareness.progress, 0.0F, 1.0F);
            }
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

    private static boolean shouldSuppressStealthTarget(Mob observer, ServerPlayer player) {
        if (player.isSpectator() || player.isCreative() || !player.isAlive() || !isTryingToSneak(player)) {
            return false;
        }
        if (observer.getTarget() == player || observer.getLastHurtByMob() == player) {
            return false;
        }
        if (!canObserve(observer, player)) {
            return false;
        }

        return !canDetectSneakingTarget(observer, player);
    }

    private static boolean canSneakAttack(ServerPlayer player, Mob observer) {
        if (player.isSpectator() || player.isCreative() || !player.isAlive() || !isTryingToSneak(player)) {
            return false;
        }
        if (!observer.isAlive() || observer.isSpectator() || observer.getId() == player.getId()) {
            return false;
        }
        if (observer.getTarget() == player || observer.getLastHurtByMob() == player) {
            return false;
        }

        PlayerAwarenessData data = PLAYER_AWARENESS.get(player.getUUID());
        if (data != null && data.progressFor(observer.getId()) >= TARGET_ACQUISITION_PROGRESS) {
            return false;
        }

        return isUndetectable(player) || !canDetectSneakingTarget(observer, player);
    }

    private static SneakAttackType getSneakAttackType(DamageSource source) {
        Entity directEntity = source.getDirectEntity();
        Entity causingEntity = source.getEntity();
        if (!(causingEntity instanceof ServerPlayer)) {
            return SneakAttackType.NONE;
        }
        if (source.is(DamageTypeTags.IS_PROJECTILE) && directEntity != causingEntity) {
            return SneakAttackType.RANGED;
        }
        if (directEntity == causingEntity) {
            return SneakAttackType.MELEE;
        }
        return SneakAttackType.NONE;
    }

    private static double getSneakAttackCritDamage(ServerPlayer player, SneakAttackType sneakAttackType) {
        double critDamage = switch (sneakAttackType) {
            case MELEE -> player.getAttributeValue(ModAttributes.MELEE_SNEAK_ATTACK_CRIT_DAMAGE.get());
            case RANGED -> player.getAttributeValue(ModAttributes.RANGED_SNEAK_ATTACK_CRIT_DAMAGE.get());
            case NONE -> MIN_SNEAK_ATTACK_CRIT_DAMAGE;
        };
        return Math.max(MIN_SNEAK_ATTACK_CRIT_DAMAGE, critDamage);
    }

    private static void spawnSneakAttackCritParticles(Mob observer, SneakAttackType sneakAttackType) {
        if (!(observer.level() instanceof ServerLevel level)) {
            return;
        }

        double x = observer.getX();
        double y = observer.getY() + observer.getBbHeight() * 0.55D;
        double z = observer.getZ();
        double horizontalSpread = Math.max(0.25D, observer.getBbWidth() * 0.45D);
        double verticalSpread = Math.max(0.35D, observer.getBbHeight() * 0.25D);

        level.sendParticles(ParticleTypes.CRIT, x, y, z, SNEAK_ATTACK_CRIT_PARTICLES,
                horizontalSpread, verticalSpread, horizontalSpread, 0.25D);
        level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, SNEAK_ATTACK_ENCHANTED_HIT_PARTICLES,
                horizontalSpread, verticalSpread, horizontalSpread, 0.18D);
        if (sneakAttackType == SneakAttackType.MELEE) {
            level.sendParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 1,
                    horizontalSpread * 0.25D, verticalSpread * 0.1D, horizontalSpread * 0.25D, 0.0D);
        }
    }

    private static double getDetectionSignal(ServerPlayer player, Mob observer, double distanceSqr, double playerNoise,
                                             double normalizedSneak, int playerLight) {
        if (isUndetectable(player)) {
            return -0.2D;
        }

        double scanRange = getObserverScanRange(observer);
        if (distanceSqr > scanRange * scanRange) {
            return -0.2D;
        }

        double distance = Math.sqrt(distanceSqr);
        double distanceScore = 1.0D - Mth.clamp(distance / scanRange, 0.0D, 1.0D);
        boolean hasLineOfSight = observer.hasLineOfSight(player);
        double visualSignal = hasLineOfSight ? getVisualSignal(player, observer, distanceScore, distanceSqr, playerLight) : 0.0D;
        double soundSignal = getSoundSignal(distance, scanRange, hasLineOfSight, playerNoise);
        double signal = visualSignal + soundSignal;

        signal *= 0.78D;
        signal *= getSneakAttributeDetectionMultiplier(normalizedSneak);

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

    private static double getVisualSignal(ServerPlayer player, Mob observer, double distanceScore, double distanceSqr,
                                          int playerLight) {
        double facingScore = getFacingScore(observer, player);
        if (facingScore <= 0.0D && distanceSqr > 9.0D) {
            return 0.0D;
        }

        int observerLight = getActualLightLevel(observer.level(), observer.blockPosition());
        double flatLight = playerLight / 15.0D;
        double lightAdvantage = Mth.clamp((playerLight - observerLight + 8.0D) / 16.0D, 0.0D, 1.0D);
        double visibilityPercent = player.getVisibilityPercent(observer);

        return distanceScore * (0.2D + facingScore * 0.8D)
                * (0.25D + flatLight * 0.55D + lightAdvantage * 0.2D)
                * visibilityPercent;
    }

    private static boolean isUndetectable(ServerPlayer player) {
        return player.hasEffect(MobEffects.INVISIBILITY) || player.isInvisible();
    }

    private static void clearNearbyObserverTargets(ServerPlayer player) {
        AABB scanArea = player.getBoundingBox().inflate(MAX_SCAN_RANGE);
        for (Mob observer : player.serverLevel().getEntitiesOfClass(Mob.class, scanArea,
                observer -> observer.isAlive() && observer.getTarget() == player)) {
            observer.setTarget(null);
        }
    }

    private static double getSoundSignal(double distance, double scanRange, boolean hasLineOfSight, double noise) {
        if (noise <= 0.0D) {
            return 0.0D;
        }

        double soundRange = Math.max(8.0D, scanRange * 0.65D);
        double falloff = 1.0D - Mth.clamp(distance / soundRange, 0.0D, 1.0D);
        double lineOfSightMultiplier = hasLineOfSight ? 1.0D : 0.35D;
        return noise * falloff * lineOfSightMultiplier;
    }

    private static double getSneakAttributeDetectionMultiplier(double normalizedSneak) {
        double multiplier = normalizedSneak >= 0.0D
                ? 1.0D - normalizedSneak * MAX_SNEAK_DETECTION_REDUCTION
                : 1.0D + -normalizedSneak * MAX_NEGATIVE_SNEAK_DETECTION_PENALTY;
        return Mth.clamp(multiplier, MIN_SNEAK_DETECTION_MULTIPLIER, MAX_SNEAK_DETECTION_MULTIPLIER);
    }

    private static boolean canDetectSneakingTarget(Mob observer, ServerPlayer player) {
        double distanceSqr = observer.distanceToSqr(player);
        if (isImmediatelyDetectable(player, distanceSqr)) {
            return true;
        }

        float recentNoise = getRecentNoise(player);
        return canDetectSneakingTarget(observer, player, distanceSqr, getSneakingPlayerNoise(player, recentNoise),
                getNormalizedSneak(player), getActualLightLevel(player.level(), player.blockPosition()));
    }

    private static boolean canDetectSneakingTarget(Mob observer, ServerPlayer player, double distanceSqr,
                                                   double playerNoise, double normalizedSneak, int playerLight) {
        if (isImmediatelyDetectable(player, distanceSqr)) {
            return true;
        }

        boolean hasLineOfSight = observer.hasLineOfSight(player);
        double distance = Math.sqrt(distanceSqr);
        double scanRange = getObserverScanRange(observer);
        if (isSoundDisrupted(playerNoise, distanceSqr, scanRange)) {
            return true;
        }

        if (!hasLineOfSight) {
            double soundRange = Math.max(GUARANTEED_SNEAK_DETECTION_RANGE, scanRange * 0.35D * playerNoise);
            return playerNoise > 0.15D && distance <= soundRange;
        }

        double facingScore = getFacingScore(observer, player);
        if (facingScore <= 0.0D && distance > MIN_SNEAK_TARGET_DETECTION_RANGE) {
            return false;
        }

        double targetRange = getSneakTargetDetectionRange(scanRange, normalizedSneak);
        double lightMultiplier = 0.65D + playerLight / 15.0D * 0.7D;
        double facingMultiplier = 0.45D + facingScore * 0.75D;
        double noiseMultiplier = 1.0D + playerNoise * 0.65D;
        targetRange *= lightMultiplier * facingMultiplier * noiseMultiplier;
        if (observer.hasEffect(MobEffects.BLINDNESS)) {
            targetRange *= 0.12D;
        }

        return distance <= Math.max(GUARANTEED_SNEAK_DETECTION_RANGE, targetRange);
    }

    private static boolean isSoundDisrupted(double playerNoise, double distanceSqr, double scanRange) {
        return playerNoise >= SOUND_DISRUPTION_NOISE_THRESHOLD && distanceSqr <= scanRange * scanRange;
    }

    private static boolean isImmediatelyDetectable(ServerPlayer player, double distanceSqr) {
        return distanceSqr <= GUARANTEED_SNEAK_DETECTION_RANGE * GUARANTEED_SNEAK_DETECTION_RANGE
                || player.hasEffect(MobEffects.GLOWING)
                || player.getRemainingFireTicks() > 0;
    }

    private static double getSneakTargetDetectionRange(double scanRange, double normalizedSneak) {
        double rangeMultiplier = normalizedSneak >= 0.0D
                ? 0.9D - normalizedSneak * 0.78D
                : 0.9D + -normalizedSneak * 0.3D;
        return Math.max(MIN_SNEAK_TARGET_DETECTION_RANGE,
                scanRange * Mth.clamp(rangeMultiplier, 0.12D, 1.2D));
    }

    private static double getObserverScanRange(Mob observer) {
        return Math.min(MAX_SCAN_RANGE, Math.max(16.0D, observer.getAttributeValue(Attributes.FOLLOW_RANGE)));
    }

    private static double getNormalizedSneak(ServerPlayer player) {
        AttributeInstance sneakAttribute = player.getAttribute(ModAttributes.SNEAK.get());
        double sneak = sneakAttribute == null ? 0.0D : sneakAttribute.getValue();
        return Mth.clamp(sneak / SNEAK_ATTRIBUTE_CAP, -1.0D, 1.0D);
    }

    private static boolean isTryingToSneak(Player player) {
        return player.isCrouching() || player.isShiftKeyDown();
    }

    private static float getRecentNoise(ServerPlayer player) {
        PlayerAwarenessData data = PLAYER_AWARENESS.get(player.getUUID());
        return data == null ? 0.0F : data.recentNoise;
    }

    private static double getSneakingPlayerNoise(ServerPlayer player, float recentNoise) {
        double movementSpeed = Math.sqrt(player.getDeltaMovement().horizontalDistanceSqr());
        double movementNoise = movementSpeed * 5.0D;
        if (player.isSprinting()) {
            movementNoise += 0.55D;
        }
        movementNoise *= 0.45D;

        double armorNoise = player.getArmorValue() / 40.0D;
        return Mth.clamp(movementNoise + armorNoise + recentNoise * 0.55D, 0.0D, 1.2D);
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
            ObserverAwareness awareness = observers.get(id);
            if (awareness == null) {
                awareness = new ObserverAwareness();
                observers.put(id, awareness);
            }
            return awareness;
        }

        private float progressFor(int id) {
            ObserverAwareness awareness = observers.get(id);
            return awareness == null ? 0.0F : awareness.progress;
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

    private enum SneakAttackType {
        NONE,
        MELEE,
        RANGED
    }
}
