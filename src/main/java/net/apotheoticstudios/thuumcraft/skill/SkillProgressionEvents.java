package net.apotheoticstudios.thuumcraft.skill;

import com.google.common.collect.Multimap;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SkillProgressionEvents {
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private static final double WEAPON_XP_PER_DAMAGE = 1.0D;
    private static final double ARMOR_XP_PER_DAMAGE = 0.9D;
    private static final double BLOCK_XP_PER_DAMAGE = 1.1D;
    private static final double SMITHING_CRAFT_XP_MULTIPLIER = 1.35D;
    private static final double SMITHING_REPAIR_XP_MULTIPLIER = 1.0D;
    private static final double BARTER_TRADE_XP_MULTIPLIER = 0.8D;

    private SkillProgressionEvents() {
    }

    @SubscribeEvent
    public static void applySkillProgression(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            SkillProgression.applyAll(player);
        }
    }

    @SubscribeEvent
    public static void copySkillProgression(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            SkillProgression.copyAll(event.getOriginal(), player);
        }
    }

    @SubscribeEvent
    public static void awardWeaponExperience(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide() || event.getAmount() <= 0.0F) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof net.minecraft.server.level.ServerPlayer player)
                || event.getEntity() == player
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        SkillProgression.Skill skill = getWeaponSkill(player, event.getSource().is(DamageTypeTags.IS_PROJECTILE));
        if (skill != null) {
            SkillProgression.award(player, skill, event.getAmount() * WEAPON_XP_PER_DAMAGE);
        }
    }

    @SubscribeEvent
    public static void awardArmorExperience(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide()
                || event.getAmount() <= 0.0F
                || !(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player)
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        int lightPieces = 0;
        int heavyPieces = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.is(ModTags.Items.LIGHT_ARMOR)) {
                lightPieces++;
            } else if (stack.is(ModTags.Items.HEAVY_ARMOR)) {
                heavyPieces++;
            }
        }

        int totalPieces = lightPieces + heavyPieces;
        if (totalPieces <= 0) {
            return;
        }

        double baseExperience = event.getAmount() * ARMOR_XP_PER_DAMAGE;
        if (lightPieces > 0) {
            SkillProgression.award(player, SkillProgression.Skill.LIGHT_ARMOR,
                    baseExperience * lightPieces / totalPieces);
        }
        if (heavyPieces > 0) {
            SkillProgression.award(player, SkillProgression.Skill.HEAVY_ARMOR,
                    baseExperience * heavyPieces / totalPieces);
        }
    }

    @SubscribeEvent
    public static void awardBlockExperience(ShieldBlockEvent event) {
        if (event.getEntity().level().isClientSide()
                || event.getBlockedDamage() <= 0.0F
                || !(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player)
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        SkillProgression.award(player, SkillProgression.Skill.BLOCK,
                event.getBlockedDamage() * BLOCK_XP_PER_DAMAGE);
    }

    @SubscribeEvent
    public static void awardCraftingExperience(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            awardSmithingExperience(player, event.getCrafting(), SMITHING_CRAFT_XP_MULTIPLIER);
        }
    }

    @SubscribeEvent
    public static void awardRepairExperience(AnvilRepairEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            awardSmithingExperience(player, event.getOutput(), SMITHING_REPAIR_XP_MULTIPLIER);
        }
    }

    @SubscribeEvent
    public static void awardBarterExperience(TradeWithVillagerEvent event) {
        if (!(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player)
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        MerchantOffer offer = event.getMerchantOffer();
        double tradeValue = getItemValue(offer.getBaseCostA()) + getItemValue(offer.getCostB()) + getItemValue(offer.getResult());
        double experience = Math.max(1.0D, Math.sqrt(Math.max(1.0D, tradeValue)) * BARTER_TRADE_XP_MULTIPLIER
                + offer.getXp() * 0.25D);
        SkillProgression.award(player, SkillProgression.Skill.BARTER, experience);
    }

    private static SkillProgression.Skill getWeaponSkill(net.minecraft.server.level.ServerPlayer player,
                                                         boolean projectileDamage) {
        ItemStack weapon = player.getMainHandItem();
        if (projectileDamage) {
            ItemStack offhand = player.getOffhandItem();
            return weapon.is(ModTags.Items.RANGED_WEAPONS)
                    || offhand.is(ModTags.Items.RANGED_WEAPONS)
                    || weapon.isEmpty()
                    ? SkillProgression.Skill.ARCHERY
                    : null;
        }
        if (weapon.is(ModTags.Items.ONE_HANDED_WEAPONS)) {
            return SkillProgression.Skill.ONE_HANDED;
        }
        if (weapon.is(ModTags.Items.TWO_HANDED_WEAPONS)) {
            return SkillProgression.Skill.TWO_HANDED;
        }
        return null;
    }

    private static void awardSmithingExperience(net.minecraft.server.level.ServerPlayer player, ItemStack stack,
                                                double multiplier) {
        if (player.isCreative() || player.isSpectator() || !isSmithingRelevant(stack)) {
            return;
        }

        double itemValue = getItemValue(stack);
        double experience = Math.max(1.0D, Math.sqrt(Math.max(1.0D, itemValue)) * multiplier);
        SkillProgression.award(player, SkillProgression.Skill.SMITHING, experience);
    }

    private static boolean isSmithingRelevant(ItemStack stack) {
        return !stack.isEmpty()
                && (isTaggedWeaponOrArmor(stack)
                || stack.getItem() instanceof ArmorItem
                || getItemModifierValue(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE) > 0.0D);
    }

    private static boolean isTaggedWeaponOrArmor(ItemStack stack) {
        return stack.is(ModTags.Items.ONE_HANDED_WEAPONS)
                || stack.is(ModTags.Items.TWO_HANDED_WEAPONS)
                || stack.is(ModTags.Items.RANGED_WEAPONS)
                || stack.is(ModTags.Items.LIGHT_ARMOR)
                || stack.is(ModTags.Items.HEAVY_ARMOR);
    }

    private static double getItemValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0D;
        }

        double value = getRarityValue(stack.getRarity());
        value += Math.sqrt(Math.max(0, stack.getMaxDamage())) * 0.08D;
        value += getItemModifierValue(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE) * 2.0D;
        value += getItemModifierValue(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_SPEED);
        value += getBestArmorModifierValue(stack, Attributes.ARMOR) * 2.0D;
        value += getBestArmorModifierValue(stack, Attributes.ARMOR_TOUGHNESS) * 3.0D;
        if (isMetalOrSmithingMaterial(stack)) {
            value += 6.0D;
        }
        return Math.max(1.0D, value) * stack.getCount();
    }

    private static double getRarityValue(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 1.0D;
            case UNCOMMON -> 3.0D;
            case RARE -> 8.0D;
            case EPIC -> 16.0D;
        };
    }

    private static double getBestArmorModifierValue(ItemStack stack, Attribute attribute) {
        double value = 0.0D;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            value = Math.max(value, getItemModifierValue(stack, slot, attribute));
        }
        return value;
    }

    private static double getItemModifierValue(ItemStack stack, EquipmentSlot slot, Attribute attribute) {
        double value = 0.0D;
        Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
        for (AttributeModifier modifier : modifiers.get(attribute)) {
            value += Math.max(0.0D, modifier.getAmount());
        }
        return value;
    }

    private static boolean isMetalOrSmithingMaterial(ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return false;
        }

        String path = itemId.getPath();
        return path.contains("ingot")
                || path.contains("nugget")
                || path.contains("ore")
                || path.contains("raw_")
                || path.contains("steel")
                || path.contains("silver")
                || path.contains("dwarven")
                || path.contains("malachite")
                || path.contains("ebony")
                || path.contains("moonstone")
                || path.contains("orichalcum")
                || path.contains("corundum")
                || path.contains("quicksilver");
    }
}
