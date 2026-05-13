package net.apotheoticstudios.thuumcraft.compat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.fml.ModList;

public final class EpicFightCompat {
    public static final String MOD_ID = "epicfight";
    private static final String EPIC_FIGHT_DAMAGE_SOURCE = "yesman.epicfight.world.damagesource.EpicFightDamageSource";
    private static boolean applyingSecondaryDamage;

    private EpicFightCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isEpicFightDamageSource(DamageSource source) {
        return isLoaded() && source != null && EPIC_FIGHT_DAMAGE_SOURCE.equals(source.getClass().getName());
    }

    public static boolean isApplyingSecondaryDamage() {
        return applyingSecondaryDamage;
    }

    public static void setApplyingSecondaryDamage(boolean applying) {
        applyingSecondaryDamage = applying;
    }
}
