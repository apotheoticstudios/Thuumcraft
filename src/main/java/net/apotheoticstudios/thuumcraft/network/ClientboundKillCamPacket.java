package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.KillCamClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundKillCamPacket {
    private final int targetId;
    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final float targetWidth;
    private final float targetHeight;
    private final double attackerX;
    private final double attackerY;
    private final double attackerZ;
    private final boolean ranged;
    private final int durationTicks;
    private final double fov;

    public ClientboundKillCamPacket(int targetId, double targetX, double targetY, double targetZ,
                                    float targetWidth, float targetHeight,
                                    double attackerX, double attackerY, double attackerZ,
                                    boolean ranged, int durationTicks, double fov) {
        this.targetId = targetId;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.attackerX = attackerX;
        this.attackerY = attackerY;
        this.attackerZ = attackerZ;
        this.ranged = ranged;
        this.durationTicks = durationTicks;
        this.fov = fov;
    }

    public ClientboundKillCamPacket(FriendlyByteBuf buffer) {
        this(buffer.readVarInt(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readBoolean(),
                buffer.readVarInt(),
                buffer.readDouble());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(targetId);
        buffer.writeDouble(targetX);
        buffer.writeDouble(targetY);
        buffer.writeDouble(targetZ);
        buffer.writeFloat(targetWidth);
        buffer.writeFloat(targetHeight);
        buffer.writeDouble(attackerX);
        buffer.writeDouble(attackerY);
        buffer.writeDouble(attackerZ);
        buffer.writeBoolean(ranged);
        buffer.writeVarInt(durationTicks);
        buffer.writeDouble(fov);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> KillCamClientEvents.start(targetId, targetX, targetY, targetZ,
                targetWidth, targetHeight, attackerX, attackerY, attackerZ, ranged, durationTicks, fov));
        context.setPacketHandled(true);
    }
}
