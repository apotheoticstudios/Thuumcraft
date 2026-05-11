package net.apotheoticstudios.thuumcraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class ParalysisMobEffect extends MobEffect {
    public ParalysisMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x9FA889);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Vec3 movement = entity.getDeltaMovement();
        entity.setDeltaMovement(0.0D, movement.y, 0.0D);
        entity.setJumping(false);

        if (entity instanceof Mob mob) {
            mob.getNavigation().stop();
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
