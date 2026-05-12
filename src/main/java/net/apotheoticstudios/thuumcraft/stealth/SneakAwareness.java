package net.apotheoticstudios.thuumcraft.stealth;

public enum SneakAwareness {
    DISABLED,
    HIDDEN,
    SUSPICIOUS,
    SEARCHING,
    DETECTED;

    public static SneakAwareness fromProgress(float progress) {
        if (progress >= 0.85F) {
            return DETECTED;
        }
        if (progress >= 0.55F) {
            return SEARCHING;
        }
        if (progress >= 0.18F) {
            return SUSPICIOUS;
        }
        return HIDDEN;
    }
}
