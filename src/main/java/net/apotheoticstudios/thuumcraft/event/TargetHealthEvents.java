package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.network.ClientboundTargetHealthPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class TargetHealthEvents {
    private TargetHealthEvents() {
    }

    @SubscribeEvent
    public static void showDamagedMobHealth(LivingHurtEvent event) {
        if (!Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                || !Config.ENABLE_SKYRIM_HUD.get()
                || !Config.SHOW_TARGET_HEALTH_BAR.get()
                || event.getAmount() <= 0.0F
                || !(event.getSource().getEntity() instanceof ServerPlayer player)
                || player.isCreative()
                || player.isSpectator()
                || !(event.getEntity() instanceof Mob mob)) {
            return;
        }

        ModMessages.sendToPlayer(new ClientboundTargetHealthPacket(mob.getId()), player);
    }
}
