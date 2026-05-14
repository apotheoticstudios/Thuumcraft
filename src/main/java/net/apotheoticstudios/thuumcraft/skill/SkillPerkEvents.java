package net.apotheoticstudios.thuumcraft.skill;

import com.google.common.collect.Multimap;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.compat.EpicFightCompat;
import net.apotheoticstudios.thuumcraft.compat.EpicFightSkillIntegration;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.stamina.StaminaEvents;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class SkillPerkEvents {
    private static final String SMITHING_QUALITY_TAG = "ThuumcraftSmithingQuality";
    private static final double SWORD_CRIT_DAMAGE_MULTIPLIER = 1.5D;
    private static final double ARCHERY_CRIT_DAMAGE_MULTIPLIER = 1.5D;
    private static final int ARCHERY_CRITICAL_SHOT_CRIT_PARTICLES = 18;
    private static final int ARCHERY_CRITICAL_SHOT_ENCHANTED_PARTICLES = 8;
    private static final int ARCHERY_POWER_SHOT_POOF_PARTICLES = 16;
    private static final int ARCHERY_POWER_SHOT_CLOUD_PARTICLES = 8;
    private static final double EAGLE_EYE_STAMINA_COST_PER_TICK = 0.08D;
    private static final double POWER_ATTACK_STAMINA_COST = 12.0D;
    private static final UUID ARMOR_BONUS_MODIFIER = UUID.fromString("f1fb1334-1098-4ed2-bcdd-803448596ff8");
    private static final UUID ATTACK_SPEED_BONUS_MODIFIER = UUID.fromString("91185af2-b84f-4ca5-b8b4-e1c34d1b334f");
    private static final UUID BLOCK_RUNNER_MODIFIER = UUID.fromString("4c915d16-8d9a-47d7-95f0-bc1139130c79");
    private static final UUID RANGER_MODIFIER = UUID.fromString("36170d64-015f-4ea7-811b-c3c8ee073efb");
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };
    private static final Map<UUID, RuntimeState> RUNTIME = new HashMap<>();
    private static final Map<UUID, BleedState> BLEEDS = new HashMap<>();
    private static boolean applyingSecondaryDamage;

    private SkillPerkEvents() {
    }

    public static void setEagleEyeZooming(ServerPlayer player, boolean zooming) {
        if (!zooming) {
            RuntimeState runtime = RUNTIME.get(player.getUUID());
            if (runtime != null) {
                runtime.eagleEyeZooming = false;
            }
            return;
        }

        RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new RuntimeState()).eagleEyeZooming = true;
    }

    @SubscribeEvent
    public static void tickPlayerPerks(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        if (!SkillPerk.isSystemEnabled() || player.isSpectator() || player.isCreative() || !player.isAlive()) {
            removeModifier(player, Attributes.ARMOR, ARMOR_BONUS_MODIFIER);
            removeModifier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_BONUS_MODIFIER);
            removeModifier(player, Attributes.MOVEMENT_SPEED, BLOCK_RUNNER_MODIFIER);
            removeModifier(player, Attributes.MOVEMENT_SPEED, RANGER_MODIFIER);
            RUNTIME.remove(player.getUUID());
            return;
        }
        if (EpicFightCompat.isLoaded()) {
            EpicFightSkillIntegration.ensureRegistered(player);
        }

        RuntimeState runtime = RUNTIME.computeIfAbsent(player.getUUID(), ignored -> new RuntimeState());
        applyArmorRatingPerks(player);
        applyCombatMovementPerks(player);
        tickShieldCharge(player, runtime);
    }

    @SubscribeEvent
    public static void tickBleeds(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide() || BLEEDS.isEmpty()) {
            return;
        }
        if (!SkillPerk.isSystemEnabled()) {
            BLEEDS.clear();
            return;
        }

        Iterator<Map.Entry<UUID, BleedState>> iterator = BLEEDS.entrySet().iterator();
        while (iterator.hasNext()) {
            BleedState bleed = iterator.next().getValue();
            if (!event.level.dimension().equals(bleed.dimension)) {
                continue;
            }
            LivingEntity target = (LivingEntity) ((ServerLevel) event.level).getEntity(bleed.targetId);
            if (target == null || !target.isAlive() || event.level.getGameTime() > bleed.expiresAt) {
                iterator.remove();
                continue;
            }

            if (event.level.getGameTime() >= bleed.nextTick) {
                applyingSecondaryDamage = true;
                target.hurt(target.damageSources().generic(), (float) bleed.damagePerSecond);
                applyingSecondaryDamage = false;
                bleed.nextTick += 20L;
            }
        }
    }

    @SubscribeEvent
    public static void clearRuntime(PlayerEvent.PlayerLoggedOutEvent event) {
        RUNTIME.remove(event.getEntity().getUUID());
        if (event.getEntity() instanceof ServerPlayer player && EpicFightCompat.isLoaded()) {
            EpicFightSkillIntegration.clear(player);
        }
    }

    @SubscribeEvent
    public static void applyWeaponPerkDamage(LivingHurtEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getEntity().level().isClientSide()
                || applyingSecondaryDamage
                || EpicFightCompat.isApplyingSecondaryDamage()
                || EpicFightCompat.isEpicFightDamageSource(event.getSource())
                || event.getAmount() <= 0.0F) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer player) || event.getEntity() == player) {
            return;
        }

        boolean projectile = event.getSource().is(DamageTypeTags.IS_PROJECTILE);
        if (projectile) {
            applyArcheryDamage(player, event);
            return;
        }

        ItemStack weapon = player.getMainHandItem();
        if (weapon.is(ModTags.Items.ONE_HANDED_WEAPONS)) {
            applyOneHandedDamage(player, event, weapon);
        } else if (weapon.is(ModTags.Items.TWO_HANDED_WEAPONS)) {
            applyTwoHandedDamage(player, event, weapon);
        } else if (weapon.isEmpty() && SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_FISTS_OF_STEEL)) {
            event.setAmount((float) (event.getAmount() + getGauntletArmor(player)));
        }
    }

    @SubscribeEvent
    public static void applyDefensivePerks(LivingHurtEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)
                || event.getAmount() <= 0.0F) {
            return;
        }

        if (SkillPerk.has(player, SkillPerk.LIGHT_ARMOR_DEFT_MOVEMENT)
                && isWearingOnlyTaggedArmor(player, ModTags.Items.LIGHT_ARMOR)
                && isMeleeDamage(event.getSource())
                && player.getRandom().nextFloat() < 0.10F) {
            event.setAmount(0.0F);
            return;
        }

        if (SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_REFLECT_BLOWS)
                && isWearingOnlyTaggedArmor(player, ModTags.Items.HEAVY_ARMOR)
                && isMeleeDamage(event.getSource())
                && event.getSource().getEntity() instanceof LivingEntity attacker
                && player.getRandom().nextFloat() < 0.10F) {
            applyingSecondaryDamage = true;
            attacker.hurt(player.damageSources().thorns(player), event.getAmount());
            applyingSecondaryDamage = false;
        }
    }

    @SubscribeEvent
    public static void applyBlockPerks(ShieldBlockEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        int shieldWallRank = SkillPerk.rank(player, SkillPerk.BLOCK_SHIELD_WALL);
        if (shieldWallRank > 0 && event.getBlockedDamage() < event.getOriginalBlockedDamage()) {
            double[] values = {0.20D, 0.25D, 0.30D, 0.35D, 0.40D};
            float extra = (float) (event.getOriginalBlockedDamage() * values[Math.min(shieldWallRank, values.length) - 1]);
            event.setBlockedDamage(Math.min(event.getOriginalBlockedDamage(), event.getBlockedDamage() + extra));
        }

        if (SkillPerk.has(player, SkillPerk.BLOCK_DEFLECT_ARROWS)
                && event.getDamageSource().is(DamageTypeTags.IS_PROJECTILE)) {
            event.setBlockedDamage(event.getOriginalBlockedDamage());
            event.setShieldTakesDamage(false);
        }

        if (SkillPerk.has(player, SkillPerk.BLOCK_ELEMENTAL_PROTECTION)
                && isElementalDamage(event.getDamageSource())) {
            event.setBlockedDamage(Math.min(event.getOriginalBlockedDamage(),
                    event.getBlockedDamage() + event.getOriginalBlockedDamage() * 0.5F));
        }

        if (SkillPerk.has(player, SkillPerk.BLOCK_QUICK_REFLEXES)
                && event.getDamageSource().getEntity() instanceof LivingEntity attacker) {
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 2));
        }
    }

    @SubscribeEvent
    public static void applyBashPerks(AttackEntityEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)
                || !(event.getTarget() instanceof LivingEntity target)
                || !isUsingShield(player)
                || !SkillPerk.has(player, SkillPerk.BLOCK_POWER_BASH)
                || !StaminaEvents.tryConsumeCurrentStamina(player, 8.0D)) {
            return;
        }

        float damage = SkillPerk.has(player, SkillPerk.BLOCK_DEADLY_BASH) ? 5.0F : 1.0F;
        target.hurt(player.damageSources().playerAttack(player), damage);
        target.knockback(0.65D, player.getX() - target.getX(), player.getZ() - target.getZ());
        if (SkillPerk.has(player, SkillPerk.BLOCK_DISARMING_BASH) && target instanceof Mob mob
                && player.getRandom().nextFloat() < 0.35F) {
            ItemStack held = mob.getMainHandItem();
            if (!held.isEmpty()) {
                mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                mob.spawnAtLocation(held.copy());
            }
        }
    }

    @SubscribeEvent
    public static void applyFallPerks(LivingFallEvent event) {
        if (SkillPerk.isSystemEnabled()
                && event.getEntity() instanceof ServerPlayer player
                && SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_CUSHIONED)
                && isWearingOnlyTaggedArmor(player, ModTags.Items.HEAVY_ARMOR)) {
            event.setDamageMultiplier(event.getDamageMultiplier() * 0.5F);
        }
    }

    @SubscribeEvent
    public static void applyStaggerResistance(LivingKnockBackEvent event) {
        if (SkillPerk.isSystemEnabled()
                && event.getEntity() instanceof ServerPlayer player
                && SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_TOWER_OF_STRENGTH)
                && isWearingOnlyTaggedArmor(player, ModTags.Items.HEAVY_ARMOR)) {
            event.setStrength(event.getStrength() * 0.5F);
        }
    }

    @SubscribeEvent
    public static void applyQuickShot(ArrowLooseEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || !(event.getEntity() instanceof ServerPlayer player)
                || !SkillPerk.has(player, SkillPerk.ARCHERY_QUICK_SHOT)
                || !event.hasAmmo()) {
            return;
        }

        event.setCharge(Math.round(event.getCharge() * 1.3F));
    }

    @SubscribeEvent
    public static void duplicateGreenThumbHarvest(BlockEvent.BreakEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getLevel().isClientSide()
                || !(event.getLevel() instanceof ServerLevel level)
                || !(event.getPlayer() instanceof ServerPlayer player)
                || !SkillPerk.has(player, SkillPerk.ALCHEMY_GREEN_THUMB)
                || player.isCreative()
                || player.isSpectator()) {
            return;
        }

        BlockState state = event.getState();
        Block block = state.getBlock();
        if (!(block instanceof BushBlock || block instanceof FlowerBlock || isMatureCrop(state))) {
            return;
        }

        BlockPos pos = event.getPos();
        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player);
        for (ItemStack drop : state.getDrops(params)) {
            if (!drop.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D,
                        pos.getZ() + 0.5D, drop.copy()));
            }
        }
    }

    @SubscribeEvent
    public static void applyHuntersDiscipline(LivingDropsEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getEntity().level().isClientSide()
                || !event.getSource().is(DamageTypeTags.IS_PROJECTILE)
                || !(event.getSource().getEntity() instanceof ServerPlayer player)
                || !SkillPerk.has(player, SkillPerk.ARCHERY_HUNTERS_DISCIPLINE)) {
            return;
        }

        boolean duplicatedArrow = false;
        for (ItemEntity drop : List.copyOf(event.getDrops())) {
            ItemStack stack = drop.getItem();
            if (stack.getItem() instanceof ArrowItem) {
                event.getDrops().add(new ItemEntity(event.getEntity().level(), drop.getX(), drop.getY(), drop.getZ(),
                        stack.copy()));
                duplicatedArrow = true;
            }
        }

        if (!duplicatedArrow && player.getRandom().nextFloat() < 0.5F) {
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
                    event.getEntity().getY() + 0.35D, event.getEntity().getZ(), new ItemStack(net.minecraft.world.item.Items.ARROW)));
        }
    }

    @SubscribeEvent
    public static void tagCraftedSmithingQuality(PlayerEvent.ItemCraftedEvent event) {
        if (SkillPerk.isSystemEnabled() && event.getEntity() instanceof ServerPlayer player) {
            applySmithingQuality(player, event.getCrafting());
        }
    }

    @SubscribeEvent
    public static void tagRepairedSmithingQuality(AnvilRepairEvent event) {
        if (SkillPerk.isSystemEnabled() && event.getEntity() instanceof ServerPlayer player) {
            applySmithingQuality(player, event.getOutput());
        }
    }

    @SubscribeEvent
    public static void applySmithingQualityAttributes(ItemAttributeModifierEvent event) {
        if (!SkillPerk.isSystemEnabled()) {
            return;
        }
        int quality = event.getItemStack().getOrCreateTag().getInt(SMITHING_QUALITY_TAG);
        if (quality <= 0) {
            return;
        }

        EquipmentSlot slot = event.getSlotType();
        ItemStack stack = event.getItemStack();
        double multiplier = quality >= 2 ? 0.18D : 0.09D;
        if (slot == EquipmentSlot.MAINHAND) {
            double damage = getItemModifierValue(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE);
            if (damage > 0.0D) {
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        qualityModifierId(stack, "damage"), "Thuumcraft smithing quality", damage * multiplier,
                        AttributeModifier.Operation.ADDITION));
            }
        } else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            double armor = getItemModifierValue(stack, slot, Attributes.ARMOR);
            if (armor > 0.0D) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(
                        qualityModifierId(stack, "armor_" + slot.getName()), "Thuumcraft smithing quality",
                        armor * multiplier, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @SubscribeEvent
    public static void applyMerchantDiscounts(PlayerInteractEvent.EntityInteract event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getLevel().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)
                || !(event.getTarget() instanceof AbstractVillager villager)) {
            return;
        }

        applyBarterDiscounts(player, villager);
    }

    @SubscribeEvent
    public static void awardSpeechTradeBonuses(TradeWithVillagerEvent event) {
        if (!SkillPerk.isSystemEnabled() || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (SkillPerk.has(player, SkillPerk.BARTER_MASTER_TRADER)) {
            player.giveExperiencePoints(2);
        }
        if (SkillPerk.has(player, SkillPerk.BARTER_INVESTOR)) {
            event.getMerchantOffer().addToSpecialPriceDiff(-1);
        }
    }

    private static void applyArcheryDamage(ServerPlayer player, LivingHurtEvent event) {
        int overdraw = SkillPerk.rank(player, SkillPerk.ARCHERY_OVERDRAW);
        if (overdraw > 0) {
            event.setAmount((float) (event.getAmount() * (1.0D + overdraw * 0.2D)));
        }

        LivingEntity target = event.getEntity();
        int criticalShot = SkillPerk.rank(player, SkillPerk.ARCHERY_CRITICAL_SHOT);
        if (criticalShot > 0) {
            double chance = switch (criticalShot) {
                case 1 -> 0.10D;
                case 2 -> 0.15D;
                default -> 0.20D;
            };
            if (player.getRandom().nextDouble() < chance) {
                double rankMultiplier = switch (criticalShot) {
                    case 1 -> 1.0D;
                    case 2 -> 1.25D;
                    default -> 1.5D;
                };
                event.setAmount((float) (event.getAmount() * ARCHERY_CRIT_DAMAGE_MULTIPLIER * rankMultiplier));
                showArcheryCriticalShotIndicator(player, target, ARCHERY_CRIT_DAMAGE_MULTIPLIER * rankMultiplier);
            }
        }

        if (SkillPerk.has(player, SkillPerk.ARCHERY_POWER_SHOT) && player.getRandom().nextBoolean()) {
            target.knockback(0.55D, player.getX() - target.getX(), player.getZ() - target.getZ());
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            showArcheryPowerShotIndicator(player, target);
        }
        if (SkillPerk.has(player, SkillPerk.ARCHERY_BULLSEYE) && player.getRandom().nextFloat() < 0.15F) {
            target.addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60));
        }
    }

    private static void showArcheryCriticalShotIndicator(ServerPlayer player, LivingEntity target,
                                                         double damageMultiplier) {
        ServerLevel level = player.serverLevel();
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.55D;
        double z = target.getZ();
        double horizontalSpread = Math.max(0.25D, target.getBbWidth() * 0.45D);
        double verticalSpread = Math.max(0.35D, target.getBbHeight() * 0.25D);

        level.sendParticles(ParticleTypes.CRIT, x, y, z, ARCHERY_CRITICAL_SHOT_CRIT_PARTICLES,
                horizontalSpread, verticalSpread, horizontalSpread, 0.18D);
        level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, ARCHERY_CRITICAL_SHOT_ENCHANTED_PARTICLES,
                horizontalSpread, verticalSpread, horizontalSpread, 0.12D);
        player.displayClientMessage(Component.literal("Critical Shot ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal(formatMultiplier(damageMultiplier) + "x")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal(" damage").withStyle(ChatFormatting.GOLD)), true);
    }

    private static void showArcheryPowerShotIndicator(ServerPlayer player, LivingEntity target) {
        ServerLevel level = player.serverLevel();
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.5D;
        double z = target.getZ();
        double horizontalSpread = Math.max(0.3D, target.getBbWidth() * 0.55D);
        double verticalSpread = Math.max(0.25D, target.getBbHeight() * 0.2D);

        level.sendParticles(ParticleTypes.POOF, x, y, z, ARCHERY_POWER_SHOT_POOF_PARTICLES,
                horizontalSpread, verticalSpread, horizontalSpread, 0.08D);
        level.sendParticles(ParticleTypes.CLOUD, x, y, z, ARCHERY_POWER_SHOT_CLOUD_PARTICLES,
                horizontalSpread * 0.7D, verticalSpread * 0.6D, horizontalSpread * 0.7D, 0.03D);
        player.displayClientMessage(Component.literal("Power Shot")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
    }

    private static String formatMultiplier(double multiplier) {
        if (multiplier == Math.rint(multiplier)) {
            return Integer.toString((int) multiplier);
        }
        return String.format(Locale.ROOT, "%.2f", multiplier).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private static void applyOneHandedDamage(ServerPlayer player, LivingHurtEvent event, ItemStack weapon) {
        int armsman = SkillPerk.rank(player, SkillPerk.ONE_HANDED_ARMSMAN);
        if (armsman > 0) {
            event.setAmount((float) (event.getAmount() * (1.0D + armsman * 0.2D)));
        }

        if (isSword(weapon)) {
            applyCriticalPerk(player, event, SkillPerk.rank(player, SkillPerk.ONE_HANDED_BLADESMAN));
        } else if (isAxe(weapon)) {
            applyBleed(event.getEntity(), SkillPerk.rank(player, SkillPerk.ONE_HANDED_HACK_AND_SLASH),
                    0.5D, 4);
        } else if (isMace(weapon)) {
            event.setAmount(applyArmorIgnore(event.getEntity(), event.getAmount(),
                    SkillPerk.rank(player, SkillPerk.ONE_HANDED_BONE_BREAKER)));
        }

        if (isFullStrengthAttack(player)) {
            boolean consumedPowerAttack = false;
            boolean chargingPowerAttack = isCharging(player)
                    && SkillPerk.has(player, SkillPerk.ONE_HANDED_CRITICAL_CHARGE);
            boolean standingPowerAttack = player.onGround()
                    && SkillPerk.has(player, SkillPerk.ONE_HANDED_SAVAGE_STRIKE);
            boolean backwardPowerAttack = isMovingBackward(player)
                    && SkillPerk.has(player, SkillPerk.ONE_HANDED_PARALYZING_STRIKE);
            if ((chargingPowerAttack || standingPowerAttack || backwardPowerAttack)
                    && consumePowerAttackStamina(player, SkillPerk.has(player, SkillPerk.ONE_HANDED_FIGHTING_STANCE))) {
                consumedPowerAttack = true;
                if (chargingPowerAttack) {
                    event.setAmount(event.getAmount() * 2.0F);
                } else if (standingPowerAttack) {
                    event.setAmount(event.getAmount() * 1.25F);
                }
                if (backwardPowerAttack && player.getRandom().nextFloat() < 0.25F) {
                    event.getEntity().addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60));
                }
            }

            if (isDualWielding(player, ModTags.Items.ONE_HANDED_WEAPONS)
                    && SkillPerk.has(player, SkillPerk.ONE_HANDED_DUAL_SAVAGERY)
                    && (consumedPowerAttack || consumePowerAttackStamina(player,
                    SkillPerk.has(player, SkillPerk.ONE_HANDED_FIGHTING_STANCE)))) {
                event.setAmount(event.getAmount() * 1.5F);
            }
        }
    }

    private static void applyTwoHandedDamage(ServerPlayer player, LivingHurtEvent event, ItemStack weapon) {
        int barbarian = SkillPerk.rank(player, SkillPerk.TWO_HANDED_BARBARIAN);
        if (barbarian > 0) {
            event.setAmount((float) (event.getAmount() * (1.0D + barbarian * 0.2D)));
        }

        if (isGreatsword(weapon)) {
            applyCriticalPerk(player, event, SkillPerk.rank(player, SkillPerk.TWO_HANDED_DEEP_WOUNDS));
        } else if (isBattleaxe(weapon)) {
            applyBleed(event.getEntity(), SkillPerk.rank(player, SkillPerk.TWO_HANDED_LIMBSPLITTER),
                    0.75D, 4);
        } else if (isWarhammer(weapon)) {
            event.setAmount(applyArmorIgnore(event.getEntity(), event.getAmount(),
                    SkillPerk.rank(player, SkillPerk.TWO_HANDED_SKULLCRUSHER)));
        }

        if (isFullStrengthAttack(player)) {
            boolean chargingPowerAttack = isCharging(player)
                    && SkillPerk.has(player, SkillPerk.TWO_HANDED_GREAT_CRITICAL_CHARGE);
            boolean standingPowerAttack = player.onGround()
                    && SkillPerk.has(player, SkillPerk.TWO_HANDED_DEVASTATING_BLOW);
            boolean backwardPowerAttack = isMovingBackward(player)
                    && SkillPerk.has(player, SkillPerk.TWO_HANDED_WARMASTER);
            boolean sidewaysPowerAttack = isMovingSideways(player) && SkillPerk.has(player, SkillPerk.TWO_HANDED_SWEEP);
            if ((chargingPowerAttack || standingPowerAttack || backwardPowerAttack || sidewaysPowerAttack)
                    && consumePowerAttackStamina(player, SkillPerk.has(player, SkillPerk.TWO_HANDED_CHAMPIONS_STANCE))) {
                if (chargingPowerAttack) {
                    event.setAmount(event.getAmount() * 2.0F);
                } else if (standingPowerAttack) {
                    event.setAmount(event.getAmount() * 1.25F);
                }
                if (backwardPowerAttack && player.getRandom().nextFloat() < 0.25F) {
                    event.getEntity().addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60));
                }
                if (sidewaysPowerAttack) {
                    sweepNearbyTargets(player, event);
                }
            }
        }
    }

    private static void applyArmorRatingPerks(ServerPlayer player) {
        ArmorProfile profile = ArmorProfile.of(player);
        double bonus = 0.0D;
        int juggernaut = SkillPerk.rank(player, SkillPerk.HEAVY_ARMOR_JUGGERNAUT);
        if (juggernaut > 0 && profile.heavyArmor > 0.0D) {
            bonus += profile.heavyArmor * juggernaut * 0.2D;
        }
        if (profile.allHeavy && SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_WELL_FITTED)) {
            bonus += profile.heavyArmor * 0.25D;
        }
        if (profile.allHeavy && profile.matchingSet && SkillPerk.has(player, SkillPerk.HEAVY_ARMOR_MATCHING_SET)) {
            bonus += profile.heavyArmor * 0.25D;
        }

        int agileDefender = SkillPerk.rank(player, SkillPerk.LIGHT_ARMOR_AGILE_DEFENDER);
        if (agileDefender > 0 && profile.lightArmor > 0.0D) {
            bonus += profile.lightArmor * agileDefender * 0.2D;
        }
        if (profile.allLight && SkillPerk.has(player, SkillPerk.LIGHT_ARMOR_CUSTOM_FIT)) {
            bonus += profile.lightArmor * 0.25D;
        }
        if (profile.allLight && profile.matchingSet && SkillPerk.has(player, SkillPerk.LIGHT_ARMOR_MATCHING_SET)) {
            bonus += profile.lightArmor * 0.25D;
        }

        setModifier(player, Attributes.ARMOR, ARMOR_BONUS_MODIFIER, "Thuumcraft armor perk bonus", bonus,
                AttributeModifier.Operation.ADDITION);
    }

    private static void applyCombatMovementPerks(ServerPlayer player) {
        double attackSpeedBonus = 0.0D;
        int dualFlurry = SkillPerk.rank(player, SkillPerk.ONE_HANDED_DUAL_FLURRY);
        if (dualFlurry > 0 && isDualWielding(player, ModTags.Items.ONE_HANDED_WEAPONS)) {
            attackSpeedBonus = dualFlurry >= 2 ? 0.35D : 0.20D;
        }
        setModifier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_BONUS_MODIFIER, "Thuumcraft dual flurry",
                attackSpeedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL);

        double blockRunner = SkillPerk.has(player, SkillPerk.BLOCK_BLOCK_RUNNER) && isUsingShield(player) ? 0.18D : 0.0D;
        setModifier(player, Attributes.MOVEMENT_SPEED, BLOCK_RUNNER_MODIFIER, "Thuumcraft block runner",
                blockRunner, AttributeModifier.Operation.MULTIPLY_TOTAL);

        boolean usingRanged = player.isUsingItem() && isRangedWeapon(player.getUseItem());
        boolean eagleEyeZooming = usingRanged
                && RUNTIME.getOrDefault(player.getUUID(), RuntimeState.EMPTY).eagleEyeZooming
                && SkillPerk.has(player, SkillPerk.ARCHERY_EAGLE_EYE);
        if (eagleEyeZooming && !StaminaEvents.tryConsumeCurrentStamina(player, EAGLE_EYE_STAMINA_COST_PER_TICK)) {
            player.stopUsingItem();
            usingRanged = false;
            eagleEyeZooming = false;
            RuntimeState runtime = RUNTIME.get(player.getUUID());
            if (runtime != null) {
                runtime.eagleEyeZooming = false;
            }
        }
        int steadyHand = SkillPerk.rank(player, SkillPerk.ARCHERY_STEADY_HAND);
        if (eagleEyeZooming && steadyHand > 0 && player.tickCount % 5 == 0) {
            applySteadyHandSlow(player, steadyHand);
        }
        double ranger = SkillPerk.has(player, SkillPerk.ARCHERY_RANGER) && usingRanged ? 0.18D : 0.0D;
        setModifier(player, Attributes.MOVEMENT_SPEED, RANGER_MODIFIER, "Thuumcraft ranger",
                ranger, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private static void applySteadyHandSlow(ServerPlayer player, int rank) {
        Vec3 look = player.getLookAngle().normalize();
        double range = rank >= 2 ? 24.0D : 18.0D;
        int amplifier = rank >= 2 ? 1 : 0;
        AABB area = player.getBoundingBox().inflate(range);
        for (Mob target : player.serverLevel().getEntitiesOfClass(Mob.class, area,
                target -> target.isAlive() && player.hasLineOfSight(target))) {
            Vec3 toTarget = target.getEyePosition().subtract(player.getEyePosition());
            if (toTarget.lengthSqr() > 0.0001D && look.dot(toTarget.normalize()) > 0.35D) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8, amplifier, false, false));
            }
        }
    }

    private static void tickShieldCharge(ServerPlayer player, RuntimeState runtime) {
        if (!SkillPerk.has(player, SkillPerk.BLOCK_SHIELD_CHARGE)
                || !isUsingShield(player)
                || !player.isSprinting()
                || player.tickCount - runtime.lastShieldChargeTick < 16) {
            return;
        }

        Vec3 look = player.getLookAngle().normalize();
        List<LivingEntity> targets = player.serverLevel().getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(1.15D), target -> target != player && target.isAlive());
        for (LivingEntity target : targets) {
            Vec3 toTarget = target.position().subtract(player.position());
            if (toTarget.horizontalDistanceSqr() > 0.0001D && look.dot(toTarget.normalize()) > 0.25D) {
                runtime.lastShieldChargeTick = player.tickCount;
                target.hurt(player.damageSources().playerAttack(player), 2.0F);
                target.knockback(1.2D, player.getX() - target.getX(), player.getZ() - target.getZ());
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 35, 3));
            }
        }
    }

    private static void applyCriticalPerk(ServerPlayer player, LivingHurtEvent event, int rank) {
        if (rank <= 0) {
            return;
        }
        double chance = switch (rank) {
            case 1 -> 0.10D;
            case 2 -> 0.15D;
            default -> 0.20D;
        };
        if (player.getRandom().nextDouble() < chance) {
            event.setAmount((float) (event.getAmount() * SWORD_CRIT_DAMAGE_MULTIPLIER));
        }
    }

    private static float applyArmorIgnore(LivingEntity target, float amount, int rank) {
        if (rank <= 0) {
            return amount;
        }
        double ignore = switch (rank) {
            case 1 -> 0.25D;
            case 2 -> 0.50D;
            default -> 0.75D;
        };
        double armorReductionEstimate = target.getArmorValue() / (target.getArmorValue() + 20.0D);
        return (float) (amount * (1.0D + armorReductionEstimate * ignore));
    }

    public static void applyBleed(LivingEntity target, int rank, double baseDamagePerSecond, int seconds) {
        if (rank <= 0 || target.level().isClientSide()) {
            return;
        }
        double damage = baseDamagePerSecond * rank;
        long gameTime = target.level().getGameTime();
        BLEEDS.put(target.getUUID(), new BleedState(target.getUUID(), target.level().dimension(), damage, gameTime + 20L,
                gameTime + seconds * 20L));
    }

    private static void sweepNearbyTargets(ServerPlayer player, LivingHurtEvent event) {
        if (applyingSecondaryDamage) {
            return;
        }

        LivingEntity primary = event.getEntity();
        Vec3 look = player.getLookAngle().normalize();
        AABB area = primary.getBoundingBox().inflate(2.0D, 0.45D, 2.0D);
        applyingSecondaryDamage = true;
        for (LivingEntity target : player.serverLevel().getEntitiesOfClass(LivingEntity.class, area,
                target -> target != player && target != primary && target.isAlive() && player.hasLineOfSight(target))) {
            Vec3 toTarget = target.position().subtract(player.position());
            if (toTarget.horizontalDistanceSqr() > 0.0001D && look.dot(toTarget.normalize()) > 0.15D) {
                target.hurt(player.damageSources().playerAttack(player), event.getAmount() * 0.5F);
            }
        }
        applyingSecondaryDamage = false;
    }

    private static boolean consumePowerAttackStamina(ServerPlayer player, boolean reducedCost) {
        return StaminaEvents.tryConsumeCurrentStamina(player,
                POWER_ATTACK_STAMINA_COST * (reducedCost ? 0.75D : 1.0D));
    }

    private static void applySmithingQuality(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty() || !isSmithableEquipment(stack)) {
            return;
        }

        SkillPerk materialPerk = getSmithingMaterialPerk(stack);
        if (materialPerk == null || !SkillPerk.has(player, materialPerk)) {
            return;
        }

        int quality = stack.isEnchanted() && !SkillPerk.has(player, SkillPerk.SMITHING_ARCANE_BLACKSMITH) ? 1 : 2;
        stack.getOrCreateTag().putInt(SMITHING_QUALITY_TAG, Math.max(stack.getOrCreateTag().getInt(SMITHING_QUALITY_TAG), quality));
    }

    private static SkillPerk getSmithingMaterialPerk(ItemStack stack) {
        String path = itemPath(stack);
        if (path.contains("steel") || path.contains("iron")) return SkillPerk.SMITHING_STEEL_SMITHING;
        if (path.contains("elven") || path.contains("moonstone")) return SkillPerk.SMITHING_ELVEN_SMITHING;
        if (path.contains("glass") || path.contains("malachite")) return SkillPerk.SMITHING_GLASS_SMITHING;
        if (path.contains("dwarven")) return SkillPerk.SMITHING_DWARVEN_SMITHING;
        if (path.contains("orcish") || path.contains("orichalcum")) return SkillPerk.SMITHING_ORCISH_SMITHING;
        if (path.contains("ebony")) return SkillPerk.SMITHING_EBONY_SMITHING;
        if (path.contains("daedric")) return SkillPerk.SMITHING_DAEDRIC_SMITHING;
        if (path.contains("dragon")) return SkillPerk.SMITHING_DRAGON_ARMOR;
        if (path.contains("scaled") || path.contains("plate")) return SkillPerk.SMITHING_ADVANCED_ARMORS;
        return null;
    }

    private static void applyBarterDiscounts(ServerPlayer player, AbstractVillager villager) {
        int haggling = SkillPerk.rank(player, SkillPerk.BARTER_HAGGLING);
        double[] hagglingValues = {0.10D, 0.15D, 0.20D, 0.25D, 0.30D};
        double discount = haggling <= 0 ? 0.0D : hagglingValues[Math.min(haggling, hagglingValues.length) - 1];
        if (SkillPerk.has(player, SkillPerk.BARTER_ALLURE)) discount += 0.10D;
        if (SkillPerk.has(player, SkillPerk.BARTER_BRIBERY)) discount += 0.05D;
        if (SkillPerk.has(player, SkillPerk.BARTER_MERCHANT)) discount += 0.05D;
        if (SkillPerk.has(player, SkillPerk.BARTER_PERSUASION)) discount += 0.05D;
        if (SkillPerk.has(player, SkillPerk.BARTER_INTIMIDATION)) discount += 0.05D;
        if (SkillPerk.has(player, SkillPerk.BARTER_FENCE)) discount += 0.05D;
        if (SkillPerk.has(player, SkillPerk.BARTER_MASTER_TRADER)) discount += 0.15D;
        if (discount <= 0.0D) {
            return;
        }

        for (net.minecraft.world.item.trading.MerchantOffer offer : villager.getOffers()) {
            int baseCost = offer.getBaseCostA().getCount();
            int targetDiscount = Math.max(1, Mth.floor(baseCost * Mth.clamp(discount, 0.0D, 0.80D)));
            offer.setSpecialPriceDiff(Math.min(offer.getSpecialPriceDiff(), -targetDiscount));
        }
    }

    private static boolean isMatureCrop(BlockState state) {
        return state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state);
    }

    private static boolean isMeleeDamage(DamageSource source) {
        return source.getEntity() instanceof LivingEntity && !source.is(DamageTypeTags.IS_PROJECTILE);
    }

    private static boolean isElementalDamage(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.IS_FREEZING)
                || source.is(DamageTypeTags.IS_LIGHTNING)
                || source.is(DamageTypeTags.WITCH_RESISTANT_TO);
    }

    private static boolean isUsingShield(ServerPlayer player) {
        return player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem;
    }

    private static boolean isRangedWeapon(ItemStack stack) {
        return stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem
                || stack.is(ModTags.Items.RANGED_WEAPONS);
    }

    private static boolean isDualWielding(ServerPlayer player, TagKey<Item> tag) {
        return player.getMainHandItem().is(tag) && player.getOffhandItem().is(tag);
    }

    private static boolean isFullStrengthAttack(ServerPlayer player) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    private static boolean isCharging(ServerPlayer player) {
        Vec3 movement = player.getDeltaMovement();
        Vec3 look = player.getLookAngle();
        return player.isSprinting()
                || (movement.horizontalDistanceSqr() > 0.055D && horizontalDot(movement, look) > 0.035D);
    }

    private static boolean isMovingBackward(ServerPlayer player) {
        return horizontalDot(player.getDeltaMovement(), player.getLookAngle()) < -0.025D;
    }

    private static boolean isMovingSideways(ServerPlayer player) {
        Vec3 movement = player.getDeltaMovement();
        Vec3 look = player.getLookAngle();
        double side = movement.x * look.z - movement.z * look.x;
        return Math.abs(side) > 0.035D;
    }

    private static double horizontalDot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.z * b.z;
    }

    private static double getGauntletArmor(ServerPlayer player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        return chest.is(ModTags.Items.HEAVY_ARMOR) ? Math.max(1.0D, getArmorValue(chest, EquipmentSlot.CHEST) * 0.25D) : 0.0D;
    }

    private static boolean isWearingOnlyTaggedArmor(ServerPlayer player, TagKey<Item> tag) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty() || !stack.is(tag)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSmithableEquipment(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem
                || stack.is(ModTags.Items.ONE_HANDED_WEAPONS)
                || stack.is(ModTags.Items.TWO_HANDED_WEAPONS)
                || stack.is(ModTags.Items.RANGED_WEAPONS)
                || getItemModifierValue(stack, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE) > 0.0D;
    }

    private static void setModifier(LivingEntity entity, Attribute attribute, UUID id, String name, double amount,
                                    AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) {
            return;
        }

        AttributeModifier current = instance.getModifier(id);
        if (Math.abs(amount) <= 0.0001D) {
            if (current != null) {
                instance.removeModifier(id);
            }
            return;
        }

        if (current != null
                && Math.abs(current.getAmount() - amount) <= 0.0001D
                && current.getOperation() == operation) {
            return;
        }

        instance.removeModifier(id);
        instance.addTransientModifier(new AttributeModifier(id, name, amount, operation));
    }

    private static void removeModifier(LivingEntity entity, Attribute attribute, UUID id) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }

    private static double getItemModifierValue(ItemStack stack, EquipmentSlot slot, Attribute attribute) {
        double value = 0.0D;
        Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
        for (AttributeModifier modifier : modifiers.get(attribute)) {
            value += modifier.getOperation() == AttributeModifier.Operation.ADDITION ? modifier.getAmount() : 0.0D;
        }
        return Math.max(0.0D, value);
    }

    private static double getArmorValue(ItemStack stack, EquipmentSlot slot) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getDefense();
        }
        return getItemModifierValue(stack, slot, Attributes.ARMOR);
    }

    private static UUID qualityModifierId(ItemStack stack, String suffix) {
        return UUID.nameUUIDFromBytes(("thuumcraft:smithing:" + itemPath(stack) + ":" + suffix)
                .getBytes(StandardCharsets.UTF_8));
    }

    private static boolean isSword(ItemStack stack) {
        String path = itemPath(stack);
        return path.contains("sword") || path.contains("dagger");
    }

    private static boolean isAxe(ItemStack stack) {
        String path = itemPath(stack);
        return path.contains("war_axe") || path.endsWith("_axe") || path.equals("axe");
    }

    private static boolean isMace(ItemStack stack) {
        return itemPath(stack).contains("mace");
    }

    private static boolean isGreatsword(ItemStack stack) {
        return itemPath(stack).contains("greatsword");
    }

    private static boolean isBattleaxe(ItemStack stack) {
        String path = itemPath(stack);
        return path.contains("battleaxe") || path.contains("battle_axe");
    }

    private static boolean isWarhammer(ItemStack stack) {
        String path = itemPath(stack);
        return path.contains("warhammer") || path.contains("war_hammer");
    }

    private static String itemPath(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id == null ? "" : id.getPath().toLowerCase(Locale.ROOT);
    }

    private static final class RuntimeState {
        private static final RuntimeState EMPTY = new RuntimeState();
        private int lastShieldChargeTick = -1000;
        private boolean eagleEyeZooming;
    }

    private static final class BleedState {
        private final UUID targetId;
        private final ResourceKey<Level> dimension;
        private final double damagePerSecond;
        private long nextTick;
        private final long expiresAt;

        private BleedState(UUID targetId, ResourceKey<Level> dimension, double damagePerSecond, long nextTick,
                           long expiresAt) {
            this.targetId = targetId;
            this.dimension = dimension;
            this.damagePerSecond = damagePerSecond;
            this.nextTick = nextTick;
            this.expiresAt = expiresAt;
        }
    }

    private static final class ArmorProfile {
        private final double heavyArmor;
        private final double lightArmor;
        private final boolean allHeavy;
        private final boolean allLight;
        private final boolean matchingSet;

        private ArmorProfile(double heavyArmor, double lightArmor, boolean allHeavy, boolean allLight,
                             boolean matchingSet) {
            this.heavyArmor = heavyArmor;
            this.lightArmor = lightArmor;
            this.allHeavy = allHeavy;
            this.allLight = allLight;
            this.matchingSet = matchingSet;
        }

        private static ArmorProfile of(ServerPlayer player) {
            double heavyArmor = 0.0D;
            double lightArmor = 0.0D;
            boolean allHeavy = true;
            boolean allLight = true;
            String material = null;
            boolean matching = true;
            for (EquipmentSlot slot : ARMOR_SLOTS) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.isEmpty()) {
                    allHeavy = false;
                    allLight = false;
                    matching = false;
                    continue;
                }
                if (stack.is(ModTags.Items.HEAVY_ARMOR)) {
                    heavyArmor += getArmorValue(stack, slot);
                    allLight = false;
                } else if (stack.is(ModTags.Items.LIGHT_ARMOR)) {
                    lightArmor += getArmorValue(stack, slot);
                    allHeavy = false;
                } else {
                    allHeavy = false;
                    allLight = false;
                    matching = false;
                }

                String stackMaterial = materialKey(stack);
                if (material == null) {
                    material = stackMaterial;
                } else if (!material.equals(stackMaterial)) {
                    matching = false;
                }
            }
            return new ArmorProfile(heavyArmor, lightArmor, allHeavy, allLight, matching);
        }

        private static String materialKey(ItemStack stack) {
            String path = itemPath(stack);
            return path.replace("_helmet", "")
                    .replace("_chestplate", "")
                    .replace("_leggings", "")
                    .replace("_boots", "");
        }
    }
}
