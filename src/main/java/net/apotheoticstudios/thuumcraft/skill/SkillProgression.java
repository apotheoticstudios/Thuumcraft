package net.apotheoticstudios.thuumcraft.skill;

import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public final class SkillProgression {
    private static final int SKILL_CAP = 100;
    private static final double LEVEL_BASE_XP = 20.0D;
    private static final double LEVEL_XP_GROWTH = 4.0D;
    private static final String KEY_PREFIX = "ThuumcraftSkill.";

    private SkillProgression() {
    }

    public static void award(ServerPlayer player, Skill skill, double amount) {
        if (amount <= 0.0D || player.isSpectator() || player.isCreative()) {
            return;
        }

        AttributeInstance attribute = player.getAttribute(skill.attribute().get());
        if (attribute == null) {
            return;
        }

        CompoundTag data = player.getPersistentData();
        int level = getPersistentLevel(player, skill, attribute);
        if (level >= SKILL_CAP) {
            data.putDouble(skill.xpKey(), 0.0D);
            return;
        }

        double experience = Math.max(0.0D, data.getDouble(skill.xpKey())) + amount;
        boolean leveledUp = false;
        while (level < SKILL_CAP) {
            double requiredExperience = getRequiredExperience(level);
            if (experience < requiredExperience) {
                break;
            }

            experience -= requiredExperience;
            level++;
            leveledUp = true;
        }

        if (level >= SKILL_CAP) {
            experience = 0.0D;
        }

        data.putInt(skill.levelKey(), level);
        data.putDouble(skill.xpKey(), experience);
        setBaseValue(attribute, level);

        if (leveledUp) {
            player.displayClientMessage(Component.literal(skill.displayName() + " increased to ")
                    .append(Component.literal(Integer.toString(level)).withStyle(Style.EMPTY.withBold(true))), true);
        }
    }

    public static void applyAll(ServerPlayer player) {
        for (Skill skill : Skill.values()) {
            apply(player, skill);
        }
    }

    public static void apply(ServerPlayer player, Skill skill) {
        AttributeInstance attribute = player.getAttribute(skill.attribute().get());
        if (attribute == null) {
            return;
        }

        CompoundTag data = player.getPersistentData();
        int level = getPersistentLevel(player, skill, attribute);
        data.putInt(skill.levelKey(), level);
        data.putDouble(skill.xpKey(), Math.max(0.0D, data.getDouble(skill.xpKey())));
        setBaseValue(attribute, level);
    }

    public static void copyAll(Player oldPlayer, ServerPlayer newPlayer) {
        for (Skill skill : Skill.values()) {
            copy(oldPlayer, newPlayer, skill);
        }
    }

    public static void copy(Player oldPlayer, ServerPlayer newPlayer, Skill skill) {
        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();
        if (oldData.contains(skill.levelKey())) {
            newData.putInt(skill.levelKey(), oldData.getInt(skill.levelKey()));
        }
        if (oldData.contains(skill.xpKey())) {
            newData.putDouble(skill.xpKey(), oldData.getDouble(skill.xpKey()));
        }
        apply(newPlayer, skill);
    }

    public static int getLevel(ServerPlayer player, Skill skill) {
        AttributeInstance attribute = player.getAttribute(skill.attribute().get());
        return attribute == null ? 0 : getPersistentLevel(player, skill, attribute);
    }

    private static int getPersistentLevel(ServerPlayer player, Skill skill, AttributeInstance attribute) {
        CompoundTag data = player.getPersistentData();
        int level = data.contains(skill.levelKey())
                ? data.getInt(skill.levelKey())
                : Mth.floor(attribute.getBaseValue());
        return Mth.clamp(level, 0, SKILL_CAP);
    }

    private static double getRequiredExperience(int currentLevel) {
        return LEVEL_BASE_XP + currentLevel * LEVEL_XP_GROWTH;
    }

    private static void setBaseValue(AttributeInstance attribute, int level) {
        if (Math.abs(attribute.getBaseValue() - level) > 0.0001D) {
            attribute.setBaseValue(level);
        }
    }

    public enum Skill {
        ALCHEMY("alchemy", "Alchemy", ModAttributes.ALCHEMY),
        ARCHERY("archery", "Archery", ModAttributes.ARCHERY),
        BARTER("barter", "Barter", ModAttributes.BARTER),
        BLOCK("block", "Block", ModAttributes.BLOCK),
        HEAVY_ARMOR("heavy_armor", "Heavy Armor", ModAttributes.HEAVY_ARMOR),
        LIGHT_ARMOR("light_armor", "Light Armor", ModAttributes.LIGHT_ARMOR),
        ONE_HANDED("one_handed", "One-Handed", ModAttributes.ONE_HANDED),
        SMITHING("smithing", "Smithing", ModAttributes.SMITHING),
        SNEAK("sneak", "Sneak", ModAttributes.SNEAK, "ThuumcraftSneakLevel", "ThuumcraftSneakXp"),
        TWO_HANDED("two_handed", "Two-Handed", ModAttributes.TWO_HANDED);

        private final String id;
        private final String displayName;
        private final RegistryObject<Attribute> attribute;
        private final String levelKey;
        private final String xpKey;

        Skill(String id, String displayName, RegistryObject<Attribute> attribute) {
            this(id, displayName, attribute, KEY_PREFIX + id + ".Level", KEY_PREFIX + id + ".Xp");
        }

        Skill(String id, String displayName, RegistryObject<Attribute> attribute, String levelKey, String xpKey) {
            this.id = id;
            this.displayName = displayName;
            this.attribute = attribute;
            this.levelKey = levelKey;
            this.xpKey = xpKey;
        }

        public String id() {
            return id;
        }

        public String displayName() {
            return displayName;
        }

        private RegistryObject<Attribute> attribute() {
            return attribute;
        }

        private String levelKey() {
            return levelKey;
        }

        private String xpKey() {
            return xpKey;
        }

        public static Skill fromId(String id) {
            String normalizedId = id.toLowerCase(Locale.ROOT);
            for (Skill skill : values()) {
                if (skill.id.equals(normalizedId)) {
                    return skill;
                }
            }
            return null;
        }
    }
}
