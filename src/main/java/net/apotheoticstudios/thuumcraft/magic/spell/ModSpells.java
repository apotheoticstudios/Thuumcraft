package net.apotheoticstudios.thuumcraft.magic.spell;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.magic.ModSpellSchools;
import net.apotheoticstudios.thuumcraft.magic.SkyrimMagicScaling;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class ModSpells {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, Thuumcraft.MOD_ID);

    public static final RegistryObject<AbstractSpell> FLAMES = SPELLS.register("flames", FlamesSpell::new);
    public static final RegistryObject<AbstractSpell> FROSTBITE = SPELLS.register("frostbite", FrostbiteSpell::new);
    public static final RegistryObject<AbstractSpell> SPARKS = SPELLS.register("sparks", SparksSpell::new);
    public static final RegistryObject<AbstractSpell> HEALING = SPELLS.register("healing", HealingSpell::new);
    public static final RegistryObject<AbstractSpell> LESSER_WARD = SPELLS.register("lesser_ward", LesserWardSpell::new);
    public static final RegistryObject<AbstractSpell> OAKFLESH = SPELLS.register("oakflesh", OakfleshSpell::new);
    public static final RegistryObject<AbstractSpell> COURAGE = SPELLS.register("courage", CourageSpell::new);
    public static final RegistryObject<AbstractSpell> FURY = SPELLS.register("fury", FurySpell::new);
    public static final RegistryObject<AbstractSpell> CLAIRVOYANCE = SPELLS.register("clairvoyance", ClairvoyanceSpell::new);
    public static final RegistryObject<AbstractSpell> CONJURE_FAMILIAR = SPELLS.register("conjure_familiar", ConjureFamiliarSpell::new);
    public static final RegistryObject<AbstractSpell> RAISE_ZOMBIE = SPELLS.register("raise_zombie", RaiseZombieSpell::new);

    private static final int TICKS_PER_SECOND = 20;
    private static final int CONCENTRATION_MANA_INTERVAL = 10;
    private static final float CONCENTRATION_TICK_FRACTION = 0.25F;
    private static final int CONCENTRATION_DAMAGE_INTERVAL = 5;
    private static final int CONCENTRATION_HEAL_INTERVAL = 10;
    private static final int NOVICE_MAX_LEVEL = 5;

    private ModSpells() {
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private abstract static class SkyrimSpell extends AbstractSpell {
        private final ResourceLocation spellId;
        private final RegistryObject<SchoolType> school;
        private final DefaultConfig defaultConfig;
        private final CastType castType;

        protected SkyrimSpell(String name,
                              RegistryObject<SchoolType> school,
                              SpellRarity rarity,
                              int maxLevel,
                              double cooldownSeconds,
                              CastType castType,
                              int baseManaCost,
                              int manaCostPerLevel,
                              int baseSpellPower,
                              int spellPowerPerLevel,
                              int castTime) {
            this.spellId = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID, name);
            this.school = school;
            this.defaultConfig = new DefaultConfig()
                    .setMinRarity(rarity)
                    .setSchoolResource(school.getId())
                    .setMaxLevel(maxLevel)
                    .setCooldownSeconds(cooldownSeconds)
                    .build();
            this.castType = castType;
            this.baseManaCost = baseManaCost;
            this.manaCostPerLevel = manaCostPerLevel;
            this.baseSpellPower = baseSpellPower;
            this.spellPowerPerLevel = spellPowerPerLevel;
            this.castTime = castTime;
        }

        @Override
        public ResourceLocation getSpellResource() {
            return spellId;
        }

        @Override
        public DefaultConfig getDefaultConfig() {
            return defaultConfig;
        }

        @Override
        public CastType getCastType() {
            return castType;
        }

        @Override
        public SchoolType getSchoolType() {
            return school.get();
        }

        @Override
        public boolean needsLearning() {
            return true;
        }

        @Override
        public boolean isLearned(Player player) {
            return player != null && MagicData.getPlayerMagicData(player).getSyncedData().isSpellLearned(this);
        }

        @Override
        public float getSpellPower(int level, Entity source) {
            float power = super.getSpellPower(level, source);
            if (source instanceof LivingEntity caster) {
                power *= (float) SkyrimMagicScaling.spellPowerMultiplier(this, caster);
            }
            return power;
        }

        @Override
        public CastResult canBeCastedBy(int level, CastSource castSource, MagicData magicData, Player player) {
            CastResult rawResult = super.canBeCastedBy(level, castSource, magicData, player);
            if (rawResult.isSuccess()
                    || !castSource.consumesMana()
                    || getManaCost(level) <= SkyrimMagicScaling.adjustedManaCost(this, level, player)) {
                return rawResult;
            }
            if (ServerConfigs.DISABLE_ADVENTURE_MODE_CASTING.get()
                    && player instanceof ServerPlayer serverPlayer
                    && serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                return rawResult;
            }
            if (castSource.respectsCooldown() && magicData.getPlayerCooldowns().isOnCooldown(this)) {
                return rawResult;
            }

            int adjustedManaCost = SkyrimMagicScaling.adjustedManaCost(this, level, player);
            if (magicData.getMana() >= adjustedManaCost) {
                return new CastResult(CastResult.Type.SUCCESS);
            }
            return rawResult;
        }

        protected boolean consumeManualContinuousMana(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            if (level.isClientSide()
                    || getCastType() != CastType.CONTINUOUS
                    || getCastTime(spellLevel) > 0
                    || !(caster instanceof ServerPlayer player)
                    || playerMagicData == null
                    || !playerMagicData.isCasting()
                    || !getSpellId().equals(playerMagicData.getCastingSpellId())) {
                return true;
            }

            CastSource castSource = playerMagicData.getCastSource();
            if (castSource == null || !castSource.consumesMana()) {
                return true;
            }

            if (caster.tickCount % CONCENTRATION_MANA_INTERVAL != 0) {
                return true;
            }

            float intervalCost = SkyrimMagicScaling.adjustedManaCost(this, spellLevel, player)
                    * (CONCENTRATION_MANA_INTERVAL / (float) TICKS_PER_SECOND);
            if (intervalCost <= 0.0F) {
                return true;
            }

            float currentMana = playerMagicData.getMana();
            if (currentMana + 0.001F < intervalCost) {
                playerMagicData.setMana(0.0F);
                syncMana(player, playerMagicData);
                ServerboundCancelCast.cancelCast(player, false);
                return false;
            }

            float remainingMana = Math.max(0.0F, currentMana - intervalCost);
            playerMagicData.setMana(remainingMana);
            syncMana(player, playerMagicData);
            awardContinuousExperience(player, intervalCost);

            if (remainingMana <= 0.001F) {
                ServerboundCancelCast.cancelCast(player, false);
            }
            return true;
        }

        private void syncMana(ServerPlayer player, MagicData magicData) {
            Messages.sendToPlayer(new ClientboundSyncMana(magicData), player);
        }

        private void awardContinuousExperience(ServerPlayer player, float manaSpent) {
            SkillProgression.Skill skill = SkyrimMagicScaling.skillFor(this);
            if (skill != null && manaSpent > 0.0F) {
                SkillProgression.award(player, skill, Math.max(0.25D, Math.sqrt(manaSpent) * 0.45D));
            }
        }

        protected MutableComponent info(String key, Object value) {
            return Component.translatable(key, value).withStyle(ChatFormatting.GRAY);
        }

        protected String number(double value, int decimals) {
            return Utils.stringTruncation(value, decimals);
        }

        protected List<LivingEntity> getConeTargets(Level level,
                                                    LivingEntity caster,
                                                    float range,
                                                    float radius,
                                                    float minDot) {
            Vec3 eye = caster.getEyePosition();
            Vec3 look = caster.getLookAngle().normalize();
            AABB bounds = caster.getBoundingBox().expandTowards(look.scale(range)).inflate(radius);

            return level.getEntitiesOfClass(LivingEntity.class, bounds, target -> {
                        if (target == caster || !target.isAlive() || DamageSources.isFriendlyFireBetween(caster, target)) {
                            return false;
                        }

                        Vec3 toTarget = target.getBoundingBox().getCenter().subtract(eye);
                        double distance = toTarget.length();
                        if (distance <= 0.01D || distance > range) {
                            return false;
                        }

                        Vec3 direction = toTarget.normalize();
                        double dot = look.dot(direction);
                        double sideDistance = toTarget.subtract(look.scale(toTarget.dot(look))).length();
                        return dot >= minDot
                                && sideDistance <= radius + target.getBbWidth() * 0.5F
                                && Utils.hasLineOfSight(level, caster, target, true);
                    }).stream()
                    .sorted(Comparator.comparingDouble(caster::distanceToSqr))
                    .toList();
        }

        protected Optional<LivingEntity> getLookTarget(Level level, LivingEntity caster, float range, float inflate) {
            HitResult hit = Utils.raycastForEntity(level, caster, range, true, inflate);
            if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
                if (target != caster && target.isAlive() && !DamageSources.isFriendlyFireBetween(caster, target)) {
                    return Optional.of(target);
                }
            }
            return Optional.empty();
        }

        protected void spawnTrailParticles(Level level,
                                           LivingEntity caster,
                                           ParticleOptions particle,
                                           float range,
                                           int count,
                                           double spread) {
            Vec3 start = caster.getEyePosition().add(caster.getLookAngle().scale(0.8D));
            Vec3 step = caster.getLookAngle().normalize().scale(range / count);
            for (int i = 0; i < count; i++) {
                Vec3 position = start.add(step.scale(i));
                MagicManager.spawnParticles(level, particle, position.x, position.y, position.z,
                        1, spread, spread, spread, 0.01D, false);
            }
        }

        protected void spawnBurst(Level level,
                                  ParticleOptions particle,
                                  Vec3 position,
                                  int count,
                                  double spread,
                                  double speed) {
            MagicManager.spawnParticles(level, particle, position.x, position.y, position.z,
                    count, spread, spread, spread, speed, false);
        }

        protected void playSound(Level level, LivingEntity caster, SoundEvent sound, float volume, float pitch) {
            level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), sound, SoundSource.PLAYERS, volume, pitch);
        }

        protected int seconds(float seconds) {
            return Mth.floor(seconds * TICKS_PER_SECOND);
        }

        protected int durationFromPower(int level, LivingEntity caster, float baseSeconds, float secondsPerPower) {
            return seconds(baseSeconds + getSpellPower(level, caster) * secondsPerPower);
        }
    }

    private abstract static class ConeDamageSpell extends SkyrimSpell {
        private final ParticleOptions particle;
        private final RegistryObject<SoundEvent> sound;
        private final float range;

        protected ConeDamageSpell(String name, ParticleOptions particle, RegistryObject<SoundEvent> sound, int baseManaCost) {
            super(name, ModSpellSchools.DESTRUCTION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.CONTINUOUS, baseManaCost, 4, 8, 2, 0);
            this.particle = particle;
            this.sound = sound;
            this.range = 5.5F;
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.damage_per_second", number(getDamagePerSecond(level, caster), 1)),
                    info("ui.irons_spellbooks.cast_range", number(range, 1))
            );
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(sound.get());
        }

        @Override
        public void onServerCastTick(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            if (!consumeManualContinuousMana(level, spellLevel, caster, playerMagicData)) {
                return;
            }
            spawnTrailParticles(level, caster, particle, range, 7, 0.05D);
            if (caster.tickCount % CONCENTRATION_DAMAGE_INTERVAL == 0) {
                float damage = getDamagePerSecond(spellLevel, caster) * CONCENTRATION_TICK_FRACTION;
                getConeTargets(level, caster, range, 1.35F, 0.84F)
                        .forEach(target -> applyElementalHit(caster, target, spellLevel, damage));
            }
        }

        protected float getDamagePerSecond(int level, LivingEntity caster) {
            return getSpellPower(level, caster);
        }

        protected void applyElementalHit(LivingEntity caster, LivingEntity target, int spellLevel, float damage) {
            DamageSources.applyDamage(target, damage, getDamageSource(caster));
            DamageSources.ignoreNextKnockback(target);
        }
    }

    private static class FlamesSpell extends ConeDamageSpell {
        private FlamesSpell() {
            super("flames", ParticleHelper.FIRE, SoundRegistry.FIRE_CAST, 12);
        }

        @Override
        public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
            return super.getDamageSource(projectile, attacker).setFireTime(2);
        }

        @Override
        protected void applyElementalHit(LivingEntity caster, LivingEntity target, int spellLevel, float damage) {
            super.applyElementalHit(caster, target, spellLevel, damage);
            target.setSecondsOnFire(2);
        }
    }

    private static class FrostbiteSpell extends ConeDamageSpell {
        private FrostbiteSpell() {
            super("frostbite", ParticleHelper.SNOWFLAKE, SoundRegistry.ICE_CAST, 16);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.damage_per_second", number(getDamagePerSecond(level, caster), 1)),
                    info("ui.thuumcraft.stamina_damage_per_second", number(getDamagePerSecond(level, caster), 1)),
                    info("ui.irons_spellbooks.cast_range", number(5.5F, 1))
            );
        }

        @Override
        public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
            return super.getDamageSource(projectile, attacker).setFreezeTicks(60);
        }

        @Override
        protected void applyElementalHit(LivingEntity caster, LivingEntity target, int spellLevel, float damage) {
            super.applyElementalHit(caster, target, spellLevel, damage);
            target.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED.get(), 45, 0));
        }
    }

    private static class SparksSpell extends ConeDamageSpell {
        private SparksSpell() {
            super("sparks", ParticleHelper.ELECTRICITY, SoundRegistry.LIGHTNING_CAST, 19);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.damage_per_second", number(getDamagePerSecond(level, caster), 1)),
                    info("ui.thuumcraft.magicka_damage_per_second", number(getDamagePerSecond(level, caster), 1)),
                    info("ui.irons_spellbooks.cast_range", number(5.5F, 1))
            );
        }

        @Override
        protected void applyElementalHit(LivingEntity caster, LivingEntity target, int spellLevel, float damage) {
            super.applyElementalHit(caster, target, spellLevel, damage);
            MagicData targetMagic = MagicData.getPlayerMagicData(target);
            targetMagic.setMana(Math.max(0.0F, targetMagic.getMana() - damage));
            target.addEffect(new MobEffectInstance(ModEffects.DAMAGE_MAGICKA.get(), 40, 0));
        }
    }

    private static class HealingSpell extends SkyrimSpell {
        private HealingSpell() {
            super("healing", ModSpellSchools.RESTORATION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.CONTINUOUS, 12, 3, 10, 2, 0);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(info("ui.thuumcraft.healing_per_second", number(getHealingPerSecond(level, caster), 1)));
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(SoundRegistry.HOLY_CAST.get());
        }

        @Override
        public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.SELF_CAST_TWO_HANDS;
        }

        @Override
        public void onServerCastTick(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            if (!consumeManualContinuousMana(level, spellLevel, caster, playerMagicData)) {
                return;
            }
            if (caster.tickCount % CONCENTRATION_HEAL_INTERVAL == 0 && caster.getHealth() < caster.getMaxHealth()) {
                float heal = getHealingPerSecond(spellLevel, caster) * 0.5F;
                SpellHealEvent event = new SpellHealEvent(caster, caster, heal, getSchoolType());
                MinecraftForge.EVENT_BUS.post(event);
                caster.heal(event.getHealAmount());
                spawnBurst(level, ParticleTypes.HEART, caster.position().add(0.0D, caster.getBbHeight() * 0.65D, 0.0D),
                        3, 0.35D, 0.02D);
            }
        }

        private float getHealingPerSecond(int level, LivingEntity caster) {
            return getSpellPower(level, caster);
        }
    }

    private static class LesserWardSpell extends SkyrimSpell {
        private LesserWardSpell() {
            super("lesser_ward", ModSpellSchools.RESTORATION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.CONTINUOUS, 34, 5, 40, 5, 0);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.armor_rating", number(getArmorRating(level, caster), 0)),
                    info("ui.irons_spellbooks.absorption", number(getTemporaryHealth(level, caster), 0))
            );
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(SoundRegistry.HOLY_CAST.get());
        }

        @Override
        public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.ANIMATION_CONTINUOUS_CAST;
        }

        @Override
        public void onServerCastTick(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            if (!consumeManualContinuousMana(level, spellLevel, caster, playerMagicData)) {
                return;
            }
            if (caster.tickCount % 10 == 0) {
                caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 18, 0, false, false, true));
                caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 18, Math.max(0, spellLevel - 1), false, false, true));
                caster.addEffect(new MobEffectInstance(ModEffects.SPELL_ABSORPTION.get(), 18, 0, false, false, true));
                spawnBurst(level, ParticleHelper.WISP, caster.position().add(0.0D, 1.0D, 0.0D), 8, 0.5D, 0.01D);
            }
        }

        private float getArmorRating(int level, LivingEntity caster) {
            return getSpellPower(level, caster);
        }

        private float getTemporaryHealth(int level, LivingEntity caster) {
            return 4.0F + level * 2.0F;
        }
    }

    private static class OakfleshSpell extends SkyrimSpell {
        private OakfleshSpell() {
            super("oakflesh", ModSpellSchools.ALTERATION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.LONG, 103, 10, 60, 8, 20);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.armor_rating", number(getArmorRating(level, caster), 0)),
                    info("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(level, caster), 1))
            );
        }

        @Override
        public Optional<SoundEvent> getCastFinishSound() {
            return Optional.of(SoundRegistry.OAKSKIN_CAST.get());
        }

        @Override
        public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.SELF_CAST_ANIMATION;
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
            caster.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), getDuration(spellLevel, caster), 0,
                    false, false, true));
            caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, getDuration(spellLevel, caster), 0,
                    false, false, true));
            spawnBurst(level, ParticleTypes.COMPOSTER, caster.position().add(0.0D, 1.0D, 0.0D), 24, 0.7D, 0.02D);
            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
        }

        private int getDuration(int level, LivingEntity caster) {
            return durationFromPower(level, caster, 0.0F, 1.0F);
        }

        private float getArmorRating(int level, LivingEntity caster) {
            return getSpellPower(level, caster);
        }
    }

    private static class CourageSpell extends SkyrimSpell {
        private CourageSpell() {
            super("courage", ModSpellSchools.ILLUSION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.LONG, 39, 6, 60, 8, 15);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(level, caster), 1)),
                    info("ui.thuumcraft.courage_bonus", number(getTemporaryHealth(level, caster), 0))
            );
        }

        @Override
        public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            Utils.preCastTargetHelper(level, caster, playerMagicData, this, 16, 0.35F,
                    true, target -> !DamageSources.isFriendlyFireBetween(caster, target));
            return true;
        }

        @Override
        public Optional<SoundEvent> getCastFinishSound() {
            return Optional.of(SoundRegistry.EVOCATION_CAST.get());
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
            LivingEntity target = getTargetFromData(level, playerMagicData).orElse(caster);
            int duration = getDuration(spellLevel, caster);
            target.removeEffect(ModEffects.FEAR.get());
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 0, false, false, true));
            target.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, Math.max(0, spellLevel - 1),
                    false, false, true));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 0, false, false, true));
            spawnBurst(level, ParticleTypes.ENCHANT, target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D),
                    20, 0.7D, 0.02D);
            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
        }

        private Optional<LivingEntity> getTargetFromData(Level level, MagicData playerMagicData) {
            if (level instanceof ServerLevel serverLevel
                    && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
                return Optional.ofNullable(targetData.getTarget(serverLevel));
            }
            return Optional.empty();
        }

        private int getDuration(int level, LivingEntity caster) {
            return durationFromPower(level, caster, 0.0F, 1.0F);
        }

        private float getTemporaryHealth(int level, LivingEntity caster) {
            return 4.0F + level * 2.0F + getSpellPower(level, caster) * 0.05F;
        }
    }

    private static class FurySpell extends SkyrimSpell {
        private FurySpell() {
            super("fury", ModSpellSchools.ILLUSION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.LONG, 67, 8, 6, 3, 20);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.target_level", number(getTargetLevel(level, caster), 0)),
                    info("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(level, caster), 1))
            );
        }

        @Override
        public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            return Utils.preCastTargetHelper(level, caster, playerMagicData, this, 24, 0.35F);
        }

        @Override
        public Optional<SoundEvent> getCastFinishSound() {
            return Optional.of(SoundRegistry.EVOCATION_CAST.get());
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
            if (level instanceof ServerLevel serverLevel
                    && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
                LivingEntity target = targetData.getTarget(serverLevel);
                if (target != null && canAffect(spellLevel, caster, target)) {
                    target.addEffect(new MobEffectInstance(ModEffects.FRENZY.get(), getDuration(spellLevel, caster), 0,
                            false, false, true));
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, seconds(3), 0, false, false, true));
                    SkyrimSpellEvents.redirectFrenziedMob(target);
                    spawnBurst(level, ParticleTypes.ANGRY_VILLAGER,
                            target.position().add(0.0D, target.getBbHeight() * 0.75D, 0.0D), 12, 0.4D, 0.02D);
                }
            }
            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
        }

        private boolean canAffect(int level, LivingEntity caster, LivingEntity target) {
            return target.getMaxHealth() <= 20.0F + getTargetLevel(level, caster) * 4.0F;
        }

        private float getTargetLevel(int level, LivingEntity caster) {
            return getSpellPower(level, caster);
        }

        private int getDuration(int level, LivingEntity caster) {
            return durationFromPower(level, caster, 5.0F, 5.0F);
        }
    }

    private static class ClairvoyanceSpell extends SkyrimSpell {
        private ClairvoyanceSpell() {
            super("clairvoyance", ModSpellSchools.ILLUSION, SpellRarity.COMMON, 1, 0.0D,
                    CastType.CONTINUOUS, 25, 0, 1, 0, 0);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(info("ui.thuumcraft.guidance_target", Component.translatable("ui.thuumcraft.guidance_target.spawn")));
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(SoundRegistry.EVOCATION_CAST.get());
        }

        @Override
        public void onServerCastTick(Level level, int spellLevel, LivingEntity caster, MagicData playerMagicData) {
            if (!consumeManualContinuousMana(level, spellLevel, caster, playerMagicData)) {
                return;
            }
            if (!(caster instanceof ServerPlayer player) || !(level instanceof ServerLevel serverLevel)) {
                return;
            }

            BlockPos target = player.getRespawnPosition();
            if (target == null || !player.getRespawnDimension().equals(serverLevel.dimension())) {
                target = serverLevel.getSharedSpawnPos();
            }

            Vec3 start = player.getEyePosition();
            Vec3 destination = Vec3.atCenterOf(target);
            Vec3 direction = destination.subtract(start);
            if (direction.lengthSqr() > 1.0D) {
                direction = direction.normalize();
            }

            if (player.tickCount % 4 == 0) {
                for (int i = 1; i <= 8; i++) {
                    Vec3 point = start.add(direction.scale(i * 0.75D));
                    MagicManager.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, point.x, point.y - 0.2D, point.z,
                            1, 0.02D, 0.02D, 0.02D, 0.0D, false);
                }
            }

            if (player.tickCount % 40 == 0) {
                player.displayClientMessage(Component.translatable("message.thuumcraft.clairvoyance",
                        target.getX(), target.getY(), target.getZ()), true);
            }
        }
    }

    private static class ConjureFamiliarSpell extends SkyrimSpell {
        private ConjureFamiliarSpell() {
            super("conjure_familiar", ModSpellSchools.CONJURATION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.LONG, 107, 8, 60, 8, 25);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.summon", Component.translatable("entity.minecraft.wolf")),
                    info("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(level, caster), 1))
            );
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
        }

        @Override
        public Optional<SoundEvent> getCastFinishSound() {
            return Optional.of(SoundRegistry.RAISE_DEAD_FINISH.get());
        }

        @Override
        public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.ANIMATION_LONG_CAST;
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
            if (!(level instanceof ServerLevel serverLevel)) {
                return;
            }

            Wolf familiar = EntityType.WOLF.create(serverLevel);
            if (familiar != null) {
                Vec3 position = Utils.moveToRelativeGroundLevel(level,
                        caster.position().add(caster.getLookAngle().normalize().scale(1.8D)), 4);
                familiar.moveTo(position.x, position.y, position.z, caster.getYRot(), 0.0F);
                if (caster instanceof Player player) {
                    familiar.tame(player);
                    familiar.setOrderedToSit(false);
                }
                familiar.setCustomName(Component.translatable("entity.thuumcraft.conjured_familiar"));
                familiar.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, getDuration(spellLevel, caster), Math.max(0, spellLevel - 1)));
                familiar.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, getDuration(spellLevel, caster), 0));
                markTemporarySummon(familiar, getDuration(spellLevel, caster));
                serverLevel.addFreshEntity(familiar);
                playSound(level, caster, SoundEvents.WOLF_AMBIENT, 0.8F, 1.2F);
                spawnBurst(level, ParticleHelper.WISP, familiar.position().add(0.0D, 0.6D, 0.0D), 20, 0.5D, 0.03D);
            }

            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
        }

        private int getDuration(int level, LivingEntity caster) {
            return durationFromPower(level, caster, 0.0F, 1.0F);
        }
    }

    private static class RaiseZombieSpell extends SkyrimSpell {
        private RaiseZombieSpell() {
            super("raise_zombie", ModSpellSchools.CONJURATION, SpellRarity.COMMON, NOVICE_MAX_LEVEL, 0.0D,
                    CastType.LONG, 103, 10, 60, 8, 25);
        }

        @Override
        public List<MutableComponent> getUniqueInfo(int level, LivingEntity caster) {
            return List.of(
                    info("ui.thuumcraft.summon", Component.translatable("entity.minecraft.zombie")),
                    info("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getDuration(level, caster), 1))
            );
        }

        @Override
        public Optional<SoundEvent> getCastStartSound() {
            return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
        }

        @Override
        public Optional<SoundEvent> getCastFinishSound() {
            return Optional.of(SoundRegistry.RAISE_DEAD_FINISH.get());
        }

        @Override
        public AnimationHolder getCastStartAnimation() {
            return SpellAnimations.ANIMATION_LONG_CAST;
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
            if (!(level instanceof ServerLevel serverLevel)) {
                return;
            }

            SummonedZombie zombie = new SummonedZombie(level, caster, true);
            Vec3 position = Utils.moveToRelativeGroundLevel(level,
                    caster.position().add(caster.getLookAngle().normalize().scale(1.8D)), 6);
            zombie.moveTo(position.x, position.y, position.z, caster.getYRot(), 0.0F);
            DifficultyInstance difficulty = level.getCurrentDifficultyAt(zombie.blockPosition());
            zombie.finalizeSpawn(serverLevel, difficulty, MobSpawnType.MOB_SUMMONED,
                    (SpawnGroupData) null, (CompoundTag) null);
            zombie.addEffect(new MobEffectInstance(MobEffectRegistry.RAISE_DEAD_TIMER.get(), getDuration(spellLevel, caster),
                    0, false, false, false));
            zombie.setCustomName(Component.translatable("entity.thuumcraft.raised_zombie"));
            serverLevel.addFreshEntity(zombie);
            spawnBurst(level, ParticleHelper.BLOOD_GROUND, zombie.position().add(0.0D, 0.25D, 0.0D), 24, 0.6D, 0.02D);

            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
        }

        private int getDuration(int level, LivingEntity caster) {
            return durationFromPower(level, caster, 0.0F, 1.0F);
        }
    }

    static void markTemporarySummon(LivingEntity entity, int ticks) {
        entity.getPersistentData().putInt(SkyrimSpellEvents.CONJURED_TICKS_TAG, ticks);
    }
}
