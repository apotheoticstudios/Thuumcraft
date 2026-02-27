package net.apotheoticstudios.thuumcraft.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties JUNIPER_BERRIES = new FoodProperties.Builder().nutrition(2).fast()
            .saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20), 1f).build();
}
