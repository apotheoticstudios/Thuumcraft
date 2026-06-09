package net.apotheoticstudios.thuumcraft.client.gui;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.apotheoticstudios.thuumcraft.client.ClientSkillPerkState;
import net.apotheoticstudios.thuumcraft.client.SelectedMagicSpellState;
import net.apotheoticstudios.thuumcraft.magic.IronLearnedSpellHelper;
import net.apotheoticstudios.thuumcraft.magic.SkyrimMagicScaling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SkyrimMagicMenuScreen extends Screen {
    private static final int TEXT_COLOR = 0xFFE9E2D2;
    private static final int SELECTED_TEXT_COLOR = 0xFFFFFFFF;
    private static final int MUTED_TEXT_COLOR = 0xAA9DA9A9;
    private static final int DIM_TEXT_COLOR = 0x775E6B70;
    private static final int LOCKED_TEXT_COLOR = 0x66778484;
    private static final int LINE_COLOR = 0x889DA9A9;
    private static final int PANEL_SHADE = 0x55040910;
    private static final int HIGHLIGHT_SHADE = 0x229DA9A9;
    private static final int ROW_HEIGHT = 18;

    private int selectedCategory;
    private int selectedSpell;
    private int categoryScroll;
    private int spellScroll;

    public SkyrimMagicMenuScreen() {
        super(Component.translatable("screen.thuumcraft.magic_menu"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackgroundWash(guiGraphics);
        renderTopNavigation(guiGraphics);

        Layout layout = layout();
        Player player = Minecraft.getInstance().player;
        List<SpellEntry> knownSpells = knownSpells(player);
        List<MagicCategory> categories = categories(knownSpells);
        clampCategory(categories);
        List<SpellEntry> visibleSpells = visibleSpells(categories.get(selectedCategory), knownSpells);
        clampSelection(visibleSpells);
        categoryScroll = scrollToSelection(selectedCategory, categoryScroll, categories.size(), maxRows(layout));
        spellScroll = scrollToSelection(selectedSpell, spellScroll, visibleSpells.size(), maxRows(layout));

        renderCategoryColumn(guiGraphics, layout, categories, mouseX, mouseY);
        renderSpellColumn(guiGraphics, layout, categories, visibleSpells, mouseX, mouseY);
        renderSpellDetails(guiGraphics, layout, visibleSpells);
        renderFooter(guiGraphics, layout);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        List<SpellEntry> knownSpells = knownSpells(Minecraft.getInstance().player);
        List<MagicCategory> categories = categories(knownSpells);
        clampCategory(categories);

        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_A) {
            selectedCategory = wrap(selectedCategory - 1, categories.size());
            selectedSpell = 0;
            spellScroll = 0;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D) {
            selectedCategory = wrap(selectedCategory + 1, categories.size());
            selectedSpell = 0;
            spellScroll = 0;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W) {
            List<SpellEntry> visibleSpells = visibleSpells(categories.get(selectedCategory), knownSpells);
            if (!visibleSpells.isEmpty()) {
                selectedSpell = wrap(selectedSpell - 1, visibleSpells.size());
            } else {
                selectedCategory = wrap(selectedCategory - 1, categories.size());
                spellScroll = 0;
            }
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S) {
            List<SpellEntry> visibleSpells = visibleSpells(categories.get(selectedCategory), knownSpells);
            if (!visibleSpells.isEmpty()) {
                selectedSpell = wrap(selectedSpell + 1, visibleSpells.size());
            } else {
                selectedCategory = wrap(selectedCategory + 1, categories.size());
                spellScroll = 0;
            }
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE) {
            return selectCurrentSpell(visibleSpells(categories.get(selectedCategory), knownSpells), true);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            Layout layout = layout();
            List<SpellEntry> knownSpells = knownSpells(Minecraft.getInstance().player);
            List<MagicCategory> categories = categories(knownSpells);
            clampCategory(categories);

            int category = clickedCategory(layout, categories, mouseX, mouseY);
            if (category >= 0) {
                selectedCategory = category;
                selectedSpell = 0;
                spellScroll = 0;
                return true;
            }

            List<SpellEntry> visibleSpells = visibleSpells(categories.get(selectedCategory), knownSpells);
            int spell = clickedSpell(layout, mouseX, mouseY, visibleSpells);
            if (spell >= 0) {
                selectedSpell = spell;
                return selectCurrentSpell(visibleSpells,
                        button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND,
                        false);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Player player = Minecraft.getInstance().player;
        List<SpellEntry> knownSpells = knownSpells(player);
        List<MagicCategory> categories = categories(knownSpells);
        clampCategory(categories);
        List<SpellEntry> visibleSpells = visibleSpells(categories.get(selectedCategory), knownSpells);
        if (visibleSpells.isEmpty()) {
            return false;
        }
        selectedSpell = wrap(selectedSpell + (delta < 0.0D ? 1 : -1), visibleSpells.size());
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderBackgroundWash(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x8A000000);
        guiGraphics.fillGradient(0, 0, this.width, this.height / 2, 0xB3061018, 0x33061018);
        guiGraphics.fillGradient(0, this.height / 2, this.width, this.height, 0x33061018, 0xC0061018);
        guiGraphics.fillGradient(0, 0, this.width / 5, this.height, 0xCC000000, 0x00000000);
        guiGraphics.fillGradient(this.width * 4 / 5, 0, this.width, this.height, 0x00000000, 0xAA000000);
    }

    private void renderTopNavigation(GuiGraphics guiGraphics) {
        int centerX = this.width / 2;
        int y = 18;

        if (this.width < 520) {
            int slotWidth = Math.max(56, (this.width - 40) / 4);
            int startX = centerX - slotWidth * 3 / 2;
            drawCenteredTrimmedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.magic"),
                    startX, y, slotWidth - 8, SELECTED_TEXT_COLOR);
            drawCenteredTrimmedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.skills"),
                    startX + slotWidth, y, slotWidth - 8, DIM_TEXT_COLOR);
            drawCenteredTrimmedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.items"),
                    startX + slotWidth * 2, y, slotWidth - 8, DIM_TEXT_COLOR);
            drawCenteredTrimmedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.map"),
                    startX + slotWidth * 3, y, slotWidth - 8, DIM_TEXT_COLOR);
            guiGraphics.fill(startX - slotWidth / 2 + 8, y + 13, startX + slotWidth / 2 - 8, y + 14, SELECTED_TEXT_COLOR);
        } else {
            drawCenteredSpacedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.magic"),
                    centerX - 132, y, SELECTED_TEXT_COLOR, 4);
            drawCenteredSpacedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.skills"),
                    centerX - 36, y, DIM_TEXT_COLOR, 3);
            drawCenteredSpacedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.items"),
                    centerX + 56, y, DIM_TEXT_COLOR, 3);
            drawCenteredSpacedString(guiGraphics, Component.translatable("screen.thuumcraft.tween_menu.map"),
                    centerX + 142, y, DIM_TEXT_COLOR, 3);
            guiGraphics.fill(centerX - 168, y + 13, centerX - 98, y + 14, SELECTED_TEXT_COLOR);
        }
        guiGraphics.fill(30, 44, this.width - 30, 45, LINE_COLOR);
    }

    private void renderCategoryColumn(GuiGraphics guiGraphics, Layout layout, List<MagicCategory> categories, int mouseX, int mouseY) {
        guiGraphics.fill(layout.categoryX - 16, layout.contentY - 14,
                layout.categoryX + layout.categoryWidth, layout.detailY - 14, PANEL_SHADE);

        int rows = maxRows(layout);
        int end = Math.min(categories.size(), categoryScroll + rows);
        for (int i = categoryScroll; i < end; i++) {
            MagicCategory category = categories.get(i);
            int y = layout.contentY + (i - categoryScroll) * ROW_HEIGHT;
            boolean selected = i == selectedCategory;
            boolean hovered = mouseX >= layout.categoryX - 8
                    && mouseX <= layout.categoryX + layout.categoryWidth - 4
                    && mouseY >= y - 3
                    && mouseY <= y + 12;
            int color = selected ? SELECTED_TEXT_COLOR : hovered ? TEXT_COLOR : MUTED_TEXT_COLOR;

            if (selected) {
                guiGraphics.fill(layout.categoryX - 11, y - 3, layout.categoryX + layout.categoryWidth - 8, y + 12, HIGHLIGHT_SHADE);
                guiGraphics.fill(layout.categoryX - 16, y - 3, layout.categoryX - 14, y + 12, SELECTED_TEXT_COLOR);
            }

            drawTrimmedString(guiGraphics, category.label(), layout.categoryX, y,
                    layout.categoryWidth - 14, color);
        }
    }

    private void renderSpellColumn(GuiGraphics guiGraphics, Layout layout, List<MagicCategory> categories,
                                   List<SpellEntry> visibleSpells, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        guiGraphics.fill(layout.spellX - 18, layout.contentY - 14,
                layout.spellX + layout.spellWidth, layout.detailY - 14, PANEL_SHADE);
        guiGraphics.fill(layout.spellX - 18, layout.contentY - 14,
                layout.spellX + layout.spellWidth, layout.contentY - 13, LINE_COLOR);

        Component heading = categories.get(selectedCategory).label();
        drawTrimmedString(guiGraphics, heading, layout.spellX, layout.contentY - 29,
                layout.spellWidth - 8, SELECTED_TEXT_COLOR);

        if (visibleSpells.isEmpty()) {
            guiGraphics.drawString(this.font, Component.translatable("screen.thuumcraft.magic_menu.no_spells"),
                    layout.spellX, layout.contentY + 4, LOCKED_TEXT_COLOR, false);
            return;
        }

        int rows = maxRows(layout);
        int end = Math.min(visibleSpells.size(), spellScroll + rows);
        for (int i = spellScroll; i < end; i++) {
            AbstractSpell spell = visibleSpells.get(i).spell();
            int y = layout.contentY + (i - spellScroll) * ROW_HEIGHT;
            boolean selected = i == selectedSpell;
            boolean mainHandEquipped = SelectedMagicSpellState.isSelected(spell, InteractionHand.MAIN_HAND);
            boolean offHandEquipped = SelectedMagicSpellState.isSelected(spell, InteractionHand.OFF_HAND);
            boolean equipped = mainHandEquipped || offHandEquipped;
            boolean hovered = mouseX >= layout.spellX - 8
                    && mouseX <= layout.spellX + layout.spellWidth - 12
                    && mouseY >= y - 3
                    && mouseY <= y + 12;
            int color = selected ? SELECTED_TEXT_COLOR : hovered || equipped ? TEXT_COLOR : MUTED_TEXT_COLOR;

            if (selected) {
                guiGraphics.fill(layout.spellX - 10, y - 3, layout.spellX + layout.spellWidth - 14, y + 12, HIGHLIGHT_SHADE);
                guiGraphics.fill(layout.spellX - 15, y - 3, layout.spellX - 13, y + 12, SELECTED_TEXT_COLOR);
            }

            drawTrimmedString(guiGraphics, spell.getDisplayName(minecraft.player), layout.spellX, y,
                    layout.spellWidth - 48, color);
            if (mainHandEquipped) {
                guiGraphics.drawString(this.font, Component.literal("R"), layout.spellX + layout.spellWidth - 34, y,
                        SELECTED_TEXT_COLOR, false);
            }
            if (offHandEquipped) {
                guiGraphics.drawString(this.font, Component.literal("L"), layout.spellX + layout.spellWidth - 22, y,
                        SELECTED_TEXT_COLOR, false);
            }
        }
    }

    private void renderSpellDetails(GuiGraphics guiGraphics, Layout layout, List<SpellEntry> visibleSpells) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.fill(30, layout.detailY - 12, this.width - 30, this.height - 34, 0x66000000);
        guiGraphics.fill(30, layout.detailY - 12, this.width - 30, layout.detailY - 11, LINE_COLOR);

        if (minecraft.player == null || visibleSpells.isEmpty()) {
            guiGraphics.drawString(this.font, Component.translatable("screen.thuumcraft.magic_menu.no_spells"),
                    layout.detailX, layout.detailY, LOCKED_TEXT_COLOR, false);
            return;
        }

        AbstractSpell spell = visibleSpells.get(selectedSpell).spell();
        boolean mainHandEquipped = SelectedMagicSpellState.isSelected(spell, InteractionHand.MAIN_HAND);
        boolean offHandEquipped = SelectedMagicSpellState.isSelected(spell, InteractionHand.OFF_HAND);
        boolean equipped = mainHandEquipped || offHandEquipped;

        drawTrimmedString(guiGraphics, spell.getDisplayName(minecraft.player), layout.detailX, layout.detailY,
                layout.detailWidth, SELECTED_TEXT_COLOR);

        Component rankCost = Component.translatable("screen.thuumcraft.magic_menu.rank_cost",
                spell.getRarity(1).getDisplayName(), displayManaCost(spell, 1));
        int rankCostMaxWidth = Math.max(80, layout.detailWidth / 2);
        int rankCostWidth = Math.min(rankCostMaxWidth, this.font.width(rankCost));
        int rankCostX = layout.detailX + layout.detailWidth - rankCostWidth;
        drawTrimmedString(guiGraphics, spell.getSchoolType().getDisplayName(), layout.detailX, layout.detailY + 13,
                Math.max(42, rankCostX - layout.detailX - 8), MUTED_TEXT_COLOR);
        drawTrimmedString(guiGraphics, rankCost, rankCostX, layout.detailY + 13,
                rankCostMaxWidth, MUTED_TEXT_COLOR);

        Component description = Component.translatable(spell.getComponentId() + ".guide");
        int textY = layout.detailY + 32;
        int descriptionBottom = this.height - (equipped ? 58 : 46);
        for (FormattedCharSequence line : this.font.split(description, layout.detailWidth)) {
            if (textY + 10 > descriptionBottom) {
                break;
            }
            guiGraphics.drawString(this.font, line, layout.detailX, textY, TEXT_COLOR, false);
            textY += 11;
        }

        if (equipped) {
            Component selectedText = mainHandEquipped && offHandEquipped
                    ? Component.translatable("screen.thuumcraft.magic_menu.selected_both")
                    : mainHandEquipped
                    ? Component.translatable("screen.thuumcraft.magic_menu.selected_main")
                    : Component.translatable("screen.thuumcraft.magic_menu.selected_off");
            drawTrimmedString(guiGraphics, selectedText, layout.detailX, this.height - 44,
                    layout.detailWidth, SELECTED_TEXT_COLOR);
        }

        int statX = layout.statX;
        int statWidth = layout.statWidth;
        int statY = layout.detailY;
        List<Component> stats = new ArrayList<>(spell.getUniqueInfo(1, minecraft.player));
        int cooldownTicks = SkyrimMagicScaling.adjustedCooldownTicks(spell, minecraft.player);
        if (cooldownTicks > 0) {
            stats.add(Component.translatable("ui.thuumcraft.cooldown", Utils.timeFromTicks(cooldownTicks, 1)));
        }
        if (!stats.isEmpty() && statWidth > 40) {
            guiGraphics.drawString(this.font, Component.translatable("screen.thuumcraft.magic_menu.effects"),
                    statX, statY, SELECTED_TEXT_COLOR, false);
            for (int i = 0; i < stats.size(); i++) {
                int y = statY + 16 + i * 12;
                if (y + 10 > this.height - 42) {
                    break;
                }
                drawTrimmedString(guiGraphics, stats.get(i), statX, y, statWidth, TEXT_COLOR);
            }
        }
    }

    private void renderFooter(GuiGraphics guiGraphics, Layout layout) {
        guiGraphics.fill(30, this.height - 28, this.width - 30, this.height - 27, LINE_COLOR);
        drawTrimmedString(guiGraphics, Component.translatable("screen.thuumcraft.magic_menu.footer"),
                34, this.height - 20, this.width - 68, MUTED_TEXT_COLOR);
    }

    private List<SpellEntry> knownSpells(Player player) {
        List<SpellEntry> result = new ArrayList<>();
        if (player == null) {
            return result;
        }

        Set<ResourceLocation> learnedSpellIds = IronLearnedSpellHelper.learnedSpellIds(ClientMagicData.getSyncedSpellData(player));
        for (AbstractSpell spell : SpellRegistry.getEnabledSpells()) {
            if (spell == null || spell == SpellRegistry.none() || spell.getCastType() == CastType.NONE) {
                continue;
            }
            if (learnedSpellIds.contains(spell.getSpellResource())) {
                result.add(new SpellEntry(spell));
            }
        }

        result.sort(Comparator
                .comparing((SpellEntry entry) -> entry.spell().getSchoolType().getDisplayName().getString())
                .thenComparing(entry -> entry.spell().getDisplayName(player).getString()));
        return result;
    }

    private List<MagicCategory> categories(List<SpellEntry> knownSpells) {
        List<MagicCategory> categories = new ArrayList<>();
        categories.add(MagicCategory.all());

        Map<String, SchoolType> schoolsByName = new LinkedHashMap<>();
        knownSpells.stream()
                .map(entry -> entry.spell().getSchoolType())
                .sorted(Comparator.comparing(school -> school.getDisplayName().getString()))
                .forEach(school -> schoolsByName.putIfAbsent(school.getDisplayName().getString(), school));
        for (SchoolType school : schoolsByName.values()) {
            categories.add(MagicCategory.school(school));
        }
        return categories;
    }

    private List<SpellEntry> visibleSpells(MagicCategory category, List<SpellEntry> knownSpells) {
        List<SpellEntry> result = new ArrayList<>();
        for (SpellEntry entry : knownSpells) {
            if (category.matches(entry.spell())) {
                result.add(entry);
            }
        }
        return result;
    }

    private boolean selectCurrentSpell(List<SpellEntry> visibleSpells, boolean closeMenu) {
        return selectCurrentSpell(visibleSpells, InteractionHand.MAIN_HAND, closeMenu);
    }

    private boolean selectCurrentSpell(List<SpellEntry> visibleSpells, InteractionHand hand, boolean closeMenu) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || visibleSpells.isEmpty()) {
            return false;
        }

        AbstractSpell spell = visibleSpells.get(selectedSpell).spell();
        SelectedMagicSpellState.select(spell, hand);
        minecraft.player.displayClientMessage(Component.translatable(hand == InteractionHand.OFF_HAND
                ? "message.thuumcraft.magic.selected_off"
                : "message.thuumcraft.magic.selected_main", spell.getDisplayName(minecraft.player)), true);
        if (closeMenu) {
            minecraft.setScreen(null);
        }
        return true;
    }

    private int displayManaCost(AbstractSpell spell, int level) {
        return SkyrimMagicScaling.adjustedManaCost(spell, level, ClientSkillPerkState::rank);
    }

    private int clickedCategory(Layout layout, List<MagicCategory> categories, double mouseX, double mouseY) {
        if (mouseX < layout.categoryX - 8 || mouseX > layout.categoryX + layout.categoryWidth - 4) {
            return -1;
        }
        int row = (int) ((mouseY - layout.contentY + 3) / ROW_HEIGHT);
        if (row < 0 || row >= maxRows(layout)) {
            return -1;
        }
        int index = categoryScroll + row;
        if (index >= 0 && index < categories.size()) {
            return index;
        }
        return -1;
    }

    private int clickedSpell(Layout layout, double mouseX, double mouseY, List<SpellEntry> visibleSpells) {
        if (mouseX < layout.spellX - 8 || mouseX > layout.spellX + layout.spellWidth - 12) {
            return -1;
        }
        int row = (int) ((mouseY - layout.contentY + 3) / ROW_HEIGHT);
        if (row < 0 || row >= maxRows(layout)) {
            return -1;
        }
        int index = spellScroll + row;
        if (index >= 0 && index < visibleSpells.size()) {
            return index;
        }
        return -1;
    }

    private void clampCategory(List<MagicCategory> categories) {
        if (selectedCategory >= categories.size()) {
            selectedCategory = categories.size() - 1;
        }
        if (selectedCategory < 0) {
            selectedCategory = 0;
        }
    }

    private void clampSelection(List<SpellEntry> visibleSpells) {
        if (visibleSpells.isEmpty()) {
            selectedSpell = 0;
        } else if (selectedSpell >= visibleSpells.size()) {
            selectedSpell = visibleSpells.size() - 1;
        }
    }

    private int wrap(int value, int size) {
        int result = value % size;
        return result < 0 ? result + size : result;
    }

    private int scrollToSelection(int selected, int scroll, int size, int visibleRows) {
        if (size <= visibleRows) {
            return 0;
        }
        if (selected < scroll) {
            return selected;
        }
        if (selected >= scroll + visibleRows) {
            return selected - visibleRows + 1;
        }
        return Math.max(0, Math.min(scroll, size - visibleRows));
    }

    private int maxRows(Layout layout) {
        return Math.max(1, (layout.detailY - layout.contentY - 18) / ROW_HEIGHT);
    }

    private Layout layout() {
        int contentY = Math.max(62, this.height / 5);
        int detailY = Math.max(contentY + 126, this.height - 118);
        int margin = Math.max(22, Math.min(34, this.width / 12));
        int gap = Math.max(18, Math.min(42, this.width / 16));
        int availableTopWidth = Math.max(160, this.width - margin * 2 - gap);
        int categoryWidth = clamp(this.width / 5, Math.min(92, availableTopWidth / 2), Math.max(92, availableTopWidth / 2));
        int spellWidth = Math.max(96, availableTopWidth - categoryWidth);
        int categoryX = margin + 10;
        int spellX = categoryX + categoryWidth + gap;

        int detailX = margin + 8;
        int detailGap = Math.max(18, Math.min(28, this.width / 24));
        int detailAvailableWidth = Math.max(120, this.width - detailX - margin);
        int detailWidth = Math.max(110, (detailAvailableWidth - detailGap) / 2);
        int statX = detailX + detailWidth + detailGap;
        int statWidth = Math.max(0, this.width - margin - statX);
        return new Layout(contentY, detailY, categoryX, categoryWidth, spellX, spellWidth,
                detailX, detailWidth, statX, statWidth);
    }

    private void drawTrimmedString(GuiGraphics guiGraphics, Component component, int x, int y, int maxWidth, int color) {
        if (maxWidth <= 0) {
            return;
        }

        String text = component.getString();
        if (this.font.width(text) <= maxWidth) {
            guiGraphics.drawString(this.font, component, x, y, color, false);
            return;
        }

        String ellipsis = "...";
        int ellipsisWidth = this.font.width(ellipsis);
        String trimmed = text;
        int targetWidth = Math.max(0, maxWidth - ellipsisWidth);
        while (!trimmed.isEmpty() && this.font.width(trimmed) > targetWidth) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        guiGraphics.drawString(this.font, trimmed + ellipsis, x, y, color, false);
    }

    private void drawCenteredTrimmedString(GuiGraphics guiGraphics, Component component, int centerX, int y, int maxWidth, int color) {
        String text = trimmedText(component.getString(), maxWidth);
        guiGraphics.drawString(this.font, text, centerX - this.font.width(text) / 2, y, color, false);
    }

    private String trimmedText(String text, int maxWidth) {
        if (this.font.width(text) <= maxWidth) {
            return text;
        }

        String ellipsis = "...";
        int targetWidth = Math.max(0, maxWidth - this.font.width(ellipsis));
        String trimmed = text;
        while (!trimmed.isEmpty() && this.font.width(trimmed) > targetWidth) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed + ellipsis;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private void drawCenteredSpacedString(GuiGraphics guiGraphics, Component component, int centerX, int y, int color, int spacing) {
        String text = component.getString().toUpperCase(Locale.ROOT);
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += this.font.width(String.valueOf(text.charAt(i)));
            if (i + 1 < text.length()) {
                width += spacing;
            }
        }

        int x = centerX - width / 2;
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            guiGraphics.drawString(this.font, character, x, y, color, false);
            x += this.font.width(character) + spacing;
        }
    }

    private record Layout(int contentY,
                          int detailY,
                          int categoryX,
                          int categoryWidth,
                          int spellX,
                          int spellWidth,
                          int detailX,
                          int detailWidth,
                          int statX,
                          int statWidth) {
    }

    private record MagicCategory(Component label, SchoolType school, CategoryMode mode) {
        static MagicCategory all() {
            return new MagicCategory(Component.translatable("screen.thuumcraft.magic_menu.all"), null, CategoryMode.ALL);
        }

        static MagicCategory school(SchoolType school) {
            return new MagicCategory(school.getDisplayName(), school, CategoryMode.SCHOOL);
        }

        boolean matches(AbstractSpell spell) {
            return switch (mode) {
                case ALL -> true;
                case SCHOOL -> spell.getSchoolType() == school;
            };
        }
    }

    private record SpellEntry(AbstractSpell spell) {
    }

    private enum CategoryMode {
        ALL,
        SCHOOL
    }
}
