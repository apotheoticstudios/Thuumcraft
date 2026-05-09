package net.apotheoticstudios.thuumcraft.entity.ai;

import net.apotheoticstudios.thuumcraft.entity.custom.SkeeverEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SkeeverAttackGoal extends MeleeAttackGoal {
    private static final int ATTACK_ANIMATION_TICKS = 25;
    private static final int ATTACK_DAMAGE_DELAY_TICKS = 20;
    private static final int ATTACK_COOLDOWN_TICKS = 40;

    private final SkeeverEntity entity;
    private int ticksUntilNextAttack = ATTACK_ANIMATION_TICKS;
    private boolean shouldCountTillNextAttack = false;
    private boolean hasHitDuringAttack = false;

    public SkeeverAttackGoal(SkeeverEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public void start() {
        super.start();
        ticksUntilNextAttack = ATTACK_ANIMATION_TICKS;
        shouldCountTillNextAttack = false;
        hasHitDuringAttack = false;
        entity.setAttacking(false);
        entity.setChasingTarget(true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;
            entity.setAttacking(isTimeToStartAttackAnimation());

            if (isTimeToApplyDamage()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }

            if (isAttackAnimationComplete()) {
                resetAttackCooldown();
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(ATTACK_COOLDOWN_TICKS);
        this.hasHitDuringAttack = false;
    }

    protected boolean isTimeToApplyDamage() {
        return !this.hasHitDuringAttack && this.ticksUntilNextAttack <= ATTACK_ANIMATION_TICKS - ATTACK_DAMAGE_DELAY_TICKS;
    }

    protected boolean isAttackAnimationComplete() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= ATTACK_ANIMATION_TICKS;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.hasHitDuringAttack = true;
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setChasingTarget(false);
        entity.setAttacking(false);
        super.stop();
    }
}
