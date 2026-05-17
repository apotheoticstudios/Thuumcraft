package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class KillCamClientEvents {
    private static final double MIN_DIRECTION_LENGTH = 0.0001D;
    private static final double CAMERA_BLOCK_PADDING = 0.25D;
    private static KillCamState active;

    private KillCamClientEvents() {
    }

    public static void start(int targetId, double targetX, double targetY, double targetZ,
                             float targetWidth, float targetHeight,
                             double attackerX, double attackerY, double attackerZ,
                             int projectileId, double projectileStartX, double projectileStartY,
                             double projectileStartZ,
                             boolean ranged, int durationTicks, double fov) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!Config.ENABLE_KILL_CAM.get()
                || minecraft.level == null
                || minecraft.player == null
                || minecraft.player.isSpectator()) {
            return;
        }

        stop(true);

        Vec3 startPosition = minecraft.gameRenderer.getMainCamera().getPosition();
        ArmorStand cameraEntity = new ArmorStand(minecraft.level, startPosition.x, startPosition.y, startPosition.z);
        cameraEntity.setInvisible(true);
        cameraEntity.setNoGravity(true);
        setCameraPosition(cameraEntity, startPosition, false);

        float safeTargetHeight = Math.max(0.6F, targetHeight);
        Vec3 targetBase = new Vec3(targetX, targetY, targetZ);
        Vec3 targetCenter = targetBase.add(0.0D, safeTargetHeight * 0.58D, 0.0D);
        active = new KillCamState(
                cameraEntity,
                minecraft.getCameraEntity(),
                minecraft.options.getCameraType(),
                targetId,
                targetBase,
                Math.max(0.6F, targetWidth),
                safeTargetHeight,
                new Vec3(attackerX, attackerY, attackerZ),
                startPosition,
                targetCenter,
                projectileId,
                new Vec3(projectileStartX, projectileStartY, projectileStartZ),
                ranged,
                Math.max(1, durationTicks),
                Mth.clamp(fov, 10.0D, 120.0D));

        updateCamera(active, 0.0F);
        minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        minecraft.setCameraEntity(cameraEntity);
    }

    @SubscribeEvent
    public static void tickKillCam(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || active == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (!Config.ENABLE_KILL_CAM.get() || minecraft.level == null || minecraft.player == null) {
            stop(true);
            return;
        }

        suppressInputs(minecraft);
        active.ageTicks++;
        if (active.ageTicks > active.durationTicks) {
            stop(true);
            return;
        }

        updateCamera(active, 0.0F);
    }

    @SubscribeEvent
    public static void applyKillCamFov(ViewportEvent.ComputeFov event) {
        if (active == null) {
            return;
        }

        double progress = active.progress((float) event.getPartialTick());
        double blend = getTransitionBlend(progress);
        event.setFOV(Mth.lerp(blend, event.getFOV(), active.fov));
    }

    @SubscribeEvent
    public static void applyKillCamRoll(ViewportEvent.ComputeCameraAngles event) {
        if (active == null) {
            return;
        }

        double progress = active.progress((float) event.getPartialTick());
        float roll = (float) (Math.sin(progress * Math.PI) * (active.ranged ? 0.35D : 0.75D));
        event.setRoll(event.getRoll() + roll);
    }

    @SubscribeEvent
    public static void hideHud(RenderGuiOverlayEvent.Pre event) {
        if (active != null && Config.KILL_CAM_HIDE_HUD.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void hideHands(RenderHandEvent event) {
        if (active != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void blockInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (active != null) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void blockMouseButtons(InputEvent.MouseButton.Pre event) {
        if (active != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void blockMouseScrolling(InputEvent.MouseScrollingEvent event) {
        if (active != null) {
            event.setCanceled(true);
        }
    }

    private static void updateCamera(KillCamState state, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || state.cameraEntity.level() != minecraft.level) {
            return;
        }

        double progress = state.progress(partialTick);
        Vec3 targetCenter = getTargetCenter(minecraft, state);
        Vec3 desiredCameraPosition = state.ranged
                ? getRangedCameraPosition(minecraft, state, targetCenter, progress)
                : getMeleeCameraPosition(state, targetCenter, progress);
        desiredCameraPosition = avoidBlockClipping(minecraft, state.cameraEntity, targetCenter, desiredCameraPosition);
        double smoothing = state.ranged ? 0.48D : 0.36D;
        Vec3 cameraPosition = lerpVec(state.smoothedCameraPosition, desiredCameraPosition, smoothing);
        state.smoothedCameraPosition = cameraPosition;
        setCameraPosition(state.cameraEntity, cameraPosition, true);
        lookAt(state.cameraEntity, cameraPosition, targetCenter);
    }

    private static Vec3 getTargetCenter(Minecraft minecraft, KillCamState state) {
        Entity target = minecraft.level == null ? null : minecraft.level.getEntity(state.targetId);
        if (target != null) {
            state.lastTargetBase = target.position();
            state.targetWidth = Math.max(0.6F, target.getBbWidth());
            state.targetHeight = Math.max(0.6F, target.getBbHeight());
        }
        Vec3 rawCenter = state.lastTargetBase.add(0.0D, state.targetHeight * 0.58D, 0.0D);
        state.smoothedTargetCenter = lerpVec(state.smoothedTargetCenter, rawCenter, 0.42D);
        return state.smoothedTargetCenter;
    }

    private static Vec3 getMeleeCameraPosition(KillCamState state, Vec3 targetCenter, double progress) {
        Vec3 direction = horizontalDirection(state.attackerPosition, targetCenter);
        double baseAngle = Math.atan2(direction.z, direction.x);
        double easedProgress = easeInOut(progress);
        double angle = baseAngle + Math.toRadians(-70.0D + 130.0D * easedProgress);
        double radius = Math.max(3.1D, state.targetWidth * 3.2D);
        double height = 0.35D + Math.sin(progress * Math.PI) * 0.7D;
        Vec3 orbit = new Vec3(Math.cos(angle) * radius, height, Math.sin(angle) * radius);
        return targetCenter.add(orbit);
    }

    private static Vec3 getRangedCameraPosition(Minecraft minecraft, KillCamState state, Vec3 targetCenter,
                                                double progress) {
        Vec3 shotDirection = targetCenter.subtract(state.projectileStartPosition);
        if (shotDirection.lengthSqr() <= MIN_DIRECTION_LENGTH) {
            shotDirection = targetCenter.subtract(state.attackerPosition);
        }
        Vec3 direction = shotDirection.lengthSqr() <= MIN_DIRECTION_LENGTH
                ? horizontalDirection(state.attackerPosition, targetCenter)
                : shotDirection.normalize();
        Vec3 side = new Vec3(-direction.z, 0.0D, direction.x);
        if (side.lengthSqr() <= MIN_DIRECTION_LENGTH) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            side = side.normalize();
        }

        double flightProgress = Mth.clamp(progress / 0.68D, 0.0D, 1.0D);
        if (flightProgress < 1.0D) {
            Vec3 projectilePoint = getProjectileReplayPoint(minecraft, state, targetCenter, direction,
                    easeOutCubic(flightProgress));
            double chaseDistance = Mth.lerp(flightProgress, 3.4D, 1.25D);
            double chaseHeight = Mth.lerp(flightProgress, 0.45D, 0.18D);
            return projectilePoint.subtract(direction.scale(chaseDistance))
                    .add(side.scale(0.45D))
                    .add(0.0D, chaseHeight, 0.0D);
        }

        double impactProgress = easeInOut((progress - 0.68D) / 0.32D);
        double orbitAngle = Math.toRadians(-25.0D + 55.0D * impactProgress);
        double radius = Math.max(3.6D, state.targetWidth * 3.3D);
        Vec3 orbitForward = direction.scale(-Math.cos(orbitAngle) * radius);
        Vec3 orbitSide = side.scale(Math.sin(orbitAngle) * radius);
        return targetCenter.add(orbitForward).add(orbitSide).add(0.0D, 0.65D, 0.0D);
    }

    private static Vec3 getProjectileReplayPoint(Minecraft minecraft, KillCamState state, Vec3 targetCenter,
                                                 Vec3 direction, double progress) {
        if (minecraft.level != null && state.projectileId >= 0 && progress < 0.18D) {
            Entity projectile = minecraft.level.getEntity(state.projectileId);
            if (projectile != null && projectile.distanceToSqr(targetCenter) > 1.0D) {
                return projectile.position();
            }
        }

        double shotDistance = state.projectileStartPosition.distanceTo(targetCenter);
        double arcHeight = Mth.clamp(shotDistance * 0.025D, 0.0D, 4.0D);
        Vec3 control = state.projectileStartPosition.lerp(targetCenter, 0.52D)
                .add(0.0D, arcHeight, 0.0D)
                .add(direction.scale(0.75D));
        return quadraticBezier(state.projectileStartPosition, control, targetCenter, progress);
    }

    private static Vec3 avoidBlockClipping(Minecraft minecraft, Entity cameraEntity, Vec3 targetCenter,
                                           Vec3 cameraPosition) {
        if (minecraft.level == null) {
            return cameraPosition;
        }

        HitResult hit = minecraft.level.clip(new ClipContext(targetCenter, cameraPosition,
                ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, cameraEntity));
        if (hit.getType() == HitResult.Type.MISS) {
            return cameraPosition;
        }

        Vec3 fromTarget = cameraPosition.subtract(targetCenter);
        if (fromTarget.lengthSqr() <= MIN_DIRECTION_LENGTH) {
            return hit.getLocation();
        }
        return hit.getLocation().subtract(fromTarget.normalize().scale(CAMERA_BLOCK_PADDING));
    }

    private static Vec3 horizontalDirection(Vec3 from, Vec3 to) {
        Vec3 direction = new Vec3(to.x - from.x, 0.0D, to.z - from.z);
        if (direction.lengthSqr() <= MIN_DIRECTION_LENGTH) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                Vec3 look = minecraft.player.getLookAngle();
                direction = new Vec3(look.x, 0.0D, look.z);
            }
        }
        if (direction.lengthSqr() <= MIN_DIRECTION_LENGTH) {
            return new Vec3(0.0D, 0.0D, 1.0D);
        }
        return direction.normalize();
    }

    private static Vec3 quadraticBezier(Vec3 start, Vec3 control, Vec3 end, double progress) {
        double inverse = 1.0D - progress;
        return start.scale(inverse * inverse)
                .add(control.scale(2.0D * inverse * progress))
                .add(end.scale(progress * progress));
    }

    private static double easeInOut(double progress) {
        double clamped = Mth.clamp(progress, 0.0D, 1.0D);
        return clamped * clamped * (3.0D - 2.0D * clamped);
    }

    private static double easeOutCubic(double progress) {
        double clamped = Mth.clamp(progress, 0.0D, 1.0D);
        double inverse = 1.0D - clamped;
        return 1.0D - inverse * inverse * inverse;
    }

    private static Vec3 lerpVec(Vec3 from, Vec3 to, double amount) {
        double clamped = Mth.clamp(amount, 0.0D, 1.0D);
        return new Vec3(
                Mth.lerp(clamped, from.x, to.x),
                Mth.lerp(clamped, from.y, to.y),
                Mth.lerp(clamped, from.z, to.z));
    }

    private static double getTransitionBlend(double progress) {
        return Mth.clamp(Math.min(progress / 0.18D, (1.0D - progress) / 0.18D), 0.0D, 1.0D);
    }

    private static void suppressInputs(Minecraft minecraft) {
        KeyMapping.releaseAll();
        if (minecraft.player != null && minecraft.player.isUsingItem()) {
            minecraft.player.stopUsingItem();
        }
    }

    private static void setCameraPosition(ArmorStand cameraEntity, Vec3 cameraPosition, boolean interpolate) {
        if (interpolate) {
            cameraEntity.xo = cameraEntity.getX();
            cameraEntity.yo = cameraEntity.getY();
            cameraEntity.zo = cameraEntity.getZ();
            cameraEntity.xOld = cameraEntity.getX();
            cameraEntity.yOld = cameraEntity.getY();
            cameraEntity.zOld = cameraEntity.getZ();
        } else {
            Vec3 entityPosition = cameraPosition.subtract(0.0D, cameraEntity.getEyeHeight(), 0.0D);
            cameraEntity.xo = entityPosition.x;
            cameraEntity.yo = entityPosition.y;
            cameraEntity.zo = entityPosition.z;
            cameraEntity.xOld = entityPosition.x;
            cameraEntity.yOld = entityPosition.y;
            cameraEntity.zOld = entityPosition.z;
        }

        cameraEntity.setPos(cameraPosition.x, cameraPosition.y - cameraEntity.getEyeHeight(), cameraPosition.z);
    }

    private static void lookAt(ArmorStand cameraEntity, Vec3 cameraPosition, Vec3 lookAt) {
        Vec3 delta = lookAt.subtract(cameraPosition);
        double horizontalDistance = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        float yaw = (float) (Math.atan2(delta.z, delta.x) * Mth.RAD_TO_DEG) - 90.0F;
        float pitch = (float) (-(Math.atan2(delta.y, horizontalDistance) * Mth.RAD_TO_DEG));
        cameraEntity.yRotO = cameraEntity.getYRot();
        cameraEntity.xRotO = cameraEntity.getXRot();
        cameraEntity.setYRot(yaw);
        cameraEntity.setXRot(pitch);
        cameraEntity.setYHeadRot(yaw);
        cameraEntity.setYBodyRot(yaw);
    }

    private static void stop(boolean restoreCamera) {
        if (active == null) {
            return;
        }

        KillCamState state = active;
        active = null;
        if (!restoreCamera) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Entity restoreEntity = state.previousCameraEntity != null ? state.previousCameraEntity : minecraft.player;
        if (minecraft.level != null && restoreEntity != null && minecraft.getCameraEntity() == state.cameraEntity) {
            minecraft.setCameraEntity(restoreEntity);
        }
        if (state.previousCameraType != null) {
            minecraft.options.setCameraType(state.previousCameraType);
        }
    }

    private static final class KillCamState {
        private final ArmorStand cameraEntity;
        private final Entity previousCameraEntity;
        private final CameraType previousCameraType;
        private final int targetId;
        private Vec3 lastTargetBase;
        private float targetWidth;
        private float targetHeight;
        private final Vec3 attackerPosition;
        private Vec3 smoothedCameraPosition;
        private Vec3 smoothedTargetCenter;
        private final int projectileId;
        private final Vec3 projectileStartPosition;
        private final boolean ranged;
        private final int durationTicks;
        private final double fov;
        private int ageTicks;

        private KillCamState(ArmorStand cameraEntity, Entity previousCameraEntity, CameraType previousCameraType,
                             int targetId, Vec3 lastTargetBase, float targetWidth, float targetHeight,
                             Vec3 attackerPosition, Vec3 smoothedCameraPosition,
                             Vec3 smoothedTargetCenter, int projectileId, Vec3 projectileStartPosition, boolean ranged,
                             int durationTicks, double fov) {
            this.cameraEntity = cameraEntity;
            this.previousCameraEntity = previousCameraEntity;
            this.previousCameraType = previousCameraType;
            this.targetId = targetId;
            this.lastTargetBase = lastTargetBase;
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            this.attackerPosition = attackerPosition;
            this.smoothedCameraPosition = smoothedCameraPosition;
            this.smoothedTargetCenter = smoothedTargetCenter;
            this.projectileId = projectileId;
            this.projectileStartPosition = projectileStartPosition;
            this.ranged = ranged;
            this.durationTicks = durationTicks;
            this.fov = fov;
        }

        private double progress(float partialTick) {
            return Mth.clamp((ageTicks + partialTick) / (double) durationTicks, 0.0D, 1.0D);
        }
    }
}
