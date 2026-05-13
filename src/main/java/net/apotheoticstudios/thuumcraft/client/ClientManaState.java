package net.apotheoticstudios.thuumcraft.client;

public final class ClientManaState {
    private static float mana = 100.0F;
    private static float maxMana = 100.0F;
    private static boolean initialized;

    private ClientManaState() {
    }

    public static float mana() {
        return mana;
    }

    public static float maxMana() {
        return maxMana;
    }

    public static boolean initialized() {
        return initialized;
    }

    public static void update(float value, float maxValue) {
        maxMana = Math.max(1.0F, maxValue);
        mana = Math.min(Math.max(0.0F, value), maxMana);
        initialized = true;
    }

    public static void reset() {
        mana = 100.0F;
        maxMana = 100.0F;
        initialized = false;
    }
}
