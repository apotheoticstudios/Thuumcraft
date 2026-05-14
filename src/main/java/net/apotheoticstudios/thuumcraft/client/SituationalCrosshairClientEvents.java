package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.stealth.SneakAwareness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CartographyTableBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class SituationalCrosshairClientEvents {
    private static final int SPRITE_SIZE = 32;
    private SituationalCrosshairClientEvents() {
    }

    @SubscribeEvent
    public static void renderCrosshair(RenderGuiOverlayEvent.Pre event) {
        if (!VanillaGuiOverlay.CROSSHAIR.id().equals(event.getOverlay().id())) {
            return;
        }

        CrosshairProfile profile = getCrosshairProfile(Minecraft.getInstance());
        if (profile == CrosshairProfile.VANILLA) {
            return;
        }

        event.setCanceled(true);
        if (profile.sprite() != null) {
            renderSprite(event.getGuiGraphics(), profile.sprite());
        }
    }

    private static CrosshairProfile getCrosshairProfile(Minecraft minecraft) {
        Player player = minecraft.player;
        Level level = minecraft.level;
        if (player == null || level == null || minecraft.options.hideGui || minecraft.options.renderDebug || player.isSpectator()) {
            return CrosshairProfile.VANILLA;
        }

        if (minecraft.screen != null || minecraft.options.getCameraType().isMirrored()) {
            return CrosshairProfile.HIDDEN;
        }

        SneakAwareness sneakAwareness = ClientSneakAwarenessState.awareness();
        if (isTryingToSneak(player)
                && Config.ENABLE_STEALTH_SYSTEM.get()
                && sneakAwareness != SneakAwareness.DISABLED) {
            return switch (sneakAwareness) {
                case DISABLED -> CrosshairProfile.of(CrosshairSprite.NORMAL);
                case DETECTED -> CrosshairProfile.of(CrosshairSprite.SNEAK_DETECTED);
                case SEARCHING -> CrosshairProfile.of(CrosshairSprite.SNEAK_SEARCHING);
                case SUSPICIOUS -> CrosshairProfile.of(CrosshairSprite.SNEAK_SUSPICIOUS);
                case HIDDEN -> CrosshairProfile.of(CrosshairSprite.SNEAK_HIDDEN);
            };
        }

        ItemStack activeOrMainHand = getActiveOrMainHandItem(player);
        if (player.isUsingItem()) {
            if (isAimingItem(activeOrMainHand)) {
                return CrosshairProfile.of(CrosshairSprite.AIM);
            }
            if (isBlockingItem(activeOrMainHand)) {
                return CrosshairProfile.of(CrosshairSprite.BLOCK);
            }
        }
        if (minecraft.options.keyUse.isDown()) {
            if (hasAimingItem(player)) {
                return CrosshairProfile.of(CrosshairSprite.AIM);
            }
            if (hasBlockingItem(player)) {
                return CrosshairProfile.of(CrosshairSprite.BLOCK);
            }
        }

        HitResult hitResult = minecraft.hitResult;
        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (isHostileEntity(player, entity) && isAttackableEntity(player, entity)) {
                return CrosshairProfile.of(CrosshairSprite.HOSTILE);
            }
            if (isInteractableEntity(player, entity)) {
                return CrosshairProfile.of(CrosshairSprite.INTERACT);
            }
            if (isAttackableEntity(player, entity)) {
                return CrosshairProfile.of(CrosshairSprite.COMBAT);
            }
        }

        if (hitResult instanceof BlockHitResult blockHitResult
                && hitResult.getType() == HitResult.Type.BLOCK
                && isInteractableBlock(level, blockHitResult.getBlockPos())) {
            return CrosshairProfile.of(CrosshairSprite.INTERACT);
        }

        if (hasCombatItem(player)) {
            return CrosshairProfile.of(CrosshairSprite.COMBAT);
        }

        return CrosshairProfile.of(CrosshairSprite.NORMAL);
    }

    private static boolean isTryingToSneak(Player player) {
        return (player.isCrouching() || player.isShiftKeyDown()) && canEnterStealthMode(player);
    }

    private static boolean canEnterStealthMode(Player player) {
        if (!player.onGround()
                || player.isInWaterOrBubble()
                || player.isInLava()
                || player.isSwimming()
                || player.isFallFlying()
                || player.getAbilities().flying) {
            return false;
        }

        BlockPos groundPos = player.getOnPos();
        BlockState groundState = player.level().getBlockState(groundPos);
        return !groundState.getCollisionShape(player.level(), groundPos).isEmpty();
    }

    private static ItemStack getActiveOrMainHandItem(Player player) {
        ItemStack useItem = player.getUseItem();
        return useItem.isEmpty() ? player.getMainHandItem() : useItem;
    }

    private static boolean isInteractableBlock(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();

        if (blockState.getMenuProvider(level, pos) != null || blockState.hasBlockEntity()) {
            return true;
        }

        return block instanceof AbstractCauldronBlock
                || block instanceof AbstractChestBlock<?>
                || block instanceof AbstractFurnaceBlock
                || block instanceof AnvilBlock
                || block instanceof BarrelBlock
                || block instanceof BeaconBlock
                || block instanceof BedBlock
                || block instanceof BellBlock
                || block instanceof BrewingStandBlock
                || block instanceof ButtonBlock
                || block instanceof CakeBlock
                || block instanceof CampfireBlock
                || block instanceof CartographyTableBlock
                || block instanceof ComposterBlock
                || block instanceof CraftingTableBlock
                || block instanceof DoorBlock
                || block instanceof EnchantmentTableBlock
                || block instanceof EnderChestBlock
                || block instanceof FenceGateBlock
                || block instanceof FlowerPotBlock
                || block instanceof GrindstoneBlock
                || block instanceof JukeboxBlock
                || block instanceof LecternBlock
                || block instanceof LeverBlock
                || block instanceof LoomBlock
                || block instanceof NoteBlock
                || block instanceof SignBlock
                || block instanceof SmithingTableBlock
                || block instanceof StonecutterBlock
                || block instanceof TrapDoorBlock;
    }

    private static boolean isInteractableEntity(Player player, Entity entity) {
        if (!entity.isAlive() || entity.isSpectator()) {
            return false;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        return entity instanceof AbstractVillager
                || entity instanceof AbstractHorse
                || entity instanceof Allay
                || entity instanceof ArmorStand
                || entity instanceof Boat
                || entity instanceof AbstractMinecart
                || entity instanceof ItemFrame
                || isAnimalInteraction(mainHand, entity)
                || isAnimalInteraction(offHand, entity)
                || isUtilityInteraction(mainHand, entity)
                || isUtilityInteraction(offHand, entity);
    }

    private static boolean isAnimalInteraction(ItemStack stack, Entity entity) {
        return !stack.isEmpty() && entity instanceof Animal animal && animal.isFood(stack);
    }

    private static boolean isUtilityInteraction(ItemStack stack, Entity entity) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        return item instanceof NameTagItem
                || item instanceof LeadItem
                || item instanceof SpawnEggItem
                || item instanceof BucketItem && entity instanceof Bucketable
                || item instanceof ShearsItem && entity instanceof Shearable;
    }

    private static boolean isHostileEntity(Player player, Entity entity) {
        return entity instanceof Enemy || entity instanceof Mob mob && mob.getTarget() == player;
    }

    private static boolean isAttackableEntity(Player player, Entity entity) {
        return entity instanceof LivingEntity livingEntity
                && livingEntity != player
                && livingEntity.isAlive()
                && livingEntity.isPickable()
                && !livingEntity.isSpectator()
                && !livingEntity.isAlliedTo(player);
    }

    private static boolean hasCombatItem(Player player) {
        return isCombatItem(player.getMainHandItem()) || isCombatItem(player.getOffhandItem());
    }

    private static boolean hasAimingItem(Player player) {
        return isAimingItem(player.getMainHandItem()) || isAimingItem(player.getOffhandItem());
    }

    private static boolean hasBlockingItem(Player player) {
        return isBlockingItem(player.getMainHandItem()) || isBlockingItem(player.getOffhandItem());
    }

    private static boolean isCombatItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        return item instanceof SwordItem
                || item instanceof AxeItem
                || item instanceof TridentItem
                || item instanceof ProjectileWeaponItem
                || item instanceof ShieldItem
                || isAimingUseAnimation(stack)
                || isBlockingUseAnimation(stack);
    }

    private static boolean isAimingItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        return item instanceof ProjectileWeaponItem
                || item instanceof TridentItem
                || isAimingUseAnimation(stack);
    }

    private static boolean isBlockingItem(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof ShieldItem || isBlockingUseAnimation(stack));
    }

    private static boolean isAimingUseAnimation(ItemStack stack) {
        UseAnim useAnimation = stack.getUseAnimation();
        return useAnimation == UseAnim.BOW || useAnimation == UseAnim.CROSSBOW || useAnimation == UseAnim.SPEAR;
    }

    private static boolean isBlockingUseAnimation(ItemStack stack) {
        return stack.getUseAnimation() == UseAnim.BLOCK;
    }

    private static void renderSprite(GuiGraphics guiGraphics, CrosshairSprite sprite) {
        int x = guiGraphics.guiWidth() / 2 - sprite.width() / 2;
        int y = guiGraphics.guiHeight() / 2 - sprite.height() / 2;
        guiGraphics.blit(sprite.texture(), x, y, 0, 0, sprite.width(), sprite.height(),
                sprite.textureWidth(), sprite.textureHeight());
    }

    private record CrosshairProfile(CrosshairSprite sprite) {
        private static final CrosshairProfile VANILLA = new CrosshairProfile(null);
        private static final CrosshairProfile HIDDEN = new CrosshairProfile(null);

        private static CrosshairProfile of(CrosshairSprite sprite) {
            return new CrosshairProfile(sprite);
        }
    }

    private enum CrosshairSprite {
        NORMAL("normal"),
        INTERACT("interact"),
        COMBAT("combat"),
        HOSTILE("hostile"),
        AIM("aim"),
        BLOCK("block"),
        SNEAK_HIDDEN("sneak_hidden", 32, 16),
        SNEAK_SUSPICIOUS("sneak_suspicious", 32, 16),
        SNEAK_SEARCHING("sneak_searching", 32, 16),
        SNEAK_DETECTED("sneak_detected", 32, 16);

        private final ResourceLocation texture;
        private final int width;
        private final int height;
        private final int textureWidth;
        private final int textureHeight;

        CrosshairSprite(String name) {
            this(name, SPRITE_SIZE, SPRITE_SIZE);
        }

        CrosshairSprite(String name, int width, int height) {
            this.texture = ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID, "textures/gui/crosshair/" + name + ".png");
            this.width = width;
            this.height = height;
            this.textureWidth = width;
            this.textureHeight = height;
        }

        private ResourceLocation texture() {
            return texture;
        }

        private int width() {
            return width;
        }

        private int height() {
            return height;
        }

        private int textureWidth() {
            return textureWidth;
        }

        private int textureHeight() {
            return textureHeight;
        }
    }

}
