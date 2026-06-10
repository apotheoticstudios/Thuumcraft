package net.apotheoticstudios.thuumcraft.sound;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Locale;

public final class StructureSoundClassifier {
    public static final TagKey<Structure> DUNGEON = tag("dungeon");
    public static final TagKey<Structure> RUIN = tag("ruin");
    public static final TagKey<Structure> SETTLEMENT = tag("settlement");
    public static final TagKey<Structure> CITY = tag("city");
    public static final TagKey<Structure> VILLAGE = tag("village");
    public static final TagKey<Structure> FARM = tag("farm");
    public static final TagKey<Structure> FORT = tag("fort");
    public static final TagKey<Structure> MINE = tag("mine");
    public static final TagKey<Structure> CAVE = tag("cave");
    public static final TagKey<Structure> TEMPLE = tag("temple");
    public static final TagKey<Structure> TOMB = tag("tomb");
    public static final TagKey<Structure> WATER = tag("water");
    public static final TagKey<Structure> WILD = tag("wild");
    public static final TagKey<Structure> NETHER = tag("nether");
    public static final TagKey<Structure> END = tag("end");

    private StructureSoundClassifier() {
    }

    public static int classify(ResourceLocation structureId, Holder<Structure> holder) {
        String searchable = normalize(structureId.getNamespace() + " " + structureId.getPath());
        boolean ancientCity = hasAny(searchable, "ancient city");
        boolean endCity = hasAny(searchable, "end city", "endcity");
        int mask = 0;

        if (is(holder, DUNGEON) || ancientCity || hasAny(searchable,
                "dungeon", "dungeons", "stronghold", "labyrinth", "maze", "lair", "den", "hideout", "prison",
                "jail", "gaol", "sewer", "vault", "crypt", "catacomb", "catacombs", "bunker", "bastion",
                "fortress", "chamber", "trial", "arena", "roguelike")) {
            mask |= StructureSoundCategory.DUNGEON.bit();
        }
        if (is(holder, RUIN) || ancientCity || endCity || hasAny(searchable,
                "ruin", "ruins", "ruined", "ancient", "relic", "relics", "archaeology", "archeology", "trail",
                "monument", "pyramid", "portal", "abandoned", "lost", "forgotten", "decayed", "overgrown")) {
            mask |= StructureSoundCategory.RUIN.bit();
        }
        if (is(holder, CITY) || (!ancientCity && !endCity && hasAny(searchable,
                "city", "cities", "metropolis", "capital", "district", "downtown", "market", "plaza", "palace"))) {
            mask |= StructureSoundCategory.CITY.bit() | StructureSoundCategory.SETTLEMENT.bit();
        }
        if (is(holder, VILLAGE) || hasAny(searchable,
                "village", "villages", "town", "towns", "hamlet", "settlement", "colony", "colonies", "outpost",
                "camp", "campsite", "encampment", "tribe", "clan", "guild", "inn", "tavern")) {
            mask |= StructureSoundCategory.VILLAGE.bit() | StructureSoundCategory.SETTLEMENT.bit();
        }
        if (is(holder, FARM) || hasAny(searchable,
                "farm", "farms", "farmstead", "barn", "stable", "stables", "ranch", "pasture", "field", "fields",
                "windmill", "mill", "orchard", "greenhouse", "granary", "crop", "crops")) {
            mask |= StructureSoundCategory.FARM.bit() | StructureSoundCategory.SETTLEMENT.bit();
        }
        if (is(holder, SETTLEMENT) || hasAny(searchable,
                "house", "houses", "home", "homes", "cottage", "cabin", "hut", "mansion", "manor", "hall",
                "lodge", "shack", "shacks", "camp", "base", "basecamp", "hideaway", "sanctuary", "guild", "shop")) {
            mask |= StructureSoundCategory.SETTLEMENT.bit();
        }
        if (is(holder, FORT) || hasAny(searchable,
                "fort", "forts", "fortress", "castle", "keep", "citadel", "tower", "watchtower", "bastion",
                "rampart", "bunker", "garrison", "battlement", "stronghold", "outpost")) {
            mask |= StructureSoundCategory.FORT.bit() | StructureSoundCategory.DUNGEON.bit();
        }
        if (is(holder, MINE) || hasAny(searchable,
                "mine", "mines", "mineshaft", "shaft", "quarry", "excavation", "digsite", "dig", "ore", "geode")) {
            mask |= StructureSoundCategory.MINE.bit() | StructureSoundCategory.CAVE.bit() | StructureSoundCategory.DUNGEON.bit();
        }
        if (is(holder, CAVE) || hasAny(searchable,
                "cave", "caves", "cavern", "caverns", "grotto", "underground", "subterranean", "hollow",
                "chasm", "abyss", "deep", "depths")) {
            mask |= StructureSoundCategory.CAVE.bit() | StructureSoundCategory.DUNGEON.bit();
        }
        if (is(holder, TEMPLE) || hasAny(searchable,
                "temple", "shrine", "sanctum", "sanctuary", "chapel", "church", "cathedral", "altar", "obelisk",
                "pyramid", "monastery", "pagoda")) {
            mask |= StructureSoundCategory.TEMPLE.bit() | StructureSoundCategory.RUIN.bit();
        }
        if (is(holder, TOMB) || hasAny(searchable,
                "tomb", "tombs", "grave", "graves", "graveyard", "burial", "barrow", "mausoleum", "necropolis",
                "crypt", "sepulcher", "sepulchre", "catacomb", "catacombs")) {
            mask |= StructureSoundCategory.TOMB.bit() | StructureSoundCategory.DUNGEON.bit() | StructureSoundCategory.RUIN.bit();
        }
        if (is(holder, WATER) || hasAny(searchable,
                "water", "ocean", "sea", "ship", "shipwreck", "wreck", "boat", "dock", "docks", "harbor", "harbour",
                "pier", "port", "lighthouse", "underwater", "river", "lake", "monument", "aquatic")) {
            mask |= StructureSoundCategory.WATER.bit();
        }
        if (is(holder, NETHER) || hasAny(searchable,
                "nether", "hell", "crimson", "warped", "bastion", "fortress")) {
            mask |= StructureSoundCategory.NETHER.bit() | StructureSoundCategory.DUNGEON.bit();
        }
        if (is(holder, END) || hasAny(searchable,
                "end_city", "endcity", "ender", "shulker", "chorus")) {
            mask |= StructureSoundCategory.END.bit() | StructureSoundCategory.RUIN.bit();
        }
        if (is(holder, WILD) || hasAny(searchable,
                "witch", "swamp", "igloo", "camp", "campsite", "treehouse", "grove", "hollow", "hut", "cabin",
                "shack", "shacks", "meteor", "boulder", "rock", "fossil", "nest", "burrow")) {
            mask |= StructureSoundCategory.WILD.bit();
        }

        if (mask == 0) {
            mask = StructureSoundCategory.WILD.bit();
        }
        return mask;
    }

    private static boolean is(Holder<Structure> holder, TagKey<Structure> tag) {
        return holder != null && holder.is(tag);
    }

    private static boolean hasAny(String searchable, String... needles) {
        for (String needle : needles) {
            if (searchable.contains(normalize(needle))) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }

    private static TagKey<Structure> tag(String path) {
        return TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Thuumcraft.MOD_ID, "dynamic_sound/" + path));
    }
}
