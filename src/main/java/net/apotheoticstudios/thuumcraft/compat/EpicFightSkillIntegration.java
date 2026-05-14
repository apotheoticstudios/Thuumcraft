package net.apotheoticstudios.thuumcraft.compat;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.effect.ModEffects;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.skill.SkillPerkEvents;
import net.apotheoticstudios.thuumcraft.stamina.StaminaEvents;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.entity.eventlistener.DealDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;
import yesman.epicfight.world.entity.eventlistener.StaminaConsumeEvent;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EpicFightSkillIntegration {
    private static final UUID DAMAGE_LISTENER_ID = UUID.fromString("a78b5585-5d9f-4f47-9d25-67ab2929883d");
    private static final UUID SKILL_CAST_LISTENER_ID = UUID.fromString("703ce180-9d96-4c44-8af3-1128a8ef72c7");
    private static final UUID SKILL_CONSUME_LISTENER_ID = UUID.fromString("f6047152-90ac-4ccb-8120-f77b554a62e2");
    private static final UUID STAMINA_CONSUME_LISTENER_ID = UUID.fromString("982d7b2d-07e0-4267-948d-f0d0a3ee6f10");
    private static final int DODGE_DIRECTION_MEMORY_TICKS = 24;
    private static final int RECENT_STAMINA_CONSUME_TICKS = 4;
    private static final int POWER_ATTACK_STAMINA_MEMORY_TICKS = 10;
    private static final double SWORD_CRIT_MULTIPLIER = 1.5D;
    private static final double POWER_ATTACK_STAMINA_COST = 30.0D;

    private static final Map<UUID, ServerPlayerPatch> REGISTERED_PATCHES = new HashMap<>();
    private static final Map<UUID, CombatState> COMBAT_STATES = new HashMap<>();

    private EpicFightSkillIntegration() {
    }

    public static void ensureRegistered(ServerPlayer player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getServerPlayerPatch(player);
        if (patch == null || REGISTERED_PATCHES.get(player.getUUID()) == patch) {
            return;
        }

        patch.getEventListener().addEventListener(EventType.DEAL_DAMAGE_EVENT_HURT, DAMAGE_LISTENER_ID,
                EpicFightSkillIntegration::applyEpicFightMeleePerks, 20);
        patch.getEventListener().addEventListener(EventType.SKILL_CAST_EVENT, SKILL_CAST_LISTENER_ID,
                EpicFightSkillIntegration::trackDodgeDirection, 20);
        patch.getEventListener().addEventListener(EventType.SKILL_CONSUME_EVENT, SKILL_CONSUME_LISTENER_ID,
                EpicFightSkillIntegration::adjustSkillCost, 20);
        patch.getEventListener().addEventListener(EventType.STAMINA_CONSUME_EVENT, STAMINA_CONSUME_LISTENER_ID,
                EpicFightSkillIntegration::replaceDirectStaminaCost, 20);
        REGISTERED_PATCHES.put(player.getUUID(), patch);
    }

    public static boolean shouldReplaceEpicFightStamina() {
        return Config.ENABLE_SKYRIM_HUD_AND_STAMINA.get()
                && Config.ENABLE_STAMINA_SYSTEM.get()
                && Config.ENABLE_EPIC_FIGHT_STAMINA_REPLACEMENT.get();
    }

    public static void refillEpicFightStamina(ServerPlayer player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getServerPlayerPatch(player);
        if (patch != null && shouldReplaceEpicFightStamina()) {
            patch.setStamina(patch.getMaxStamina());
        }
    }

    public static void clear(ServerPlayer player) {
        UUID playerId = player.getUUID();
        REGISTERED_PATCHES.remove(playerId);
        COMBAT_STATES.remove(playerId);
    }

    private static void adjustSkillCost(SkillConsumeEvent event) {
        if (event.getResourceType() != Skill.Resource.STAMINA
                || event.getAmount() <= 0.0F
                || event.getSkill() == null
                || !(event.getPlayerPatch() instanceof ServerPlayerPatch playerPatch)) {
            return;
        }

        ServerPlayer player = playerPatch.getOriginal();
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        float adjustedAmount = getAdjustedEpicFightStaminaCost(event, playerPatch, player);
        if (!shouldReplaceEpicFightStamina()) {
            event.setAmount(adjustedAmount);
            return;
        }

        if (!consumeThuumcraftStamina(playerPatch, player, adjustedAmount)) {
            event.setCanceled(true);
            return;
        }

        event.setAmount(0.0F);
        playerPatch.setStamina(playerPatch.getMaxStamina());
    }

    private static void replaceDirectStaminaCost(StaminaConsumeEvent event) {
        if (!shouldReplaceEpicFightStamina()
                || event.getAmount() <= 0.0F
                || !(event.getPlayerPatch() instanceof ServerPlayerPatch playerPatch)) {
            return;
        }

        ServerPlayer player = playerPatch.getOriginal();
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (!consumeThuumcraftStamina(playerPatch, player, event.getAmount())) {
            event.setCanceled(true);
            return;
        }

        event.setAmount(0.0F);
        playerPatch.setStamina(playerPatch.getMaxStamina());
    }

    private static float getAdjustedEpicFightStaminaCost(SkillConsumeEvent event, ServerPlayerPatch playerPatch,
                                                         ServerPlayer player) {
        float amount = event.getAmount();
        if (!SkillPerk.isSystemEnabled() || event.getSkill().getCategory() != SkillCategories.BASIC_ATTACK) {
            return amount;
        }

        WeaponProfile weapon = WeaponProfile.of(playerPatch, player.getMainHandItem());
        if ((weapon.oneHanded() && SkillPerk.has(player, SkillPerk.ONE_HANDED_FIGHTING_STANCE))
                || (weapon.twoHanded() && SkillPerk.has(player, SkillPerk.TWO_HANDED_CHAMPIONS_STANCE))) {
            return amount * 0.75F;
        }
        return amount;
    }

    private static boolean consumeThuumcraftStamina(ServerPlayerPatch playerPatch, ServerPlayer player,
                                                    float epicFightCost) {
        double cost = convertEpicFightStaminaCost(playerPatch, player, epicFightCost);
        if (cost <= 0.0D) {
            return true;
        }
        if (!StaminaEvents.tryConsumeCurrentStamina(player, cost)) {
            return false;
        }
        CombatState state = COMBAT_STATES.computeIfAbsent(player.getUUID(), ignored -> new CombatState());
        state.lastStaminaConsumeTick = player.tickCount;
        return true;
    }

    private static double convertEpicFightStaminaCost(ServerPlayerPatch playerPatch, ServerPlayer player,
                                                     float epicFightCost) {
        double epicFightMaxStamina = playerPatch.getMaxStamina();
        if (epicFightMaxStamina <= 0.0D) {
            return Math.max(0.0D, epicFightCost);
        }
        return Math.max(0.0D, epicFightCost / epicFightMaxStamina * StaminaEvents.getMaxStamina(player));
    }

    private static boolean consumePowerAttackStamina(ServerPlayerPatch playerPatch, ServerPlayer player,
                                                     EpicFightAttackContext attack, boolean reducedCost) {
        if (!shouldReplaceEpicFightStamina()) {
            return true;
        }

        CombatState state = attack.combatState();
        if (player.tickCount - state.lastStaminaConsumeTick <= RECENT_STAMINA_CONSUME_TICKS) {
            return true;
        }
        if (player.tickCount - state.lastPowerAttackStaminaTick <= POWER_ATTACK_STAMINA_MEMORY_TICKS) {
            return state.lastPowerAttackStaminaPaid;
        }

        double cost = POWER_ATTACK_STAMINA_COST * (reducedCost ? 0.75D : 1.0D);
        boolean paid = StaminaEvents.tryConsumeCurrentStamina(player, cost);
        state.lastPowerAttackStaminaTick = player.tickCount;
        state.lastPowerAttackStaminaPaid = paid;
        if (paid) {
            state.lastStaminaConsumeTick = player.tickCount;
            playerPatch.setStamina(playerPatch.getMaxStamina());
        }
        return paid;
    }

    private static void trackDodgeDirection(SkillCastEvent event) {
        if (!SkillPerk.isSystemEnabled()
                || !event.isExecutable()
                || event.getSkillContainer() == null
                || event.getSkillContainer().getSkill() == null
                || event.getSkillContainer().getSkill().getCategory() != SkillCategories.DODGE
                || !(event.getPlayerPatch().getOriginal() instanceof ServerPlayer player)) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        DodgeDirection direction = readDodgeDirection(event.getArguments());
        if (direction == DodgeDirection.NONE) {
            return;
        }

        CombatState state = COMBAT_STATES.computeIfAbsent(player.getUUID(), ignored -> new CombatState());
        state.lastDodgeTick = player.tickCount;
        state.lastDodgeDirection = direction;
    }

    private static void applyEpicFightMeleePerks(DealDamageEvent.Hurt event) {
        if (!SkillPerk.isSystemEnabled()
                || event.getTarget() == null
                || event.getDamageSource() == null
                || event.getPlayerPatch().getOriginal() == null) {
            return;
        }

        ServerPlayer player = event.getPlayerPatch().getOriginal();
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        ItemStack weaponStack = player.getMainHandItem();
        WeaponProfile weapon = WeaponProfile.of(event.getPlayerPatch(), weaponStack);
        if (!weapon.oneHanded() && !weapon.twoHanded()) {
            return;
        }

        EpicFightDamageSource source = event.getDamageSource();
        double damageMultiplier = weapon.oneHanded()
                ? applyOneHandedPerks(player, event, weapon, source)
                : applyTwoHandedPerks(player, event, weapon, source);

        if (Math.abs(damageMultiplier - 1.0D) > 0.0001D) {
            source.attachDamageModifier(ValueModifier.multiplier((float) damageMultiplier));
        }
    }

    private static double applyOneHandedPerks(ServerPlayer player, DealDamageEvent.Hurt event, WeaponProfile weapon,
                                             EpicFightDamageSource source) {
        double damageMultiplier = 1.0D;
        int armsman = SkillPerk.rank(player, SkillPerk.ONE_HANDED_ARMSMAN);
        if (armsman > 0) {
            damageMultiplier *= 1.0D + armsman * 0.2D;
        }

        if (weapon.sword()) {
            damageMultiplier *= criticalMultiplier(player, event.getTarget(),
                    SkillPerk.rank(player, SkillPerk.ONE_HANDED_BLADESMAN));
        } else if (weapon.axe()) {
            SkillPerkEvents.applyBleed(event.getTarget(), SkillPerk.rank(player, SkillPerk.ONE_HANDED_HACK_AND_SLASH),
                    0.5D, 4);
        } else if (weapon.mace()) {
            addArmorNegation(source, SkillPerk.rank(player, SkillPerk.ONE_HANDED_BONE_BREAKER));
        }

        EpicFightAttackContext attack = EpicFightAttackContext.of(player, source);
        if (!attack.powerAttack()) {
            return damageMultiplier;
        }
        if (!consumePowerAttackStamina(event.getPlayerPatch(), player, attack,
                SkillPerk.has(player, SkillPerk.ONE_HANDED_FIGHTING_STANCE))) {
            attack.consumeDodgeDirection();
            return damageMultiplier;
        }

        if (attack.charging() && SkillPerk.has(player, SkillPerk.ONE_HANDED_CRITICAL_CHARGE)) {
            damageMultiplier *= 2.0D;
        } else if (attack.standing() && SkillPerk.has(player, SkillPerk.ONE_HANDED_SAVAGE_STRIKE)) {
            damageMultiplier *= 1.25D;
        }
        if (attack.backward() && SkillPerk.has(player, SkillPerk.ONE_HANDED_PARALYZING_STRIKE)
                && player.getRandom().nextFloat() < 0.25F) {
            event.getTarget().addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60));
        }
        if (weapon.dualWielding() && SkillPerk.has(player, SkillPerk.ONE_HANDED_DUAL_SAVAGERY)) {
            damageMultiplier *= 1.5D;
        }
        attack.consumeDodgeDirection();
        return damageMultiplier;
    }

    private static double applyTwoHandedPerks(ServerPlayer player, DealDamageEvent.Hurt event, WeaponProfile weapon,
                                             EpicFightDamageSource source) {
        double damageMultiplier = 1.0D;
        int barbarian = SkillPerk.rank(player, SkillPerk.TWO_HANDED_BARBARIAN);
        if (barbarian > 0) {
            damageMultiplier *= 1.0D + barbarian * 0.2D;
        }

        if (weapon.greatsword()) {
            damageMultiplier *= criticalMultiplier(player, event.getTarget(),
                    SkillPerk.rank(player, SkillPerk.TWO_HANDED_DEEP_WOUNDS));
        } else if (weapon.battleaxe()) {
            SkillPerkEvents.applyBleed(event.getTarget(), SkillPerk.rank(player, SkillPerk.TWO_HANDED_LIMBSPLITTER),
                    0.75D, 4);
        } else if (weapon.warhammer()) {
            addArmorNegation(source, SkillPerk.rank(player, SkillPerk.TWO_HANDED_SKULLCRUSHER));
        }

        EpicFightAttackContext attack = EpicFightAttackContext.of(player, source);
        if (!attack.powerAttack()) {
            return damageMultiplier;
        }
        if (!consumePowerAttackStamina(event.getPlayerPatch(), player, attack,
                SkillPerk.has(player, SkillPerk.TWO_HANDED_CHAMPIONS_STANCE))) {
            attack.consumeDodgeDirection();
            return damageMultiplier;
        }

        if (attack.charging() && SkillPerk.has(player, SkillPerk.TWO_HANDED_GREAT_CRITICAL_CHARGE)) {
            damageMultiplier *= 2.0D;
        } else if (attack.standing() && SkillPerk.has(player, SkillPerk.TWO_HANDED_DEVASTATING_BLOW)) {
            damageMultiplier *= 1.25D;
        }
        if (attack.backward() && SkillPerk.has(player, SkillPerk.TWO_HANDED_WARMASTER)
                && player.getRandom().nextFloat() < 0.25F) {
            event.getTarget().addEffect(new MobEffectInstance(ModEffects.PARALYSIS.get(), 60));
        }
        if (attack.sideways() && SkillPerk.has(player, SkillPerk.TWO_HANDED_SWEEP)) {
            sweepNearbyTargets(player, event.getTarget(), (float) (event.getAttackDamage() * damageMultiplier * 0.5D));
        }
        attack.consumeDodgeDirection();
        return damageMultiplier;
    }

    private static double criticalMultiplier(ServerPlayer player, LivingEntity target, int rank) {
        if (rank <= 0) {
            return 1.0D;
        }
        double chance = switch (rank) {
            case 1 -> 0.10D;
            case 2 -> 0.15D;
            default -> 0.20D;
        };
        if (player.getRandom().nextDouble() >= chance) {
            return 1.0D;
        }
        player.serverLevel().sendParticles(ParticleTypes.CRIT, target.getX(), target.getY(0.55D), target.getZ(),
                12, 0.35D, 0.35D, 0.35D, 0.08D);
        return SWORD_CRIT_MULTIPLIER;
    }

    private static void addArmorNegation(EpicFightDamageSource source, int rank) {
        if (rank <= 0) {
            return;
        }
        float armorNegation = switch (rank) {
            case 1 -> 25.0F;
            case 2 -> 50.0F;
            default -> 75.0F;
        };
        source.attachArmorNegationModifier(ValueModifier.adder(armorNegation));
    }

    private static void sweepNearbyTargets(ServerPlayer player, LivingEntity primary, float amount) {
        CombatState state = COMBAT_STATES.computeIfAbsent(player.getUUID(), ignored -> new CombatState());
        if (state.lastSweepTick == player.tickCount || EpicFightCompat.isApplyingSecondaryDamage()) {
            return;
        }

        state.lastSweepTick = player.tickCount;
        Vec3 look = player.getLookAngle().normalize();
        AABB area = primary.getBoundingBox().inflate(2.2D, 0.5D, 2.2D);
        EpicFightCompat.setApplyingSecondaryDamage(true);
        try {
            for (LivingEntity target : player.serverLevel().getEntitiesOfClass(LivingEntity.class, area,
                    target -> target != player && target != primary && target.isAlive() && player.hasLineOfSight(target))) {
                Vec3 toTarget = target.position().subtract(player.position());
                if (toTarget.horizontalDistanceSqr() > 0.0001D && look.dot(toTarget.normalize()) > 0.15D) {
                    target.hurt(player.damageSources().playerAttack(player), amount);
                }
            }
        } finally {
            EpicFightCompat.setApplyingSecondaryDamage(false);
        }
    }

    private static DodgeDirection readDodgeDirection(FriendlyByteBuf args) {
        if (args == null || args.readableBytes() < Integer.BYTES) {
            return DodgeDirection.NONE;
        }

        int readerIndex = args.readerIndex();
        int animationIndex = args.readInt();
        args.readerIndex(readerIndex);
        return switch (animationIndex) {
            case 1 -> DodgeDirection.BACKWARD;
            case 2, 3 -> DodgeDirection.SIDEWAYS;
            default -> DodgeDirection.FORWARD;
        };
    }

    private static boolean isTagged(ItemStack stack, TagKey<Item> tag) {
        return !stack.isEmpty() && stack.is(tag);
    }

    private static boolean isDualWielding(ServerPlayerPatch playerPatch, ServerPlayer player) {
        ItemStack offhand = player.getOffhandItem();
        if (offhand.isEmpty()) {
            return false;
        }
        if (isTagged(offhand, ModTags.Items.ONE_HANDED_WEAPONS)) {
            return true;
        }
        CapabilityItem offhandCapability = EpicFightCapabilities.getItemStackCapability(offhand);
        return offhandCapability.getWeaponCategory() == WeaponCategories.SWORD
                || offhandCapability.getWeaponCategory() == WeaponCategories.AXE
                || offhandCapability.getWeaponCategory() == WeaponCategories.DAGGER;
    }

    private static String itemPath(ItemStack stack) {
        return net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()) == null
                ? ""
                : net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
    }

    private enum DodgeDirection {
        NONE,
        FORWARD,
        BACKWARD,
        SIDEWAYS
    }

    private record WeaponProfile(boolean oneHanded, boolean twoHanded, boolean sword, boolean axe, boolean mace,
                                 boolean greatsword, boolean battleaxe, boolean warhammer, boolean dualWielding) {
        private static WeaponProfile of(ServerPlayerPatch playerPatch, ItemStack stack) {
            ServerPlayer player = playerPatch.getOriginal();
            CapabilityItem capability = EpicFightCapabilities.getItemStackCapability(stack);
            boolean taggedOneHanded = isTagged(stack, ModTags.Items.ONE_HANDED_WEAPONS);
            boolean taggedTwoHanded = isTagged(stack, ModTags.Items.TWO_HANDED_WEAPONS);
            boolean dualWielding = isDualWielding(playerPatch, player);
            boolean epicOneHanded = isEpicFightOneHanded(capability, playerPatch, dualWielding);
            boolean epicTwoHanded = isEpicFightTwoHanded(capability, playerPatch, dualWielding);
            boolean oneHanded = SkillPerk.isSkillEnabled(net.apotheoticstudios.thuumcraft.skill.SkillProgression.Skill.ONE_HANDED)
                    && (taggedOneHanded || (!taggedTwoHanded && epicOneHanded));
            boolean twoHanded = SkillPerk.isSkillEnabled(net.apotheoticstudios.thuumcraft.skill.SkillProgression.Skill.TWO_HANDED)
                    && !oneHanded
                    && (taggedTwoHanded || epicTwoHanded);
            String path = itemPath(stack);

            boolean sword = capability.getWeaponCategory() == WeaponCategories.SWORD
                    || path.contains("sword")
                    || path.contains("blade");
            boolean axe = capability.getWeaponCategory() == WeaponCategories.AXE
                    || path.contains("war_axe")
                    || path.contains("waraxe")
                    || path.endsWith("_axe")
                    || path.contains("axe");
            boolean mace = path.contains("mace") || path.contains("club") || path.contains("hammer");
            boolean greatsword = capability.getWeaponCategory() == WeaponCategories.GREATSWORD
                    || capability.getWeaponCategory() == WeaponCategories.LONGSWORD
                    || capability.getWeaponCategory() == WeaponCategories.TACHI
                    || path.contains("greatsword")
                    || path.contains("longsword")
                    || path.contains("claymore");
            boolean battleaxe = twoHanded && axe && (path.contains("battle") || path.contains("great"));
            boolean warhammer = twoHanded && (path.contains("warhammer") || path.contains("war_hammer")
                    || path.contains("hammer"));

            return new WeaponProfile(oneHanded, twoHanded, sword, axe, mace, greatsword, battleaxe, warhammer,
                    dualWielding);
        }

        private static boolean isEpicFightOneHanded(CapabilityItem capability, ServerPlayerPatch playerPatch,
                                                    boolean dualWielding) {
            if (dualWielding && capability.getWeaponCategory() == WeaponCategories.SWORD) {
                return true;
            }
            return capability.getStyle(playerPatch) == Styles.ONE_HAND
                    && (capability.getWeaponCategory() == WeaponCategories.SWORD
                    || capability.getWeaponCategory() == WeaponCategories.AXE
                    || capability.getWeaponCategory() == WeaponCategories.DAGGER);
        }

        private static boolean isEpicFightTwoHanded(CapabilityItem capability, ServerPlayerPatch playerPatch,
                                                    boolean dualWielding) {
            if (dualWielding) {
                return false;
            }
            return capability.getStyle(playerPatch) == Styles.TWO_HAND
                    && (capability.getWeaponCategory() == WeaponCategories.GREATSWORD
                    || capability.getWeaponCategory() == WeaponCategories.LONGSWORD
                    || capability.getWeaponCategory() == WeaponCategories.TACHI
                    || capability.getWeaponCategory() == WeaponCategories.SPEAR);
        }
    }

    private record EpicFightAttackContext(ServerPlayer player, boolean powerAttack, boolean charging,
                                          boolean standing, boolean backward, boolean sideways,
                                          CombatState combatState, boolean usedDodgeDirection) {
        private static EpicFightAttackContext of(ServerPlayer player, EpicFightDamageSource source) {
            DynamicAnimation animation = source.getAnimation().get();
            boolean dashAttack = animation instanceof DashAttackAnimation;
            boolean airAttack = animation instanceof AirSlashAnimation;
            boolean weaponInnate = source.is(EpicFightDamageTypeTags.WEAPON_INNATE) || !source.isBasicAttack();
            boolean powerAttack = dashAttack || airAttack || weaponInnate;
            CombatState state = COMBAT_STATES.computeIfAbsent(player.getUUID(), ignored -> new CombatState());
            DodgeDirection recentDodge = state.dodgeDirection(player.tickCount);
            boolean usedDodgeDirection = recentDodge != DodgeDirection.NONE;
            DodgeDirection movementDirection = usedDodgeDirection ? recentDodge : movementDirection(player);
            boolean charging = powerAttack && (dashAttack || player.isSprinting()
                    || movementDirection == DodgeDirection.FORWARD);
            boolean standing = powerAttack && !charging && movementDirection == DodgeDirection.NONE && player.onGround();
            boolean backward = powerAttack && movementDirection == DodgeDirection.BACKWARD;
            boolean sideways = powerAttack && movementDirection == DodgeDirection.SIDEWAYS;
            return new EpicFightAttackContext(player, powerAttack, charging, standing, backward, sideways, state,
                    usedDodgeDirection);
        }

        private static DodgeDirection movementDirection(ServerPlayer player) {
            Vec3 movement = player.getDeltaMovement();
            Vec3 look = player.getLookAngle();
            double forward = movement.x * look.x + movement.z * look.z;
            double side = movement.x * look.z - movement.z * look.x;
            if (forward < -0.025D) {
                return DodgeDirection.BACKWARD;
            }
            if (Math.abs(side) > 0.035D) {
                return DodgeDirection.SIDEWAYS;
            }
            if (forward > 0.025D) {
                return DodgeDirection.FORWARD;
            }
            return DodgeDirection.NONE;
        }

        private void consumeDodgeDirection() {
            if (usedDodgeDirection) {
                combatState.lastDodgeDirection = DodgeDirection.NONE;
                combatState.lastDodgeTick = -DODGE_DIRECTION_MEMORY_TICKS;
            }
        }
    }

    private static final class CombatState {
        private int lastDodgeTick = -DODGE_DIRECTION_MEMORY_TICKS;
        private DodgeDirection lastDodgeDirection = DodgeDirection.NONE;
        private int lastStaminaConsumeTick = -RECENT_STAMINA_CONSUME_TICKS - 1;
        private int lastPowerAttackStaminaTick = -POWER_ATTACK_STAMINA_MEMORY_TICKS - 1;
        private boolean lastPowerAttackStaminaPaid = true;
        private int lastSweepTick = -1;

        private DodgeDirection dodgeDirection(int currentTick) {
            return currentTick - lastDodgeTick <= DODGE_DIRECTION_MEMORY_TICKS ? lastDodgeDirection : DodgeDirection.NONE;
        }
    }
}
