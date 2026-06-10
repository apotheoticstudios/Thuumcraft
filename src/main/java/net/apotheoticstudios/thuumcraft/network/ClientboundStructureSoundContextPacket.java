package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientStructureSoundState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundStructureSoundContextPacket {
    private final int categoryMask;

    public ClientboundStructureSoundContextPacket(int categoryMask) {
        this.categoryMask = categoryMask;
    }

    public ClientboundStructureSoundContextPacket(FriendlyByteBuf buffer) {
        this(buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(categoryMask);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientStructureSoundState.update(categoryMask));
        context.setPacketHandled(true);
    }
}
