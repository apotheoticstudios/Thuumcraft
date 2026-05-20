package net.apotheoticstudios.thuumcraft.item;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.apotheoticstudios.thuumcraft.stamina.StaminaEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
    private static final int FORTIFY_RESOURCE_DURATION_TICKS = 60 * 20;
    private static final int REGENERATE_RESOURCE_DURATION_TICKS = 300 * 20;
    private static final int DAMAGE_REGENERATION_DURATION_TICKS = 5 * 20;
    private static final int LINGERING_DAMAGE_DURATION_TICKS = 10 * 20;
    private static final int RAVAGE_RESOURCE_DURATION_TICKS = 10 * 20;
    private static final double RESTORE_RESOURCE_AMOUNT = 5.0D;
    private static final double DAMAGE_RESOURCE_AMOUNT = 3.0D;
    private static final double DAMAGE_STAMINA_AMOUNT = 15.0D;
    private static final double FORTIFY_RESOURCE_AMOUNT = 4.0D;
    private static final double RAVAGE_RESOURCE_AMOUNT = -2.0D;
    private static final double REGENERATE_RESOURCE_MULTIPLIER = 0.05D;
    private static final double DAMAGE_REGENERATION_MULTIPLIER = -1.0D;
    private static final double LINGERING_DAMAGE_PER_SECOND = -1.0D;
    private static final double SKILL_ATTRIBUTE_BONUS = 5.0D;

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
            entry("bittergreen_petals", AlchemyEffect.LINGERING_DAMAGE_STAMINA),
            entry("bleeding_crown", AlchemyEffect.WEAKNESS_TO_FIRE),
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
            entry("felsaad_tern_feathers", AlchemyEffect.RESTORE_HEALTH),
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
            entry("wisp_stalk_caps", AlchemyEffect.DAMAGE_HEALTH),
            entry("wisp_wrappings", AlchemyEffect.RESTORE_STAMINA),
            entry("withering_moon", AlchemyEffect.RESTORE_MAGICKA),
            entry("worms_head_cap", AlchemyEffect.FORTIFY_LOCKPICKING),
            entry("yellow_mountain_flower", AlchemyEffect.RESIST_POISON)
    );
    private static final Map<ResourceLocation, String> ITEM_ALIASES = Map.of(
            ResourceLocation.fromNamespaceAndPath("minecraft", "bone_meal"), "bone_meal",
            ResourceLocation.fromNamespaceAndPath("minecraft", "egg"), "chickens_egg",
            ResourceLocation.fromNamespaceAndPath("minecraft", "wheat"), "wheat"
    );
    private static final Map<String, List<AlchemyEffect>> COUNTERPART_TRANSFERRED_EFFECTS = Map.of(
            "grass_pod", List.of(AlchemyEffect.RESIST_POISON),
            "spawn_ash", List.of(AlchemyEffect.DAMAGE_STAMINA),
            "watchers_eye", List.of(AlchemyEffect.LIGHT),
            "frost_salts", List.of(AlchemyEffect.WEAKNESS_TO_FIRE),
            "void_salts", List.of(AlchemyEffect.WEAKNESS_TO_SHOCK),
            "human_heart", List.of(AlchemyEffect.DAMAGE_HEALTH)
    );

    private static final Map<UUID, List<TemporaryAttributeModifier>> ACTIVE_ATTRIBUTE_MODIFIERS = new HashMap<>();
    private static final Map<UUID, List<ActiveResourceEffect>> ACTIVE_RESOURCE_EFFECTS = new HashMap<>();

    public static void applyIngredientEffects(String ingredientId, LivingEntity entity) {
        List<AlchemyEffect> effects = getBaseIngredientEffects(ingredientId);
        if (effects.isEmpty()) {
            return;
        }

        effects.forEach(effect -> applyEffect(effect, entity));
        if (entity instanceof ServerPlayer player) {
            int experimenterRank = SkillPerk.rank(player, SkillPerk.ALCHEMY_EXPERIMENTER);
            IngredientKnowledge.discover(player, ingredientId, Math.min(4,
                    Math.max(effects.size(), 1 + experimenterRank)));
            SkillProgression.award(player, SkillProgression.Skill.ALCHEMY,
                    getAlchemyIngredientExperience(effects.get(0)));
        }
    }

    public static String getIngredientId(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return null;
        }

        String alias = ITEM_ALIASES.get(itemId);
        if (alias != null) {
            return alias;
        }

        String path = itemId.getPath().toLowerCase(Locale.ROOT);
        return PRIMARY_EFFECTS.containsKey(path) ? path : null;
    }

    public static Component getIngredientEffectName(String ingredientId) {
        List<AlchemyEffect> effects = getBaseIngredientEffects(ingredientId);
        return effects.isEmpty() ? Component.empty() : effects.get(0).getDisplayName();
    }

    public static List<Component> getIngredientEffectNames(String ingredientId, int count) {
        List<Component> names = new ArrayList<>();
        for (AlchemyEffect effect : getIngredientEffects(ingredientId, Math.max(1, count))) {
            names.add(effect.getDisplayName());
        }
        return names;
    }

    public static void tickIngredientAttributeModifiers(LivingEntity entity) {
        if (entity.level().isClientSide
                || (ACTIVE_ATTRIBUTE_MODIFIERS.isEmpty() && ACTIVE_RESOURCE_EFFECTS.isEmpty())) {
            return;
        }

        tickIngredientResourceEffects(entity);

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
                clampResourceAfterAttributeChange(entity, modifier.attribute());
                iterator.remove();
            }
        }

        if (modifiers.isEmpty()) {
            ACTIVE_ATTRIBUTE_MODIFIERS.remove(entity.getUUID());
        }
    }

    private static void tickIngredientResourceEffects(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        List<ActiveResourceEffect> effects = ACTIVE_RESOURCE_EFFECTS.get(player.getUUID());
        if (effects == null) {
            return;
        }

        long gameTime = player.level().getGameTime();
        Iterator<ActiveResourceEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            ActiveResourceEffect effect = iterator.next();
            if (gameTime > effect.expiresAt) {
                iterator.remove();
                continue;
            }

            if (gameTime >= effect.nextApplicationTick) {
                applyResourceChange(player, effect.resource, effect.amountPerSecond);
                effect.nextApplicationTick += 20L;
            }
        }

        if (effects.isEmpty()) {
            ACTIVE_RESOURCE_EFFECTS.remove(player.getUUID());
        }
    }

    private static void applyEffect(AlchemyEffect effect, LivingEntity entity) {
        if (entity instanceof ServerPlayer player
                && SkillPerk.has(player, SkillPerk.ALCHEMY_PURITY)
                && isHarmfulEffect(effect)) {
            return;
        }

        double magnitude = getAlchemyMagnitudeMultiplier(entity, effect);
        int duration = getAlchemyDuration(entity, effect, INGREDIENT_EFFECT_DURATION_TICKS);
        int fortifyDuration = getAlchemyDuration(entity, effect, FORTIFY_RESOURCE_DURATION_TICKS);
        int regenerationDuration = getAlchemyDuration(entity, effect, REGENERATE_RESOURCE_DURATION_TICKS);
        int damageRegenerationDuration = getAlchemyDuration(entity, effect, DAMAGE_REGENERATION_DURATION_TICKS);
        int lingeringDuration = getAlchemyDuration(entity, effect, LINGERING_DAMAGE_DURATION_TICKS);
        int ravageDuration = getAlchemyDuration(entity, effect, RAVAGE_RESOURCE_DURATION_TICKS);
        switch (effect) {
            case RESTORE_HEALTH -> {
                entity.heal((float) (2.0F * magnitude));
                addEffect(entity, MobEffects.REGENERATION, duration);
            }
            case DAMAGE_HEALTH -> entity.hurt(entity.damageSources().magic(), (float) (2.0F * magnitude));
            case RAVAGE_HEALTH -> entity.hurt(entity.damageSources().magic(), (float) (4.0F * magnitude));
            case LINGERING_DAMAGE_HEALTH -> addEffect(entity, MobEffects.POISON, lingeringDuration);
            case RESTORE_MAGICKA -> applyResourceChange(entity, ResourceType.MAGICKA,
                    RESTORE_RESOURCE_AMOUNT * magnitude);
            case REGENERATE_HEALTH -> addEffect(entity, MobEffects.REGENERATION, duration);
            case REGENERATE_MAGICKA ->
                    addAttributeModifier(entity, AttributeRegistry.MANA_REGEN, "regenerate_magicka",
                            REGENERATE_RESOURCE_MULTIPLIER * magnitude, AttributeModifier.Operation.MULTIPLY_TOTAL,
                            regenerationDuration);
            case FORTIFY_MAGICKA -> {
                addAttributeModifier(entity, AttributeRegistry.MAX_MANA, "fortify_magicka",
                        FORTIFY_RESOURCE_AMOUNT * magnitude, AttributeModifier.Operation.ADDITION, fortifyDuration);
                applyResourceChange(entity, ResourceType.MAGICKA, FORTIFY_RESOURCE_AMOUNT * magnitude);
            }
            case RESTORE_STAMINA -> applyResourceChange(entity, ResourceType.STAMINA,
                    RESTORE_RESOURCE_AMOUNT * magnitude);
            case REGENERATE_STAMINA ->
                    addAttributeModifier(entity, ModAttributes.STAMINA_REGENERATION, "regenerate_stamina",
                            REGENERATE_RESOURCE_MULTIPLIER * magnitude, AttributeModifier.Operation.MULTIPLY_TOTAL,
                            regenerationDuration);
            case FORTIFY_STAMINA -> {
                addAttributeModifier(entity, ModAttributes.STAMINA, "fortify_stamina",
                        FORTIFY_RESOURCE_AMOUNT * magnitude, AttributeModifier.Operation.ADDITION, fortifyDuration);
                applyResourceChange(entity, ResourceType.STAMINA, FORTIFY_RESOURCE_AMOUNT * magnitude);
            }
            case DAMAGE_STAMINA -> applyResourceChange(entity, ResourceType.STAMINA, -DAMAGE_STAMINA_AMOUNT * magnitude);
            case RAVAGE_STAMINA -> {
                addAttributeModifier(entity, ModAttributes.STAMINA, "ravage_stamina",
                        RAVAGE_RESOURCE_AMOUNT * magnitude, AttributeModifier.Operation.ADDITION, ravageDuration);
                clampResourceAfterAttributeChange(entity, ModAttributes.STAMINA.get());
            }
            case LINGERING_DAMAGE_STAMINA ->
                    addResourceEffect(entity, ResourceType.STAMINA, "lingering_damage_stamina",
                            LINGERING_DAMAGE_PER_SECOND * magnitude, lingeringDuration);
            case DAMAGE_STAMINA_REGENERATION ->
                    addAttributeModifier(entity, ModAttributes.STAMINA_REGENERATION, "damage_stamina_regeneration",
                            DAMAGE_REGENERATION_MULTIPLIER * magnitude, AttributeModifier.Operation.MULTIPLY_TOTAL,
                            damageRegenerationDuration);
            case DAMAGE_MAGICKA -> applyResourceChange(entity, ResourceType.MAGICKA, -DAMAGE_RESOURCE_AMOUNT * magnitude);
            case DAMAGE_MAGICKA_REGENERATION ->
                    addAttributeModifier(entity, AttributeRegistry.MANA_REGEN, "damage_magicka_regeneration",
                            DAMAGE_REGENERATION_MULTIPLIER * magnitude, AttributeModifier.Operation.MULTIPLY_TOTAL,
                            damageRegenerationDuration);
            case RESIST_FIRE -> addEffect(entity, MobEffects.FIRE_RESISTANCE, duration);
            case RESIST_FROST -> addEffect(entity, ModEffects.FROST_RESISTANCE.get(), duration);
            case RESIST_MAGIC -> addEffect(entity, ModEffects.MAGIC_RESISTANCE.get(), duration);
            case RESIST_POISON -> addEffect(entity, ModEffects.POISON_RESISTANCE.get(), duration);
            case RESIST_SHOCK -> addEffect(entity, ModEffects.SHOCK_RESISTANCE.get(), duration);
            case WEAKNESS_TO_FIRE -> addEffect(entity, ModEffects.FIRE_WEAKNESS.get(), duration);
            case WEAKNESS_TO_FROST -> addEffect(entity, ModEffects.FROST_WEAKNESS.get(), duration);
            case WEAKNESS_TO_MAGIC -> addEffect(entity, ModEffects.MAGIC_WEAKNESS.get(), duration);
            case WEAKNESS_TO_POISON -> addEffect(entity, ModEffects.POISON_WEAKNESS.get(), duration);
            case WEAKNESS_TO_SHOCK -> addEffect(entity, ModEffects.SHOCK_WEAKNESS.get(), duration);
            case INVISIBILITY -> addEffect(entity, MobEffects.INVISIBILITY, duration);
            case LIGHT -> {
                addEffect(entity, ModEffects.LIGHT.get(), duration);
                addEffect(entity, MobEffects.GLOWING, duration);
            }
            case NIGHT_EYE -> addEffect(entity, MobEffects.NIGHT_VISION, duration);
            case PARALYSIS -> addEffect(entity, ModEffects.PARALYSIS.get(), duration);
            case SLOW -> addEffect(entity, MobEffects.MOVEMENT_SLOWDOWN, duration);
            case WATERBREATHING -> addEffect(entity, MobEffects.WATER_BREATHING, duration);
            case CURE_DISEASE -> cureDiseaseLikeEffects(entity);
            case FORTIFY_LOCKPICKING ->
                    addAttributeModifier(entity, ModAttributes.LOCKPICKING, "fortify_lockpicking",
                            SKILL_ATTRIBUTE_BONUS * magnitude);
            case FORTIFY_ARCHERY ->
                    addAttributeModifier(entity, ModAttributes.ARCHERY, "fortify_archery",
                            SKILL_ATTRIBUTE_BONUS * magnitude);
        }
    }

    private static double getAlchemyMagnitudeMultiplier(LivingEntity entity, AlchemyEffect effect) {
        if (!(entity instanceof ServerPlayer player)) {
            return 1.0D;
        }

        double multiplier = 1.0D + SkillPerk.rank(player, SkillPerk.ALCHEMY_ALCHEMIST) * 0.2D;
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_PHYSICIAN) && isRestorativeEffect(effect)) {
            multiplier *= 1.25D;
        }
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_BENEFACTOR) && isBeneficialEffect(effect)) {
            multiplier *= 1.25D;
        }
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_POISONER) && isHarmfulEffect(effect)) {
            multiplier *= 1.25D;
        }
        return multiplier;
    }

    private static int getAlchemyDuration(LivingEntity entity, AlchemyEffect effect, int baseDuration) {
        if (!(entity instanceof ServerPlayer player)) {
            return baseDuration;
        }

        double multiplier = 1.0D;
        if (SkillPerk.rank(player, SkillPerk.ALCHEMY_ALCHEMIST) > 0) {
            multiplier += SkillPerk.rank(player, SkillPerk.ALCHEMY_ALCHEMIST) * 0.08D;
        }
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_BENEFACTOR) && isBeneficialEffect(effect)) {
            multiplier *= 1.15D;
        }
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_POISONER) && isHarmfulEffect(effect)) {
            multiplier *= 1.15D;
        }
        if (SkillPerk.has(player, SkillPerk.ALCHEMY_CONCENTRATED_POISON) && isHarmfulEffect(effect)) {
            multiplier *= 2.0D;
        }
        return Math.max(1, Mth.floor(baseDuration * multiplier));
    }

    private static List<AlchemyEffect> getIngredientEffects(String ingredientId, int count) {
        List<AlchemyEffect> effects = new ArrayList<>(getBaseIngredientEffects(ingredientId));
        if (effects.isEmpty() || count <= 0) {
            return List.of();
        }

        AlchemyEffect[] values = AlchemyEffect.values();
        int seed = ingredientId.hashCode();
        int targetCount = Math.min(4, Math.max(count, effects.size()));
        for (int offset = 1; effects.size() < targetCount && offset < values.length * 2; offset++) {
            AlchemyEffect candidate = values[Math.floorMod(seed + offset * 13, values.length)];
            if (!effects.contains(candidate)) {
                effects.add(candidate);
            }
        }
        return effects;
    }

    private static List<AlchemyEffect> getBaseIngredientEffects(String ingredientId) {
        AlchemyEffect primary = PRIMARY_EFFECTS.get(ingredientId);
        if (primary == null) {
            return List.of();
        }

        List<AlchemyEffect> effects = new ArrayList<>();
        effects.add(primary);
        for (AlchemyEffect effect : COUNTERPART_TRANSFERRED_EFFECTS.getOrDefault(ingredientId, List.of())) {
            if (!effects.contains(effect)) {
                effects.add(effect);
            }
        }
        return effects;
    }

    private static boolean isRestorativeEffect(AlchemyEffect effect) {
        return effect == AlchemyEffect.RESTORE_HEALTH
                || effect == AlchemyEffect.RESTORE_MAGICKA
                || effect == AlchemyEffect.RESTORE_STAMINA;
    }

    private static boolean isBeneficialEffect(AlchemyEffect effect) {
        return switch (effect) {
            case CURE_DISEASE, FORTIFY_ARCHERY, FORTIFY_LOCKPICKING, FORTIFY_MAGICKA, FORTIFY_STAMINA,
                    INVISIBILITY, LIGHT, NIGHT_EYE, REGENERATE_HEALTH, REGENERATE_MAGICKA, REGENERATE_STAMINA,
                    RESIST_FIRE, RESIST_FROST, RESIST_MAGIC, RESIST_POISON, RESIST_SHOCK, RESTORE_HEALTH,
                    RESTORE_MAGICKA, RESTORE_STAMINA, WATERBREATHING -> true;
            default -> false;
        };
    }

    private static boolean isHarmfulEffect(AlchemyEffect effect) {
        return !isBeneficialEffect(effect);
    }

    private static double getAlchemyIngredientExperience(AlchemyEffect effect) {
        return switch (effect) {
            case PARALYSIS, INVISIBILITY, RAVAGE_HEALTH, RAVAGE_STAMINA, LINGERING_DAMAGE_HEALTH,
                    LINGERING_DAMAGE_STAMINA -> 5.0D;
            case DAMAGE_HEALTH, DAMAGE_MAGICKA, DAMAGE_STAMINA, DAMAGE_MAGICKA_REGENERATION,
                    DAMAGE_STAMINA_REGENERATION -> 4.0D;
            case FORTIFY_ARCHERY, FORTIFY_LOCKPICKING, FORTIFY_MAGICKA, FORTIFY_STAMINA,
                    REGENERATE_HEALTH, REGENERATE_MAGICKA, REGENERATE_STAMINA -> 3.5D;
            default -> 3.0D;
        };
    }

    private static void addEffect(LivingEntity entity, MobEffect effect) {
        entity.addEffect(new MobEffectInstance(effect, INGREDIENT_EFFECT_DURATION_TICKS));
    }

    private static void addEffect(LivingEntity entity, MobEffect effect, int durationTicks) {
        entity.addEffect(new MobEffectInstance(effect, durationTicks));
    }

    private static void addAttributeModifier(LivingEntity entity, RegistryObject<Attribute> attribute, String effectName, double amount) {
        addAttributeModifier(entity, attribute, effectName, amount, AttributeModifier.Operation.ADDITION,
                INGREDIENT_EFFECT_DURATION_TICKS);
    }

    private static void addAttributeModifier(LivingEntity entity, RegistryObject<Attribute> attribute, String effectName,
                                             double amount, AttributeModifier.Operation operation, int durationTicks) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute.get());
        if (attributeInstance == null) {
            return;
        }

        UUID modifierId = UUID.nameUUIDFromBytes(("thuumcraft:ingredient:" + effectName).getBytes(StandardCharsets.UTF_8));
        attributeInstance.removeModifier(modifierId);
        attributeInstance.addTransientModifier(new AttributeModifier(modifierId, "Ingredient " + effectName,
                amount, operation));

        List<TemporaryAttributeModifier> modifiers = ACTIVE_ATTRIBUTE_MODIFIERS.computeIfAbsent(entity.getUUID(),
                uuid -> new ArrayList<>());
        modifiers.removeIf(modifier -> modifier.modifierId().equals(modifierId));
        modifiers.add(new TemporaryAttributeModifier(attribute.get(), modifierId,
                entity.level().getGameTime() + durationTicks));
        clampResourceAfterAttributeChange(entity, attribute.get());
    }

    private static void applyResourceChange(LivingEntity entity, ResourceType resource, double amount) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        switch (resource) {
            case STAMINA -> StaminaEvents.addCurrentStamina(player, amount);
            case MAGICKA -> addMagicka(player, amount);
        }
    }

    private static void addMagicka(ServerPlayer player, double amount) {
        MagicData magicData = MagicData.getPlayerMagicData(player);
        float maxMana = Math.max(1.0F, (float) player.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
        magicData.setMana(Mth.clamp((float) (magicData.getMana() + amount), 0.0F, maxMana));
    }

    private static void addResourceEffect(LivingEntity entity, ResourceType resource, String effectName,
                                          double amountPerSecond, int durationTicks) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        UUID effectId = UUID.nameUUIDFromBytes(("thuumcraft:ingredient:" + effectName).getBytes(StandardCharsets.UTF_8));
        long gameTime = player.level().getGameTime();
        List<ActiveResourceEffect> effects = ACTIVE_RESOURCE_EFFECTS.computeIfAbsent(player.getUUID(),
                uuid -> new ArrayList<>());
        effects.removeIf(effect -> effect.effectId.equals(effectId));
        effects.add(new ActiveResourceEffect(resource, effectId, amountPerSecond, gameTime + 20L,
                gameTime + durationTicks));
    }

    private static void clampResourceAfterAttributeChange(LivingEntity entity, Attribute attribute) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        if (attribute == ModAttributes.STAMINA.get()) {
            StaminaEvents.clampCurrentStamina(player);
        } else if (attribute == AttributeRegistry.MAX_MANA.get()) {
            addMagicka(player, 0.0D);
        }
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

    private enum ResourceType {
        STAMINA,
        MAGICKA
    }

    private static final class ActiveResourceEffect {
        private final ResourceType resource;
        private final UUID effectId;
        private final double amountPerSecond;
        private long nextApplicationTick;
        private final long expiresAt;

        private ActiveResourceEffect(ResourceType resource, UUID effectId, double amountPerSecond,
                                     long nextApplicationTick, long expiresAt) {
            this.resource = resource;
            this.effectId = effectId;
            this.amountPerSecond = amountPerSecond;
            this.nextApplicationTick = nextApplicationTick;
            this.expiresAt = expiresAt;
        }
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
        FORTIFY_MAGICKA,
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
