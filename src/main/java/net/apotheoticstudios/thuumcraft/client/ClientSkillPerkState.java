package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.skill.SkillPerk;

import java.util.Arrays;

public final class ClientSkillPerkState {
    private static final int[] RANKS = new int[SkillPerk.values().length];
    private static int perkPoints;

    private ClientSkillPerkState() {
    }

    public static int rank(SkillPerk perk) {
        return perk == null ? 0 : RANKS[perk.ordinal()];
    }

    public static int perkPoints() {
        return perkPoints;
    }

    public static void update(int[] ranks, int points) {
        Arrays.fill(RANKS, 0);
        System.arraycopy(ranks, 0, RANKS, 0, Math.min(ranks.length, RANKS.length));
        perkPoints = Math.max(0, points);
    }

    public static void reset() {
        Arrays.fill(RANKS, 0);
        perkPoints = 0;
    }
}
