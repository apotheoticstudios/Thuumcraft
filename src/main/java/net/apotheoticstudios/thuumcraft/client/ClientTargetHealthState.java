package net.apotheoticstudios.thuumcraft.client;

import net.minecraft.client.Minecraft;

public final class ClientTargetHealthState {
    private static final int TARGET_HEALTH_VISIBLE_TICKS = 100;
    private static int entityId = -1;
    private static long visibleUntil;

    private ClientTargetHealthState() {
    }

    public static int entityId() {
        return entityId;
    }

    public static boolean shouldShow(long gameTime) {
        return entityId != -1 && gameTime <= visibleUntil;
    }

    public static void show(int id) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            reset();
            return;
        }

        entityId = id;
        visibleUntil = minecraft.level.getGameTime() + TARGET_HEALTH_VISIBLE_TICKS;
    }

    public static void reset() {
        entityId = -1;
        visibleUntil = 0L;
    }
}
