package net.apotheoticstudios.thuumcraft.skill;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class RestedSkillBonusEvents {
    private static final String RESTED_UNTIL_TAG = Thuumcraft.MOD_ID + ".rested_skill_xp_until";

    private RestedSkillBonusEvents() {
    }

    @SubscribeEvent
    public static void grantRestedBonus(PlayerWakeUpEvent event) {
        if (!isEnabled() || !(event.getEntity() instanceof ServerPlayer player) || player.isSpectator()) {
            return;
        }

        int durationTicks = Config.RESTED_SKILL_XP_DURATION_TICKS.get();
        if (durationTicks <= 0) {
            clearRestedBonus(player);
            return;
        }

        player.getPersistentData().putLong(RESTED_UNTIL_TAG, player.level().getGameTime() + durationTicks);
        player.displayClientMessage(Component.literal("Well Rested: skills improve faster"), true);
    }

    @SubscribeEvent
    public static void copyRestedBonus(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CompoundTag oldData = event.getOriginal().getPersistentData();
        if (oldData.contains(RESTED_UNTIL_TAG)) {
            player.getPersistentData().putLong(RESTED_UNTIL_TAG, oldData.getLong(RESTED_UNTIL_TAG));
        }
    }

    public static double getSkillExperienceMultiplier(ServerPlayer player) {
        if (!isEnabled() || player == null) {
            return 1.0D;
        }

        CompoundTag data = player.getPersistentData();
        long restedUntil = data.getLong(RESTED_UNTIL_TAG);
        if (restedUntil <= player.level().getGameTime()) {
            data.remove(RESTED_UNTIL_TAG);
            return 1.0D;
        }

        return Math.max(1.0D, Config.RESTED_SKILL_XP_MULTIPLIER.get());
    }

    private static boolean isEnabled() {
        return Config.ENABLE_SKILL_SYSTEM.get() && Config.ENABLE_RESTED_SKILL_XP_BONUS.get();
    }

    private static void clearRestedBonus(ServerPlayer player) {
        player.getPersistentData().remove(RESTED_UNTIL_TAG);
    }
}
