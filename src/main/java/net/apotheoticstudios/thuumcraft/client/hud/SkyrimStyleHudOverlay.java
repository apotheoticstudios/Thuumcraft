package net.apotheoticstudios.thuumcraft.client.hud;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.client.ClientManaState;
import net.apotheoticstudios.thuumcraft.client.ClientStaminaState;
import net.apotheoticstudios.thuumcraft.client.ClientTargetHealthState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public final class SkyrimStyleHudOverlay implements IGuiOverlay {
    public static final SkyrimStyleHudOverlay INSTANCE = new SkyrimStyleHudOverlay();

    private static final ResourceLocation COMPASS_BAR = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/compass_bar.png");
    private static final ResourceLocation TARGET_HEALTH_FRAME = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/target_health_frame.png");
    private static final ResourceLocation TARGET_HEALTH_FILL = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/target_health_fill.png");
    private static final ResourceLocation PLAYER_BAR_FRAME = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/player_bar_frame.png");
    private static final ResourceLocation HEALTH_BAR_FILL = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/health_bar_fill.png");
    private static final ResourceLocation MANA_BAR_FILL = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/mana_bar_fill.png");
    private static final ResourceLocation STAMINA_BAR_FILL = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/stamina_bar_fill.png");
    private static final ResourceLocation ARMOR_EMPTY = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/armor_empty.png");
    private static final ResourceLocation ARMOR_HALF = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/armor_half.png");
    private static final ResourceLocation ARMOR_FULL = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/armor_full.png");
    private static final ResourceLocation AIR = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/air.png");
    private static final ResourceLocation AIR_BURSTING = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/air_bursting.png");
    private static final ResourceLocation XP_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/experience_bar_background.png");
    private static final ResourceLocation XP_PROGRESS = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID,
            "textures/gui/hud/experience_bar_progress.png");

    private static final int COMPASS_BAR_WIDTH = 221;
    private static final int COMPASS_BAR_HEIGHT = 14;
    private static final int TARGET_HEALTH_FRAME_WIDTH = 156;
    private static final int TARGET_HEALTH_FRAME_HEIGHT = 8;
    private static final int TARGET_HEALTH_FILL_WIDTH = 142;
    private static final int TARGET_HEALTH_FILL_HEIGHT = 3;
    private static final int TARGET_HEALTH_Y_WITH_COMPASS = 38;
    private static final int TARGET_HEALTH_Y_WITHOUT_COMPASS = 18;
    private static final double TARGET_HEALTH_MAX_DISTANCE_SQR = 96.0D * 96.0D;
    private static final int PLAYER_BAR_WIDTH = 102;
    private static final int PLAYER_BAR_HEIGHT = 10;
    private static final int PLAYER_BAR_FILL_X = 12;
    private static final int PLAYER_BAR_FILL_Y = 2;
    private static final int PLAYER_BAR_FILL_WIDTH = 78;
    private static final int PLAYER_BAR_FILL_HEIGHT = 6;
    private static final int HUD_BOTTOM_Y_OFFSET = 40;
    private static final int XP_BAR_WIDTH = 182;
    private static final int XP_BAR_HEIGHT = 5;
    private static float displayedMana = -1.0F;

    private SkyrimStyleHudOverlay() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                || !Config.ENABLE_SKYRIM_HUD.get()
                || player == null
                || minecraft.options.hideGui
                || minecraft.options.renderDebug
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        if (Config.SHOW_SKYRIM_COMPASS.get()) {
            renderCompass(graphics, player, partialTick, width);
        }
        if (Config.SHOW_TARGET_HEALTH_BAR.get()) {
            renderTargetHealth(graphics, minecraft, width);
        }
        renderPlayerBars(graphics, player, width, height);
        if (Config.SHOW_ARMOR_ICONS.get()) {
            renderArmor(graphics, player, width, height);
        }
        if (Config.SHOW_AIR_ICONS.get()) {
            renderAir(graphics, player, width, height);
        }
        if (Config.SHOW_EXPERIENCE_BAR.get()) {
            renderExperience(graphics, minecraft, width, height);
        }
    }

    private static void renderCompass(GuiGraphics graphics, Player player, float partialTick, int width) {
        int x = width / 2 - 110;
        graphics.blit(COMPASS_BAR, x, 10, 0, 0, COMPASS_BAR_WIDTH, COMPASS_BAR_HEIGHT,
                COMPASS_BAR_WIDTH, COMPASS_BAR_HEIGHT);

        float yaw = Mth.lerp(partialTick, player.yHeadRotO, player.yHeadRot) % 360.0F;
        if (yaw < 0.0F) {
            yaw += 360.0F;
        }

        drawCardinalDirection(graphics, yaw, 0.0F, width / 2, "S");
        drawCardinalDirection(graphics, yaw, 90.0F, width / 2, "W");
        drawCardinalDirection(graphics, yaw, 180.0F, width / 2, "N");
        drawCardinalDirection(graphics, yaw, 270.0F, width / 2, "E");
    }

    private static void renderTargetHealth(GuiGraphics graphics, Minecraft minecraft, int width) {
        LivingEntity livingTarget = getSkyrimStyleHealthTarget(minecraft);
        if (livingTarget == null) {
            return;
        }

        float maxHealth = Math.max(1.0F, livingTarget.getMaxHealth());
        float ratio = Mth.clamp(livingTarget.getHealth() / maxHealth, 0.0F, 1.0F);
        int fillWidth = ratio <= 0.0F ? 0 : Mth.clamp((int) Math.floor(TARGET_HEALTH_FILL_WIDTH * ratio), 1,
                TARGET_HEALTH_FILL_WIDTH);
        int x = width / 2 - TARGET_HEALTH_FRAME_WIDTH / 2;
        int y = Config.SHOW_SKYRIM_COMPASS.get() ? TARGET_HEALTH_Y_WITH_COMPASS : TARGET_HEALTH_Y_WITHOUT_COMPASS;

        String name = livingTarget.getDisplayName().getString();
        graphics.drawCenteredString(minecraft.font, name, width / 2, y - 11, 0xC0C0C0);
        graphics.blit(TARGET_HEALTH_FRAME, x, y, 0, 0, TARGET_HEALTH_FRAME_WIDTH, TARGET_HEALTH_FRAME_HEIGHT,
                TARGET_HEALTH_FRAME_WIDTH, TARGET_HEALTH_FRAME_HEIGHT);
        if (fillWidth > 0) {
            int fillInset = (TARGET_HEALTH_FILL_WIDTH - fillWidth) / 2;
            graphics.blit(TARGET_HEALTH_FILL, x + 9 + fillInset, y + 2, fillInset, 0, fillWidth,
                    TARGET_HEALTH_FILL_HEIGHT, TARGET_HEALTH_FILL_WIDTH, TARGET_HEALTH_FILL_HEIGHT);
        }
    }

    private static LivingEntity getSkyrimStyleHealthTarget(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player == null || minecraft.level == null) {
            ClientTargetHealthState.reset();
            return null;
        }

        long gameTime = minecraft.level.getGameTime();
        if (!ClientTargetHealthState.shouldShow(gameTime)) {
            ClientTargetHealthState.reset();
            return null;
        }

        Entity target = minecraft.level.getEntity(ClientTargetHealthState.entityId());
        if (target instanceof LivingEntity livingTarget && isValidHealthTarget(player, livingTarget)) {
            return livingTarget;
        }

        ClientTargetHealthState.reset();
        return null;
    }

    private static boolean isValidHealthTarget(Player player, LivingEntity target) {
        return target != null
                && target instanceof Mob
                && target.isAlive()
                && target != player
                && !target.isInvisibleTo(player)
                && player.distanceToSqr(target) <= TARGET_HEALTH_MAX_DISTANCE_SQR;
    }

    private static void renderPlayerBars(GuiGraphics graphics, Player player, int width, int height) {
        int y = height - HUD_BOTTOM_Y_OFFSET;
        if (Config.SHOW_MANA_BAR.get()) {
            drawPlayerBar(graphics, 20, y, MANA_BAR_FILL,
                    getDisplayedMana(ClientManaState.mana()), ClientManaState.maxMana());
        }
        if (Config.SHOW_PLAYER_HEALTH_BAR.get()) {
            drawPlayerBar(graphics, width / 2 - PLAYER_BAR_WIDTH / 2, y, HEALTH_BAR_FILL,
                    player.getHealth(), Math.max(1.0F, player.getMaxHealth()));
        }
        if (Config.SHOW_STAMINA_BAR.get() && Config.ENABLE_STAMINA_SYSTEM.get()) {
            drawPlayerBar(graphics, width - 120, y, STAMINA_BAR_FILL,
                    ClientStaminaState.stamina(), ClientStaminaState.maxStamina());
        }
    }

    private static void renderArmor(GuiGraphics graphics, Player player, int width, int height) {
        int armor = Mth.clamp(player.getArmorValue(), 0, 20);
        if (armor <= 0) {
            return;
        }

        int x = width - 29;
        int y = height - 53;
        for (int value = 1; value <= 20; value += 2) {
            ResourceLocation sprite = value < armor ? ARMOR_FULL : value == armor ? ARMOR_HALF : ARMOR_EMPTY;
            graphics.blit(sprite, x, y, 0, 0, 9, 9, 9, 9);
            x -= 8;
        }
    }

    private static void renderAir(GuiGraphics graphics, Player player, int width, int height) {
        int air = player.getAirSupply();
        int maxAir = player.getMaxAirSupply();
        if (!player.isEyeInFluid(FluidTags.WATER) && air >= maxAir) {
            return;
        }

        int full = Mth.ceil((double) (air - 2) * 10.0D / maxAir);
        int partial = Mth.ceil((double) air * 10.0D / maxAir) - full;
        int x = width - 29;
        int y = height - (player.getArmorValue() > 0 ? 65 : 53);
        for (int i = 0; i < full + partial; i++) {
            graphics.blit(i < full ? AIR : AIR_BURSTING, x - i * 8, y, 0, 0, 9, 9, 9, 9);
        }
    }

    private static void renderExperience(GuiGraphics graphics, Minecraft minecraft, int width, int height) {
        if (minecraft.gameMode == null || !minecraft.gameMode.hasExperience()) {
            return;
        }

        int x = width / 2 - XP_BAR_WIDTH / 2;
        int y = height - 29;
        int fillWidth = Mth.clamp(Math.round(minecraft.player.experienceProgress * XP_BAR_WIDTH), 0, XP_BAR_WIDTH);
        graphics.blit(XP_BACKGROUND, x, y, 0, 0, XP_BAR_WIDTH, XP_BAR_HEIGHT, XP_BAR_WIDTH, XP_BAR_HEIGHT);
        if (fillWidth > 0) {
            graphics.blit(XP_PROGRESS, x, y, 0, 0, fillWidth, XP_BAR_HEIGHT, XP_BAR_WIDTH, XP_BAR_HEIGHT);
        }
    }

    private static void drawPlayerBar(GuiGraphics graphics, int x, int y, ResourceLocation fillTexture,
                                      float current, float max) {
        float ratio = max <= 0.0F || current <= 0.01F ? 0.0F : Mth.clamp(current / max, 0.0F, 1.0F);
        int fillWidth = ratio <= 0.0F ? 0 : Mth.clamp((int) Math.floor(PLAYER_BAR_FILL_WIDTH * ratio), 1,
                PLAYER_BAR_FILL_WIDTH);
        int inset = (PLAYER_BAR_FILL_WIDTH - fillWidth) / 2;

        graphics.blit(PLAYER_BAR_FRAME, x, y, 0, 0, PLAYER_BAR_WIDTH, PLAYER_BAR_HEIGHT,
                PLAYER_BAR_WIDTH, PLAYER_BAR_HEIGHT);
        if (fillWidth <= 0) {
            return;
        }

        graphics.blit(fillTexture, x + PLAYER_BAR_FILL_X + inset, y + PLAYER_BAR_FILL_Y,
                inset, 0, fillWidth, PLAYER_BAR_FILL_HEIGHT, PLAYER_BAR_FILL_WIDTH, PLAYER_BAR_FILL_HEIGHT);
    }

    private static float getDisplayedMana(float targetMana) {
        if (displayedMana < 0.0F) {
            displayedMana = targetMana;
            return targetMana;
        }
        displayedMana = Mth.lerp(0.2F, displayedMana, targetMana);
        if (Math.abs(displayedMana - targetMana) < 0.05F) {
            displayedMana = targetMana;
        }
        return Math.max(0.0F, displayedMana);
    }

    private static void drawCardinalDirection(GuiGraphics graphics, float yaw, float angle, int xPos, String text) {
        float distance = angleDistance(yaw, angle);
        if (Mth.abs(distance) <= 90.0F) {
            graphics.drawCenteredString(Minecraft.getInstance().font, text, (int) (xPos + distance), 13, 0xFFFFFF);
        }
    }

    private static float angleDistance(float yaw, float other) {
        float distance = other - yaw;
        if (distance > 0.0F) {
            return distance > 180.0F ? distance - 360.0F : distance;
        }
        return distance < -180.0F ? distance + 360.0F : distance;
    }
}
