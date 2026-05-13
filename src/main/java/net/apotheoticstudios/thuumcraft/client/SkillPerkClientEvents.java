package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class SkillPerkClientEvents {
    private SkillPerkClientEvents() {
    }

    @SubscribeEvent
    public static void applyEagleEyeZoom(ComputeFovModifierEvent event) {
        if (ClientSkillPerkState.rank(SkillPerk.ARCHERY_EAGLE_EYE) <= 0
                || !event.getPlayer().isUsingItem()
                || !isRangedWeapon(event.getPlayer().getUseItem())) {
            return;
        }

        float zoom = ClientSkillPerkState.rank(SkillPerk.ARCHERY_STEADY_HAND) > 0 ? 0.55F : 0.68F;
        event.setNewFovModifier(event.getNewFovModifier() * zoom);
    }

    private static boolean isRangedWeapon(ItemStack stack) {
        return stack.getItem() instanceof BowItem
                || stack.getItem() instanceof CrossbowItem
                || stack.is(ModTags.Items.RANGED_WEAPONS);
    }
}
