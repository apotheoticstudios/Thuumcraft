package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.stealth.SneakAwareness;
import net.minecraft.Util;

public final class ClientSneakAwarenessState {
    private static final long STALE_AFTER_MILLIS = 4000L;

    private static SneakAwareness awareness = SneakAwareness.HIDDEN;
    private static float progress;
    private static int observerId = -1;
    private static long lastUpdateMillis;

    private ClientSneakAwarenessState() {
    }

    public static void update(SneakAwareness newAwareness, float newProgress, int newObserverId) {
        awareness = newAwareness;
        progress = newProgress;
        observerId = newObserverId;
        lastUpdateMillis = Util.getMillis();
    }

    public static SneakAwareness awareness() {
        if (Util.getMillis() - lastUpdateMillis > STALE_AFTER_MILLIS) {
            return SneakAwareness.HIDDEN;
        }
        return awareness;
    }

    public static float progress() {
        SneakAwareness currentAwareness = awareness();
        return currentAwareness == SneakAwareness.HIDDEN || currentAwareness == SneakAwareness.DISABLED ? 0.0F : progress;
    }

    public static int observerId() {
        SneakAwareness currentAwareness = awareness();
        return currentAwareness == SneakAwareness.HIDDEN || currentAwareness == SneakAwareness.DISABLED ? -1 : observerId;
    }
}
