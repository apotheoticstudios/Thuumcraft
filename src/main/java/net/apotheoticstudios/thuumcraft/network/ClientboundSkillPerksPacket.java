package net.apotheoticstudios.thuumcraft.network;

import net.apotheoticstudios.thuumcraft.client.ClientSkillPerkState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSkillPerksPacket {
    private final int[] ranks;
    private final int perkPoints;
    private final int playerLevel;
    private final boolean meleeSkillTreesEnabled;

    public ClientboundSkillPerksPacket(int[] ranks, int perkPoints, int playerLevel, boolean meleeSkillTreesEnabled) {
        this.ranks = ranks.clone();
        this.perkPoints = perkPoints;
        this.playerLevel = playerLevel;
        this.meleeSkillTreesEnabled = meleeSkillTreesEnabled;
    }

    public ClientboundSkillPerksPacket(FriendlyByteBuf buffer) {
        this.ranks = buffer.readVarIntArray();
        this.perkPoints = buffer.readVarInt();
        this.playerLevel = buffer.readVarInt();
        this.meleeSkillTreesEnabled = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarIntArray(ranks);
        buffer.writeVarInt(perkPoints);
        buffer.writeVarInt(playerLevel);
        buffer.writeBoolean(meleeSkillTreesEnabled);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientSkillPerkState.update(ranks, perkPoints, playerLevel,
                meleeSkillTreesEnabled));
        context.setPacketHandled(true);
    }
}
