package net.apotheoticstudios.thuumcraft.client;

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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class SkyrimMagicClientEvents {
    private static InteractionHand heldCastHand;

    private SkyrimMagicClientEvents() {
    }

    @SubscribeEvent
    public static void castSelectedSpell(InputEvent.InteractionKeyMappingTriggered event) {
        InteractionHand castHand = castHandFor(event);
        if (castHand == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) {
            return;
        }

        ResourceLocation spellId = SelectedMagicSpellState.selectedSpellId(castHand);
        if (spellId == null) {
            return;
        }

        if (heldCastHand != null) {
            event.setSwingHand(false);
            event.setCanceled(true);
            return;
        }

        if (!minecraft.player.getItemInHand(castHand).isEmpty()) {
            minecraft.player.displayClientMessage(Component.translatable(castHand == InteractionHand.OFF_HAND
                    ? "message.thuumcraft.magic.off_hand_blocked"
                    : "message.thuumcraft.magic.main_hand_blocked").withStyle(ChatFormatting.RED), true);
            return;
        }

        AbstractSpell spell = SelectedMagicSpellState.selectedSpell(castHand);
        if (spell == null || spell == SpellRegistry.none() || spell.getCastType() == CastType.NONE) {
            SelectedMagicSpellState.clear(castHand);
            return;
        }

        if (!IronLearnedSpellHelper.learnedSpellIds(ClientMagicData.getSyncedSpellData(minecraft.player)).contains(spell.getSpellResource())) {
            minecraft.player.displayClientMessage(Component.translatable("message.thuumcraft.magic.not_learned", spell.getDisplayName(minecraft.player))
                    .withStyle(ChatFormatting.RED), true);
            return;
        }

        ModMessages.sendToServer(new ServerboundCastSelectedSpellPacket(spellId.toString(), castHand));
        heldCastHand = castHand;
        event.setSwingHand(false);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void releaseSelectedSpellMouse(InputEvent.MouseButton.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options == null || event.getAction() != GLFW.GLFW_RELEASE) {
            return;
        }

        if (heldCastHand == InteractionHand.MAIN_HAND && minecraft.options.keyAttack.matchesMouse(event.getButton())) {
            releaseSelectedSpellCast();
        } else if (heldCastHand == InteractionHand.OFF_HAND && minecraft.options.keyUse.matchesMouse(event.getButton())) {
            releaseSelectedSpellCast();
        }
    }

    @SubscribeEvent
    public static void releaseSelectedSpellKey(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options == null || event.getAction() != GLFW.GLFW_RELEASE) {
            return;
        }

        if (heldCastHand == InteractionHand.MAIN_HAND && minecraft.options.keyAttack.matches(event.getKey(), event.getScanCode())) {
            releaseSelectedSpellCast();
        } else if (heldCastHand == InteractionHand.OFF_HAND && minecraft.options.keyUse.matches(event.getKey(), event.getScanCode())) {
            releaseSelectedSpellCast();
        }
    }

    @SubscribeEvent
    public static void hideContinuousSkyrimCastBar(RenderGuiOverlayEvent.Pre event) {
        if (!ClientMagicData.isCasting()
                || ClientMagicData.getCastType() != CastType.CONTINUOUS
                || event.getOverlay() == null
                || event.getOverlay().id() == null) {
            return;
        }

        AbstractSpell spell = SpellRegistry.getSpell(ClientMagicData.getCastingSpellId());
        if (SkyrimMagicScaling.isThuumcraftSkyrimSpell(spell)
                && "irons_spellbooks".equals(event.getOverlay().id().getNamespace())
                && event.getOverlay().id().getPath().contains("cast")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void clearSelectedSpell(ClientPlayerNetworkEvent.LoggingOut event) {
        heldCastHand = null;
        SelectedMagicSpellState.clear();
    }

    private static InteractionHand castHandFor(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isAttack()) {
            return InteractionHand.MAIN_HAND;
        }
        if (event.isUseItem()) {
            return InteractionHand.OFF_HAND;
        }
        return null;
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
                && spell.getSpellCooldown() > 0;
        Messages.sendToServer(new ServerboundCancelCast(triggerCooldown));
    }
}
