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
    private static final double BASE_MAX_STAMINA = 100.0D;
    private static final int SYNC_INTERVAL_TICKS = 5;
    private static final float SYNC_EPSILON = 0.01F;

    private static final Map<UUID, PlayerStaminaRuntime> RUNTIME = new HashMap<>();

    private StaminaEvents() {
    }

    @SubscribeEvent
    public static void tickPlayerStamina(TickEvent.PlayerTickEvent event) {
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                || event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (!Config.ENABLE_STAMINA_SYSTEM.get() && !Config.ENABLE_SKYRIM_HEALTH_REGENERATION.get()) {
            return;
        }

        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());

        if (Config.ENABLE_STAMINA_SYSTEM.get() && Config.ENABLE_STAMINA_HUNGER_OVERRIDE.get()) {
            overrideVanillaHunger(player);
        }

        if (player.isSpectator() || player.isCreative()) {
            if (Config.ENABLE_STAMINA_SYSTEM.get()) {
                double maxStamina = getMaxStamina(player);
                setCurrentStamina(player, maxStamina);
                syncIfNeeded(player, runtime, (float) maxStamina, (float) maxStamina);
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

    public static void addCurrentStamina(ServerPlayer player, double amount) {
        double maxStamina = getMaxStamina(player);
        double stamina = Mth.clamp(getCurrentStamina(player, maxStamina) + amount, 0.0D, maxStamina);
        setCurrentStamina(player, stamina);
        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());
        if (amount > 0.0D && runtime.sprintLocked && stamina >= getSprintResumeStamina(maxStamina)) {
            runtime.sprintLocked = false;
        }
        syncNow(player, runtime, (float) stamina, (float) maxStamina);
    }

    public static void clampCurrentStamina(ServerPlayer player) {
        double maxStamina = getMaxStamina(player);
        double stamina = Mth.clamp(getCurrentStamina(player, maxStamina), 0.0D, maxStamina);
        setCurrentStamina(player, stamina);
        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());
        syncNow(player, runtime, (float) stamina, (float) maxStamina);
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                && Config.ENABLE_STAMINA_SYSTEM.get()
                && event.getEntity() instanceof ServerPlayer player) {
            refillAndSyncStamina(player);
        }
    }

    @SubscribeEvent
    public static void playerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        if (Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                && Config.ENABLE_STAMINA_SYSTEM.get()
                && event.getEntity() instanceof ServerPlayer player) {
            refillAndSyncStamina(player);
        }
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
            boolean wasSprinting = player.isSprinting();
            boolean canContinueSprinting = runtime.wasSprintingLastTick && stamina > 0.0D;
            if (runtime.sprintLocked && stamina >= getSprintResumeStamina(maxStamina)) {
                runtime.sprintLocked = false;
            }

            if (wasSprinting
                    && (runtime.sprintLocked
                    || stamina <= 0.0D
                    || (!canContinueSprinting && stamina < getSprintStartStamina(maxStamina)))) {
                player.setSprinting(false);
            }

            if (!runtime.sprintLocked && wasSprinting && player.isSprinting()) {
                stamina = Math.max(0.0D, stamina - getSprintDrainPerTick(player));
                runtime.regenDelayTicks = Config.STAMINA_REGEN_DELAY_TICKS.get();
                if (stamina <= 0.0D) {
                    runtime.sprintLocked = true;
                    player.setSprinting(false);
                }
            } else if (wasSprinting) {
                runtime.regenDelayTicks = Config.STAMINA_REGEN_DELAY_TICKS.get();
            } else if (runtime.regenDelayTicks > 0) {
                runtime.regenDelayTicks = Math.max(0, runtime.regenDelayTicks - 1);
            } else if (stamina < maxStamina) {
                stamina = Math.min(maxStamina, stamina + getStaminaRegenerationPerTick(player));
            }
            runtime.wasSprintingLastTick = player.isSprinting();
        } else {
            runtime.sprintLocked = false;
            runtime.wasSprintingLastTick = false;
            if (runtime.regenDelayTicks > 0) {
                runtime.regenDelayTicks = Math.max(0, runtime.regenDelayTicks - 1);
            } else if (stamina < maxStamina) {
                stamina = Math.min(maxStamina, stamina + getStaminaRegenerationPerTick(player));
            }
        }

        stamina = Mth.clamp(stamina, 0.0D, maxStamina);
        setCurrentStamina(player, stamina);
        syncIfNeeded(player, runtime, (float) stamina, (float) maxStamina);
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
        runtime.wasSprintingLastTick = false;
    }

    private static void refillAndSyncStamina(ServerPlayer player) {
        double maxStamina = getMaxStamina(player);
        setCurrentStamina(player, maxStamina);
        PlayerStaminaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerStaminaRuntime());
        resetStaminaRuntime(runtime);
        syncNow(player, runtime, (float) maxStamina, (float) maxStamina);
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
        CompoundTag data = player.getPersistentData();
        data.putBoolean(STAMINA_INITIALIZED_TAG, true);
        data.putDouble(STAMINA_TAG, stamina);
    }

    private static double getMaxStamina(ServerPlayer player) {
        AttributeInstance stamina = player.getAttribute(ModAttributes.STAMINA.get());
        if (stamina == null || stamina.getValue() <= 0.0D) {
            return BASE_MAX_STAMINA;
        }
        return stamina.getValue();
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

    private static void syncIfNeeded(ServerPlayer player, PlayerStaminaRuntime runtime, float stamina, float maxStamina) {
        runtime.syncTicks++;
        if (runtime.syncTicks < SYNC_INTERVAL_TICKS
                && Math.abs(runtime.lastSyncedStamina - stamina) < SYNC_EPSILON
                && Math.abs(runtime.lastSyncedMaxStamina - maxStamina) < SYNC_EPSILON) {
            return;
        }

        syncNow(player, runtime, stamina, maxStamina);
    }

    private static void syncNow(ServerPlayer player, PlayerStaminaRuntime runtime, float stamina, float maxStamina) {
        runtime.syncTicks = 0;
        runtime.lastSyncedStamina = stamina;
        runtime.lastSyncedMaxStamina = maxStamina;
        ModMessages.sendToPlayer(new ClientboundStaminaPacket(stamina, maxStamina), player);
    }

    private static final class PlayerStaminaRuntime {
        private int regenDelayTicks;
        private int healthRegenTicks;
        private int combatTicks;
        private boolean sprintLocked;
        private boolean wasSprintingLastTick;
        private int syncTicks = SYNC_INTERVAL_TICKS;
        private float lastSyncedStamina = -1.0F;
        private float lastSyncedMaxStamina = -1.0F;
    }
}
