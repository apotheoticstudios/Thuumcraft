package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    private static int packetId = 0;
    private static SimpleChannel channel;

    public static void register() {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(Thuumcraft.MOD_ID, "messages"),
                () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

        channel.messageBuilder(ClientboundKnownIngredientEffectsPacket.class, nextPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundKnownIngredientEffectsPacket::encode)
                .decoder(ClientboundKnownIngredientEffectsPacket::new)
                .consumerMainThread(ClientboundKnownIngredientEffectsPacket::handle)
                .add();
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    private static int nextPacketId() {
        return packetId++;
    }
}
