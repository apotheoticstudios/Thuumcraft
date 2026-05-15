package net.apotheoticstudios.thuumcraft.food;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.item.IngredientItem;
import net.apotheoticstudios.thuumcraft.stamina.StaminaEvents;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SkyrimFoodEvents {
    private static final int RESOURCE_TICK_INTERVAL = 20;
    private static final Map<UUID, ActiveFoodRegeneration> ACTIVE_REGENERATION = new HashMap<>();

    private SkyrimFoodEvents() {
    }

    @SubscribeEvent
    public static void applyFoodEffects(LivingEntityUseItemEvent.Finish event) {
        if (!isFoodSystemEnabled() || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        ItemStack stack = event.getItem();
        if (stack.isEmpty()
                || !stack.isEdible()
                || stack.getItem() instanceof IngredientItem
                || stack.is(ModTags.Items.INGREDIENT)) {
            return;
        }

        FoodProperties food = stack.getFoodProperties(player);
        if (food == null || food.getNutrition() <= 0) {
            return;
        }

        FoodProfile profile = FoodProfile.from(stack, food);
        double restoreMultiplier = profile.restoreMultiplier();
        double nutrition = food.getNutrition();
        heal(player, nutrition * Config.FOOD_HEALTH_RESTORE_PER_NUTRITION.get() * restoreMultiplier);

        double staminaRestore = nutrition * Config.FOOD_STAMINA_RESTORE_PER_NUTRITION.get() * restoreMultiplier;
        if (Config.ENABLE_STAMINA_SYSTEM.get() && staminaRestore > 0.0D) {
            StaminaEvents.addCurrentStamina(player, staminaRestore);
        }
        if (profile.magickaFood()) {
            addMagicka(player, nutrition * Config.FOOD_MAGICKA_RESTORE_PER_NUTRITION.get() * restoreMultiplier);
        }
        if (profile.regeneratingMeal()) {
            addFoodRegeneration(player, profile);
        }
    }

    @SubscribeEvent
    public static void tickFoodRegeneration(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || ACTIVE_REGENERATION.isEmpty()) {
            return;
        }
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        ActiveFoodRegeneration regeneration = ACTIVE_REGENERATION.get(player.getUUID());
        if (regeneration == null) {
            return;
        }
        if (!isFoodSystemEnabled() || player.isCreative() || player.isSpectator() || !player.isAlive()) {
            ACTIVE_REGENERATION.remove(player.getUUID());
            return;
        }

        long gameTime = player.level().getGameTime();
        if (gameTime > regeneration.expiresAt) {
            ACTIVE_REGENERATION.remove(player.getUUID());
            return;
        }
        if (gameTime < regeneration.nextApplicationTick) {
            return;
        }

        regeneration.nextApplicationTick = gameTime + RESOURCE_TICK_INTERVAL;
        heal(player, regeneration.healthPerSecond);
        if (Config.ENABLE_STAMINA_SYSTEM.get() && regeneration.staminaPerSecond > 0.0D) {
            StaminaEvents.addCurrentStamina(player, regeneration.staminaPerSecond);
        }
        addMagicka(player, regeneration.magickaPerSecond);
    }

    @SubscribeEvent
    public static void clearPlayer(PlayerEvent.PlayerLoggedOutEvent event) {
        ACTIVE_REGENERATION.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void clearServer(ServerStoppingEvent event) {
        ACTIVE_REGENERATION.clear();
    }

    private static void addFoodRegeneration(ServerPlayer player, FoodProfile profile) {
        int durationTicks = Config.FOOD_MEAL_REGEN_DURATION_TICKS.get();
        if (durationTicks <= 0) {
            return;
        }

        long gameTime = player.level().getGameTime();
        double healthPerSecond = Config.FOOD_MEAL_HEALTH_REGEN_PER_SECOND.get();
        double staminaPerSecond = Config.ENABLE_STAMINA_SYSTEM.get()
                ? Config.FOOD_MEAL_STAMINA_REGEN_PER_SECOND.get()
                : 0.0D;
        double magickaPerSecond = profile.magickaFood() ? Config.FOOD_MEAL_MAGICKA_REGEN_PER_SECOND.get() : 0.0D;
        ACTIVE_REGENERATION.merge(player.getUUID(),
                new ActiveFoodRegeneration(healthPerSecond, staminaPerSecond, magickaPerSecond,
                        gameTime + RESOURCE_TICK_INTERVAL, gameTime + durationTicks),
                ActiveFoodRegeneration::merge);
    }

    private static void heal(ServerPlayer player, double amount) {
        if (amount <= 0.0D
                || player.getHealth() <= 0.0F
                || player.getHealth() >= player.getMaxHealth()) {
            return;
        }

        player.heal((float) Math.min(amount, player.getMaxHealth() - player.getHealth()));
    }

    private static void addMagicka(ServerPlayer player, double amount) {
        if (amount <= 0.0D) {
            return;
        }

        MagicData magicData = MagicData.getPlayerMagicData(player);
        float maxMana = Math.max(1.0F, (float) player.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
        magicData.setMana(Mth.clamp((float) (magicData.getMana() + amount), 0.0F, maxMana));
    }

    private static boolean isFoodSystemEnabled() {
        return Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get() && Config.ENABLE_SKYRIM_FOOD_EFFECTS.get();
    }

    private record FoodProfile(boolean rawFood, boolean cookedFood, boolean meal, boolean magickaFood) {
        private static FoodProfile from(ItemStack stack, FoodProperties food) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            String path = itemId == null ? "" : itemId.getPath().toLowerCase(Locale.ROOT);
            boolean meal = containsAny(path, "soup", "stew", "chowder", "meal", "fondue", "pie", "salad",
                    "sandwich", "pasta", "rice", "noodle", "burger", "feast", "roll", "dumpling", "casserole");
            boolean cooked = meal || containsAny(path, "baked", "boiled", "cooked", "grilled", "roast", "roasted",
                    "smoked", "fried", "seared", "steamed", "toasted", "bread", "steak", "chop");
            boolean raw = !cooked && (path.startsWith("raw_") || path.contains("_raw_") || food.isMeat());
            boolean magicka = containsAny(path, "fondue", "glow", "chorus", "sweet", "honey", "berry", "cookie",
                    "cake", "pie");
            return new FoodProfile(raw, cooked, meal, magicka);
        }

        private double restoreMultiplier() {
            if (meal) {
                return Config.FOOD_MEAL_RESTORE_MULTIPLIER.get();
            }
            if (cookedFood) {
                return Config.FOOD_COOKED_RESTORE_MULTIPLIER.get();
            }
            if (rawFood) {
                return Config.FOOD_RAW_RESTORE_MULTIPLIER.get();
            }
            return 1.0D;
        }

        private boolean regeneratingMeal() {
            return meal;
        }

        private static boolean containsAny(String path, String... terms) {
            for (String term : terms) {
                if (path.contains(term)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class ActiveFoodRegeneration {
        private final double healthPerSecond;
        private final double staminaPerSecond;
        private final double magickaPerSecond;
        private long nextApplicationTick;
        private final long expiresAt;

        private ActiveFoodRegeneration(double healthPerSecond, double staminaPerSecond, double magickaPerSecond,
                                       long nextApplicationTick, long expiresAt) {
            this.healthPerSecond = Math.max(0.0D, healthPerSecond);
            this.staminaPerSecond = Math.max(0.0D, staminaPerSecond);
            this.magickaPerSecond = Math.max(0.0D, magickaPerSecond);
            this.nextApplicationTick = nextApplicationTick;
            this.expiresAt = expiresAt;
        }

        private static ActiveFoodRegeneration merge(ActiveFoodRegeneration existing,
                                                    ActiveFoodRegeneration incoming) {
            return new ActiveFoodRegeneration(
                    Math.max(existing.healthPerSecond, incoming.healthPerSecond),
                    Math.max(existing.staminaPerSecond, incoming.staminaPerSecond),
                    Math.max(existing.magickaPerSecond, incoming.magickaPerSecond),
                    Math.min(existing.nextApplicationTick, incoming.nextApplicationTick),
                    Math.max(existing.expiresAt, incoming.expiresAt));
        }
    }
}
