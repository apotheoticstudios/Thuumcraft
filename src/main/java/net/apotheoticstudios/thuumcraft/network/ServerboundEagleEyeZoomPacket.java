package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.skill.SkillPerkEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundEagleEyeZoomPacket {
    private final boolean zooming;

    public ServerboundEagleEyeZoomPacket(boolean zooming) {
        this.zooming = zooming;
    }

    public ServerboundEagleEyeZoomPacket(FriendlyByteBuf buffer) {
        this(buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(zooming);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            if (player != null) {
                SkillPerkEvents.setEagleEyeZooming(player, zooming);
            }
        });
        context.setPacketHandled(true);
    }
}
