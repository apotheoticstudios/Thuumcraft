package net.apotheoticstudios.thuumcraft.entity.ai;

import net.apotheoticstudios.thuumcraft.entity.custom.GiantEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class GiantAttackGoal extends MeleeAttackGoal {
    private static final int ATTACK_ANIMATION_TICKS = 35;
    private static final int ATTACK_DAMAGE_TICK = 18;
    private static final int ATTACK_RECOVERY_TICKS = 18;

    private final GiantEntity entity;
    private int attackAnimationTick = 0;
    private int attackRecoveryTicks = 0;
    private boolean hasAppliedDamage = false;

    public GiantAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((GiantEntity) pMob);
    }

    @Override
    public void start() {
        super.start();
        this.attackAnimationTick = 0;
        this.attackRecoveryTicks = 0;
        this.hasAppliedDamage = false;
        this.entity.setAttacking(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());

            if (this.entity.isAttacking()) {
                this.attackAnimationTick++;

                if (!this.hasAppliedDamage && this.attackAnimationTick >= ATTACK_DAMAGE_TICK) {
                    this.hasAppliedDamage = true;
                    performAttack(pEnemy);
                }

                if (this.attackAnimationTick >= ATTACK_ANIMATION_TICKS) {
                    this.entity.setAttacking(false);
                    this.attackAnimationTick = 0;
                    this.attackRecoveryTicks = ATTACK_RECOVERY_TICKS;
                }
            } else if (this.attackRecoveryTicks > 0) {
                this.attackRecoveryTicks--;
            } else {
                this.entity.setAttacking(true);
                this.attackAnimationTick = 0;
                this.hasAppliedDamage = false;
            }
        } else {
            this.attackAnimationTick = 0;
            this.attackRecoveryTicks = 0;
            this.hasAppliedDamage = false;
            this.entity.setAttacking(false);
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void performAttack(LivingEntity pEnemy) {
        if (!this.mob.isAlive() || !pEnemy.isAlive()) {
            return;
        }

        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void stop() {
        this.attackAnimationTick = 0;
        this.attackRecoveryTicks = 0;
        this.hasAppliedDamage = false;
        this.entity.setAttacking(false);
        super.stop();
    }
}
