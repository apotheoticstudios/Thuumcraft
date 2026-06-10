package net.apotheoticstudios.thuumcraft.client;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.setup.Messages;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.magic.IronLearnedSpellHelper;
import net.apotheoticstudios.thuumcraft.magic.SkyrimMagicScaling;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.apotheoticstudios.thuumcraft.network.ServerboundCastSelectedSpellPacket;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.EmptyMapItem;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class SkyrimMagicClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final double FIRST_PERSON_HAND_FORWARD = 0.48D;
    private static final double FIRST_PERSON_HAND_SIDE = 0.50D;
    private static final double FIRST_PERSON_HAND_DOWN = 0.56D;
    private static final double THIRD_PERSON_HAND_FORWARD = 0.16D;
    private static final double THIRD_PERSON_HAND_SIDE = 0.44D;
    private static final double THIRD_PERSON_HAND_HEIGHT = 0.55D;

    private static InteractionHand heldCastHand;
    private static boolean attackKeyWasDown;
    private static boolean useKeyWasDown;

    private SkyrimMagicClientEvents() {
    }

    @SubscribeEvent
    public static void castSelectedSpell(InputEvent.InteractionKeyMappingTriggered event) {
        try {
            InteractionHand castHand = castHandFor(event);
            if (castHand == null) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) {
                return;
            }

            if (tryStartSelectedSpellCast(castHand)) {
                cancelInteraction(event);
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to handle selected spell interaction input", exception);
        }
    }

    @SubscribeEvent
    public static void releaseSelectedSpellMouse(InputEvent.MouseButton.Pre event) {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options == null) {
                return;
            }

            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (minecraft.options.keyAttack.matchesMouse(event.getButton())
                        && tryStartSelectedSpellCast(InteractionHand.MAIN_HAND)) {
                    cancelMouse(event);
                } else if (minecraft.options.keyUse.matchesMouse(event.getButton())
                        && tryStartSelectedSpellCast(InteractionHand.OFF_HAND)) {
                    cancelMouse(event);
                }
                return;
            }

            if (event.getAction() != GLFW.GLFW_RELEASE) {
                return;
            }

            if (heldCastHand == InteractionHand.MAIN_HAND && minecraft.options.keyAttack.matchesMouse(event.getButton())) {
                releaseSelectedSpellCast();
            } else if (heldCastHand == InteractionHand.OFF_HAND && minecraft.options.keyUse.matchesMouse(event.getButton())) {
                releaseSelectedSpellCast();
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to handle selected spell mouse input", exception);
        }
    }

    @SubscribeEvent
    public static void releaseSelectedSpellKey(InputEvent.Key event) {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options == null || event.getAction() != GLFW.GLFW_RELEASE) {
                return;
            }

            if (heldCastHand == InteractionHand.MAIN_HAND && minecraft.options.keyAttack.matches(event.getKey(), event.getScanCode())) {
                releaseSelectedSpellCast();
            } else if (heldCastHand == InteractionHand.OFF_HAND && minecraft.options.keyUse.matches(event.getKey(), event.getScanCode())) {
                releaseSelectedSpellCast();
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to handle selected spell key input", exception);
        }
    }

    @SubscribeEvent
    public static void tickSelectedSpellInputAndParticles(TickEvent.ClientTickEvent event) {
        try {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null || minecraft.level == null || minecraft.options == null) {
                attackKeyWasDown = false;
                useKeyWasDown = false;
                return;
            }

            spawnSelectedSpellHandParticles(minecraft);

            boolean attackDown = minecraft.options.keyAttack.isDown();
            boolean useDown = minecraft.options.keyUse.isDown();
            if (minecraft.screen == null) {
                if (attackDown && !attackKeyWasDown) {
                    tryStartSelectedSpellCast(InteractionHand.MAIN_HAND);
                }
                if (useDown && !useKeyWasDown) {
                    tryStartSelectedSpellCast(InteractionHand.OFF_HAND);
                }
            }

            if (!attackDown && attackKeyWasDown && heldCastHand == InteractionHand.MAIN_HAND) {
                releaseSelectedSpellCast();
            }
            if (!useDown && useKeyWasDown && heldCastHand == InteractionHand.OFF_HAND) {
                releaseSelectedSpellCast();
            }

            attackKeyWasDown = attackDown;
            useKeyWasDown = useDown;
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to tick selected spell input or hand particles", exception);
            heldCastHand = null;
            attackKeyWasDown = false;
            useKeyWasDown = false;
        }
    }

    @SubscribeEvent
    public static void hideContinuousSkyrimCastBar(RenderGuiOverlayEvent.Pre event) {
        try {
            if (!ClientMagicData.isCasting()
                    || ClientMagicData.getCastType() != CastType.CONTINUOUS
                    || event.getOverlay() == null
                    || event.getOverlay().id() == null) {
                return;
            }

            AbstractSpell spell = SpellRegistry.getSpell(ClientMagicData.getCastingSpellId());
            if (SkyrimMagicScaling.isThuumcraftSkyrimSpell(spell)
                    && "irons_spellbooks".equals(event.getOverlay().id().getNamespace())
                    && event.getOverlay().id().getPath().contains("cast")
                    && event.isCancelable()) {
                event.setCanceled(true);
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to hide Skyrim continuous cast bar", exception);
        }
    }

    @SubscribeEvent
    public static void loadSelectedSpells(ClientPlayerNetworkEvent.LoggingIn event) {
        heldCastHand = null;
        attackKeyWasDown = false;
        useKeyWasDown = false;
        SelectedMagicSpellState.load(event.getPlayer());
    }

    @SubscribeEvent
    public static void resetSelectedSpellInput(ClientPlayerNetworkEvent.LoggingOut event) {
        heldCastHand = null;
        attackKeyWasDown = false;
        useKeyWasDown = false;
        SelectedMagicSpellState.forgetLoadedPlayer();
    }

    private static InteractionHand castHandFor(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isAttack()) {
            return InteractionHand.MAIN_HAND;
        }
        if (event.isUseItem() && event.getHand() == InteractionHand.OFF_HAND) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    private static boolean tryStartSelectedSpellCast(InteractionHand castHand) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) {
            return false;
        }

        ResourceLocation spellId = SelectedMagicSpellState.selectedSpellId(castHand);
        if (spellId == null) {
            return false;
        }

        if (castHand == InteractionHand.OFF_HAND && shouldDeferOffhandSpellToRightClickUse(minecraft)) {
            if (heldCastHand != null) {
                releaseSelectedSpellCast();
            }
            return false;
        }

        if (heldCastHand != null) {
            return true;
        }

        if (!minecraft.player.getItemInHand(castHand).isEmpty()) {
            return false;
        }

        AbstractSpell spell = SelectedMagicSpellState.selectedSpell(castHand);
        if (spell == null || spell == SpellRegistry.none() || spell.getCastType() == CastType.NONE) {
            SelectedMagicSpellState.clear(castHand);
            return false;
        }

        if (!isLearnedOnClient(minecraft, spell)) {
            minecraft.player.displayClientMessage(Component.translatable("message.thuumcraft.magic.not_learned", spell.getDisplayName(minecraft.player))
                    .withStyle(ChatFormatting.RED), true);
            return false;
        }

        ModMessages.sendToServer(new ServerboundCastSelectedSpellPacket(spellId.toString(), castHand));
        heldCastHand = castHand;
        return true;
    }

    private static boolean shouldDeferOffhandSpellToRightClickUse(Minecraft minecraft) {
        if (minecraft.player == null) {
            return false;
        }
        return canUseWithRightClick(minecraft, minecraft.player.getMainHandItem())
                || canUseWithRightClick(minecraft, minecraft.player.getOffhandItem());
    }

    private static boolean canUseWithRightClick(Minecraft minecraft, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        if (hasAirRightClickUse(minecraft, stack, item)) {
            return true;
        }

        HitResult hitResult = minecraft.hitResult;
        if (hitResult == null) {
            return false;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hasBlockRightClickUse(item);
        }
        return hitResult.getType() == HitResult.Type.ENTITY && hasEntityRightClickUse(item);
    }

    private static boolean hasAirRightClickUse(Minecraft minecraft, ItemStack stack, Item item) {
        UseAnim useAnimation = stack.getUseAnimation();
        if (useAnimation == UseAnim.EAT) {
            FoodProperties food = stack.getFoodProperties(minecraft.player);
            return food != null && minecraft.player.canEat(food.canAlwaysEat());
        }
        if (useAnimation != UseAnim.NONE) {
            return true;
        }
        return item instanceof BucketItem
                || item instanceof BottleItem
                || item instanceof MilkBucketItem
                || item instanceof EnderpearlItem
                || item instanceof EggItem
                || item instanceof ExperienceBottleItem
                || item instanceof ThrowablePotionItem
                || item instanceof FireworkRocketItem
                || item instanceof FishingRodItem
                || item instanceof EnderEyeItem
                || item instanceof InstrumentItem
                || item instanceof EmptyMapItem
                || item instanceof WritableBookItem
                || item instanceof WrittenBookItem
                || item instanceof FoodOnAStickItem<?>
                || item instanceof ArmorItem
                || item instanceof HorseArmorItem
                || item instanceof ElytraItem;
    }

    private static boolean hasBlockRightClickUse(Item item) {
        return item instanceof BlockItem
                || item instanceof BoatItem
                || item instanceof MinecartItem
                || item instanceof SpawnEggItem
                || item instanceof EndCrystalItem
                || item instanceof HangingEntityItem
                || item instanceof BucketItem
                || item instanceof BottleItem
                || item instanceof FlintAndSteelItem
                || item instanceof FireChargeItem
                || item instanceof BoneMealItem
                || item instanceof DyeItem
                || item instanceof HoneycombItem
                || item instanceof ShearsItem
                || item instanceof HoeItem
                || item instanceof ShovelItem
                || item instanceof AxeItem;
    }

    private static boolean hasEntityRightClickUse(Item item) {
        return item instanceof SpawnEggItem
                || item instanceof NameTagItem
                || item instanceof LeadItem
                || item instanceof SaddleItem
                || item instanceof BucketItem
                || item instanceof ShearsItem
                || item instanceof DyeItem;
    }

    private static void spawnSelectedSpellHandParticles(Minecraft minecraft) {
        if (minecraft.player.tickCount % 2 != 0) {
            return;
        }

        spawnSelectedSpellHandParticles(minecraft, InteractionHand.MAIN_HAND);
        spawnSelectedSpellHandParticles(minecraft, InteractionHand.OFF_HAND);
    }

    private static void spawnSelectedSpellHandParticles(Minecraft minecraft, InteractionHand hand) {
        if (!minecraft.player.getItemInHand(hand).isEmpty()) {
            return;
        }

        AbstractSpell spell = SelectedMagicSpellState.selectedSpell(hand);
        if (spell == null || spell == SpellRegistry.none() || spell.getCastType() == CastType.NONE) {
            return;
        }
        if (!isLearnedOnClient(minecraft, spell)) {
            return;
        }

        HandParticleStyle style = particleStyle(spell);
        Vec3 position = handParticlePosition(minecraft, hand);
        RandomSource random = minecraft.player.getRandom();
        boolean activeCastHand = hand == heldCastHand && ClientMagicData.isCasting();
        double jitter = activeCastHand ? 0.034D : 0.045D;
        int dustCount = activeCastHand ? 2 : 1;
        for (int i = 0; i < dustCount; i++) {
            minecraft.level.addParticle(style.dust(),
                    position.x + random.nextGaussian() * jitter,
                    position.y + random.nextGaussian() * jitter,
                    position.z + random.nextGaussian() * jitter,
                    random.nextGaussian() * 0.002D, 0.010D, random.nextGaussian() * 0.002D);
        }

        int accentInterval = activeCastHand ? Math.max(2, style.accentInterval() / 2) : style.accentInterval();
        if (minecraft.player.tickCount % accentInterval == 0) {
            minecraft.level.addParticle(style.accent(),
                    position.x + random.nextGaussian() * jitter,
                    position.y + random.nextGaussian() * jitter,
                    position.z + random.nextGaussian() * jitter,
                    random.nextGaussian() * 0.006D, 0.010D, random.nextGaussian() * 0.006D);
        }
    }

    private static Vec3 handParticlePosition(Minecraft minecraft, InteractionHand hand) {
        Vec3 look = minecraft.player.getLookAngle().normalize();
        float yawRadians = minecraft.player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 right = new Vec3(Mth.cos(yawRadians), 0.0D, Mth.sin(yawRadians));
        boolean mainArmRight = minecraft.player.getMainArm() == HumanoidArm.RIGHT;
        double sideSign = (hand == InteractionHand.MAIN_HAND) == mainArmRight ? 1.0D : -1.0D;

        if (minecraft.options.getCameraType().isFirstPerson()) {
            return minecraft.player.getEyePosition()
                    .add(look.scale(FIRST_PERSON_HAND_FORWARD))
                    .add(right.scale(sideSign * FIRST_PERSON_HAND_SIDE))
                    .add(0.0D, -FIRST_PERSON_HAND_DOWN, 0.0D);
        }

        return minecraft.player.position()
                .add(0.0D, minecraft.player.getBbHeight() * THIRD_PERSON_HAND_HEIGHT, 0.0D)
                .add(look.scale(THIRD_PERSON_HAND_FORWARD))
                .add(right.scale(sideSign * THIRD_PERSON_HAND_SIDE));
    }

    private static HandParticleStyle particleStyle(AbstractSpell spell) {
        return switch (SkyrimMagicScaling.elementFor(spell)) {
            case FIRE -> new HandParticleStyle(dust(1.0F, 0.24F, 0.04F, 0.75F), ParticleTypes.FLAME, 4);
            case FROST -> new HandParticleStyle(dust(0.45F, 0.85F, 1.0F, 0.7F), ParticleTypes.SNOWFLAKE, 5);
            case SHOCK -> new HandParticleStyle(dust(0.62F, 0.58F, 1.0F, 0.75F), ParticleTypes.END_ROD, 3);
            case NONE -> schoolParticleStyle(spell);
        };
    }

    private static HandParticleStyle schoolParticleStyle(AbstractSpell spell) {
        SkillProgression.Skill skill = SkyrimMagicScaling.skillFor(spell);
        if (skill == SkillProgression.Skill.ALTERATION) {
            return new HandParticleStyle(dust(0.45F, 0.95F, 0.82F, 0.65F), ParticleTypes.COMPOSTER, 6);
        }
        if (skill == SkillProgression.Skill.CONJURATION) {
            return new HandParticleStyle(dust(0.48F, 0.18F, 0.92F, 0.75F), ParticleTypes.PORTAL, 5);
        }
        if (skill == SkillProgression.Skill.ILLUSION) {
            return new HandParticleStyle(dust(0.92F, 0.42F, 1.0F, 0.7F), ParticleTypes.ENCHANT, 5);
        }
        if (skill == SkillProgression.Skill.RESTORATION) {
            return new HandParticleStyle(dust(0.42F, 1.0F, 0.48F, 0.7F), ParticleTypes.HAPPY_VILLAGER, 5);
        }
        return new HandParticleStyle(dust(0.82F, 0.82F, 0.82F, 0.55F), ParticleTypes.ENCHANT, 7);
    }

    private static boolean isLearnedOnClient(Minecraft minecraft, AbstractSpell spell) {
        if (minecraft.player == null || spell == null) {
            return false;
        }
        try {
            return IronLearnedSpellHelper.learnedSpellIds(ClientMagicData.getSyncedSpellData(minecraft.player))
                    .contains(spell.getSpellResource());
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static void cancelInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        event.setSwingHand(false);
        if (event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    private static void cancelMouse(InputEvent.MouseButton.Pre event) {
        if (event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    private static DustParticleOptions dust(float red, float green, float blue, float scale) {
        return new DustParticleOptions(new Vector3f(red, green, blue), scale);
    }

    private static void releaseSelectedSpellCast() {
        if (heldCastHand == null) {
            return;
        }
        heldCastHand = null;

        if (!ClientMagicData.isCasting()) {
            return;
        }

        AbstractSpell spell = SpellRegistry.getSpell(ClientMagicData.getCastingSpellId());
        boolean triggerCooldown = spell != null
                && spell.getCastType() == CastType.CONTINUOUS
                && spell.getSpellCooldown() > 0
                && !SkyrimMagicScaling.usesSkyrimNoCooldownRules(spell);
        try {
            Messages.sendToServer(new ServerboundCancelCast(triggerCooldown));
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to release selected spell cast", exception);
        }
    }

    private record HandParticleStyle(ParticleOptions dust, ParticleOptions accent, int accentInterval) {
    }
}
