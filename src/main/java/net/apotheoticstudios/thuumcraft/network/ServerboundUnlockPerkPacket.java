package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundUnlockPerkPacket {
    private final String perkId;

    public ServerboundUnlockPerkPacket(String perkId) {
        this.perkId = perkId;
    }

    public ServerboundUnlockPerkPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(perkId);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SkillPerk.unlock(player, perkId);
            }
        });
        context.setPacketHandled(true);
    }
}
