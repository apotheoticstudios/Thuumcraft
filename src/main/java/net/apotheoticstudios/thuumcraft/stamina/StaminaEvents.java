package net.apotheoticstudios.thuumcraft.stamina;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.network.ClientboundStaminaPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class StaminaEvents {
    private static final String STAMINA_TAG = Thuumcraft.MOD_ID + ".stamina";
    private static final String STAMINA_INITIALIZED_TAG = Thuumcraft.MOD_ID + ".stamina_initialized";
    private static final int SYNC_INTERVAL_TICKS = 5;
    private static final float SYNC_EPSILON = 0.01F;

    private static final Map<UUID, PlayerStaminaRuntime> RUNTIME = new HashMap<>();

    private StaminaEvents() {
    }

    @SubscribeEvent
    public static void tickPlayerStamina(TickEvent.PlayerTickEvent event) {
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get() || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());

        if (event.phase == TickEvent.Phase.START) {
            if (Config.ENABLE_STAMINA_SYSTEM.get() && Config.ENABLE_STAMINA_SPRINT_LIMIT.get()) {
                double maxStamina = getMaxStamina(player);
                enforceSprintLimit(player, runtime, getCurrentStamina(player, maxStamina), maxStamina);
            }
            return;
        }

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (Config.ENABLE_STAMINA_SYSTEM.get() && Config.ENABLE_STAMINA_HUNGER_OVERRIDE.get()) {
            overrideVanillaHunger(player);
        }

        if (player.isSpectator() || player.isCreative()) {
            if (Config.ENABLE_STAMINA_SYSTEM.get()) {
                double maxStamina = getMaxStamina(player);
                setCurrentStamina(player, maxStamina);
                syncIfNeeded(player, runtime, (float) maxStamina);
            }
            resetStaminaRuntime(runtime);
            runtime.healthRegenTicks = 0;
            runtime.combatTicks = 0;
        } else {
            if (runtime.combatTicks > 0) {
                runtime.combatTicks--;
            }
            if (Config.ENABLE_STAMINA_SYSTEM.get()) {
                tickStamina(player, runtime);
            } else {
                resetStaminaRuntime(runtime);
            }
            if (Config.ENABLE_SKYRIM_HEALTH_REGENERATION.get()) {
                tickHealthRegeneration(player, runtime);
            } else {
                runtime.healthRegenTicks = 0;
            }
        }
    }

    public static float getCurrentStamina(ServerPlayer player) {
        return (float) getCurrentStamina(player, getMaxStamina(player));
    }

    @SubscribeEvent
    public static void trackPlayerCombat(LivingHurtEvent event) {
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get() || !Config.ENABLE_SKYRIM_HEALTH_REGENERATION.get()) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer hurtPlayer) {
            markInCombat(hurtPlayer);
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer attackingPlayer && event.getEntity() != attackingPlayer) {
            markInCombat(attackingPlayer);
        }
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        RUNTIME.remove(event.getEntity().getUUID());
    }

    private static void overrideVanillaHunger(ServerPlayer player) {
        FoodData foodData = player.getFoodData();
        int foodLevel = Config.STAMINA_HUNGER_FOOD_LEVEL.get();
        if (foodData.getFoodLevel() != foodLevel) {
            foodData.setFoodLevel(foodLevel);
        }
        if (foodData.getSaturationLevel() != 0.0F) {
            foodData.setSaturation(0.0F);
        }
        if (foodData.getExhaustionLevel() != 0.0F) {
            foodData.setExhaustion(0.0F);
        }
    }

    private static void tickStamina(ServerPlayer player, PlayerStaminaRuntime runtime) {
        double maxStamina = getMaxStamina(player);
        double stamina = getCurrentStamina(player, maxStamina);
        if (Config.ENABLE_STAMINA_SPRINT_LIMIT.get()) {
            enforceSprintLimit(player, runtime, stamina, maxStamina);
            if (!runtime.sprintLocked && player.isSprinting() && isMovingHorizontally(player)) {
                stamina = Math.max(0.0D, stamina - getSprintDrainPerTick(player));
                runtime.regenDelayTicks = Config.STAMINA_REGEN_DELAY_TICKS.get();
                if (stamina <= 0.0D) {
                    runtime.sprintLocked = true;
                    player.setSprinting(false);
                }
            } else if (runtime.regenDelayTicks > 0) {
                runtime.regenDelayTicks = Math.max(0, runtime.regenDelayTicks - 1);
            } else if (stamina < maxStamina) {
                stamina = Math.min(maxStamina, stamina + getStaminaRegenerationPerTick(player));
            }
            enforceSprintLimit(player, runtime, stamina, maxStamina);
        } else {
            runtime.sprintLocked = false;
            if (runtime.regenDelayTicks > 0) {
                runtime.regenDelayTicks = Math.max(0, runtime.regenDelayTicks - 1);
            } else if (stamina < maxStamina) {
                stamina = Math.min(maxStamina, stamina + getStaminaRegenerationPerTick(player));
            }
        }

        stamina = Mth.clamp(stamina, 0.0D, maxStamina);
        setCurrentStamina(player, stamina);
        syncIfNeeded(player, runtime, (float) stamina);
    }

    private static void enforceSprintLimit(ServerPlayer player, PlayerStaminaRuntime runtime, double stamina,
                                           double maxStamina) {
        if (player.isSpectator() || player.isCreative()) {
            runtime.sprintLocked = false;
            return;
        }

        if (runtime.sprintLocked && stamina >= getSprintResumeStamina(maxStamina)) {
            runtime.sprintLocked = false;
        }

        if (runtime.sprintLocked || stamina < getSprintStartStamina(maxStamina)) {
            if (player.isSprinting()) {
                player.setSprinting(false);
            }
            if (stamina <= 0.0D) {
                runtime.sprintLocked = true;
            }
        }
    }

    private static void tickHealthRegeneration(ServerPlayer player, PlayerStaminaRuntime runtime) {
        if (!player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
                || player.getHealth() <= 0.0F
                || player.getHealth() >= player.getMaxHealth()
                || player.hasEffect(MobEffects.POISON)
                || player.hasEffect(MobEffects.WITHER)) {
            runtime.healthRegenTicks = 0;
            return;
        }

        runtime.healthRegenTicks++;
        int intervalTicks = Config.HEALTH_REGEN_INTERVAL_TICKS.get();
        if (runtime.healthRegenTicks >= intervalTicks) {
            runtime.healthRegenTicks = 0;
            double regenRate = runtime.combatTicks > 0 ? Config.HEALTH_REGEN_COMBAT_PERCENT_PER_SECOND.get()
                    : Config.HEALTH_REGEN_OUT_OF_COMBAT_PERCENT_PER_SECOND.get();
            float regenAmount = (float) (player.getMaxHealth() * regenRate * intervalTicks / 20.0D);
            player.heal(Math.min(regenAmount, player.getMaxHealth() - player.getHealth()));
        }
    }

    private static void markInCombat(ServerPlayer player) {
        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());
        runtime.combatTicks = Config.HEALTH_REGEN_COMBAT_DURATION_TICKS.get();
    }

    private static void resetStaminaRuntime(PlayerStaminaRuntime runtime) {
        runtime.regenDelayTicks = 0;
        runtime.sprintLocked = false;
    }

    private static double getCurrentStamina(ServerPlayer player, double maxStamina) {
        CompoundTag data = player.getPersistentData();
        if (!data.getBoolean(STAMINA_INITIALIZED_TAG)) {
            data.putBoolean(STAMINA_INITIALIZED_TAG, true);
            data.putDouble(STAMINA_TAG, maxStamina);
            return maxStamina;
        }
        return Mth.clamp(data.getDouble(STAMINA_TAG), 0.0D, maxStamina);
    }

    private static void setCurrentStamina(ServerPlayer player, double stamina) {
        player.getPersistentData().putDouble(STAMINA_TAG, stamina);
    }

    private static double getMaxStamina(ServerPlayer player) {
        AttributeInstance stamina = player.getAttribute(ModAttributes.STAMINA.get());
        return Math.max(1.0D, stamina == null ? 100.0D : stamina.getValue());
    }

    private static double getStaminaRegenerationPerTick(ServerPlayer player) {
        AttributeInstance regeneration = player.getAttribute(ModAttributes.STAMINA_REGENERATION.get());
        double regenerationPerSecond = regeneration == null ? 10.0D : regeneration.getValue();
        return Math.max(0.0D, regenerationPerSecond) / 20.0D;
    }

    private static double getSprintDrainPerTick(ServerPlayer player) {
        double wornWeightMultiplier = 1.0D + player.getArmorValue() * Config.STAMINA_SPRINT_ARMOR_DRAIN_MULTIPLIER.get();
        return Config.STAMINA_SPRINT_DRAIN_PER_SECOND.get() * wornWeightMultiplier / 20.0D;
    }

    private static double getSprintStartStamina(double maxStamina) {
        return Math.max(Config.STAMINA_SPRINT_START_FLOOR.get(),
                maxStamina * Config.STAMINA_SPRINT_START_RATIO.get());
    }

    private static double getSprintResumeStamina(double maxStamina) {
        return Math.max(Config.STAMINA_SPRINT_RESUME_FLOOR.get(),
                maxStamina * Config.STAMINA_SPRINT_RESUME_RATIO.get());
    }

    private static boolean isMovingHorizontally(ServerPlayer player) {
        return player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4D;
    }

    private static void syncIfNeeded(ServerPlayer player, PlayerStaminaRuntime runtime, float stamina) {
        runtime.syncTicks++;
        if (runtime.syncTicks < SYNC_INTERVAL_TICKS && Math.abs(runtime.lastSyncedStamina - stamina) < SYNC_EPSILON) {
            return;
        }

        runtime.syncTicks = 0;
        runtime.lastSyncedStamina = stamina;
        ModMessages.sendToPlayer(new ClientboundStaminaPacket(stamina), player);
    }

    private static final class PlayerStaminaRuntime {
        private int regenDelayTicks;
        private int healthRegenTicks;
        private int combatTicks;
        private boolean sprintLocked;
        private int syncTicks = SYNC_INTERVAL_TICKS;
        private float lastSyncedStamina = -1.0F;
    }
}
