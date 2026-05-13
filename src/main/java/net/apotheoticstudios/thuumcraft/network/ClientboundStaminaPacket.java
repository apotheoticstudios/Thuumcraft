package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientStaminaState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundStaminaPacket {
    private final float stamina;
    private final float maxStamina;

    public ClientboundStaminaPacket(float stamina, float maxStamina) {
        this.stamina = stamina;
        this.maxStamina = maxStamina;
    }

    public ClientboundStaminaPacket(FriendlyByteBuf buffer) {
        this(buffer.readFloat(), buffer.readFloat());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(stamina);
        buffer.writeFloat(maxStamina);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientStaminaState.update(stamina, maxStamina));
        context.setPacketHandled(true);
    }
}
