package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ClientboundKillCamPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class KillCamEvents {
    private static final Map<UUID, Integer> LAST_KILL_CAM_TICK = new HashMap<>();

    private KillCamEvents() {
    }

    @SubscribeEvent
    public static void triggerKillCam(LivingDeathEvent event) {
        if (!Config.ENABLE_KILL_CAM.get() || event.getEntity().level().isClientSide()) {
            return;
        }

        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        double chance = Config.KILL_CAM_CHANCE.get();
        if (chance <= 0.0D
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

        LAST_KILL_CAM_TICK.put(player.getUUID(), player.tickCount);
        Vec3 attackerPosition = player.position();
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
                source.is(DamageTypeTags.IS_PROJECTILE),
                Config.KILL_CAM_DURATION_TICKS.get(),
                Config.KILL_CAM_FOV.get()), player);
    }

    @SubscribeEvent
    public static void clearPlayer(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_KILL_CAM_TICK.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void clearServer(ServerStoppingEvent event) {
        LAST_KILL_CAM_TICK.clear();
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
}
