package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientStaminaState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundStaminaPacket {
    private final float stamina;

    public ClientboundStaminaPacket(float stamina) {
        this.stamina = stamina;
    }

    public ClientboundStaminaPacket(FriendlyByteBuf buffer) {
        this(buffer.readFloat());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(stamina);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientStaminaState.update(stamina));
        context.setPacketHandled(true);
    }
}
