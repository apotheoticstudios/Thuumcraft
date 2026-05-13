package net.apotheoticstudios.thuumcraft.magic;

import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ClientboundManaPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class IronManaSyncEvents {
    private static final int SYNC_INTERVAL_TICKS = 5;
    private static final float SYNC_EPSILON = 0.01F;

    private static final Map<UUID, PlayerManaRuntime> RUNTIME = new HashMap<>();

    private IronManaSyncEvents() {
    }

    @SubscribeEvent
    public static void syncManaOnTick(TickEvent.PlayerTickEvent event) {
        if (!shouldSyncMana()
                || event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % SYNC_INTERVAL_TICKS != 0) {
            return;
        }

        syncManaIfNeeded(player, getCurrentMana(player), getMaxMana(player));
    }

    @SubscribeEvent
    public static void syncManaOnChange(ChangeManaEvent event) {
        if (!shouldSyncMana() || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        syncManaNow(player, event.getNewMana(), getMaxMana(player));
    }

    @SubscribeEvent
    public static void syncManaOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (shouldSyncMana() && event.getEntity() instanceof ServerPlayer player) {
            syncManaNow(player, getCurrentMana(player), getMaxMana(player));
        }
    }

    @SubscribeEvent
    public static void syncManaOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (shouldSyncMana() && event.getEntity() instanceof ServerPlayer player) {
            syncManaNow(player, getCurrentMana(player), getMaxMana(player));
        }
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        RUNTIME.remove(event.getEntity().getUUID());
    }

    private static boolean shouldSyncMana() {
        return Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                && Config.ENABLE_SKYRIM_HUD.get()
                && Config.SHOW_MANA_BAR.get();
    }

    private static float getCurrentMana(ServerPlayer player) {
        return MagicData.getPlayerMagicData(player).getMana();
    }

    private static float getMaxMana(ServerPlayer player) {
        return Math.max(1.0F, (float) player.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
    }

    private static void syncManaIfNeeded(ServerPlayer player, float mana, float maxMana) {
        PlayerManaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerManaRuntime());
        runtime.syncTicks++;
        if (runtime.syncTicks < SYNC_INTERVAL_TICKS
                && Math.abs(runtime.lastSyncedMana - mana) < SYNC_EPSILON
                && Math.abs(runtime.lastSyncedMaxMana - maxMana) < SYNC_EPSILON) {
            return;
        }

        syncManaNow(player, mana, maxMana);
    }

    private static void syncManaNow(ServerPlayer player, float mana, float maxMana) {
        PlayerManaRuntime runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new PlayerManaRuntime());
        float clampedMaxMana = Math.max(1.0F, maxMana);
        float clampedMana = Mth.clamp(mana, 0.0F, clampedMaxMana);
        runtime.syncTicks = 0;
        runtime.lastSyncedMana = clampedMana;
        runtime.lastSyncedMaxMana = clampedMaxMana;
        ModMessages.sendToPlayer(new ClientboundManaPacket(clampedMana, clampedMaxMana), player);
    }

    private static final class PlayerManaRuntime {
        private int syncTicks = SYNC_INTERVAL_TICKS;
        private float lastSyncedMana = -1.0F;
        private float lastSyncedMaxMana = -1.0F;
    }
}
