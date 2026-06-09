package net.apotheoticstudios.thuumcraft.magic;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.ToIntFunction;

public final class SkyrimMagicScaling {
    public static final String MAIN_HAND_EQUIPMENT_SLOT = Thuumcraft.MOD_ID + ":skyrim_magic_main_hand";
    public static final String OFF_HAND_EQUIPMENT_SLOT = Thuumcraft.MOD_ID + ":skyrim_magic_off_hand";

    private SkyrimMagicScaling() {
    }

    public static boolean isThuumcraftSkyrimSpell(AbstractSpell spell) {
        ResourceLocation id = spell == null ? null : spell.getSpellResource();
        return id != null && Thuumcraft.MOD_ID.equals(id.getNamespace());
    }

    public static SkillProgression.Skill skillFor(AbstractSpell spell) {
        return spell == null ? null : skillFor(spell.getSchoolType());
    }

    public static SkillProgression.Skill skillFor(SchoolType school) {
        if (school == null || school.getId() == null) {
            return null;
        }

        String schoolPath = school.getId().getPath().toLowerCase(Locale.ROOT);
        return switch (schoolPath) {
            case "alteration", "nature" -> SkillProgression.Skill.ALTERATION;
            case "conjuration", "ender" -> SkillProgression.Skill.CONJURATION;
            case "destruction", "fire", "ice", "lightning", "evocation", "blood", "eldritch" ->
                    SkillProgression.Skill.DESTRUCTION;
            case "illusion" -> SkillProgression.Skill.ILLUSION;
            case "restoration", "holy" -> SkillProgression.Skill.RESTORATION;
            default -> null;
        };
    }

    public static int adjustedManaCost(AbstractSpell spell, int level, Player player) {
        return adjustedManaCost(spell, level, perk -> SkillPerk.rank(player, perk));
    }

    public static int adjustedManaCost(AbstractSpell spell, int level, ToIntFunction<SkillPerk> rankProvider) {
        int rawCost = spell == null ? 0 : spell.getManaCost(level);
        if (rawCost <= 0) {
            return 0;
        }

        SkillPerk costPerk = costReductionPerk(skillFor(spell), spell.getRarity(level));
        if (costPerk != null && rankProvider.applyAsInt(costPerk) > 0) {
            return Math.max(1, Mth.ceil(rawCost * 0.5D));
        }
        return rawCost;
    }

    public static double spellPowerMultiplier(AbstractSpell spell, LivingEntity caster) {
        return elementPowerFor(spell, caster);
    }

    public static int adjustedCooldownTicks(AbstractSpell spell, Player player) {
        if (spell == null || player == null || spell.getSpellCooldown() <= 0) {
            return 0;
        }
        return MagicManager.getEffectiveSpellCooldown(spell, player, CastSource.SPELLBOOK);
    }

    public static double elementPowerFor(AbstractSpell spell, LivingEntity caster) {
        if (caster == null || spell == null) {
            return 1.0D;
        }
        return switch (elementFor(spell)) {
            case FIRE -> attributeValue(caster, ModAttributes.FIRE_SPELL_POWER);
            case FROST -> attributeValue(caster, ModAttributes.FROST_SPELL_POWER);
            case SHOCK -> attributeValue(caster, ModAttributes.SHOCK_SPELL_POWER);
            case NONE -> 1.0D;
        };
    }

    public static SpellElement elementFor(AbstractSpell spell) {
        if (spell == null) {
            return SpellElement.NONE;
        }

        ResourceLocation spellId = spell.getSpellResource();
        String path = spellId == null ? "" : spellId.getPath().toLowerCase(Locale.ROOT);
        String school = spell.getSchoolType() == null || spell.getSchoolType().getId() == null
                ? ""
                : spell.getSchoolType().getId().getPath().toLowerCase(Locale.ROOT);

        if (school.equals("fire") || containsAny(path, "fire", "flame", "flames", "burn", "magma", "ember", "scorch")) {
            return SpellElement.FIRE;
        }
        if (school.equals("ice") || containsAny(path, "frost", "freeze", "ice", "cold", "snow", "blizzard")) {
            return SpellElement.FROST;
        }
        if (school.equals("lightning") || containsAny(path, "shock", "spark", "lightning", "thunder", "electr", "chain")) {
            return SpellElement.SHOCK;
        }
        return SpellElement.NONE;
    }

    private static SkillPerk costReductionPerk(SkillProgression.Skill skill, SpellRarity rarity) {
        if (skill == null || rarity == null) {
            return null;
        }

        return switch (skill) {
            case ALTERATION -> switch (rarity) {
                case COMMON -> SkillPerk.ALTERATION_NOVICE_ALTERATION;
                case UNCOMMON -> SkillPerk.ALTERATION_APPRENTICE_ALTERATION;
                case RARE -> SkillPerk.ALTERATION_ADEPT_ALTERATION;
                case EPIC -> SkillPerk.ALTERATION_EXPERT_ALTERATION;
                case LEGENDARY -> SkillPerk.ALTERATION_MASTER_ALTERATION;
            };
            case CONJURATION -> switch (rarity) {
                case COMMON -> SkillPerk.CONJURATION_NOVICE_CONJURATION;
                case UNCOMMON -> SkillPerk.CONJURATION_APPRENTICE_CONJURATION;
                case RARE -> SkillPerk.CONJURATION_ADEPT_CONJURATION;
                case EPIC -> SkillPerk.CONJURATION_EXPERT_CONJURATION;
                case LEGENDARY -> SkillPerk.CONJURATION_MASTER_CONJURATION;
            };
            case DESTRUCTION -> switch (rarity) {
                case COMMON -> SkillPerk.DESTRUCTION_NOVICE_DESTRUCTION;
                case UNCOMMON -> SkillPerk.DESTRUCTION_APPRENTICE_DESTRUCTION;
                case RARE -> SkillPerk.DESTRUCTION_ADEPT_DESTRUCTION;
                case EPIC -> SkillPerk.DESTRUCTION_EXPERT_DESTRUCTION;
                case LEGENDARY -> SkillPerk.DESTRUCTION_MASTER_DESTRUCTION;
            };
            case ILLUSION -> switch (rarity) {
                case COMMON -> SkillPerk.ILLUSION_NOVICE_ILLUSION;
                case UNCOMMON -> SkillPerk.ILLUSION_APPRENTICE_ILLUSION;
                case RARE -> SkillPerk.ILLUSION_ADEPT_ILLUSION;
                case EPIC -> SkillPerk.ILLUSION_EXPERT_ILLUSION;
                case LEGENDARY -> SkillPerk.ILLUSION_MASTER_ILLUSION;
            };
            case RESTORATION -> switch (rarity) {
                case COMMON -> SkillPerk.RESTORATION_NOVICE_RESTORATION;
                case UNCOMMON -> SkillPerk.RESTORATION_APPRENTICE_RESTORATION;
                case RARE -> SkillPerk.RESTORATION_ADEPT_RESTORATION;
                case EPIC -> SkillPerk.RESTORATION_EXPERT_RESTORATION;
                case LEGENDARY -> SkillPerk.RESTORATION_MASTER_RESTORATION;
            };
            default -> null;
        };
    }

    private static double attributeValue(LivingEntity entity, RegistryObject<Attribute> attribute) {
        return entity.getAttribute(attribute.get()) == null ? 1.0D : entity.getAttributeValue(attribute.get());
    }

    private static boolean containsAny(String value, String... needles) {
        for (String needle : needles) {
            if (value.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    public enum SpellElement {
        FIRE,
        FROST,
        SHOCK,
        NONE
    }
}
