package net.apotheoticstudios.thuumcraft.client.gui;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillTreeScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            Thuumcraft.MOD_ID, "textures/gui/skill_trees/skill_tree_background.png");
    private static final int BACKGROUND_WIDTH = 1672;
    private static final int BACKGROUND_HEIGHT = 941;
    private static final int NODE_PICK_RADIUS = 8;
    private static final int TEXT_COLOR = 0xFFE4D6AD;
    private static final int MUTED_TEXT_COLOR = 0x998EA4B4;
    private static final int DIM_TEXT_COLOR = 0x665D7484;
    private static final int LINE_GLOW_COLOR = 0x224F8AA7;
    private static final int LINE_COLOR = 0x88D3B66D;
    private static final int NODE_GLOW_COLOR = 0x33F4DFA2;
    private static final int NODE_COLOR = 0xDDF1D58B;
    private static final int NODE_SELECTED_COLOR = 0xFFFFFFFF;
    private static final int NODE_HOVER_COLOR = 0xFFFFECB6;
    private static final int VISIBLE_NAVIGATION_TREES = 5;

    private static final List<SkillTreeDefinition> TREES = List.of(
            tree(ModAttributes.ARCHERY, nodes(
                    0.50D, 0.92D, 0.50D, 0.75D, 0.32D, 0.61D, 0.68D, 0.62D, 0.50D, 0.53D,
                    0.24D, 0.38D, 0.76D, 0.39D, 0.48D, 0.22D, 0.62D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 1, 4, 2, 5, 3, 6, 4, 7, 6, 8, 7, 8)),
            tree(ModAttributes.BLOCK, nodes(
                    0.50D, 0.92D, 0.48D, 0.75D, 0.32D, 0.62D, 0.62D, 0.60D, 0.28D, 0.43D,
                    0.50D, 0.45D, 0.74D, 0.43D, 0.52D, 0.25D, 0.50D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 5, 7, 7, 8)),
            tree(ModAttributes.HEAVY_ARMOR, nodes(
                    0.50D, 0.92D, 0.42D, 0.74D, 0.58D, 0.72D, 0.32D, 0.55D, 0.70D, 0.52D,
                    0.42D, 0.35D, 0.64D, 0.28D, 0.50D, 0.08D),
                    edges(0, 1, 0, 2, 1, 3, 2, 4, 3, 5, 4, 6, 5, 7, 6, 7)),
            tree(ModAttributes.ONE_HANDED, nodes(
                    0.50D, 0.92D, 0.50D, 0.75D, 0.30D, 0.62D, 0.50D, 0.58D, 0.70D, 0.62D,
                    0.22D, 0.42D, 0.48D, 0.38D, 0.78D, 0.42D, 0.38D, 0.20D, 0.62D, 0.20D),
                    edges(0, 1, 1, 2, 1, 3, 1, 4, 2, 5, 3, 6, 4, 7, 5, 8, 6, 8, 7, 9, 8, 9)),
            tree(ModAttributes.SMITHING, nodes(
                    0.50D, 0.92D, 0.62D, 0.76D, 0.72D, 0.58D, 0.76D, 0.38D, 0.64D, 0.20D,
                    0.38D, 0.76D, 0.28D, 0.58D, 0.24D, 0.38D, 0.36D, 0.20D, 0.50D, 0.08D),
                    edges(0, 1, 1, 2, 2, 3, 3, 4, 4, 9, 0, 5, 5, 6, 6, 7, 7, 8, 8, 9)),
            tree(ModAttributes.TWO_HANDED, nodes(
                    0.50D, 0.92D, 0.50D, 0.75D, 0.30D, 0.62D, 0.50D, 0.57D, 0.70D, 0.62D,
                    0.28D, 0.43D, 0.50D, 0.37D, 0.72D, 0.43D, 0.50D, 0.12D),
                    edges(0, 1, 1, 2, 1, 3, 1, 4, 2, 5, 3, 6, 4, 7, 5, 8, 6, 8, 7, 8)),
            tree(ModAttributes.ALTERATION, nodes(
                    0.50D, 0.92D, 0.50D, 0.78D, 0.35D, 0.64D, 0.65D, 0.64D, 0.25D, 0.48D,
                    0.50D, 0.48D, 0.75D, 0.48D, 0.35D, 0.30D, 0.62D, 0.28D, 0.50D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 7, 5, 8, 6, 8, 7, 9, 8, 9)),
            tree(ModAttributes.CONJURATION, nodes(
                    0.50D, 0.94D, 0.50D, 0.82D, 0.35D, 0.70D, 0.65D, 0.70D, 0.25D, 0.58D,
                    0.45D, 0.56D, 0.55D, 0.56D, 0.75D, 0.58D, 0.18D, 0.44D, 0.36D, 0.38D,
                    0.64D, 0.38D, 0.82D, 0.44D, 0.28D, 0.22D, 0.72D, 0.22D, 0.50D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 6, 3, 7, 4, 8, 5, 9, 6, 10, 7, 11,
                            8, 12, 9, 12, 10, 13, 11, 13, 12, 14, 13, 14)),
            tree(ModAttributes.DESTRUCTION, nodes(
                    0.50D, 0.94D, 0.50D, 0.80D, 0.28D, 0.66D, 0.50D, 0.64D, 0.72D, 0.66D,
                    0.22D, 0.50D, 0.40D, 0.46D, 0.60D, 0.46D, 0.78D, 0.50D, 0.30D, 0.31D,
                    0.50D, 0.28D, 0.70D, 0.31D, 0.40D, 0.14D, 0.60D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 1, 4, 2, 5, 2, 6, 3, 6, 3, 7, 4, 7, 4, 8, 5, 9,
                            6, 9, 6, 10, 7, 10, 7, 11, 8, 11, 9, 12, 10, 12, 10, 13, 11, 13)),
            tree(ModAttributes.ENCHANTING, nodes(
                    0.50D, 0.92D, 0.50D, 0.74D, 0.34D, 0.58D, 0.66D, 0.58D, 0.24D, 0.40D,
                    0.50D, 0.42D, 0.76D, 0.40D, 0.42D, 0.24D, 0.58D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 7, 6, 8, 7, 8)),
            tree(ModAttributes.ILLUSION, nodes(
                    0.50D, 0.94D, 0.50D, 0.80D, 0.35D, 0.68D, 0.65D, 0.68D, 0.25D, 0.54D,
                    0.50D, 0.54D, 0.75D, 0.54D, 0.20D, 0.39D, 0.40D, 0.35D, 0.60D, 0.35D,
                    0.80D, 0.39D, 0.50D, 0.20D, 0.50D, 0.06D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 8, 5, 9, 6, 10,
                            7, 11, 8, 11, 9, 11, 10, 11, 11, 12)),
            tree(ModAttributes.RESTORATION, nodes(
                    0.50D, 0.94D, 0.50D, 0.80D, 0.35D, 0.66D, 0.65D, 0.66D, 0.28D, 0.51D,
                    0.50D, 0.50D, 0.72D, 0.51D, 0.24D, 0.34D, 0.44D, 0.32D, 0.64D, 0.32D,
                    0.50D, 0.17D, 0.50D, 0.06D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 8, 5, 9, 6, 9,
                            7, 10, 8, 10, 9, 10, 10, 11)),
            tree(ModAttributes.ALCHEMY, nodes(
                    0.50D, 0.92D, 0.50D, 0.76D, 0.32D, 0.62D, 0.68D, 0.62D, 0.23D, 0.45D,
                    0.50D, 0.44D, 0.77D, 0.45D, 0.38D, 0.24D, 0.58D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 7, 6, 8, 7, 8)),
            tree(ModAttributes.LIGHT_ARMOR, nodes(
                    0.50D, 0.92D, 0.45D, 0.72D, 0.62D, 0.58D, 0.32D, 0.48D, 0.54D, 0.32D,
                    0.50D, 0.10D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 3, 4, 4, 5)),
            tree(ModAttributes.LOCKPICKING, nodes(
                    0.50D, 0.92D, 0.50D, 0.78D, 0.36D, 0.64D, 0.64D, 0.64D, 0.26D, 0.50D,
                    0.50D, 0.48D, 0.74D, 0.50D, 0.18D, 0.34D, 0.40D, 0.28D, 0.62D, 0.28D,
                    0.50D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 4, 7, 2, 5, 3, 5, 3, 6, 5, 8, 5, 9, 8, 10, 9, 10)),
            tree(ModAttributes.PICKPOCKET, nodes(
                    0.50D, 0.92D, 0.48D, 0.74D, 0.34D, 0.58D, 0.62D, 0.58D, 0.28D, 0.40D,
                    0.50D, 0.38D, 0.70D, 0.40D, 0.50D, 0.12D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 7, 6, 7)),
            tree(ModAttributes.SNEAK, nodes(
                    0.50D, 0.92D, 0.50D, 0.76D, 0.35D, 0.62D, 0.63D, 0.60D, 0.26D, 0.44D,
                    0.48D, 0.44D, 0.74D, 0.42D, 0.58D, 0.26D, 0.50D, 0.08D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 5, 7, 6, 7, 7, 8)),
            tree(ModAttributes.BARTER, nodes(
                    0.50D, 0.92D, 0.50D, 0.75D, 0.32D, 0.62D, 0.68D, 0.62D, 0.24D, 0.45D,
                    0.48D, 0.46D, 0.76D, 0.45D, 0.38D, 0.28D, 0.58D, 0.10D),
                    edges(0, 1, 1, 2, 1, 3, 2, 4, 2, 5, 3, 5, 3, 6, 4, 7, 5, 7, 6, 8, 7, 8))
    );

    private static int lastTreeIndex;

    private int treeIndex = lastTreeIndex;
    private int selectedNodeIndex = -1;

    public SkillTreeScreen() {
        super(Component.translatable("screen.thuumcraft.skill_trees"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderSkyBackground(guiGraphics);
        SkillTreeDefinition tree = currentTree();
        TreeBounds bounds = treeBounds();
        renderTreeNavigation(guiGraphics, tree);
        renderTree(guiGraphics, tree, bounds, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_A) {
            previousTree();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D) {
            nextTree();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int selectedTree = clickedNavigationTree(mouseX, mouseY);
            if (selectedTree >= 0) {
                setTreeIndex(selectedTree);
                return true;
            }

            int lowerNavigationY = this.height - 32;
            if (mouseY >= lowerNavigationY && mouseX < this.width / 2.0D) {
                previousTree();
                return true;
            }
            if (mouseY >= lowerNavigationY && mouseX >= this.width / 2.0D) {
                nextTree();
                return true;
            }

            SkillTreeDefinition tree = currentTree();
            selectedNodeIndex = hoveredNode(tree, treeBounds(), (int) mouseX, (int) mouseY);
            return selectedNodeIndex >= 0;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta > 0.0D) {
            previousTree();
            return true;
        }
        if (delta < 0.0D) {
            nextTree();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderSkyBackground(GuiGraphics guiGraphics) {
        guiGraphics.blit(BACKGROUND, 0, 0, this.width, this.height, 0.0F, 0.0F,
                BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        guiGraphics.fill(0, 0, this.width, this.height, 0xA8040912);
        guiGraphics.fillGradient(0, 0, this.width, this.height / 3, 0xD8000000, 0x22000000);
        guiGraphics.fillGradient(0, this.height * 2 / 3, this.width, this.height, 0x22000000, 0xD8000000);
    }

    private void renderTreeNavigation(GuiGraphics guiGraphics, SkillTreeDefinition tree) {
        Component heading = tree.displayName().copy().append(" " + skillValue(tree));
        guiGraphics.drawCenteredString(this.font, heading, this.width / 2, 18, TEXT_COLOR);
        renderBottomSkillStrip(guiGraphics);
        guiGraphics.drawCenteredString(this.font, Component.literal("<"), this.width / 2 - 118, this.height - 18, DIM_TEXT_COLOR);
        guiGraphics.drawCenteredString(this.font, Component.literal(">"), this.width / 2 + 118, this.height - 18, DIM_TEXT_COLOR);
    }

    private void renderBottomSkillStrip(GuiGraphics guiGraphics) {
        int centerX = this.width / 2;
        int y = this.height - 31;
        int spacing = Math.max(58, Math.min(86, this.width / VISIBLE_NAVIGATION_TREES));
        int middle = VISIBLE_NAVIGATION_TREES / 2;

        for (int offset = -middle; offset <= middle; offset++) {
            int index = wrapTreeIndex(treeIndex + offset);
            SkillTreeDefinition tree = TREES.get(index);
            int x = centerX + offset * spacing;
            int color = offset == 0 ? TEXT_COLOR : Math.abs(offset) == 1 ? MUTED_TEXT_COLOR : DIM_TEXT_COLOR;
            guiGraphics.drawCenteredString(this.font, tree.displayName(), x, y, color);
        }
    }

    private void renderTree(GuiGraphics guiGraphics, SkillTreeDefinition tree, TreeBounds bounds, int mouseX, int mouseY) {
        int hoveredNode = hoveredNode(tree, bounds, mouseX, mouseY);
        for (Edge edge : tree.edges()) {
            Node from = tree.nodes().get(edge.from());
            Node to = tree.nodes().get(edge.to());
            int fromX = bounds.x(from.x());
            int fromY = bounds.y(from.y());
            int toX = bounds.x(to.x());
            int toY = bounds.y(to.y());
            drawLine(guiGraphics, fromX, fromY, toX, toY, LINE_GLOW_COLOR, 1);
            drawLine(guiGraphics, fromX, fromY, toX, toY, LINE_COLOR, 0);
        }

        for (int i = 0; i < tree.nodes().size(); i++) {
            Node node = tree.nodes().get(i);
            int x = bounds.x(node.x());
            int y = bounds.y(node.y());
            int color = i == selectedNodeIndex ? NODE_SELECTED_COLOR : i == hoveredNode ? NODE_HOVER_COLOR : NODE_COLOR;
            drawNode(guiGraphics, x, y, color);
        }
    }

    private void drawNode(GuiGraphics guiGraphics, int x, int y, int color) {
        guiGraphics.fill(x - 5, y, x + 6, y + 1, NODE_GLOW_COLOR);
        guiGraphics.fill(x, y - 5, x + 1, y + 6, NODE_GLOW_COLOR);
        guiGraphics.fill(x - 3, y, x + 4, y + 1, color);
        guiGraphics.fill(x, y - 3, x + 1, y + 4, color);
        guiGraphics.fill(x - 1, y - 1, x + 2, y + 2, 0xFFFFFFFF);
    }

    private void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color, int radius) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int error = dx - dy;
        int x = x1;
        int y = y1;

        while (true) {
            guiGraphics.fill(x - radius, y - radius, x + radius + 1, y + radius + 1, color);
            if (x == x2 && y == y2) {
                return;
            }

            int doubledError = error * 2;
            if (doubledError > -dy) {
                error -= dy;
                x += sx;
            }
            if (doubledError < dx) {
                error += dx;
                y += sy;
            }
        }
    }

    private int hoveredNode(SkillTreeDefinition tree, TreeBounds bounds, int mouseX, int mouseY) {
        for (int i = 0; i < tree.nodes().size(); i++) {
            Node node = tree.nodes().get(i);
            int x = bounds.x(node.x());
            int y = bounds.y(node.y());
            int dx = mouseX - x;
            int dy = mouseY - y;
            if (dx * dx + dy * dy <= NODE_PICK_RADIUS * NODE_PICK_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private TreeBounds treeBounds() {
        int treeWidth = Math.min(Math.max(this.width - 128, 170), 440);
        int treeHeight = Math.min(Math.max(this.height - 96, 126), 300);
        int left = this.width / 2 - treeWidth / 2;
        int top = this.height / 2 - treeHeight / 2 + 4;
        return new TreeBounds(left, top, treeWidth, treeHeight);
    }

    private SkillTreeDefinition currentTree() {
        return TREES.get(treeIndex);
    }

    private void previousTree() {
        setTreeIndex(wrapTreeIndex(treeIndex - 1));
    }

    private void nextTree() {
        setTreeIndex(wrapTreeIndex(treeIndex + 1));
    }

    private void setTreeIndex(int treeIndex) {
        this.treeIndex = wrapTreeIndex(treeIndex);
        lastTreeIndex = this.treeIndex;
        selectedNodeIndex = -1;
    }

    private int clickedNavigationTree(double mouseX, double mouseY) {
        int y = this.height - 31;
        if (mouseY < y - 8 || mouseY > y + 12) {
            return -1;
        }

        int centerX = this.width / 2;
        int spacing = Math.max(58, Math.min(86, this.width / VISIBLE_NAVIGATION_TREES));
        int middle = VISIBLE_NAVIGATION_TREES / 2;
        for (int offset = -middle; offset <= middle; offset++) {
            int x = centerX + offset * spacing;
            if (Math.abs(mouseX - x) <= spacing / 2.0D) {
                return wrapTreeIndex(treeIndex + offset);
            }
        }
        return -1;
    }

    private int wrapTreeIndex(int index) {
        return (index % TREES.size() + TREES.size()) % TREES.size();
    }

    private int skillValue(SkillTreeDefinition tree) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return 0;
        }

        AttributeInstance instance = this.minecraft.player.getAttribute(tree.attribute().get());
        return instance == null ? 0 : (int) Math.round(instance.getValue());
    }

    private static SkillTreeDefinition tree(RegistryObject<Attribute> attribute, List<Node> nodes, List<Edge> edges) {
        return new SkillTreeDefinition(attribute, nodes, edges);
    }

    private static List<Node> nodes(double... coordinates) {
        if (coordinates.length % 2 != 0) {
            throw new IllegalArgumentException("Node coordinates must be x/y pairs");
        }

        ArrayList<Node> nodes = new ArrayList<>(coordinates.length / 2);
        for (int i = 0; i < coordinates.length; i += 2) {
            nodes.add(new Node(coordinates[i], coordinates[i + 1]));
        }
        return List.copyOf(nodes);
    }

    private static List<Edge> edges(int... indices) {
        if (indices.length % 2 != 0) {
            throw new IllegalArgumentException("Edge indices must be from/to pairs");
        }

        ArrayList<Edge> edges = new ArrayList<>(indices.length / 2);
        for (int i = 0; i < indices.length; i += 2) {
            edges.add(new Edge(indices[i], indices[i + 1]));
        }
        return List.copyOf(edges);
    }

    private record SkillTreeDefinition(RegistryObject<Attribute> attribute, List<Node> nodes, List<Edge> edges) {
        private Component displayName() {
            return Component.translatable(attribute.get().getDescriptionId());
        }
    }

    private record Node(double x, double y) {
    }

    private record Edge(int from, int to) {
    }

    private record TreeBounds(int left, int top, int width, int height) {
        private int x(double normalizedX) {
            return left + (int) Math.round(normalizedX * width);
        }

        private int y(double normalizedY) {
            return top + (int) Math.round(normalizedY * height);
        }
    }

}
