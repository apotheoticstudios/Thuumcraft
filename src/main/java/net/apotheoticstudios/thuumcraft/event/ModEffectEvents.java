package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.item.ModFoods;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public class ModEffectEvents {
    private static final double RESISTANCE_PER_LEVEL = 0.25D;
    private static final double MAX_RESISTANCE = 0.85D;
    private static final double WEAKNESS_PER_LEVEL = 0.25D;
    private static final double MAX_WEAKNESS = 1.0D;

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity().hasEffect(ModEffects.POISON_RESISTANCE.get())
                && event.getEffectInstance().getEffect() == MobEffects.POISON) {
            event.setResult(Event.Result.DENY);
        }
        if (event.getEntity() instanceof Player player
                && SkillPerk.has(player, SkillPerk.ALCHEMY_SNAKEBLOOD)
                && event.getEffectInstance().getEffect() == MobEffects.POISON
                && player.getRandom().nextFloat() < 0.5F) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntity().hasEffect(ModEffects.PARALYSIS.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (isParalyzed(event.getSource().getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.setAmount(applyWeakness(entity, ModEffects.FIRE_WEAKNESS.get(), event.getAmount()));
        }

        if (event.getSource().is(DamageTypeTags.IS_FREEZING)) {
            event.setAmount(applyResistance(entity, ModEffects.FROST_RESISTANCE.get(), event.getAmount()));
            event.setAmount(applyWeakness(entity, ModEffects.FROST_WEAKNESS.get(), event.getAmount()));
        }

        if (event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
            event.setAmount(applyResistance(entity, ModEffects.SHOCK_RESISTANCE.get(), event.getAmount()));
            event.setAmount(applyWeakness(entity, ModEffects.SHOCK_WEAKNESS.get(), event.getAmount()));
        }

        removeInvisibilityAfterAttackingMob(event);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        ModFoods.tickIngredientAttributeModifiers(event.getEntity());
    }

    private static float applyResistance(LivingEntity entity, MobEffect effect, float amount) {
        MobEffectInstance instance = entity.getEffect(effect);
        if (instance == null) {
            return amount;
        }

        double reduction = Math.min(MAX_RESISTANCE, RESISTANCE_PER_LEVEL * (instance.getAmplifier() + 1));
        return (float) (amount * (1.0D - reduction));
    }

    private static float applyWeakness(LivingEntity entity, MobEffect effect, float amount) {
        MobEffectInstance instance = entity.getEffect(effect);
        if (instance == null) {
            return amount;
        }

        double increase = Math.min(MAX_WEAKNESS, WEAKNESS_PER_LEVEL * (instance.getAmplifier() + 1));
        return (float) (amount * (1.0D + increase));
    }

    private static boolean isParalyzed(Entity entity) {
        return entity instanceof LivingEntity livingEntity
                && livingEntity.hasEffect(ModEffects.PARALYSIS.get());
    }

    private static void removeInvisibilityAfterAttackingMob(LivingHurtEvent event) {
        if (Config.ENABLE_STEALTH_SYSTEM.get()
                && event.getEntity() instanceof Mob
                && event.getSource().getEntity() instanceof Player player
                && player.hasEffect(MobEffects.INVISIBILITY)) {
            player.removeEffect(MobEffects.INVISIBILITY);
        }
    }
}
