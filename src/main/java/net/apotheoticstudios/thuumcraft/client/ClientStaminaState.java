package net.apotheoticstudios.thuumcraft.client;

public final class ClientStaminaState {
    private static float stamina = 100.0F;
    private static float maxStamina = 100.0F;
    private static boolean initialized;

    private ClientStaminaState() {
    }

    public static float stamina() {
        return stamina;
    }

    public static float maxStamina() {
        return maxStamina;
    }

    public static boolean initialized() {
        return initialized;
    }

    public static void update(float value, float maxValue) {
        maxStamina = Math.max(1.0F, maxValue);
        stamina = Math.min(Math.max(0.0F, value), maxStamina);
        initialized = true;
    }

    public static void reset() {
        stamina = 100.0F;
        maxStamina = 100.0F;
        initialized = false;
    }
}
