package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
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
    private static final double EPSILON = 0.000001D;

    private PlayerArrowAttributeEvents() {
    }

    @SubscribeEvent
    public static void applyPlayerArrowAttributeTuning(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }

        Attribute arrowVelocity = ForgeRegistries.ATTRIBUTES.getValue(ATTRIBUTESLIB_ARROW_VELOCITY);
        Attribute arrowDamage = ForgeRegistries.ATTRIBUTES.getValue(ATTRIBUTESLIB_ARROW_DAMAGE);
        if (arrowVelocity == null || arrowDamage == null) {
            return;
        }

        Player player = event.player;
        if (!Config.ENABLE_PLAYER_ARROW_TRAJECTORY_TUNING.get()) {
            removeModifier(player, arrowVelocity, ARROW_VELOCITY_MODIFIER);
            removeModifier(player, arrowDamage, ARROW_DAMAGE_COMPENSATION_MODIFIER);
            return;
        }

        double velocityMultiplier = Math.max(EPSILON, Config.PLAYER_ARROW_VELOCITY_MULTIPLIER.get());
        setMultiplicativeModifier(player, arrowVelocity, ARROW_VELOCITY_MODIFIER,
                "Thuumcraft arrow velocity", velocityMultiplier - 1.0D);

        // Vanilla arrow damage scales with impact speed, so inverse scaling keeps faster arrows from double dipping.
        double damageMultiplier = Config.PRESERVE_PLAYER_ARROW_DAMAGE.get() ? 1.0D / velocityMultiplier : 1.0D;
        setMultiplicativeModifier(player, arrowDamage, ARROW_DAMAGE_COMPENSATION_MODIFIER,
                "Thuumcraft arrow damage compensation", damageMultiplier - 1.0D);
    }

    private static void setMultiplicativeModifier(Player player, Attribute attribute, UUID id, String name,
                                                  double amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }

        AttributeModifier current = instance.getModifier(id);
        if (Math.abs(amount) < EPSILON) {
            if (current != null) {
                instance.removeModifier(id);
            }
            return;
        }

        if (current != null
                && current.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL
                && Math.abs(current.getAmount() - amount) < EPSILON) {
            return;
        }

        if (current != null) {
            instance.removeModifier(id);
        }
        instance.addTransientModifier(new AttributeModifier(id, name, amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    private static void removeModifier(Player player, Attribute attribute, UUID id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null && instance.getModifier(id) != null) {
            instance.removeModifier(id);
        }
    }
}
