package net.apotheoticstudios.thuumcraft.magic.spell;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SkyrimSpellEvents {
    static final String CONJURED_TICKS_TAG = Thuumcraft.MOD_ID + ":conjured_ticks";

    private SkyrimSpellEvents() {
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }

        tickTemporarySummon(entity);

        if (entity.tickCount % 10 == 0 && entity.hasEffect(ModEffects.FRENZY.get())) {
            redirectFrenziedMob(entity);
        }
    }

    static void redirectFrenziedMob(LivingEntity entity) {
        if (!(entity instanceof Mob mob)) {
            return;
        }

        LivingEntity currentTarget = mob.getTarget();
        if (currentTarget != null && currentTarget.isAlive() && mob.distanceToSqr(currentTarget) < 256.0D) {
            return;
        }

        Level level = mob.level();
        AABB searchBox = mob.getBoundingBox().inflate(16.0D);
        level.getEntitiesOfClass(LivingEntity.class, searchBox, candidate -> isFrenzyTarget(mob, candidate)).stream()
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .ifPresent(mob::setTarget);
    }

    private static boolean isFrenzyTarget(Mob mob, LivingEntity candidate) {
        if (candidate == mob || !candidate.isAlive() || candidate.isSpectator()) {
            return false;
        }
        if (candidate instanceof Player player && player.getAbilities().instabuild) {
            return false;
        }
        return true;
    }

    private static void tickTemporarySummon(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        if (!data.contains(CONJURED_TICKS_TAG)) {
            return;
        }

        int ticks = data.getInt(CONJURED_TICKS_TAG) - 1;
        if (ticks <= 0) {
            entity.discard();
        } else {
            data.putInt(CONJURED_TICKS_TAG, ticks);
        }
    }
}
