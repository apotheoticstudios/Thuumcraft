package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientManaState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundManaPacket {
    private final float mana;
    private final float maxMana;

    public ClientboundManaPacket(float mana, float maxMana) {
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public ClientboundManaPacket(FriendlyByteBuf buffer) {
        this(buffer.readFloat(), buffer.readFloat());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(mana);
        buffer.writeFloat(maxMana);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientManaState.update(mana, maxMana));
        context.setPacketHandled(true);
    }
}
