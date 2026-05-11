package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientSneakAwarenessState;
import net.apotheoticstudios.thuumcraft.stealth.SneakAwareness;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSneakAwarenessPacket {
    private final SneakAwareness awareness;
    private final float progress;
    private final int observerId;

    public ClientboundSneakAwarenessPacket(SneakAwareness awareness, float progress, int observerId) {
        this.awareness = awareness;
        this.progress = progress;
        this.observerId = observerId;
    }

    public ClientboundSneakAwarenessPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(SneakAwareness.class), buffer.readFloat(), buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(awareness);
        buffer.writeFloat(progress);
        buffer.writeVarInt(observerId);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientSneakAwarenessState.update(awareness, progress, observerId));
        context.setPacketHandled(true);
    }
}
