package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundRequestSkillPerksPacket {
    public ServerboundRequestSkillPerksPacket() {
    }

    public ServerboundRequestSkillPerksPacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SkillPerk.sync(player);
            }
        });
        context.setPacketHandled(true);
    }
}
