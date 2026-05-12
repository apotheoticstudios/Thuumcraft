package net.apotheoticstudios.thuumcraft.client;

public final class ClientStaminaState {
    private static float stamina = 100.0F;

    private ClientStaminaState() {
    }

    public static float stamina() {
        return stamina;
    }

    public static void update(float value) {
        stamina = Math.max(0.0F, value);
    }
}
