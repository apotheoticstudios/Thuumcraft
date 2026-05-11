package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.registries.RegistryObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Locale;

import static java.util.Map.entry;

public class ModFoods {
    private static final int INGREDIENT_EFFECT_DURATION_TICKS = 30;
    private static final double SKILL_ATTRIBUTE_BONUS = 5.0D;
    private static final double SKILL_ATTRIBUTE_PENALTY = -5.0D;

    public static final FoodProperties INGREDIENT = new FoodProperties.Builder().nutrition(1).fast().alwaysEat()
            .saturationMod(0.1f).build();
    public static final FoodProperties JUNIPER_BERRIES = INGREDIENT;

    private static final Map<String, AlchemyEffect> PRIMARY_EFFECTS = Map.ofEntries(
            entry("abecean_longfin", AlchemyEffect.WEAKNESS_TO_FROST),
            entry("alocasia_fruit", AlchemyEffect.REGENERATE_STAMINA),
            entry("aloe_vera_leaves", AlchemyEffect.RESTORE_HEALTH),
            entry("ambrosia", AlchemyEffect.RESTORE_HEALTH),
            entry("ancestor_moth_wing", AlchemyEffect.DAMAGE_STAMINA),
            entry("angelfish", AlchemyEffect.REGENERATE_HEALTH),
            entry("angler_larvae", AlchemyEffect.LINGERING_DAMAGE_HEALTH),
            entry("ash_creep_cluster", AlchemyEffect.DAMAGE_STAMINA),
            entry("ash_hopper_jelly", AlchemyEffect.RESTORE_HEALTH),
            entry("ashen_grass_pod", AlchemyEffect.RESIST_FIRE),
            entry("aster_bloom_core", AlchemyEffect.RESIST_MAGIC),
            entry("bear_claws", AlchemyEffect.RESTORE_STAMINA),
            entry("bee", AlchemyEffect.RESTORE_STAMINA),
            entry("beehive_husk", AlchemyEffect.RESIST_POISON),
            entry("berits_ashes", AlchemyEffect.DAMAGE_STAMINA),
            entry("bittergreen_petals", AlchemyEffect.LINGERING_DAMAGE_STAMINA),
            entry("bleeding_crown", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("blind_watchers_eye", AlchemyEffect.LIGHT),
            entry("bliss_bug_thorax", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("blister_pod_cap", AlchemyEffect.RESTORE_MAGICKA),
            entry("blisterwort", AlchemyEffect.DAMAGE_STAMINA),
            entry("bloodgrass", AlchemyEffect.INVISIBILITY),
            entry("blue_butterfly_wing", AlchemyEffect.DAMAGE_STAMINA),
            entry("blue_dartwing", AlchemyEffect.RESIST_SHOCK),
            entry("blue_mountain_flower", AlchemyEffect.RESTORE_HEALTH),
            entry("boar_tusk", AlchemyEffect.FORTIFY_STAMINA),
            entry("bog_beacon", AlchemyEffect.RESTORE_MAGICKA),
            entry("bone_meal", AlchemyEffect.DAMAGE_STAMINA),
            entry("briar_heart", AlchemyEffect.RESTORE_MAGICKA),
            entry("bunglers_bane", AlchemyEffect.SLOW),
            entry("burnt_spriggan_wood", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("butterfly_wing", AlchemyEffect.RESTORE_HEALTH),
            entry("canis_root", AlchemyEffect.DAMAGE_STAMINA),
            entry("charred_skeever_hide", AlchemyEffect.RESTORE_STAMINA),
            entry("chaurus_eggs", AlchemyEffect.WEAKNESS_TO_POISON),
            entry("chaurus_hunter_antennae", AlchemyEffect.DAMAGE_STAMINA),
            entry("chickens_egg", AlchemyEffect.RESIST_MAGIC),
            entry("chokeberry", AlchemyEffect.DAMAGE_HEALTH),
            entry("chokeweed", AlchemyEffect.WEAKNESS_TO_FROST),
            entry("coda_flower", AlchemyEffect.DAMAGE_HEALTH),
            entry("comberry", AlchemyEffect.DAMAGE_STAMINA),
            entry("congealed_putrescence", AlchemyEffect.RAVAGE_HEALTH),
            entry("corkbulb_root", AlchemyEffect.PARALYSIS),
            entry("corrupted_human_heart", AlchemyEffect.DAMAGE_HEALTH),
            entry("creep_cluster", AlchemyEffect.RESTORE_MAGICKA),
            entry("crimson_nirnroot", AlchemyEffect.DAMAGE_HEALTH),
            entry("cyrodilic_spadetail", AlchemyEffect.DAMAGE_STAMINA),
            entry("daedra_heart", AlchemyEffect.RESTORE_HEALTH),
            entry("daedra_silk", AlchemyEffect.LINGERING_DAMAGE_STAMINA),
            entry("daedra_venin", AlchemyEffect.RAVAGE_HEALTH),
            entry("daedroth_teeth", AlchemyEffect.RESIST_FROST),
            entry("deathbell", AlchemyEffect.DAMAGE_HEALTH),
            entry("dragons_tongue", AlchemyEffect.RESIST_FIRE),
            entry("dreugh_wax", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("dwarven_oil", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("ectoplasm", AlchemyEffect.RESTORE_MAGICKA),
            entry("elves_ear", AlchemyEffect.RESTORE_MAGICKA),
            entry("elytra_ichor", AlchemyEffect.RESTORE_MAGICKA),
            entry("emperor_parasol_moss", AlchemyEffect.DAMAGE_HEALTH),
            entry("eye_of_sabre_cat", AlchemyEffect.RESTORE_STAMINA),
            entry("falmer_ear", AlchemyEffect.DAMAGE_HEALTH),
            entry("farengars_frost_salt", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("felsaad_tern_feathers", AlchemyEffect.RESTORE_HEALTH),
            entry("fine_cut_void_salts", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("fire_petal", AlchemyEffect.DAMAGE_HEALTH),
            entry("fire_salts", AlchemyEffect.WEAKNESS_TO_FROST),
            entry("flame_stalk", AlchemyEffect.RESTORE_HEALTH),
            entry("fly_amanita", AlchemyEffect.RESIST_FIRE),
            entry("frost_mirriam", AlchemyEffect.RESIST_FROST),
            entry("frost_salts", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("fungus_stalk", AlchemyEffect.RESTORE_MAGICKA),
            entry("garlic", AlchemyEffect.RESIST_POISON),
            entry("giant_lichen", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("giants_toe", AlchemyEffect.DAMAGE_STAMINA),
            entry("glassfish", AlchemyEffect.RESTORE_MAGICKA),
            entry("gleamblossom", AlchemyEffect.RESIST_MAGIC),
            entry("glow_dust", AlchemyEffect.DAMAGE_MAGICKA),
            entry("glowing_mushroom", AlchemyEffect.RESIST_SHOCK),
            entry("gnarl_bark", AlchemyEffect.DAMAGE_HEALTH),
            entry("gold_kanet", AlchemyEffect.PARALYSIS),
            entry("goldfish", AlchemyEffect.RESTORE_STAMINA),
            entry("grass_pod", AlchemyEffect.RESIST_POISON),
            entry("green_butterfly_wing", AlchemyEffect.RESTORE_MAGICKA),
            entry("hackle_lo_leaf", AlchemyEffect.RESTORE_STAMINA),
            entry("hagraven_claw", AlchemyEffect.RESIST_MAGIC),
            entry("hagraven_feathers", AlchemyEffect.DAMAGE_MAGICKA),
            entry("hanging_moss", AlchemyEffect.DAMAGE_MAGICKA),
            entry("harrada", AlchemyEffect.DAMAGE_HEALTH),
            entry("hawk_beak", AlchemyEffect.RESTORE_STAMINA),
            entry("hawk_feathers", AlchemyEffect.CURE_DISEASE),
            entry("hawks_egg", AlchemyEffect.RESIST_MAGIC),
            entry("healing_salts", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("heart_of_order", AlchemyEffect.RESTORE_HEALTH),
            entry("histcarp", AlchemyEffect.RESTORE_STAMINA),
            entry("honeycomb", AlchemyEffect.RESTORE_STAMINA),
            entry("human_flesh", AlchemyEffect.DAMAGE_HEALTH),
            entry("human_heart", AlchemyEffect.DAMAGE_HEALTH),
            entry("hunger_tongue", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("hydnum_azure_giant_spore", AlchemyEffect.RESIST_FROST),
            entry("hypha_facia", AlchemyEffect.WEAKNESS_TO_POISON),
            entry("ice_wraith_teeth", AlchemyEffect.WEAKNESS_TO_FROST),
            entry("imp_gall", AlchemyEffect.DAMAGE_HEALTH),
            entry("imp_stool", AlchemyEffect.DAMAGE_HEALTH),
            entry("ironwood_fruit", AlchemyEffect.RESTORE_HEALTH),
            entry("jarrin_root", AlchemyEffect.DAMAGE_HEALTH),
            entry("jazbay_grapes", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("juniper_berries", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("juvenile_mudcrab", AlchemyEffect.REGENERATE_STAMINA),
            entry("kagouti_hide", AlchemyEffect.LINGERING_DAMAGE_STAMINA),
            entry("kresh_fiber", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("large_antlers", AlchemyEffect.RESTORE_STAMINA),
            entry("lavender", AlchemyEffect.RESIST_MAGIC),
            entry("lichor", AlchemyEffect.RESTORE_MAGICKA),
            entry("luminous_russula", AlchemyEffect.LINGERING_DAMAGE_STAMINA),
            entry("luna_moth_wing", AlchemyEffect.DAMAGE_MAGICKA),
            entry("lyretail_anthias", AlchemyEffect.RESTORE_MAGICKA),
            entry("marshmerrow", AlchemyEffect.RESTORE_HEALTH),
            entry("minotaur_horn", AlchemyEffect.RESIST_POISON),
            entry("moon_sugar", AlchemyEffect.WEAKNESS_TO_FIRE),
            entry("mora_tapinella", AlchemyEffect.RESTORE_MAGICKA),
            entry("mort_flesh", AlchemyEffect.DAMAGE_HEALTH),
            entry("mudcrab_chitin", AlchemyEffect.RESTORE_STAMINA),
            entry("namiras_rot", AlchemyEffect.DAMAGE_MAGICKA),
            entry("netch_jelly", AlchemyEffect.PARALYSIS),
            entry("nightshade", AlchemyEffect.DAMAGE_HEALTH),
            entry("nirnroot", AlchemyEffect.DAMAGE_HEALTH),
            entry("nordic_barnacle", AlchemyEffect.DAMAGE_MAGICKA),
            entry("ogres_teeth", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("orange_dartwing", AlchemyEffect.RESTORE_STAMINA),
            entry("pearl", AlchemyEffect.RESTORE_STAMINA),
            entry("pearlfish", AlchemyEffect.RESTORE_STAMINA),
            entry("pine_thrush_egg", AlchemyEffect.RESTORE_STAMINA),
            entry("poison_bloom", AlchemyEffect.DAMAGE_HEALTH),
            entry("powdered_mammoth_tusk", AlchemyEffect.RESTORE_STAMINA),
            entry("purple_butterfly_wing", AlchemyEffect.REGENERATE_HEALTH),
            entry("purple_mountain_flower", AlchemyEffect.RESTORE_STAMINA),
            entry("pygmy_sunfish", AlchemyEffect.RESTORE_STAMINA),
            entry("red_kelp_gas_bladder", AlchemyEffect.REGENERATE_STAMINA),
            entry("red_mountain_flower", AlchemyEffect.RESTORE_MAGICKA),
            entry("redwort_flower", AlchemyEffect.RESIST_FROST),
            entry("river_betty", AlchemyEffect.DAMAGE_HEALTH),
            entry("rock_warbler_egg", AlchemyEffect.RESTORE_HEALTH),
            entry("roobrush", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("rot_scale", AlchemyEffect.SLOW),
            entry("sabre_cat_tooth", AlchemyEffect.RESTORE_STAMINA),
            entry("salmon_roe", AlchemyEffect.RESTORE_STAMINA),
            entry("salt_pile", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("saltrice", AlchemyEffect.RESTORE_STAMINA),
            entry("scalon_fin", AlchemyEffect.WATERBREATHING),
            entry("scaly_pholiota", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("scathecraw", AlchemyEffect.RAVAGE_HEALTH),
            entry("screaming_maw", AlchemyEffect.REGENERATE_MAGICKA),
            entry("scrib_jelly", AlchemyEffect.REGENERATE_MAGICKA),
            entry("scrib_jerky", AlchemyEffect.RESTORE_STAMINA),
            entry("silverside_perch", AlchemyEffect.RESTORE_STAMINA),
            entry("simon_rodaynes_heart", AlchemyEffect.DAMAGE_HEALTH),
            entry("skeever_tail", AlchemyEffect.DAMAGE_STAMINA_REGENERATION),
            entry("slaughterfish_egg", AlchemyEffect.RESIST_POISON),
            entry("slaughterfish_scales", AlchemyEffect.RESIST_POISON),
            entry("sload_soap", AlchemyEffect.RESIST_FIRE),
            entry("small_antlers", AlchemyEffect.WEAKNESS_TO_POISON),
            entry("small_pearl", AlchemyEffect.RESTORE_STAMINA),
            entry("snowberries", AlchemyEffect.RESIST_FIRE),
            entry("spadefish", AlchemyEffect.RESTORE_HEALTH),
            entry("spawn_ash", AlchemyEffect.RAVAGE_STAMINA),
            entry("spiddal_stick", AlchemyEffect.DAMAGE_HEALTH),
            entry("spider_egg", AlchemyEffect.DAMAGE_STAMINA),
            entry("spriggan_sap", AlchemyEffect.DAMAGE_MAGICKA_REGENERATION),
            entry("steel_blue_entoloma", AlchemyEffect.RESTORE_MAGICKA),
            entry("stoneflower_petals", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("swamp_fungal_pod", AlchemyEffect.RESIST_SHOCK),
            entry("taproot", AlchemyEffect.WEAKNESS_TO_MAGIC),
            entry("thistle_branch", AlchemyEffect.RESIST_FROST),
            entry("thorn_hook", AlchemyEffect.LINGERING_DAMAGE_HEALTH),
            entry("torchbug_thorax", AlchemyEffect.RESTORE_STAMINA),
            entry("trama_root", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("troll_fat", AlchemyEffect.RESIST_POISON),
            entry("tundra_cotton", AlchemyEffect.RESIST_MAGIC),
            entry("vampire_dust", AlchemyEffect.INVISIBILITY),
            entry("void_essence", AlchemyEffect.RESTORE_HEALTH),
            entry("void_salts", AlchemyEffect.WEAKNESS_TO_SHOCK),
            entry("watchers_eye", AlchemyEffect.NIGHT_EYE),
            entry("wheat", AlchemyEffect.RESTORE_HEALTH),
            entry("white_cap", AlchemyEffect.WEAKNESS_TO_FROST),
            entry("wild_grass_pod", AlchemyEffect.RESIST_POISON),
            entry("wisp_stalk_caps", AlchemyEffect.DAMAGE_HEALTH),
            entry("wisp_wrappings", AlchemyEffect.RESTORE_STAMINA),
            entry("withering_moon", AlchemyEffect.RESTORE_MAGICKA),
            entry("worms_head_cap", AlchemyEffect.FORTIFY_LOCKPICKING),
            entry("yellow_mountain_flower", AlchemyEffect.RESIST_POISON)
    );

    private static final Map<UUID, List<TemporaryAttributeModifier>> ACTIVE_ATTRIBUTE_MODIFIERS = new HashMap<>();

    public static void applyIngredientEffects(String ingredientId, LivingEntity entity) {
        AlchemyEffect effect = PRIMARY_EFFECTS.get(ingredientId);
        if (effect == null) {
            return;
        }

        applyEffect(effect, entity);
    }

    public static Component getIngredientEffectName(String ingredientId) {
        AlchemyEffect effect = PRIMARY_EFFECTS.get(ingredientId);
        return effect == null ? Component.empty() : effect.getDisplayName();
    }

    public static void tickIngredientAttributeModifiers(LivingEntity entity) {
        if (entity.level().isClientSide) {
            return;
        }

        List<TemporaryAttributeModifier> modifiers = ACTIVE_ATTRIBUTE_MODIFIERS.get(entity.getUUID());
        if (modifiers == null) {
            return;
        }

        long gameTime = entity.level().getGameTime();
        Iterator<TemporaryAttributeModifier> iterator = modifiers.iterator();
        while (iterator.hasNext()) {
            TemporaryAttributeModifier modifier = iterator.next();
            if (gameTime >= modifier.expiresAt()) {
                AttributeInstance attributeInstance = entity.getAttribute(modifier.attribute());
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(modifier.modifierId());
                }
                iterator.remove();
            }
        }

        if (modifiers.isEmpty()) {
            ACTIVE_ATTRIBUTE_MODIFIERS.remove(entity.getUUID());
        }
    }

    private static void applyEffect(AlchemyEffect effect, LivingEntity entity) {
        switch (effect) {
            case RESTORE_HEALTH -> {
                entity.heal(2.0F);
                addEffect(entity, MobEffects.REGENERATION);
            }
            case DAMAGE_HEALTH -> entity.hurt(entity.damageSources().magic(), 2.0F);
            case RAVAGE_HEALTH -> entity.hurt(entity.damageSources().magic(), 4.0F);
            case LINGERING_DAMAGE_HEALTH -> addEffect(entity, MobEffects.POISON);
            case RESTORE_MAGICKA -> addEffect(entity, ModEffects.RESTORE_MAGICKA.get());
            case REGENERATE_HEALTH -> addEffect(entity, MobEffects.REGENERATION);
            case REGENERATE_MAGICKA -> addEffect(entity, ModEffects.REGENERATE_MAGICKA.get());
            case RESTORE_STAMINA, REGENERATE_STAMINA, FORTIFY_STAMINA ->
                    addAttributeModifier(entity, ModAttributes.STAMINA, "stamina", SKILL_ATTRIBUTE_BONUS);
            case DAMAGE_STAMINA, RAVAGE_STAMINA, LINGERING_DAMAGE_STAMINA ->
                    addAttributeModifier(entity, ModAttributes.STAMINA, "damage_stamina", SKILL_ATTRIBUTE_PENALTY);
            case DAMAGE_STAMINA_REGENERATION ->
                    addAttributeModifier(entity, ModAttributes.STAMINA_REGENERATION, "damage_stamina_regeneration", SKILL_ATTRIBUTE_PENALTY);
            case DAMAGE_MAGICKA -> addEffect(entity, ModEffects.DAMAGE_MAGICKA.get());
            case DAMAGE_MAGICKA_REGENERATION -> addEffect(entity, ModEffects.DAMAGE_MAGICKA_REGENERATION.get());
            case RESIST_FIRE -> addEffect(entity, MobEffects.FIRE_RESISTANCE);
            case RESIST_FROST -> addEffect(entity, ModEffects.FROST_RESISTANCE.get());
            case RESIST_MAGIC -> addEffect(entity, ModEffects.MAGIC_RESISTANCE.get());
            case RESIST_POISON -> addEffect(entity, ModEffects.POISON_RESISTANCE.get());
            case RESIST_SHOCK -> addEffect(entity, ModEffects.SHOCK_RESISTANCE.get());
            case WEAKNESS_TO_FIRE -> addEffect(entity, ModEffects.FIRE_WEAKNESS.get());
            case WEAKNESS_TO_FROST -> addEffect(entity, ModEffects.FROST_WEAKNESS.get());
            case WEAKNESS_TO_MAGIC -> addEffect(entity, ModEffects.MAGIC_WEAKNESS.get());
            case WEAKNESS_TO_POISON -> addEffect(entity, ModEffects.POISON_WEAKNESS.get());
            case WEAKNESS_TO_SHOCK -> addEffect(entity, ModEffects.SHOCK_WEAKNESS.get());
            case INVISIBILITY -> addEffect(entity, MobEffects.INVISIBILITY);
            case LIGHT -> {
                addEffect(entity, ModEffects.LIGHT.get());
                addEffect(entity, MobEffects.GLOWING);
            }
            case NIGHT_EYE -> addEffect(entity, MobEffects.NIGHT_VISION);
            case PARALYSIS -> addEffect(entity, ModEffects.PARALYSIS.get());
            case SLOW -> addEffect(entity, MobEffects.MOVEMENT_SLOWDOWN);
            case WATERBREATHING -> addEffect(entity, MobEffects.WATER_BREATHING);
            case CURE_DISEASE -> cureDiseaseLikeEffects(entity);
            case FORTIFY_LOCKPICKING ->
                    addAttributeModifier(entity, ModAttributes.LOCKPICKING, "fortify_lockpicking", SKILL_ATTRIBUTE_BONUS);
            case FORTIFY_ARCHERY ->
                    addAttributeModifier(entity, ModAttributes.ARCHERY, "fortify_archery", SKILL_ATTRIBUTE_BONUS);
        }
    }

    private static void addEffect(LivingEntity entity, MobEffect effect) {
        entity.addEffect(new MobEffectInstance(effect, INGREDIENT_EFFECT_DURATION_TICKS));
    }

    private static void addAttributeModifier(LivingEntity entity, RegistryObject<Attribute> attribute, String effectName, double amount) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute.get());
        if (attributeInstance == null) {
            return;
        }

        UUID modifierId = UUID.nameUUIDFromBytes(("thuumcraft:ingredient:" + effectName).getBytes(StandardCharsets.UTF_8));
        attributeInstance.removeModifier(modifierId);
        attributeInstance.addTransientModifier(new AttributeModifier(modifierId, "Ingredient " + effectName,
                amount, AttributeModifier.Operation.ADDITION));

        List<TemporaryAttributeModifier> modifiers = ACTIVE_ATTRIBUTE_MODIFIERS.computeIfAbsent(entity.getUUID(),
                uuid -> new ArrayList<>());
        modifiers.removeIf(modifier -> modifier.modifierId().equals(modifierId));
        modifiers.add(new TemporaryAttributeModifier(attribute.get(), modifierId,
                entity.level().getGameTime() + INGREDIENT_EFFECT_DURATION_TICKS));
    }

    private static void cureDiseaseLikeEffects(LivingEntity entity) {
        entity.removeEffect(MobEffects.POISON);
        entity.removeEffect(MobEffects.WITHER);
        entity.removeEffect(MobEffects.HUNGER);
        entity.removeEffect(MobEffects.CONFUSION);
        entity.removeEffect(MobEffects.WEAKNESS);
    }

    private record TemporaryAttributeModifier(Attribute attribute, UUID modifierId, long expiresAt) {
    }

    private enum AlchemyEffect {
        CURE_DISEASE,
        DAMAGE_HEALTH,
        DAMAGE_MAGICKA,
        DAMAGE_MAGICKA_REGENERATION,
        DAMAGE_STAMINA,
        DAMAGE_STAMINA_REGENERATION,
        FORTIFY_ARCHERY,
        FORTIFY_LOCKPICKING,
        FORTIFY_STAMINA,
        INVISIBILITY,
        LIGHT,
        LINGERING_DAMAGE_HEALTH,
        LINGERING_DAMAGE_STAMINA,
        NIGHT_EYE,
        PARALYSIS,
        RAVAGE_HEALTH,
        RAVAGE_STAMINA,
        REGENERATE_HEALTH,
        REGENERATE_MAGICKA,
        REGENERATE_STAMINA,
        RESIST_FIRE,
        RESIST_FROST,
        RESIST_MAGIC,
        RESIST_POISON,
        RESIST_SHOCK,
        RESTORE_HEALTH,
        RESTORE_MAGICKA,
        RESTORE_STAMINA,
        SLOW,
        WATERBREATHING,
        WEAKNESS_TO_FIRE,
        WEAKNESS_TO_FROST,
        WEAKNESS_TO_MAGIC,
        WEAKNESS_TO_POISON,
        WEAKNESS_TO_SHOCK;

        private Component getDisplayName() {
            return Component.translatable("tooltip.thuumcraft.alchemy_effect."
                    + name().toLowerCase(Locale.ROOT));
        }
    }
}
