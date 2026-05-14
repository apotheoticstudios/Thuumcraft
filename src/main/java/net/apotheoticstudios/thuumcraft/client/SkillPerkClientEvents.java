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

    private SkillPerkClientEvents() {
    }

    @SubscribeEvent
    public static void applyEagleEyeZoom(ComputeFovModifierEvent event) {
        if (!isEagleEyeZooming()) {
            return;
        }

        float zoom = ClientSkillPerkState.rank(SkillPerk.ARCHERY_STEADY_HAND) > 0 ? 0.55F : 0.68F;
        event.setNewFovModifier(event.getNewFovModifier() * zoom);
    }

    @SubscribeEvent
    public static void syncEagleEyeZoom(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null) {
            lastSentEagleEyeZooming = false;
            return;
        }

        boolean zooming = isEagleEyeZooming();
        if (zooming != lastSentEagleEyeZooming) {
            lastSentEagleEyeZooming = zooming;
            ModMessages.sendToServer(new ServerboundEagleEyeZoomPacket(zooming));
        }
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
