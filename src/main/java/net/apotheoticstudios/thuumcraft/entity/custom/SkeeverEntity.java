package net.apotheoticstudios.thuumcraft.entity.custom;

import net.apotheoticstudios.thuumcraft.entity.ai.SkeeverAttackGoal;
import net.apotheoticstudios.thuumcraft.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SkeeverEntity extends Monster {
    private static final int ATTACK_ANIMATION_LENGTH = 25;

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(SkeeverEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHASING_TARGET =
            SynchedEntityData.defineId(SkeeverEntity.class, EntityDataSerializers.BOOLEAN);

    public SkeeverEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    private boolean wasAttacking = false;


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            -- this.idleAnimationTimeout;

        }

        if (this.isAttacking()) {
            if (!this.wasAttacking) {
                attackAnimationTimeout = ATTACK_ANIMATION_LENGTH;
                attackAnimationState.start(this.tickCount);
            } else if (attackAnimationTimeout > 0) {
                --this.attackAnimationTimeout;
            }
        } else {
            attackAnimationTimeout = 0;
            attackAnimationState.stop();
        }

        if (attackAnimationTimeout <= 0 && this.wasAttacking && !this.isAttacking()) {
            attackAnimationState.stop();
        }

        this.wasAttacking = this.isAttacking();
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isChasingTarget() {
        return this.entityData.get(CHASING_TARGET);
    }

    public void setChasingTarget(boolean chasingTarget) {
        this.entityData.set(CHASING_TARGET, chasingTarget);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(CHASING_TARGET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SkeeverAttackGoal(this, 1.1D, true));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().
                add(Attributes.FOLLOW_RANGE, 30.0D).
                add(Attributes.MAX_HEALTH, 14.0D).
                add(Attributes.MOVEMENT_SPEED, (double) 0.35F).
                add(Attributes.ATTACK_DAMAGE, 2.5D).
                add(Attributes.ARMOR, 1.0D);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.SKEEVER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.SKEEVER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SKEEVER_DEATH.get();
    }
}
