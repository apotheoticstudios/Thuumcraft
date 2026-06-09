package net.apotheoticstudios.thuumcraft.client.gui;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.apotheoticstudios.thuumcraft.network.ServerboundRequestSkillPerksPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class SkyrimTweenMenuScreen extends Screen {
    private static final int TEXT_COLOR = 0xFFE9E2D2;
    private static final int SELECTED_TEXT_COLOR = 0xFFFFFFFF;
    private static final int MUTED_TEXT_COLOR = 0x999DA9A9;
    private static final int LINE_COLOR = 0xB0C9D0CF;
    private static final int LINE_MUTED_COLOR = 0x52818B8B;
    private static final int CENTER_COLOR = 0xDCE9E2D2;
    private static final int MIN_MOUSE_SELECTION_DISTANCE = 20;

    private MenuOption selected = MenuOption.MAGIC;

    public SkyrimTweenMenuScreen() {
        super(Component.translatable("screen.thuumcraft.tween_menu"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        updateMouseSelection(mouseX, mouseY);
        renderBackgroundWash(guiGraphics);

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int horizontalArm = Math.max(118, Math.min(235, this.width / 4));
        int verticalArm = Math.max(58, Math.min(92, this.height / 5));

        renderSkyrimCompass(guiGraphics, centerX, centerY, horizontalArm, verticalArm);
        renderLabels(guiGraphics, centerX, centerY, horizontalArm, verticalArm);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_A) {
            selected = MenuOption.MAGIC;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D) {
            selected = MenuOption.ITEMS;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W) {
            selected = MenuOption.SKILLS;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S) {
            selected = MenuOption.MAP;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE) {
            activateSelected();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            updateMouseSelection((int) mouseX, (int) mouseY);
            activateSelected();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void activateSelected() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            onClose();
            return;
        }

        switch (selected) {
            case MAGIC -> minecraft.setScreen(new SkyrimMagicMenuScreen());
            case SKILLS -> {
                if (!Config.ENABLE_SKILL_SYSTEM.get()) {
                    minecraft.player.displayClientMessage(Component.translatable("message.thuumcraft.menu.skills_disabled"), true);
                    onClose();
                    return;
                }
                ModMessages.sendToServer(new ServerboundRequestSkillPerksPacket());
                minecraft.setScreen(new SkillTreeScreen());
            }
            case ITEMS -> minecraft.setScreen(new InventoryScreen(minecraft.player));
            case MAP -> {
                minecraft.player.displayClientMessage(Component.translatable("message.thuumcraft.menu.map_unavailable"), true);
                onClose();
            }
        }
    }

    private void updateMouseSelection(int mouseX, int mouseY) {
        int dx = mouseX - this.width / 2;
        int dy = mouseY - this.height / 2;
        if (dx * dx + dy * dy < MIN_MOUSE_SELECTION_DISTANCE * MIN_MOUSE_SELECTION_DISTANCE) {
            return;
        }

        if (Math.abs(dx) >= Math.abs(dy)) {
            selected = dx < 0 ? MenuOption.MAGIC : MenuOption.ITEMS;
        } else {
            selected = dy < 0 ? MenuOption.SKILLS : MenuOption.MAP;
        }
    }

    private void renderBackgroundWash(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x8A000000);
        guiGraphics.fillGradient(0, 0, this.width, this.height / 2, 0xA2061016, 0x28061016);
        guiGraphics.fillGradient(0, this.height / 2, this.width, this.height, 0x28061016, 0xB0061016);
        guiGraphics.fillGradient(0, 0, this.width / 4, this.height, 0xB9000000, 0x00000000);
        guiGraphics.fillGradient(this.width * 3 / 4, 0, this.width, this.height, 0x00000000, 0xB9000000);

        int mistY = this.height / 2 - 48;
        guiGraphics.fillGradient(0, mistY, this.width, mistY + 34, 0x00000000, 0x260A151B);
        guiGraphics.fillGradient(0, mistY + 34, this.width, mistY + 92, 0x260A151B, 0x00000000);
    }

    private void renderSkyrimCompass(GuiGraphics guiGraphics, int centerX, int centerY, int horizontalArm, int verticalArm) {
        renderArm(guiGraphics, centerX, centerY, -horizontalArm, 0, selected == MenuOption.MAGIC);
        renderArm(guiGraphics, centerX, centerY, horizontalArm, 0, selected == MenuOption.ITEMS);
        renderArm(guiGraphics, centerX, centerY, 0, -verticalArm, selected == MenuOption.SKILLS);
        renderArm(guiGraphics, centerX, centerY, 0, verticalArm, selected == MenuOption.MAP);

        guiGraphics.fill(centerX - 8, centerY, centerX + 9, centerY + 1, CENTER_COLOR);
        guiGraphics.fill(centerX, centerY - 8, centerX + 1, centerY + 9, CENTER_COLOR);
        renderDiamond(guiGraphics, centerX, centerY, 6, 0x90000000);
        renderDiamond(guiGraphics, centerX, centerY, 4, CENTER_COLOR);
        renderDiamond(guiGraphics, centerX, centerY, 2, 0xEE0D151A);
    }

    private void renderArm(GuiGraphics guiGraphics, int centerX, int centerY, int dx, int dy, boolean active) {
        int color = active ? LINE_COLOR : LINE_MUTED_COLOR;
        int endX = centerX + dx;
        int endY = centerY + dy;
        if (dy == 0) {
            int startX = Math.min(centerX, endX);
            int stopX = Math.max(centerX, endX);
            guiGraphics.fill(startX, centerY, stopX, centerY + 1, color);
            if (active) {
                guiGraphics.fill(startX, centerY - 1, stopX, centerY, 0x55FFFFFF);
            }
            renderHorizontalChevron(guiGraphics, endX, centerY, dx < 0, color);
        } else {
            int startY = Math.min(centerY, endY);
            int stopY = Math.max(centerY, endY);
            guiGraphics.fill(centerX, startY, centerX + 1, stopY, color);
            if (active) {
                guiGraphics.fill(centerX - 1, startY, centerX, stopY, 0x55FFFFFF);
            }
            renderVerticalChevron(guiGraphics, centerX, endY, dy < 0, color);
        }
    }

    private void renderHorizontalChevron(GuiGraphics guiGraphics, int x, int y, boolean left, int color) {
        int direction = left ? -1 : 1;
        for (int i = 0; i < 7; i++) {
            int offset = i / 2;
            fillPixel(guiGraphics, x + direction * i, y - offset, color);
            fillPixel(guiGraphics, x + direction * i, y + offset, color);
        }
    }

    private void renderVerticalChevron(GuiGraphics guiGraphics, int x, int y, boolean up, int color) {
        int direction = up ? -1 : 1;
        for (int i = 0; i < 7; i++) {
            int offset = i / 2;
            fillPixel(guiGraphics, x - offset, y + direction * i, color);
            fillPixel(guiGraphics, x + offset, y + direction * i, color);
        }
    }

    private void fillPixel(GuiGraphics guiGraphics, int x, int y, int color) {
        guiGraphics.fill(x, y, x + 1, y + 1, color);
    }

    private void renderDiamond(GuiGraphics guiGraphics, int centerX, int centerY, int radius, int color) {
        for (int y = -radius; y <= radius; y++) {
            int halfWidth = radius - Math.abs(y);
            guiGraphics.fill(centerX - halfWidth, centerY + y, centerX + halfWidth + 1, centerY + y + 1, color);
        }
    }

    private void renderLabels(GuiGraphics guiGraphics, int centerX, int centerY, int horizontalArm, int verticalArm) {
        drawMenuLabel(guiGraphics, MenuOption.SKILLS, centerX, centerY - verticalArm - 34);
        drawMenuLabel(guiGraphics, MenuOption.MAGIC, centerX - horizontalArm - 56, centerY - 5);
        drawMenuLabel(guiGraphics, MenuOption.ITEMS, centerX + horizontalArm + 56, centerY - 5);
        drawMenuLabel(guiGraphics, MenuOption.MAP, centerX, centerY + verticalArm + 25);
    }

    private void drawMenuLabel(GuiGraphics guiGraphics, MenuOption option, int centerX, int y) {
        boolean active = option == selected;
        int color = active ? SELECTED_TEXT_COLOR : MUTED_TEXT_COLOR;
        drawCenteredSpacedString(guiGraphics, option.label, centerX, y, color, active ? 4 : 3);
        if (active) {
            int underlineWidth = Math.max(36, spacedWidth(option.label.getString(), 4) + 8);
            guiGraphics.fill(centerX - underlineWidth / 2, y + 12, centerX + underlineWidth / 2, y + 13, 0xAEE9E2D2);
        }
    }

    private void drawCenteredSpacedString(GuiGraphics guiGraphics, Component component, int centerX, int y, int color, int spacing) {
        String text = component.getString().toUpperCase(Locale.ROOT);
        int width = spacedWidth(text, spacing);
        int x = centerX - width / 2;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            guiGraphics.drawString(this.font, character, x, y, color, false);
            x += this.font.width(character) + spacing;
        }
    }

    private int spacedWidth(String text, int spacing) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += this.font.width(String.valueOf(text.charAt(i)));
            if (i + 1 < text.length()) {
                width += spacing;
            }
        }
        return width;
    }

    private enum MenuOption {
        MAGIC(Component.translatable("screen.thuumcraft.tween_menu.magic")),
        SKILLS(Component.translatable("screen.thuumcraft.tween_menu.skills")),
        ITEMS(Component.translatable("screen.thuumcraft.tween_menu.items")),
        MAP(Component.translatable("screen.thuumcraft.tween_menu.map"));

        private final Component label;

        MenuOption(Component label) {
            this.label = label;
        }
    }
}
