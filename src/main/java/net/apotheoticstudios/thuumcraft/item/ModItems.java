package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.entity.ModEntities;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Thuumcraft.MOD_ID);

    public static final RegistryObject<Item> REFINED_MALACHITE = ITEMS.register("refined_malachite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_METAL_INGOT = ITEMS.register("dwarven_metal_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REFINED_MOONSTONE = ITEMS.register("refined_moonstone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EBONY_INGOT = ITEMS.register("ebony_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORUNDUM_INGOT = ITEMS.register("corundum_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCUM_INGOT = ITEMS.register("orichalcum_ingot",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> MALACHITE_ORE = ITEMS.register("malachite_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORUNDUM_ORE = ITEMS.register("corundum_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOONSTONE_ORE = ITEMS.register("moonstone_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCUM_ORE = ITEMS.register("orichalcum_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EBONY_ORE = ITEMS.register("ebony_ore",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LEATHER_STRIPS = ITEMS.register("leather_strips",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HANDLE = ITEMS.register("handle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SKEEVER_TAIL = ITEMS.register("skeever_tail",
            () -> new Item(new Item.Properties()));


    // Skyrim ingredient items
    public static final RegistryObject<Item> ABECEAN_LONGFIN = ITEMS.register("abecean_longfin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALOCASIA_FRUIT = ITEMS.register("alocasia_fruit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALOE_VERA_LEAVES = ITEMS.register("aloe_vera_leaves",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMBROSIA = ITEMS.register("ambrosia",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCESTOR_MOTH_WING = ITEMS.register("ancestor_moth_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANGELFISH = ITEMS.register("angelfish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANGLER_LARVAE = ITEMS.register("angler_larvae",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ASH_CREEP_CLUSTER = ITEMS.register("ash_creep_cluster",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ASH_HOPPER_JELLY = ITEMS.register("ash_hopper_jelly",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ASHEN_GRASS_POD = ITEMS.register("ashen_grass_pod",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ASTER_BLOOM_CORE = ITEMS.register("aster_bloom_core",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEAR_CLAWS = ITEMS.register("bear_claws",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEE = ITEMS.register("bee",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEEHIVE_HUSK = ITEMS.register("beehive_husk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BERITS_ASHES = ITEMS.register("berits_ashes",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BITTERGREEN_PETALS = ITEMS.register("bittergreen_petals",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLEEDING_CROWN = ITEMS.register("bleeding_crown",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLIND_WATCHERS_EYE = ITEMS.register("blind_watchers_eye",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLISS_BUG_THORAX = ITEMS.register("bliss_bug_thorax",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLISTER_POD_CAP = ITEMS.register("blister_pod_cap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLISTERWORT = ITEMS.register("blisterwort",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLOODGRASS = ITEMS.register("bloodgrass",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_BUTTERFLY_WING = ITEMS.register("blue_butterfly_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_DARTWING = ITEMS.register("blue_dartwing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_MOUNTAIN_FLOWER = ITEMS.register("blue_mountain_flower",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BOAR_TUSK = ITEMS.register("boar_tusk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BOG_BEACON = ITEMS.register("bog_beacon",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BONE_MEAL = ITEMS.register("bone_meal",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRIAR_HEART = ITEMS.register("briar_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BUNGLERS_BANE = ITEMS.register("bunglers_bane",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BURNT_SPRIGGAN_WOOD = ITEMS.register("burnt_spriggan_wood",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BUTTERFLY_WING = ITEMS.register("butterfly_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CANIS_ROOT = ITEMS.register("canis_root",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHARRED_SKEEVER_HIDE = ITEMS.register("charred_skeever_hide",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAURUS_EGGS = ITEMS.register("chaurus_eggs",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAURUS_HUNTER_ANTENNAE = ITEMS.register("chaurus_hunter_antennae",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKENS_EGG = ITEMS.register("chickens_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOKEBERRY = ITEMS.register("chokeberry",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOKEWEED = ITEMS.register("chokeweed",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CODA_FLOWER = ITEMS.register("coda_flower",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMBERRY = ITEMS.register("comberry",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CONGEALED_PUTRESCENCE = ITEMS.register("congealed_putrescence",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORKBULB_ROOT = ITEMS.register("corkbulb_root",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORRUPTED_HUMAN_HEART = ITEMS.register("corrupted_human_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CREEP_CLUSTER = ITEMS.register("creep_cluster",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRIMSON_NIRNROOT = ITEMS.register("crimson_nirnroot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CYRODILIC_SPADETAIL = ITEMS.register("cyrodilic_spadetail",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAEDRA_HEART = ITEMS.register("daedra_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAEDRA_SILK = ITEMS.register("daedra_silk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAEDRA_VENIN = ITEMS.register("daedra_venin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAEDROTH_TEETH = ITEMS.register("daedroth_teeth",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEATHBELL = ITEMS.register("deathbell",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRAGONS_TONGUE = ITEMS.register("dragons_tongue",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DREUGH_WAX = ITEMS.register("dreugh_wax",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_OIL = ITEMS.register("dwarven_oil",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELVES_EAR = ITEMS.register("elves_ear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELYTRA_ICHOR = ITEMS.register("elytra_ichor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMPEROR_PARASOL_MOSS = ITEMS.register("emperor_parasol_moss",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EYE_OF_SABRE_CAT = ITEMS.register("eye_of_sabre_cat",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FALMER_EAR = ITEMS.register("falmer_ear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FARENGARS_FROST_SALT = ITEMS.register("farengars_frost_salt",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FELSAAD_TERN_FEATHERS = ITEMS.register("felsaad_tern_feathers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FINE_CUT_VOID_SALTS = ITEMS.register("fine_cut_void_salts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_PETAL = ITEMS.register("fire_petal",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_SALTS = ITEMS.register("fire_salts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLAME_STALK = ITEMS.register("flame_stalk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLY_AMANITA = ITEMS.register("fly_amanita",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROST_MIRRIAM = ITEMS.register("frost_mirriam",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROST_SALTS = ITEMS.register("frost_salts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FUNGUS_STALK = ITEMS.register("fungus_stalk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIANT_LICHEN = ITEMS.register("giant_lichen",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIANTS_TOE = ITEMS.register("giants_toe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLASSFISH = ITEMS.register("glassfish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLEAMBLOSSOM = ITEMS.register("gleamblossom",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLOW_DUST = ITEMS.register("glow_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLOWING_MUSHROOM = ITEMS.register("glowing_mushroom",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GNARL_BARK = ITEMS.register("gnarl_bark",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_KANET = ITEMS.register("gold_kanet",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLDFISH = ITEMS.register("goldfish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRASS_POD = ITEMS.register("grass_pod",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_BUTTERFLY_WING = ITEMS.register("green_butterfly_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HACKLE_LO_LEAF = ITEMS.register("hackle_lo_leaf",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAGRAVEN_CLAW = ITEMS.register("hagraven_claw",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAGRAVEN_FEATHERS = ITEMS.register("hagraven_feathers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HANGING_MOSS = ITEMS.register("hanging_moss",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HARRADA = ITEMS.register("harrada",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAWK_BEAK = ITEMS.register("hawk_beak",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAWK_FEATHERS = ITEMS.register("hawk_feathers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAWKS_EGG = ITEMS.register("hawks_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEALING_SALTS = ITEMS.register("healing_salts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEART_OF_ORDER = ITEMS.register("heart_of_order",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HISTCARP = ITEMS.register("histcarp",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HONEYCOMB = ITEMS.register("honeycomb",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUMAN_FLESH = ITEMS.register("human_flesh",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUMAN_HEART = ITEMS.register("human_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUNGER_TONGUE = ITEMS.register("hunger_tongue",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HYDNUM_AZURE_GIANT_SPORE = ITEMS.register("hydnum_azure_giant_spore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HYPHA_FACIA = ITEMS.register("hypha_facia",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ICE_WRAITH_TEETH = ITEMS.register("ice_wraith_teeth",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMP_GALL = ITEMS.register("imp_gall",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMP_STOOL = ITEMS.register("imp_stool",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRONWOOD_FRUIT = ITEMS.register("ironwood_fruit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JARRIN_ROOT = ITEMS.register("jarrin_root",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JAZBAY_GRAPES = ITEMS.register("jazbay_grapes",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JUVENILE_MUDCRAB = ITEMS.register("juvenile_mudcrab",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KAGOUTI_HIDE = ITEMS.register("kagouti_hide",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KRESH_FIBER = ITEMS.register("kresh_fiber",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LARGE_ANTLERS = ITEMS.register("large_antlers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LAVENDER = ITEMS.register("lavender",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LICHOR = ITEMS.register("lichor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUMINOUS_RUSSULA = ITEMS.register("luminous_russula",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUNA_MOTH_WING = ITEMS.register("luna_moth_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LYRETAIL_ANTHIAS = ITEMS.register("lyretail_anthias",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MARSHMERROW = ITEMS.register("marshmerrow",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MINOTAUR_HORN = ITEMS.register("minotaur_horn",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOON_SUGAR = ITEMS.register("moon_sugar",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MORA_TAPINELLA = ITEMS.register("mora_tapinella",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MORT_FLESH = ITEMS.register("mort_flesh",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MUDCRAB_CHITIN = ITEMS.register("mudcrab_chitin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAMIRAS_ROT = ITEMS.register("namiras_rot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETCH_JELLY = ITEMS.register("netch_jelly",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NIGHTSHADE = ITEMS.register("nightshade",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NIRNROOT = ITEMS.register("nirnroot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NORDIC_BARNACLE = ITEMS.register("nordic_barnacle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OGRES_TEETH = ITEMS.register("ogres_teeth",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_DARTWING = ITEMS.register("orange_dartwing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEARLFISH = ITEMS.register("pearlfish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PINE_THRUSH_EGG = ITEMS.register("pine_thrush_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POISON_BLOOM = ITEMS.register("poison_bloom",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDERED_MAMMOTH_TUSK = ITEMS.register("powdered_mammoth_tusk",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_BUTTERFLY_WING = ITEMS.register("purple_butterfly_wing",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_MOUNTAIN_FLOWER = ITEMS.register("purple_mountain_flower",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PYGMY_SUNFISH = ITEMS.register("pygmy_sunfish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_KELP_GAS_BLADDER = ITEMS.register("red_kelp_gas_bladder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_MOUNTAIN_FLOWER = ITEMS.register("red_mountain_flower",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDWORT_FLOWER = ITEMS.register("redwort_flower",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RIVER_BETTY = ITEMS.register("river_betty",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROCK_WARBLER_EGG = ITEMS.register("rock_warbler_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROOBRUSH = ITEMS.register("roobrush",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROT_SCALE = ITEMS.register("rot_scale",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SABRE_CAT_TOOTH = ITEMS.register("sabre_cat_tooth",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SALMON_ROE = ITEMS.register("salmon_roe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SALT_PILE = ITEMS.register("salt_pile",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SALTRICE = ITEMS.register("saltrice",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCALON_FIN = ITEMS.register("scalon_fin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCALY_PHOLIOTA = ITEMS.register("scaly_pholiota",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCATHECRAW = ITEMS.register("scathecraw",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCREAMING_MAW = ITEMS.register("screaming_maw",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCRIB_JELLY = ITEMS.register("scrib_jelly",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCRIB_JERKY = ITEMS.register("scrib_jerky",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVERSIDE_PERCH = ITEMS.register("silverside_perch",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SIMON_RODAYNES_HEART = ITEMS.register("simon_rodaynes_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SLAUGHTERFISH_EGG = ITEMS.register("slaughterfish_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SLAUGHTERFISH_SCALES = ITEMS.register("slaughterfish_scales",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SLOAD_SOAP = ITEMS.register("sload_soap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_ANTLERS = ITEMS.register("small_antlers",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_PEARL = ITEMS.register("small_pearl",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SNOWBERRIES = ITEMS.register("snowberries",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPADEFISH = ITEMS.register("spadefish",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ASH = ITEMS.register("spawn_ash",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIDDAL_STICK = ITEMS.register("spiddal_stick",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIDER_EGG = ITEMS.register("spider_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPRIGGAN_SAP = ITEMS.register("spriggan_sap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_BLUE_ENTOLOMA = ITEMS.register("steel_blue_entoloma",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STONEFLOWER_PETALS = ITEMS.register("stoneflower_petals",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_FUNGAL_POD = ITEMS.register("swamp_fungal_pod",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TAPROOT = ITEMS.register("taproot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THISTLE_BRANCH = ITEMS.register("thistle_branch",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THORN_HOOK = ITEMS.register("thorn_hook",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TORCHBUG_THORAX = ITEMS.register("torchbug_thorax",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TRAMA_ROOT = ITEMS.register("trama_root",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TROLL_FAT = ITEMS.register("troll_fat",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNDRA_COTTON = ITEMS.register("tundra_cotton",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VAMPIRE_DUST = ITEMS.register("vampire_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_ESSENCE = ITEMS.register("void_essence",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_SALTS = ITEMS.register("void_salts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATCHERS_EYE = ITEMS.register("watchers_eye",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WHEAT = ITEMS.register("wheat",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WHITE_CAP = ITEMS.register("white_cap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WILD_GRASS_POD = ITEMS.register("wild_grass_pod",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WISP_STALK_CAPS = ITEMS.register("wisp_stalk_caps",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WISP_WRAPPINGS = ITEMS.register("wisp_wrappings",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WITHERING_MOON = ITEMS.register("withering_moon",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WORMS_HEAD_CAP = ITEMS.register("worms_head_cap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_MOUNTAIN_FLOWER = ITEMS.register("yellow_mountain_flower",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> JUNIPER_BERRIES = ITEMS.register("juniper_berries",
            () -> new Item(new Item.Properties().food(ModFoods.JUNIPER_BERRIES)));


    public static final List<RegistryObject<Item>> INGREDIENT_ITEMS = List.of(
            ABECEAN_LONGFIN,
            ALOCASIA_FRUIT,
            ALOE_VERA_LEAVES,
            AMBROSIA,
            ANCESTOR_MOTH_WING,
            ANGELFISH,
            ANGLER_LARVAE,
            ASH_CREEP_CLUSTER,
            ASH_HOPPER_JELLY,
            ASHEN_GRASS_POD,
            ASTER_BLOOM_CORE,
            BEAR_CLAWS,
            BEE,
            BEEHIVE_HUSK,
            BERITS_ASHES,
            BITTERGREEN_PETALS,
            BLEEDING_CROWN,
            BLIND_WATCHERS_EYE,
            BLISS_BUG_THORAX,
            BLISTER_POD_CAP,
            BLISTERWORT,
            BLOODGRASS,
            BLUE_BUTTERFLY_WING,
            BLUE_DARTWING,
            BLUE_MOUNTAIN_FLOWER,
            BOAR_TUSK,
            BOG_BEACON,
            BONE_MEAL,
            BRIAR_HEART,
            BUNGLERS_BANE,
            BURNT_SPRIGGAN_WOOD,
            BUTTERFLY_WING,
            CANIS_ROOT,
            CHARRED_SKEEVER_HIDE,
            CHAURUS_EGGS,
            CHAURUS_HUNTER_ANTENNAE,
            CHICKENS_EGG,
            CHOKEBERRY,
            CHOKEWEED,
            CODA_FLOWER,
            COMBERRY,
            CONGEALED_PUTRESCENCE,
            CORKBULB_ROOT,
            CORRUPTED_HUMAN_HEART,
            CREEP_CLUSTER,
            CRIMSON_NIRNROOT,
            CYRODILIC_SPADETAIL,
            DAEDRA_HEART,
            DAEDRA_SILK,
            DAEDRA_VENIN,
            DAEDROTH_TEETH,
            DEATHBELL,
            DRAGONS_TONGUE,
            DREUGH_WAX,
            DWARVEN_OIL,
            ECTOPLASM,
            ELVES_EAR,
            ELYTRA_ICHOR,
            EMPEROR_PARASOL_MOSS,
            EYE_OF_SABRE_CAT,
            FALMER_EAR,
            FARENGARS_FROST_SALT,
            FELSAAD_TERN_FEATHERS,
            FINE_CUT_VOID_SALTS,
            FIRE_PETAL,
            FIRE_SALTS,
            FLAME_STALK,
            FLY_AMANITA,
            FROST_MIRRIAM,
            FROST_SALTS,
            FUNGUS_STALK,
            GARLIC,
            GIANT_LICHEN,
            GIANTS_TOE,
            GLASSFISH,
            GLEAMBLOSSOM,
            GLOW_DUST,
            GLOWING_MUSHROOM,
            GNARL_BARK,
            GOLD_KANET,
            GOLDFISH,
            GRASS_POD,
            GREEN_BUTTERFLY_WING,
            HACKLE_LO_LEAF,
            HAGRAVEN_CLAW,
            HAGRAVEN_FEATHERS,
            HANGING_MOSS,
            HARRADA,
            HAWK_BEAK,
            HAWK_FEATHERS,
            HAWKS_EGG,
            HEALING_SALTS,
            HEART_OF_ORDER,
            HISTCARP,
            HONEYCOMB,
            HUMAN_FLESH,
            HUMAN_HEART,
            HUNGER_TONGUE,
            HYDNUM_AZURE_GIANT_SPORE,
            HYPHA_FACIA,
            ICE_WRAITH_TEETH,
            IMP_GALL,
            IMP_STOOL,
            IRONWOOD_FRUIT,
            JARRIN_ROOT,
            JAZBAY_GRAPES,
            JUNIPER_BERRIES,
            JUVENILE_MUDCRAB,
            KAGOUTI_HIDE,
            KRESH_FIBER,
            LARGE_ANTLERS,
            LAVENDER,
            LICHOR,
            LUMINOUS_RUSSULA,
            LUNA_MOTH_WING,
            LYRETAIL_ANTHIAS,
            MARSHMERROW,
            MINOTAUR_HORN,
            MOON_SUGAR,
            MORA_TAPINELLA,
            MORT_FLESH,
            MUDCRAB_CHITIN,
            NAMIRAS_ROT,
            NETCH_JELLY,
            NIGHTSHADE,
            NIRNROOT,
            NORDIC_BARNACLE,
            OGRES_TEETH,
            ORANGE_DARTWING,
            PEARL,
            PEARLFISH,
            PINE_THRUSH_EGG,
            POISON_BLOOM,
            POWDERED_MAMMOTH_TUSK,
            PURPLE_BUTTERFLY_WING,
            PURPLE_MOUNTAIN_FLOWER,
            PYGMY_SUNFISH,
            RED_KELP_GAS_BLADDER,
            RED_MOUNTAIN_FLOWER,
            REDWORT_FLOWER,
            RIVER_BETTY,
            ROCK_WARBLER_EGG,
            ROOBRUSH,
            ROT_SCALE,
            SABRE_CAT_TOOTH,
            SALMON_ROE,
            SALT_PILE,
            SALTRICE,
            SCALON_FIN,
            SCALY_PHOLIOTA,
            SCATHECRAW,
            SCREAMING_MAW,
            SCRIB_JELLY,
            SCRIB_JERKY,
            SILVERSIDE_PERCH,
            SIMON_RODAYNES_HEART,
            SKEEVER_TAIL,
            SLAUGHTERFISH_EGG,
            SLAUGHTERFISH_SCALES,
            SLOAD_SOAP,
            SMALL_ANTLERS,
            SMALL_PEARL,
            SNOWBERRIES,
            SPADEFISH,
            SPAWN_ASH,
            SPIDDAL_STICK,
            SPIDER_EGG,
            SPRIGGAN_SAP,
            STEEL_BLUE_ENTOLOMA,
            STONEFLOWER_PETALS,
            SWAMP_FUNGAL_POD,
            TAPROOT,
            THISTLE_BRANCH,
            THORN_HOOK,
            TORCHBUG_THORAX,
            TRAMA_ROOT,
            TROLL_FAT,
            TUNDRA_COTTON,
            VAMPIRE_DUST,
            VOID_ESSENCE,
            VOID_SALTS,
            WATCHERS_EYE,
            WHEAT,
            WHITE_CAP,
            WILD_GRASS_POD,
            WISP_STALK_CAPS,
            WISP_WRAPPINGS,
            WITHERING_MOON,
            WORMS_HEAD_CAP,
            YELLOW_MOUNTAIN_FLOWER
    );

    public static final RegistryObject<Item> GLASS_SWORD = ITEMS.register("glass_sword",
            () -> new SwordItem(ModToolTiers.GLASS, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> GLASS_WAR_AXE = ITEMS.register("glass_war_axe",
            () -> new AxeItem(ModToolTiers.GLASS, 5, -3, new Item.Properties()));

    public static final RegistryObject<Item> DWARVEN_SWORD = ITEMS.register("dwarven_sword",
            () -> new SwordItem(ModToolTiers.DWARVEN, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> DWARVEN_WAR_AXE = ITEMS.register("dwarven_war_axe",
            () -> new AxeItem(ModToolTiers.DWARVEN, 5, -3, new Item.Properties()));

    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword",
            () -> new SwordItem(ModToolTiers.STEEL, 3, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> SEPTIM = ITEMS.register("septim",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> DRAUGR_SPAWN_EGG = ITEMS.register("draugr_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DRAUGR, 0x7e9680, 0xc5d1c5, new Item.Properties()));
    public static final RegistryObject<Item> GIANT_SPAWN_EGG = ITEMS.register("giant_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GIANT, 0x805009, 0xbf9521, new Item.Properties()));
    public static final RegistryObject<Item> SKEEVER_SPAWN_EGG = ITEMS.register("skeever_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SKEEVER, 0x805009, 0xbf9521, new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
