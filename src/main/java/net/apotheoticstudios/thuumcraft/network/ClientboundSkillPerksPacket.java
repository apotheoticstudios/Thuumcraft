package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientSkillPerkState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSkillPerksPacket {
    private final int[] ranks;
    private final int perkPoints;

    public ClientboundSkillPerksPacket(int[] ranks, int perkPoints) {
        this.ranks = ranks.clone();
        this.perkPoints = perkPoints;
    }

    public ClientboundSkillPerksPacket(FriendlyByteBuf buffer) {
        this.ranks = buffer.readVarIntArray();
        this.perkPoints = buffer.readVarInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarIntArray(ranks);
        buffer.writeVarInt(perkPoints);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientSkillPerkState.update(ranks, perkPoints));
        context.setPacketHandled(true);
    }
}
