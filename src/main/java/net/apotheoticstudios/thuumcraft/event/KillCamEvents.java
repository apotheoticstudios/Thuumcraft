package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ClientboundKillCamPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class KillCamEvents {
    private static final Map<UUID, Integer> LAST_KILL_CAM_TICK = new HashMap<>();
    private static final Map<UUID, Integer> ACTION_LOCKED_UNTIL_TICK = new HashMap<>();
    private static final Map<UUID, ProjectileTrace> PROJECTILE_TRACES = new HashMap<>();
    private static final int ACTION_LOCK_GRACE_TICKS = 5;
    private static final long PROJECTILE_TRACE_TTL_TICKS = 20L * 20L;

    private KillCamEvents() {
    }

    @SubscribeEvent
    public static void trackProjectile(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()
                || event.loadedFromDisk()
                || !(event.getEntity() instanceof Projectile projectile)
                || !(projectile.getOwner() instanceof ServerPlayer owner)) {
            return;
        }

        long gameTime = event.getLevel().getGameTime();
        pruneProjectileTraces(gameTime);
        PROJECTILE_TRACES.put(projectile.getUUID(), new ProjectileTrace(owner.getUUID(),
                projectile.position(), gameTime));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void triggerPredictedKillCam(LivingDamageEvent event) {
        if (!Config.ENABLE_KILL_CAM.get() || event.getEntity().level().isClientSide()) {
            return;
        }

        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        double chance = Config.KILL_CAM_CHANCE.get();
        pruneProjectileTraces(target.level().getGameTime());
        if (chance <= 0.0D
                || event.getAmount() < target.getHealth()
                || !(source.getEntity() instanceof ServerPlayer player)
                || target == player
                || player.isCreative()
                || player.isSpectator()
                || !player.isAlive()
                || !isValidTarget(target)
                || !isWithinDistance(player, target)
                || isOnCooldown(player)
                || hasOtherActiveThreat(player, target)
                || player.getRandom().nextDouble() >= chance) {
            return;
        }

        int durationTicks = Config.KILL_CAM_DURATION_TICKS.get();
        LAST_KILL_CAM_TICK.put(player.getUUID(), player.tickCount);
        ACTION_LOCKED_UNTIL_TICK.put(player.getUUID(), player.tickCount + durationTicks + ACTION_LOCK_GRACE_TICKS);

        Entity directEntity = source.getDirectEntity();
        boolean ranged = source.is(DamageTypeTags.IS_PROJECTILE) || directEntity instanceof Projectile;
        ProjectileTrace projectileTrace = directEntity instanceof Projectile projectile
                ? PROJECTILE_TRACES.get(projectile.getUUID())
                : null;
        if (directEntity instanceof Projectile projectile) {
            PROJECTILE_TRACES.remove(projectile.getUUID());
        }
        Vec3 attackerPosition = player.position();
        Vec3 projectileStart = projectileTrace != null && projectileTrace.owner().equals(player.getUUID())
                ? projectileTrace.startPosition()
                : player.getEyePosition();
        int projectileId = ranged && directEntity != null ? directEntity.getId() : -1;

        Vec3 targetPosition = target.position();
        ModMessages.sendToPlayer(new ClientboundKillCamPacket(
                target.getId(),
                targetPosition.x,
                targetPosition.y,
                targetPosition.z,
                target.getBbWidth(),
                target.getBbHeight(),
                attackerPosition.x,
                attackerPosition.y,
                attackerPosition.z,
                projectileId,
                projectileStart.x,
                projectileStart.y,
                projectileStart.z,
                ranged,
                durationTicks,
                Config.KILL_CAM_FOV.get()), player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void blockLockedAttacks(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide() || !isActionLocked(event.getEntity())) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void blockLockedInteractions(PlayerInteractEvent event) {
        if (event.getLevel().isClientSide() || !isActionLocked(event.getEntity()) || !event.isCancelable()) {
            return;
        }
        event.setCancellationResult(InteractionResult.FAIL);
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void blockLockedArrowRelease(ArrowLooseEvent event) {
        if (event.getEntity().level().isClientSide() || !isActionLocked(event.getEntity())) {
            return;
        }
        event.setCharge(-1);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void tickActionLock(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (!isActionLocked(player)) {
            return;
        }

        player.setSprinting(false);
        if (player.isUsingItem()) {
            player.stopUsingItem();
        }
    }

    @SubscribeEvent
    public static void clearPlayer(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerId = event.getEntity().getUUID();
        LAST_KILL_CAM_TICK.remove(playerId);
        ACTION_LOCKED_UNTIL_TICK.remove(playerId);
        PROJECTILE_TRACES.entrySet().removeIf(entry -> entry.getValue().owner().equals(playerId));
    }

    @SubscribeEvent
    public static void clearServer(ServerStoppingEvent event) {
        LAST_KILL_CAM_TICK.clear();
        ACTION_LOCKED_UNTIL_TICK.clear();
        PROJECTILE_TRACES.clear();
    }

    private static boolean isValidTarget(LivingEntity target) {
        return !target.isSpectator()
                && (!Config.KILL_CAM_HOSTILE_ONLY.get() || target instanceof Enemy);
    }

    private static boolean isWithinDistance(ServerPlayer player, LivingEntity target) {
        double maxDistance = Config.KILL_CAM_MAX_DISTANCE.get();
        return player.distanceToSqr(target) <= maxDistance * maxDistance;
    }

    private static boolean isOnCooldown(ServerPlayer player) {
        int cooldownTicks = Config.KILL_CAM_COOLDOWN_TICKS.get();
        if (cooldownTicks <= 0) {
            return false;
        }

        Integer lastTick = LAST_KILL_CAM_TICK.get(player.getUUID());
        return lastTick != null && player.tickCount - lastTick < cooldownTicks;
    }

    private static boolean hasOtherActiveThreat(ServerPlayer player, LivingEntity killedTarget) {
        if (!Config.KILL_CAM_REQUIRE_LAST_THREAT.get()) {
            return false;
        }

        double radius = Config.KILL_CAM_THREAT_RADIUS.get();
        if (radius <= 0.0D) {
            return false;
        }

        AABB scanArea = player.getBoundingBox().inflate(radius);
        for (Mob mob : player.serverLevel().getEntitiesOfClass(Mob.class, scanArea,
                mob -> mob.isAlive() && mob != killedTarget && mob instanceof Enemy)) {
            Entity target = mob.getTarget();
            if (target == player || mob.getLastHurtByMob() == player) {
                return true;
            }
        }
        return false;
    }

    private static boolean isActionLocked(Player player) {
        UUID playerId = player.getUUID();
        Integer lockedUntilTick = ACTION_LOCKED_UNTIL_TICK.get(playerId);
        if (!Config.ENABLE_KILL_CAM.get() || lockedUntilTick == null) {
            ACTION_LOCKED_UNTIL_TICK.remove(playerId);
            return false;
        }
        if (player.tickCount > lockedUntilTick) {
            ACTION_LOCKED_UNTIL_TICK.remove(playerId);
            return false;
        }
        return true;
    }

    private static void pruneProjectileTraces(long currentGameTime) {
        Iterator<Map.Entry<UUID, ProjectileTrace>> iterator = PROJECTILE_TRACES.entrySet().iterator();
        while (iterator.hasNext()) {
            ProjectileTrace trace = iterator.next().getValue();
            if (currentGameTime - trace.spawnedGameTime() > PROJECTILE_TRACE_TTL_TICKS) {
                iterator.remove();
            }
        }
    }

    private record ProjectileTrace(UUID owner, Vec3 startPosition, long spawnedGameTime) {
    }
}
