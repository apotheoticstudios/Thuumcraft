package net.apotheoticstudios.thuumcraft.magic;

import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.apotheoticstudios.thuumcraft.stamina.StaminaEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SkyrimMagicSkillEvents {
    private static final UUID ALTERATION_POWER_MODIFIER = UUID.fromString("12ab4602-c30e-438a-9b64-08d34ade7291");
    private static final UUID CONJURATION_POWER_MODIFIER = UUID.fromString("cb90dc3e-b155-477c-b125-8721876e2fe0");
    private static final UUID DESTRUCTION_POWER_MODIFIER = UUID.fromString("5dfbdbe5-116b-481e-8db4-f395cc12b9cd");
    private static final UUID FIRE_POWER_MODIFIER = UUID.fromString("d0278684-7951-4d4b-8ba7-9911c436e0cc");
    private static final UUID FROST_POWER_MODIFIER = UUID.fromString("5289af75-64ec-4d3e-81a6-3ef659d701bd");
    private static final UUID SHOCK_POWER_MODIFIER = UUID.fromString("6a2bdb68-1427-4663-b825-e23e277217b1");
    private static final UUID ILLUSION_POWER_MODIFIER = UUID.fromString("93eaf5bd-1a56-4652-b877-1d8329c58b11");
    private static final UUID RESTORATION_POWER_MODIFIER = UUID.fromString("767285a7-4ac5-4e99-a1e0-2dd43771fdd5");
    private static final UUID RESTORATION_RECOVERY_MODIFIER = UUID.fromString("6ab39020-a999-4e9c-b4ba-4ef3fd1e7df5");
    private static final String AVOID_DEATH_LAST_USE_TAG = Thuumcraft.MOD_ID + ":avoid_death_last_use";
    private static final int AVOID_DEATH_COOLDOWN_TICKS = 24_000;

    private SkyrimMagicSkillEvents() {
    }

    @SubscribeEvent
    public static void tickMagicPerks(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        removeSkyrimSpellCooldowns(player);

        if (!SkillPerk.isSystemEnabled() || player.isSpectator() || player.isCreative() || !player.isAlive()) {
            removeMagicPowerModifiers(player);
            return;
        }

        setModifier(player, ModAttributes.ALTERATION_SPELL_POWER.get(), ALTERATION_POWER_MODIFIER,
                "Thuumcraft Alteration perk power", alterationPowerBonus(player), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.CONJURATION_SPELL_POWER.get(), CONJURATION_POWER_MODIFIER,
                "Thuumcraft Conjuration perk power", conjurationPowerBonus(player), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.DESTRUCTION_SPELL_POWER.get(), DESTRUCTION_POWER_MODIFIER,
                "Thuumcraft Destruction perk power", 0.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.FIRE_SPELL_POWER.get(), FIRE_POWER_MODIFIER,
                "Thuumcraft Augmented Flames", SkillPerk.rank(player, SkillPerk.DESTRUCTION_AUGMENTED_FLAMES) * 0.25D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.FROST_SPELL_POWER.get(), FROST_POWER_MODIFIER,
                "Thuumcraft Augmented Frost", SkillPerk.rank(player, SkillPerk.DESTRUCTION_AUGMENTED_FROST) * 0.25D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.SHOCK_SPELL_POWER.get(), SHOCK_POWER_MODIFIER,
                "Thuumcraft Augmented Shock", SkillPerk.rank(player, SkillPerk.DESTRUCTION_AUGMENTED_SHOCK) * 0.25D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.ILLUSION_SPELL_POWER.get(), ILLUSION_POWER_MODIFIER,
                "Thuumcraft Illusion perk power", illusionPowerBonus(player), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, ModAttributes.RESTORATION_SPELL_POWER.get(), RESTORATION_POWER_MODIFIER,
                "Thuumcraft Restoration perk power", restorationPowerBonus(player), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setModifier(player, AttributeRegistry.MANA_REGEN.get(), RESTORATION_RECOVERY_MODIFIER,
                "Thuumcraft Recovery", SkillPerk.rank(player, SkillPerk.RESTORATION_RECOVERY) * 0.25D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);

        tickAvoidDeath(player);
    }

    @SubscribeEvent
    public static void applyDynamicManaCostAndExperience(SpellOnCastEvent event) {
        AbstractSpell spell = SpellRegistry.getSpell(event.getSpellId());
        SkillProgression.Skill skill = SkyrimMagicScaling.skillFor(spell);
        if (spell == null || spell == SpellRegistry.none() || skill == null) {
            return;
        }

        int adjustedManaCost = SkyrimMagicScaling.adjustedManaCost(spell, event.getSpellLevel(), event.getEntity());
        event.setManaCost(adjustedManaCost);

        if (event.getEntity() instanceof ServerPlayer player) {
            double experience = Math.max(0.5D, Math.sqrt(Math.max(1, adjustedManaCost)) * 0.45D);
            SkillProgression.award(player, skill, experience);
        }
    }

    @SubscribeEvent
    public static void applyMagicResistanceAndElementalFinishers(LivingHurtEvent event) {
        if (event.getAmount() <= 0.0F || event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer defender && event.getSource() instanceof SpellDamageSource) {
            int magicResistance = SkillPerk.rank(defender, SkillPerk.ALTERATION_MAGIC_RESISTANCE);
            if (magicResistance > 0) {
                event.setAmount((float) (event.getAmount() * (1.0D - magicResistance * 0.1D)));
            }
        }

        if (!(event.getSource() instanceof SpellDamageSource spellDamageSource)
                || !(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        AbstractSpell spell = spellDamageSource.spell();
        if (SkyrimMagicScaling.skillFor(spell) != SkillProgression.Skill.DESTRUCTION) {
            return;
        }

        LivingEntity target = event.getEntity();
        float remainingHealth = target.getHealth() - event.getAmount();
        if (remainingHealth > target.getMaxHealth() * 0.2F) {
            return;
        }

        switch (SkyrimMagicScaling.elementFor(spell)) {
            case FIRE -> {
                if (SkillPerk.has(player, SkillPerk.DESTRUCTION_INTENSE_FLAMES)) {
                    target.addEffect(new MobEffectInstance(ModEffects.FEAR.get(), 80, 0, false, false, true));
                }
            }
            case FROST -> {
                if (SkillPerk.has(player, SkillPerk.DESTRUCTION_DEEP_FREEZE)) {
                    target.addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60, 0, false, false, true));
                }
            }
            case SHOCK -> {
                if (SkillPerk.has(player, SkillPerk.DESTRUCTION_DISINTEGRATE)) {
                    event.setAmount(Math.max(event.getAmount(), target.getHealth() + target.getAbsorptionAmount() + 1.0F));
                }
            }
            case NONE -> {
            }
        }
    }

    @SubscribeEvent
    public static void applyRespite(SpellHealEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !SkillPerk.has(player, SkillPerk.RESTORATION_RESPITE)
                || SkyrimMagicScaling.skillFor(event.getSchoolType()) != SkillProgression.Skill.RESTORATION) {
            return;
        }

        StaminaEvents.addCurrentStamina(player, event.getHealAmount());
    }

    private static double alterationPowerBonus(ServerPlayer player) {
        double bonus = SkillPerk.rank(player, SkillPerk.ALTERATION_MAGE_ARMOR) * 0.25D;
        if (SkillPerk.has(player, SkillPerk.ALTERATION_STABILITY)) {
            bonus += 0.5D;
        }
        return bonus;
    }

    private static double conjurationPowerBonus(ServerPlayer player) {
        double bonus = SkillPerk.rank(player, SkillPerk.CONJURATION_SUMMONER) * 0.25D;
        if (SkillPerk.has(player, SkillPerk.CONJURATION_ATROMANCY)) {
            bonus += 0.5D;
        }
        if (SkillPerk.has(player, SkillPerk.CONJURATION_NECROMANCY)) {
            bonus += 0.25D;
        }
        if (SkillPerk.has(player, SkillPerk.CONJURATION_DARK_SOULS)) {
            bonus += 0.25D;
        }
        if (SkillPerk.has(player, SkillPerk.CONJURATION_ELEMENTAL_POTENCY)) {
            bonus += 0.25D;
        }
        return bonus;
    }

    private static double illusionPowerBonus(ServerPlayer player) {
        double bonus = 0.0D;
        if (SkillPerk.has(player, SkillPerk.ILLUSION_ANIMAGE)) {
            bonus += 0.15D;
        }
        if (SkillPerk.has(player, SkillPerk.ILLUSION_KINDRED_MAGE)) {
            bonus += 0.15D;
        }
        if (SkillPerk.has(player, SkillPerk.ILLUSION_HYPNOTIC_GAZE)) {
            bonus += 0.10D;
        }
        if (SkillPerk.has(player, SkillPerk.ILLUSION_ASPECT_OF_TERROR)) {
            bonus += 0.10D;
        }
        if (SkillPerk.has(player, SkillPerk.ILLUSION_RAGE)) {
            bonus += 0.10D;
        }
        if (SkillPerk.has(player, SkillPerk.ILLUSION_MASTER_OF_THE_MIND)) {
            bonus += 0.20D;
        }
        return bonus;
    }

    private static double restorationPowerBonus(ServerPlayer player) {
        return SkillPerk.has(player, SkillPerk.RESTORATION_REGENERATION) ? 0.5D : 0.0D;
    }

    private static void tickAvoidDeath(ServerPlayer player) {
        if (!SkillPerk.has(player, SkillPerk.RESTORATION_AVOID_DEATH)
                || player.getHealth() > player.getMaxHealth() * 0.1F) {
            return;
        }

        long gameTime = player.level().getGameTime();
        long lastUse = player.getPersistentData().getLong(AVOID_DEATH_LAST_USE_TAG);
        if (gameTime - lastUse < AVOID_DEATH_COOLDOWN_TICKS) {
            return;
        }

        player.getPersistentData().putLong(AVOID_DEATH_LAST_USE_TAG, gameTime);
        player.heal(player.getMaxHealth() * 0.5F);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false, true));
    }

    private static void removeSkyrimSpellCooldowns(ServerPlayer player) {
        MagicData magicData = MagicData.getPlayerMagicData(player);
        PlayerCooldowns cooldowns = magicData.getPlayerCooldowns();
        boolean changed = false;
        for (String spellId : new ArrayList<>(cooldowns.getSpellCooldowns().keySet())) {
            AbstractSpell spell = SpellRegistry.getSpell(spellId);
            if (SkyrimMagicScaling.usesSkyrimNoCooldownRules(spell)) {
                changed |= cooldowns.removeCooldown(spellId);
            }
        }
        if (changed) {
            cooldowns.syncToPlayer(player);
        }
    }

    private static void removeMagicPowerModifiers(ServerPlayer player) {
        removeModifier(player, ModAttributes.ALTERATION_SPELL_POWER.get(), ALTERATION_POWER_MODIFIER);
        removeModifier(player, ModAttributes.CONJURATION_SPELL_POWER.get(), CONJURATION_POWER_MODIFIER);
        removeModifier(player, ModAttributes.DESTRUCTION_SPELL_POWER.get(), DESTRUCTION_POWER_MODIFIER);
        removeModifier(player, ModAttributes.FIRE_SPELL_POWER.get(), FIRE_POWER_MODIFIER);
        removeModifier(player, ModAttributes.FROST_SPELL_POWER.get(), FROST_POWER_MODIFIER);
        removeModifier(player, ModAttributes.SHOCK_SPELL_POWER.get(), SHOCK_POWER_MODIFIER);
        removeModifier(player, ModAttributes.ILLUSION_SPELL_POWER.get(), ILLUSION_POWER_MODIFIER);
        removeModifier(player, ModAttributes.RESTORATION_SPELL_POWER.get(), RESTORATION_POWER_MODIFIER);
        removeModifier(player, AttributeRegistry.MANA_REGEN.get(), RESTORATION_RECOVERY_MODIFIER);
    }

    private static void setModifier(LivingEntity entity,
                                    Attribute attribute,
                                    UUID id,
                                    String name,
                                    double amount,
                                    AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) {
            return;
        }

        AttributeModifier current = instance.getModifier(id);
        if (Math.abs(amount) <= 0.0001D) {
            if (current != null) {
                instance.removeModifier(id);
            }
            return;
        }

        if (current != null
                && Math.abs(current.getAmount() - amount) <= 0.0001D
                && current.getOperation() == operation) {
            return;
        }

        instance.removeModifier(id);
        instance.addTransientModifier(new AttributeModifier(id, name, amount, operation));
    }

    private static void removeModifier(LivingEntity entity, Attribute attribute, UUID id) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }
}
