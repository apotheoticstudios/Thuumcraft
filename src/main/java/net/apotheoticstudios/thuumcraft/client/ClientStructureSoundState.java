package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.sound.StructureSoundCategory;

public final class ClientStructureSoundState {
    private static int categoryMask;

    private ClientStructureSoundState() {
    }

    public static void update(int mask) {
        categoryMask = mask;
    }

    public static void clear() {
        categoryMask = 0;
    }

    public static int categoryMask() {
        return categoryMask;
    }

    public static boolean has(StructureSoundCategory category) {
        return StructureSoundCategory.has(categoryMask, category);
    }

    public static boolean hasAny(StructureSoundCategory... categories) {
        return StructureSoundCategory.hasAny(categoryMask, categories);
    }
}
