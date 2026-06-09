package net.apotheoticstudios.thuumcraft.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import net.apotheoticstudios.thuumcraft.magic.IronLearnedSpellHelper;
import net.apotheoticstudios.thuumcraft.magic.SkyrimMagicScaling;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundCastSelectedSpellPacket {
    private final String spellId;
    private final InteractionHand hand;

    public ServerboundCastSelectedSpellPacket(String spellId, InteractionHand hand) {
        this.spellId = spellId;
        this.hand = hand;
    }

    public ServerboundCastSelectedSpellPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(128), buffer.readEnum(InteractionHand.class));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(spellId, 128);
        buffer.writeEnum(hand);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            AbstractSpell spell = SpellRegistry.getSpell(spellId);
            if (spell == null || spell == SpellRegistry.none() || !spell.isEnabled() || spell.getCastType() == CastType.NONE) {
                return;
            }

            if (!player.getItemInHand(hand).isEmpty()) {
                player.displayClientMessage(Component.translatable(hand == InteractionHand.OFF_HAND
                        ? "message.thuumcraft.magic.off_hand_blocked"
                        : "message.thuumcraft.magic.main_hand_blocked").withStyle(ChatFormatting.RED), true);
                return;
            }

            MagicData magicData = MagicData.getPlayerMagicData(player);
            if (!IronLearnedSpellHelper.hasLearned(magicData, spell)) {
                player.displayClientMessage(Component.translatable("message.thuumcraft.magic.not_learned", spell.getDisplayName(player))
                        .withStyle(ChatFormatting.RED), true);
                return;
            }

            if (magicData.isCasting() && !magicData.getCastingSpellId().equals(spell.getSpellId())) {
                ServerboundCancelCast.cancelCast(player, magicData.getCastType() != CastType.LONG);
            }

            int level = spell.getLevelFor(1, player);
            spell.attemptInitiateCast(ItemStack.EMPTY, level, player.level(), player, CastSource.SPELLBOOK, true,
                    hand == InteractionHand.OFF_HAND
                            ? SkyrimMagicScaling.OFF_HAND_EQUIPMENT_SLOT
                            : SkyrimMagicScaling.MAIN_HAND_EQUIPMENT_SLOT);
        });
        context.setPacketHandled(true);
    }
}
