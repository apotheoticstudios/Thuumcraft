package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientTargetHealthState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundTargetHealthPacket {
    private final int entityId;

    public ClientboundTargetHealthPacket(int entityId) {
        this.entityId = entityId;
    }

    public ClientboundTargetHealthPacket(FriendlyByteBuf buffer) {
        this(buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientTargetHealthState.show(entityId));
        context.setPacketHandled(true);
    }
}
