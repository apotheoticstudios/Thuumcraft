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

    private static RegistryObject<Item> registerIngredient(String name) {
        return ITEMS.register(name, () -> new IngredientItem(name, new Item.Properties().food(ModFoods.INGREDIENT)));
    }

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
    public static final RegistryObject<Item> SKEEVER_TAIL = registerIngredient("skeever_tail");


    // Skyrim ingredient items
    public static final RegistryObject<Item> ABECEAN_LONGFIN = registerIngredient("abecean_longfin");
    public static final RegistryObject<Item> ALOCASIA_FRUIT = registerIngredient("alocasia_fruit");
    public static final RegistryObject<Item> ALOE_VERA_LEAVES = registerIngredient("aloe_vera_leaves");
    public static final RegistryObject<Item> AMBROSIA = registerIngredient("ambrosia");
    public static final RegistryObject<Item> ANCESTOR_MOTH_WING = registerIngredient("ancestor_moth_wing");
    public static final RegistryObject<Item> ANGELFISH = registerIngredient("angelfish");
    public static final RegistryObject<Item> ANGLER_LARVAE = registerIngredient("angler_larvae");
    public static final RegistryObject<Item> ASH_CREEP_CLUSTER = registerIngredient("ash_creep_cluster");
    public static final RegistryObject<Item> ASH_HOPPER_JELLY = registerIngredient("ash_hopper_jelly");
    public static final RegistryObject<Item> ASHEN_GRASS_POD = registerIngredient("ashen_grass_pod");
    public static final RegistryObject<Item> ASTER_BLOOM_CORE = registerIngredient("aster_bloom_core");
    public static final RegistryObject<Item> BEAR_CLAWS = registerIngredient("bear_claws");
    public static final RegistryObject<Item> BEE = registerIngredient("bee");
    public static final RegistryObject<Item> BEEHIVE_HUSK = registerIngredient("beehive_husk");
    public static final RegistryObject<Item> BERITS_ASHES = registerIngredient("berits_ashes");
    public static final RegistryObject<Item> BITTERGREEN_PETALS = registerIngredient("bittergreen_petals");
    public static final RegistryObject<Item> BLEEDING_CROWN = registerIngredient("bleeding_crown");
    public static final RegistryObject<Item> BLIND_WATCHERS_EYE = registerIngredient("blind_watchers_eye");
    public static final RegistryObject<Item> BLISS_BUG_THORAX = registerIngredient("bliss_bug_thorax");
    public static final RegistryObject<Item> BLISTER_POD_CAP = registerIngredient("blister_pod_cap");
    public static final RegistryObject<Item> BLISTERWORT = registerIngredient("blisterwort");
    public static final RegistryObject<Item> BLOODGRASS = registerIngredient("bloodgrass");
    public static final RegistryObject<Item> BLUE_BUTTERFLY_WING = registerIngredient("blue_butterfly_wing");
    public static final RegistryObject<Item> BLUE_DARTWING = registerIngredient("blue_dartwing");
    public static final RegistryObject<Item> BLUE_MOUNTAIN_FLOWER = registerIngredient("blue_mountain_flower");
    public static final RegistryObject<Item> BOAR_TUSK = registerIngredient("boar_tusk");
    public static final RegistryObject<Item> BOG_BEACON = registerIngredient("bog_beacon");
    public static final RegistryObject<Item> BONE_MEAL = registerIngredient("bone_meal");
    public static final RegistryObject<Item> BRIAR_HEART = registerIngredient("briar_heart");
    public static final RegistryObject<Item> BUNGLERS_BANE = registerIngredient("bunglers_bane");
    public static final RegistryObject<Item> BURNT_SPRIGGAN_WOOD = registerIngredient("burnt_spriggan_wood");
    public static final RegistryObject<Item> BUTTERFLY_WING = registerIngredient("butterfly_wing");
    public static final RegistryObject<Item> CANIS_ROOT = registerIngredient("canis_root");
    public static final RegistryObject<Item> CHARRED_SKEEVER_HIDE = registerIngredient("charred_skeever_hide");
    public static final RegistryObject<Item> CHAURUS_EGGS = registerIngredient("chaurus_eggs");
    public static final RegistryObject<Item> CHAURUS_HUNTER_ANTENNAE = registerIngredient("chaurus_hunter_antennae");
    public static final RegistryObject<Item> CHICKENS_EGG = registerIngredient("chickens_egg");
    public static final RegistryObject<Item> CHOKEBERRY = registerIngredient("chokeberry");
    public static final RegistryObject<Item> CHOKEWEED = registerIngredient("chokeweed");
    public static final RegistryObject<Item> CODA_FLOWER = registerIngredient("coda_flower");
    public static final RegistryObject<Item> COMBERRY = registerIngredient("comberry");
    public static final RegistryObject<Item> CONGEALED_PUTRESCENCE = registerIngredient("congealed_putrescence");
    public static final RegistryObject<Item> CORKBULB_ROOT = registerIngredient("corkbulb_root");
    public static final RegistryObject<Item> CORRUPTED_HUMAN_HEART = registerIngredient("corrupted_human_heart");
    public static final RegistryObject<Item> CREEP_CLUSTER = registerIngredient("creep_cluster");
    public static final RegistryObject<Item> CRIMSON_NIRNROOT = registerIngredient("crimson_nirnroot");
    public static final RegistryObject<Item> CYRODILIC_SPADETAIL = registerIngredient("cyrodilic_spadetail");
    public static final RegistryObject<Item> DAEDRA_HEART = registerIngredient("daedra_heart");
    public static final RegistryObject<Item> DAEDRA_SILK = registerIngredient("daedra_silk");
    public static final RegistryObject<Item> DAEDRA_VENIN = registerIngredient("daedra_venin");
    public static final RegistryObject<Item> DAEDROTH_TEETH = registerIngredient("daedroth_teeth");
    public static final RegistryObject<Item> DEATHBELL = registerIngredient("deathbell");
    public static final RegistryObject<Item> DRAGONS_TONGUE = registerIngredient("dragons_tongue");
    public static final RegistryObject<Item> DREUGH_WAX = registerIngredient("dreugh_wax");
    public static final RegistryObject<Item> DWARVEN_OIL = registerIngredient("dwarven_oil");
    public static final RegistryObject<Item> ECTOPLASM = registerIngredient("ectoplasm");
    public static final RegistryObject<Item> ELVES_EAR = registerIngredient("elves_ear");
    public static final RegistryObject<Item> ELYTRA_ICHOR = registerIngredient("elytra_ichor");
    public static final RegistryObject<Item> EMPEROR_PARASOL_MOSS = registerIngredient("emperor_parasol_moss");
    public static final RegistryObject<Item> EYE_OF_SABRE_CAT = registerIngredient("eye_of_sabre_cat");
    public static final RegistryObject<Item> FALMER_EAR = registerIngredient("falmer_ear");
    public static final RegistryObject<Item> FARENGARS_FROST_SALT = registerIngredient("farengars_frost_salt");
    public static final RegistryObject<Item> FELSAAD_TERN_FEATHERS = registerIngredient("felsaad_tern_feathers");
    public static final RegistryObject<Item> FINE_CUT_VOID_SALTS = registerIngredient("fine_cut_void_salts");
    public static final RegistryObject<Item> FIRE_PETAL = registerIngredient("fire_petal");
    public static final RegistryObject<Item> FIRE_SALTS = registerIngredient("fire_salts");
    public static final RegistryObject<Item> FLAME_STALK = registerIngredient("flame_stalk");
    public static final RegistryObject<Item> FLY_AMANITA = registerIngredient("fly_amanita");
    public static final RegistryObject<Item> FROST_MIRRIAM = registerIngredient("frost_mirriam");
    public static final RegistryObject<Item> FROST_SALTS = registerIngredient("frost_salts");
    public static final RegistryObject<Item> FUNGUS_STALK = registerIngredient("fungus_stalk");
    public static final RegistryObject<Item> GARLIC = registerIngredient("garlic");
    public static final RegistryObject<Item> GIANT_LICHEN = registerIngredient("giant_lichen");
    public static final RegistryObject<Item> GIANTS_TOE = registerIngredient("giants_toe");
    public static final RegistryObject<Item> GLASSFISH = registerIngredient("glassfish");
    public static final RegistryObject<Item> GLEAMBLOSSOM = registerIngredient("gleamblossom");
    public static final RegistryObject<Item> GLOW_DUST = registerIngredient("glow_dust");
    public static final RegistryObject<Item> GLOWING_MUSHROOM = registerIngredient("glowing_mushroom");
    public static final RegistryObject<Item> GNARL_BARK = registerIngredient("gnarl_bark");
    public static final RegistryObject<Item> GOLD_KANET = registerIngredient("gold_kanet");
    public static final RegistryObject<Item> GOLDFISH = registerIngredient("goldfish");
    public static final RegistryObject<Item> GRASS_POD = registerIngredient("grass_pod");
    public static final RegistryObject<Item> GREEN_BUTTERFLY_WING = registerIngredient("green_butterfly_wing");
    public static final RegistryObject<Item> HACKLE_LO_LEAF = registerIngredient("hackle_lo_leaf");
    public static final RegistryObject<Item> HAGRAVEN_CLAW = registerIngredient("hagraven_claw");
    public static final RegistryObject<Item> HAGRAVEN_FEATHERS = registerIngredient("hagraven_feathers");
    public static final RegistryObject<Item> HANGING_MOSS = registerIngredient("hanging_moss");
    public static final RegistryObject<Item> HARRADA = registerIngredient("harrada");
    public static final RegistryObject<Item> HAWK_BEAK = registerIngredient("hawk_beak");
    public static final RegistryObject<Item> HAWK_FEATHERS = registerIngredient("hawk_feathers");
    public static final RegistryObject<Item> HAWKS_EGG = registerIngredient("hawks_egg");
    public static final RegistryObject<Item> HEALING_SALTS = registerIngredient("healing_salts");
    public static final RegistryObject<Item> HEART_OF_ORDER = registerIngredient("heart_of_order");
    public static final RegistryObject<Item> HISTCARP = registerIngredient("histcarp");
    public static final RegistryObject<Item> HONEYCOMB = registerIngredient("honeycomb");
    public static final RegistryObject<Item> HUMAN_FLESH = registerIngredient("human_flesh");
    public static final RegistryObject<Item> HUMAN_HEART = registerIngredient("human_heart");
    public static final RegistryObject<Item> HUNGER_TONGUE = registerIngredient("hunger_tongue");
    public static final RegistryObject<Item> HYDNUM_AZURE_GIANT_SPORE = registerIngredient("hydnum_azure_giant_spore");
    public static final RegistryObject<Item> HYPHA_FACIA = registerIngredient("hypha_facia");
    public static final RegistryObject<Item> ICE_WRAITH_TEETH = registerIngredient("ice_wraith_teeth");
    public static final RegistryObject<Item> IMP_GALL = registerIngredient("imp_gall");
    public static final RegistryObject<Item> IMP_STOOL = registerIngredient("imp_stool");
    public static final RegistryObject<Item> IRONWOOD_FRUIT = registerIngredient("ironwood_fruit");
    public static final RegistryObject<Item> JARRIN_ROOT = registerIngredient("jarrin_root");
    public static final RegistryObject<Item> JAZBAY_GRAPES = registerIngredient("jazbay_grapes");
    public static final RegistryObject<Item> JUVENILE_MUDCRAB = registerIngredient("juvenile_mudcrab");
    public static final RegistryObject<Item> KAGOUTI_HIDE = registerIngredient("kagouti_hide");
    public static final RegistryObject<Item> KRESH_FIBER = registerIngredient("kresh_fiber");
    public static final RegistryObject<Item> LARGE_ANTLERS = registerIngredient("large_antlers");
    public static final RegistryObject<Item> LAVENDER = registerIngredient("lavender");
    public static final RegistryObject<Item> LICHOR = registerIngredient("lichor");
    public static final RegistryObject<Item> LUMINOUS_RUSSULA = registerIngredient("luminous_russula");
    public static final RegistryObject<Item> LUNA_MOTH_WING = registerIngredient("luna_moth_wing");
    public static final RegistryObject<Item> LYRETAIL_ANTHIAS = registerIngredient("lyretail_anthias");
    public static final RegistryObject<Item> MARSHMERROW = registerIngredient("marshmerrow");
    public static final RegistryObject<Item> MINOTAUR_HORN = registerIngredient("minotaur_horn");
    public static final RegistryObject<Item> MOON_SUGAR = registerIngredient("moon_sugar");
    public static final RegistryObject<Item> MORA_TAPINELLA = registerIngredient("mora_tapinella");
    public static final RegistryObject<Item> MORT_FLESH = registerIngredient("mort_flesh");
    public static final RegistryObject<Item> MUDCRAB_CHITIN = registerIngredient("mudcrab_chitin");
    public static final RegistryObject<Item> NAMIRAS_ROT = registerIngredient("namiras_rot");
    public static final RegistryObject<Item> NETCH_JELLY = registerIngredient("netch_jelly");
    public static final RegistryObject<Item> NIGHTSHADE = registerIngredient("nightshade");
    public static final RegistryObject<Item> NIRNROOT = registerIngredient("nirnroot");
    public static final RegistryObject<Item> NORDIC_BARNACLE = registerIngredient("nordic_barnacle");
    public static final RegistryObject<Item> OGRES_TEETH = registerIngredient("ogres_teeth");
    public static final RegistryObject<Item> ORANGE_DARTWING = registerIngredient("orange_dartwing");
    public static final RegistryObject<Item> PEARL = registerIngredient("pearl");
    public static final RegistryObject<Item> PEARLFISH = registerIngredient("pearlfish");
    public static final RegistryObject<Item> PINE_THRUSH_EGG = registerIngredient("pine_thrush_egg");
    public static final RegistryObject<Item> POISON_BLOOM = registerIngredient("poison_bloom");
    public static final RegistryObject<Item> POWDERED_MAMMOTH_TUSK = registerIngredient("powdered_mammoth_tusk");
    public static final RegistryObject<Item> PURPLE_BUTTERFLY_WING = registerIngredient("purple_butterfly_wing");
    public static final RegistryObject<Item> PURPLE_MOUNTAIN_FLOWER = registerIngredient("purple_mountain_flower");
    public static final RegistryObject<Item> PYGMY_SUNFISH = registerIngredient("pygmy_sunfish");
    public static final RegistryObject<Item> RED_KELP_GAS_BLADDER = registerIngredient("red_kelp_gas_bladder");
    public static final RegistryObject<Item> RED_MOUNTAIN_FLOWER = registerIngredient("red_mountain_flower");
    public static final RegistryObject<Item> REDWORT_FLOWER = registerIngredient("redwort_flower");
    public static final RegistryObject<Item> RIVER_BETTY = registerIngredient("river_betty");
    public static final RegistryObject<Item> ROCK_WARBLER_EGG = registerIngredient("rock_warbler_egg");
    public static final RegistryObject<Item> ROOBRUSH = registerIngredient("roobrush");
    public static final RegistryObject<Item> ROT_SCALE = registerIngredient("rot_scale");
    public static final RegistryObject<Item> SABRE_CAT_TOOTH = registerIngredient("sabre_cat_tooth");
    public static final RegistryObject<Item> SALMON_ROE = registerIngredient("salmon_roe");
    public static final RegistryObject<Item> SALT_PILE = registerIngredient("salt_pile");
    public static final RegistryObject<Item> SALTRICE = registerIngredient("saltrice");
    public static final RegistryObject<Item> SCALON_FIN = registerIngredient("scalon_fin");
    public static final RegistryObject<Item> SCALY_PHOLIOTA = registerIngredient("scaly_pholiota");
    public static final RegistryObject<Item> SCATHECRAW = registerIngredient("scathecraw");
    public static final RegistryObject<Item> SCREAMING_MAW = registerIngredient("screaming_maw");
    public static final RegistryObject<Item> SCRIB_JELLY = registerIngredient("scrib_jelly");
    public static final RegistryObject<Item> SCRIB_JERKY = registerIngredient("scrib_jerky");
    public static final RegistryObject<Item> SILVERSIDE_PERCH = registerIngredient("silverside_perch");
    public static final RegistryObject<Item> SIMON_RODAYNES_HEART = registerIngredient("simon_rodaynes_heart");
    public static final RegistryObject<Item> SLAUGHTERFISH_EGG = registerIngredient("slaughterfish_egg");
    public static final RegistryObject<Item> SLAUGHTERFISH_SCALES = registerIngredient("slaughterfish_scales");
    public static final RegistryObject<Item> SLOAD_SOAP = registerIngredient("sload_soap");
    public static final RegistryObject<Item> SMALL_ANTLERS = registerIngredient("small_antlers");
    public static final RegistryObject<Item> SMALL_PEARL = registerIngredient("small_pearl");
    public static final RegistryObject<Item> SNOWBERRIES = registerIngredient("snowberries");
    public static final RegistryObject<Item> SPADEFISH = registerIngredient("spadefish");
    public static final RegistryObject<Item> SPAWN_ASH = registerIngredient("spawn_ash");
    public static final RegistryObject<Item> SPIDDAL_STICK = registerIngredient("spiddal_stick");
    public static final RegistryObject<Item> SPIDER_EGG = registerIngredient("spider_egg");
    public static final RegistryObject<Item> SPRIGGAN_SAP = registerIngredient("spriggan_sap");
    public static final RegistryObject<Item> STEEL_BLUE_ENTOLOMA = registerIngredient("steel_blue_entoloma");
    public static final RegistryObject<Item> STONEFLOWER_PETALS = registerIngredient("stoneflower_petals");
    public static final RegistryObject<Item> SWAMP_FUNGAL_POD = registerIngredient("swamp_fungal_pod");
    public static final RegistryObject<Item> TAPROOT = registerIngredient("taproot");
    public static final RegistryObject<Item> THISTLE_BRANCH = registerIngredient("thistle_branch");
    public static final RegistryObject<Item> THORN_HOOK = registerIngredient("thorn_hook");
    public static final RegistryObject<Item> TORCHBUG_THORAX = registerIngredient("torchbug_thorax");
    public static final RegistryObject<Item> TRAMA_ROOT = registerIngredient("trama_root");
    public static final RegistryObject<Item> TROLL_FAT = registerIngredient("troll_fat");
    public static final RegistryObject<Item> TUNDRA_COTTON = registerIngredient("tundra_cotton");
    public static final RegistryObject<Item> VAMPIRE_DUST = registerIngredient("vampire_dust");
    public static final RegistryObject<Item> VOID_ESSENCE = registerIngredient("void_essence");
    public static final RegistryObject<Item> VOID_SALTS = registerIngredient("void_salts");
    public static final RegistryObject<Item> WATCHERS_EYE = registerIngredient("watchers_eye");
    public static final RegistryObject<Item> WHEAT = registerIngredient("wheat");
    public static final RegistryObject<Item> WHITE_CAP = registerIngredient("white_cap");
    public static final RegistryObject<Item> WILD_GRASS_POD = registerIngredient("wild_grass_pod");
    public static final RegistryObject<Item> WISP_STALK_CAPS = registerIngredient("wisp_stalk_caps");
    public static final RegistryObject<Item> WISP_WRAPPINGS = registerIngredient("wisp_wrappings");
    public static final RegistryObject<Item> WITHERING_MOON = registerIngredient("withering_moon");
    public static final RegistryObject<Item> WORMS_HEAD_CAP = registerIngredient("worms_head_cap");
    public static final RegistryObject<Item> YELLOW_MOUNTAIN_FLOWER = registerIngredient("yellow_mountain_flower");


    public static final RegistryObject<Item> JUNIPER_BERRIES = registerIngredient("juniper_berries");


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
