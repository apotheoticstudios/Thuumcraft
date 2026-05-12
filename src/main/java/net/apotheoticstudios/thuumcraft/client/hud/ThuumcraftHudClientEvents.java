package net.apotheoticstudios.thuumcraft.client.hud;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.client.ClientStaminaState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class ThuumcraftHudClientEvents {
    private static final ResourceLocation IRONS_MANA_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "mana_overlay");

    private ThuumcraftHudClientEvents() {
    }

    @SubscribeEvent
    public static void hideReplacedOverlays(RenderGuiOverlayEvent.Pre event) {
        if (Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get() && shouldHideOverlay(event.getOverlay().id())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void preventEmptyStaminaSprinting(TickEvent.ClientTickEvent event) {
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                || !Config.ENABLE_STAMINA_SYSTEM.get()
                || !Config.ENABLE_STAMINA_SPRINT_LIMIT.get()
                || event.phase != TickEvent.Phase.END) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator() || !player.isSprinting()) {
            return;
        }

        if (ClientStaminaState.stamina() < getSprintStartStamina(player)) {
            player.setSprinting(false);
        }
    }

    private static double getSprintStartStamina(LocalPlayer player) {
        AttributeInstance stamina = player.getAttribute(ModAttributes.STAMINA.get());
        double maxStamina = stamina == null ? 100.0D : stamina.getValue();
        return Math.max(Config.STAMINA_SPRINT_START_FLOOR.get(),
                maxStamina * Config.STAMINA_SPRINT_START_RATIO.get());
    }

    private static boolean shouldHideOverlay(ResourceLocation overlayId) {
        if (overlayId.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            return Config.ENABLE_STAMINA_SYSTEM.get() && Config.ENABLE_STAMINA_HUNGER_OVERRIDE.get();
        }
        if (!Config.ENABLE_SKYRIM_HUD.get()) {
            return false;
        }
        if (overlayId.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            return Config.SHOW_PLAYER_HEALTH_BAR.get();
        }
        if (overlayId.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
            return Config.SHOW_ARMOR_ICONS.get();
        }
        if (overlayId.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
            return Config.SHOW_AIR_ICONS.get();
        }
        if (overlayId.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            return Config.SHOW_EXPERIENCE_BAR.get();
        }
        if (overlayId.equals(IRONS_MANA_OVERLAY)) {
            return Config.SHOW_MANA_BAR.get();
        }
        return false;
    }
}
