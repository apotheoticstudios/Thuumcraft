package net.apotheoticstudios.thuumcraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.apotheoticstudios.thuumcraft.network.ServerboundEagleEyeZoomPacket;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class SkillPerkClientEvents {
    public static final KeyMapping EAGLE_EYE_ZOOM = new KeyMapping(
            "key.thuumcraft.eagle_eye_zoom",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "key.categories.thuumcraft");

    private static boolean lastSentEagleEyeZooming;
    private static float displayedEagleEyeZoom = 1.0F;

    private SkillPerkClientEvents() {
    }

    @SubscribeEvent
    public static void applyEagleEyeZoom(ComputeFovModifierEvent event) {
        if (displayedEagleEyeZoom >= 0.999F) {
            return;
        }

        event.setNewFovModifier(event.getNewFovModifier() * displayedEagleEyeZoom);
    }

    @SubscribeEvent
    public static void syncEagleEyeZoom(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null) {
            lastSentEagleEyeZooming = false;
            displayedEagleEyeZoom = 1.0F;
            return;
        }

        boolean zooming = isEagleEyeZooming();
        updateDisplayedZoom(zooming);
        if (zooming != lastSentEagleEyeZooming) {
            lastSentEagleEyeZooming = zooming;
            ModMessages.sendToServer(new ServerboundEagleEyeZoomPacket(zooming));
        }
    }

    private static void updateDisplayedZoom(boolean zooming) {
        float targetZoom = 1.0F;
        if (zooming) {
            targetZoom = ClientSkillPerkState.rank(SkillPerk.ARCHERY_STEADY_HAND) > 0 ? 0.24F : 0.34F;
        }

        float step = zooming ? 0.025F : 0.09F;
        displayedEagleEyeZoom = Mth.clamp(Mth.approach(displayedEagleEyeZoom, targetZoom, step),
                0.2F, 1.0F);
    }

    private static boolean isEagleEyeZooming() {
        Minecraft minecraft = Minecraft.getInstance();
        return Config.ENABLE_SKILL_SYSTEM.get()
                && minecraft.screen == null
                && minecraft.player != null
                && ClientSkillPerkState.rank(SkillPerk.ARCHERY_EAGLE_EYE) > 0
                && EAGLE_EYE_ZOOM.isDown()
                && minecraft.player.isUsingItem()
                && isRangedWeapon(minecraft.player.getUseItem());
    }

    private static boolean isRangedWeapon(ItemStack stack) {
        return stack.getItem() instanceof BowItem
                || stack.getItem() instanceof CrossbowItem
                || stack.is(ModTags.Items.RANGED_WEAPONS);
    }
}
