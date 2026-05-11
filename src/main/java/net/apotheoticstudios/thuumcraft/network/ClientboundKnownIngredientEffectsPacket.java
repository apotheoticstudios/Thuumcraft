package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.item.IngredientKnowledge;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundKnownIngredientEffectsPacket {
    private final List<String> ingredientIds;

    public ClientboundKnownIngredientEffectsPacket(Collection<String> ingredientIds) {
        this.ingredientIds = List.copyOf(ingredientIds);
    }

    public ClientboundKnownIngredientEffectsPacket(FriendlyByteBuf buffer) {
        this(buffer.readList(FriendlyByteBuf::readUtf));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeCollection(ingredientIds, FriendlyByteBuf::writeUtf);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> IngredientKnowledge.setKnownClient(ingredientIds));
        context.setPacketHandled(true);
    }
}
