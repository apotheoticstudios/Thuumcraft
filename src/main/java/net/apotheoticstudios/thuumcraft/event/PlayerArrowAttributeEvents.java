package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class PlayerArrowAttributeEvents {
    private static final ResourceLocation ATTRIBUTESLIB_ARROW_DAMAGE = ResourceLocation.fromNamespaceAndPath("attributeslib", "arrow_damage");
    private static final ResourceLocation ATTRIBUTESLIB_ARROW_VELOCITY = ResourceLocation.fromNamespaceAndPath("attributeslib", "arrow_velocity");
    private static final UUID ARROW_DAMAGE_COMPENSATION_MODIFIER = UUID.fromString("c5bd4eb8-20cb-4c8f-9d68-1d0f7ed24627");
    private static final UUID ARROW_VELOCITY_MODIFIER = UUID.fromString("0b8419ff-399c-4d04-b250-7c7e7bdb6a69");
    private static final String PROJECTILE_TUNED_TAG = "ThuumcraftTrajectoryTuned";
    private static final double EPSILON = 0.000001D;

    private PlayerArrowAttributeEvents() {
    }

    @SubscribeEvent
    public static void tuneSpawnedPlayerArrow(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide()
                || event.loadedFromDisk()
                || !Config.ENABLE_PLAYER_ARROW_TRAJECTORY_TUNING.get()
                || !(event.getEntity() instanceof AbstractArrow arrow)
                || arrow.getPersistentData().getBoolean(PROJECTILE_TUNED_TAG)) {
            return;
        }

        Entity owner = arrow.getOwner();
        if (!(owner instanceof Player player) || player.isSpectator()) {
            return;
        }

        double velocityMultiplier = Math.max(EPSILON, Config.PLAYER_ARROW_VELOCITY_MULTIPLIER.get());
        if (Math.abs(velocityMultiplier - 1.0D) < EPSILON) {
            arrow.getPersistentData().putBoolean(PROJECTILE_TUNED_TAG, true);
            return;
        }

        Vec3 movement = arrow.getDeltaMovement();
        if (movement.lengthSqr() <= EPSILON) {
            return;
        }

        arrow.setDeltaMovement(movement.scale(velocityMultiplier));
        arrow.hurtMarked = true;
        if (Config.PRESERVE_PLAYER_ARROW_DAMAGE.get()) {
            arrow.setBaseDamage(arrow.getBaseDamage() / velocityMultiplier);
        }
        arrow.getPersistentData().putBoolean(PROJECTILE_TUNED_TAG, true);
    }

    @SubscribeEvent
    public static void clearLegacyPlayerArrowAttributeTuning(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END
                || event.player.level().isClientSide()
                || event.player.tickCount % 20 != 0) {
            return;
        }

        Attribute arrowVelocity = ForgeRegistries.ATTRIBUTES.getValue(ATTRIBUTESLIB_ARROW_VELOCITY);
        Attribute arrowDamage = ForgeRegistries.ATTRIBUTES.getValue(ATTRIBUTESLIB_ARROW_DAMAGE);
        Player player = event.player;
        if (arrowVelocity != null) {
            removeModifier(player, arrowVelocity, ARROW_VELOCITY_MODIFIER);
        }
        if (arrowDamage != null) {
            removeModifier(player, arrowDamage, ARROW_DAMAGE_COMPENSATION_MODIFIER);
        }
    }

    private static void removeModifier(Player player, Attribute attribute, UUID id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null && instance.getModifier(id) != null) {
            instance.removeModifier(id);
        }
    }
}
