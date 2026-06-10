package net.apotheoticstudios.thuumcraft.sound;

public enum StructureSoundCategory {
    DUNGEON(1 << 0),
    RUIN(1 << 1),
    SETTLEMENT(1 << 2),
    CITY(1 << 3),
    VILLAGE(1 << 4),
    FARM(1 << 5),
    FORT(1 << 6),
    MINE(1 << 7),
    CAVE(1 << 8),
    TEMPLE(1 << 9),
    TOMB(1 << 10),
    WATER(1 << 11),
    WILD(1 << 12),
    NETHER(1 << 13),
    END(1 << 14);

    private final int bit;

    StructureSoundCategory(int bit) {
        this.bit = bit;
    }

    public int bit() {
        return bit;
    }

    public static boolean has(int mask, StructureSoundCategory category) {
        return (mask & category.bit) != 0;
    }

    public static boolean hasAny(int mask, StructureSoundCategory... categories) {
        for (StructureSoundCategory category : categories) {
            if (has(mask, category)) {
                return true;
            }
        }
        return false;
    }
}
