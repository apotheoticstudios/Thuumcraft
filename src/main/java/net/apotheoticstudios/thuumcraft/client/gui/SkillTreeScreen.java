package net.apotheoticstudios.thuumcraft.client.gui;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.client.ClientSkillPerkState;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.apotheoticstudios.thuumcraft.network.ServerboundUnlockPerkPacket;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillTreeScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            Thuumcraft.MOD_ID, "textures/gui/skill_trees/skill_tree_background.png");
    private static final int BACKGROUND_WIDTH = 1672;
    private static final int BACKGROUND_HEIGHT = 941;
    private static final int NODE_PICK_RADIUS = 8;
    private static final int TEXT_COLOR = 0xFFE4D6AD;
    private static final int MUTED_TEXT_COLOR = 0x998EA4B4;
    private static final int DIM_TEXT_COLOR = 0x665D7484;
    private static final int LINE_GLOW_COLOR = 0x224F8AA7;
    private static final int LINE_COLOR = 0x88D3B66D;
    private static final int NODE_GLOW_COLOR = 0x33F4DFA2;
    private static final int NODE_COLOR = 0xDDF1D58B;
    private static final int NODE_LOCKED_COLOR = 0x665D7484;
    private static final int NODE_AVAILABLE_COLOR = 0xFFF1D58B;
    private static final int NODE_SELECTED_COLOR = 0xFFFFFFFF;
    private static final int NODE_HOVER_COLOR = 0xFFFFECB6;
    private static final int PERK_LABEL_COLOR = 0xDDE4D6AD;
    private static final int PERK_LABEL_LOCKED_COLOR = 0x775D7484;
    private static final int TOOLTIP_BACKGROUND_COLOR = 0xE8040912;
    private static final int TOOLTIP_BORDER_COLOR = 0x99D3B66D;
    private static final int TOOLTIP_WRAP_WIDTH = 220;
    private static final int VISIBLE_NAVIGATION_TREES = 5;
    private static final int VISIBLE_SIDE_TREES = 2;
    private static final long TREE_TRANSITION_MILLIS = 220L;

    private static final List<SkillTreeDefinition> TREES = List.of(
            tree(ModAttributes.ARCHERY, 400, 487, perks(
                    perk(SkillPerk.ARCHERY_OVERDRAW, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Archery",
                            "Bonus: Ranged projectile attacks deal +20/40/60/80/100% damage.",
                            "Effect: Every shot hits harder as your Archery mastery rises."), 0.730D, 0.749D),
                    perk(SkillPerk.ARCHERY_CRITICAL_SHOT, tooltip("Ranks: 3", "Requires: 30/60/90 Archery",
                            "Bonus: Ranged projectile attacks have a 10/15/20% critical chance.",
                            "Effect: Critical hits add bonus damage based on projectile damage."), 0.668D, 0.497D),
                    perk(SkillPerk.ARCHERY_EAGLE_EYE, tooltip("Requires: 30 Archery",
                            "Effect: Aiming with a bow or crossbow zooms your view.",
                            "Cost: Zooming drains stamina while held."), 0.400D, 0.550D),
                    perk(SkillPerk.ARCHERY_STEADY_HAND, tooltip("Ranks: 2", "Requires: 40/60 Archery",
                            "Bonus: Eagle Eye slows nearby mobs while zooming.",
                            "Effect: Rank 2 applies a stronger slow."), 0.510D, 0.499D),
                    perk(SkillPerk.ARCHERY_POWER_SHOT, tooltip("Requires: 50 Archery",
                            "Effect: Ranged projectiles stagger most targets 50% of the time.",
                            "Note: Applies knockback and a brief slow when it triggers."), 0.323D, 0.396D),
                    perk(SkillPerk.ARCHERY_HUNTERS_DISCIPLINE, tooltip("Requires: 50 Archery",
                            "Effect: Recover extra arrows from fallen enemies.",
                            "Utility: Keeps ranged builds supplied during long fights."), 0.560D, 0.308D),
                    perk(SkillPerk.ARCHERY_RANGER, tooltip("Requires: 60 Archery",
                            "Effect: Move faster while a bow or crossbow is drawn.",
                            "Utility: Lets archers reposition without lowering their weapon."), 0.528D, 0.234D),
                    perk(SkillPerk.ARCHERY_QUICK_SHOT, tooltip("Requires: 70 Archery",
                            "Bonus: Bow shots reach full draw 30% faster.",
                            "Effect: Increases sustained ranged pressure."), 0.330D, 0.199D),
                    perk(SkillPerk.ARCHERY_BULLSEYE, tooltip("Requires: 100 Archery",
                            "Effect: Ranged projectiles have a 15% chance to paralyze briefly.",
                            "Utility: Rewards precision with crowd control."), 0.443D, 0.142D)),
                    links("Overdraw", "Critical Shot", "Critical Shot", "Hunter's Discipline",
                            "Hunter's Discipline", "Ranger", "Overdraw", "Eagle Eye", "Eagle Eye", "Power Shot",
                            "Power Shot", "Quick Shot", "Eagle Eye", "Steady Hand", "Ranger", "Bullseye",
                            "Quick Shot", "Bullseye")),
            tree(ModAttributes.BLOCK, 300, 275, perks(
                    perk(SkillPerk.BLOCK_SHIELD_WALL, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Block",
                            "Bonus: Blocking is 20/25/30/35/40% more effective.",
                            "Effect: Works as the foundation for shield blocking."), 0.527D, 0.862D),
                    perk(SkillPerk.BLOCK_DEFLECT_ARROWS, tooltip("Requires: 30 Block",
                            "Effect: Projectiles that strike a raised shield deal no damage.",
                            "Utility: Turns shield timing into reliable ranged defense."), 0.093D, 0.629D),
                    perk(SkillPerk.BLOCK_BLOCK_RUNNER, tooltip("Requires: 70 Block",
                            "Effect: Move faster while blocking.",
                            "Utility: Lets defensive fighters close gaps without dropping guard."), 0.340D, 0.175D),
                    perk(SkillPerk.BLOCK_ELEMENTAL_PROTECTION, tooltip("Requires: 50 Block",
                            "Bonus: Blocking with a shield reduces fire, frost, and shock damage by 50%.",
                            "Effect: Makes shields useful against elemental attacks."), 0.217D, 0.262D),
                    perk(SkillPerk.BLOCK_QUICK_REFLEXES, tooltip("Requires: 30 Block",
                            "Effect: Briefly slows heavy melee attackers when you block them.",
                            "Utility: Creates a reaction window for counterplay."), 0.427D, 0.545D),
                    perk(SkillPerk.BLOCK_POWER_BASH, tooltip("Requires: 30 Block",
                            "Effect: Unlocks power bashes while blocking.",
                            "Cost: Uses stamina for a stronger interrupt."), 0.887D, 0.615D),
                    perk(SkillPerk.BLOCK_DEADLY_BASH, tooltip("Requires: 50 Block",
                            "Bonus: Bashes deal 5x damage.",
                            "Effect: Turns bash interrupts into real offense."), 0.867D, 0.356D),
                    perk(SkillPerk.BLOCK_DISARMING_BASH, tooltip("Requires: 70 Block",
                            "Effect: Power bashes can disarm enemies.",
                            "Utility: Punishes armed opponents who pressure your guard."), 0.787D, 0.164D),
                    perk(SkillPerk.BLOCK_SHIELD_CHARGE, tooltip("Requires: 100 Block",
                            "Effect: Sprinting with a raised shield knocks back and slows nearby targets.",
                            "Utility: Breaks enemy lines with a defensive rush."), 0.527D, 0.102D)),
                    links("Shield Wall", "Deflect Arrows", "Deflect Arrows", "Elemental Protection",
                            "Elemental Protection", "Block Runner", "Shield Wall", "Power Bash", "Power Bash",
                            "Deadly Bash", "Deadly Bash", "Disarming Bash", "Disarming Bash", "Shield Charge",
                            "Block Runner", "Shield Charge", "Shield Wall", "Quick Reflexes")),
            tree(ModAttributes.HEAVY_ARMOR, 300, 292, perks(
                    perk(SkillPerk.HEAVY_ARMOR_JUGGERNAUT, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Heavy Armor",
                            "Bonus: Heavy armor rating increases by 20/40/60/80/100%.",
                            "Effect: Shields are not counted in this armor bonus."), 0.503D, 0.901D),
                    perk(SkillPerk.HEAVY_ARMOR_FISTS_OF_STEEL, tooltip("Requires: 30 Heavy Armor",
                            "Bonus: Heavy armor adds bonus unarmed damage.",
                            "Effect: Uses your lowest heavy armor piece as the gauntlet proxy."), 0.277D, 0.685D),
                    perk(SkillPerk.HEAVY_ARMOR_CUSHIONED, tooltip("Requires: 50 Heavy Armor",
                            "Bonus: Take 50% less fall damage while wearing all heavy armor.",
                            "Effect: Rewards committing to a full heavy set."), 0.157D, 0.479D),
                    perk(SkillPerk.HEAVY_ARMOR_CONDITIONING, tooltip("Requires: 70 Heavy Armor",
                            "Effect: Heavy armor no longer increases sprint stamina drain.",
                            "Utility: Removes the stamina mobility drawback of heavy gear."), 0.167D, 0.247D),
                    perk(SkillPerk.HEAVY_ARMOR_WELL_FITTED, tooltip("Requires: 30 Heavy Armor",
                            "Bonus: Gain +25% armor rating while wearing all heavy armor.",
                            "Effect: Stacks with Juggernaut for full-set defense."), 0.727D, 0.668D),
                    perk(SkillPerk.HEAVY_ARMOR_TOWER_OF_STRENGTH, tooltip("Requires: 50 Heavy Armor",
                            "Bonus: Take 50% less knockback while wearing only heavy armor.",
                            "Effect: Helps heavy fighters keep attacking under pressure."), 0.790D, 0.438D),
                    perk(SkillPerk.HEAVY_ARMOR_MATCHING_SET, tooltip("Requires: 70 Heavy Armor",
                            "Bonus: Gain another +25% armor rating with a matched heavy set.",
                            "Effect: Rewards wearing armor pieces from the same material family."), 0.850D, 0.301D),
                    perk(SkillPerk.HEAVY_ARMOR_REFLECT_BLOWS, tooltip("Requires: 100 Heavy Armor",
                            "Effect: 10% chance to reflect melee damage while wearing all heavy armor.",
                            "Utility: Makes attackers pay for striking a fully armored target."), 0.800D, 0.072D)),
                    links("Juggernaut", "Fists of Steel", "Fists of Steel", "Cushioned", "Cushioned",
                            "Conditioning", "Juggernaut", "Well Fitted", "Well Fitted", "Tower of Strength",
                            "Tower of Strength", "Matching Set", "Matching Set", "Reflect Blows")),
            tree(ModAttributes.ONE_HANDED, 300, 282, perks(
                    perk(SkillPerk.ONE_HANDED_ARMSMAN, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 One-handed",
                            "Bonus: One-handed weapons deal +20/40/60/80/100% damage.",
                            "Effect: Applies to swords, war axes, maces, and daggers."), 0.437D, 0.929D),
                    perk(SkillPerk.ONE_HANDED_BLADESMAN, tooltip("Ranks: 3", "Requires: 30/60/90 One-handed",
                            "Bonus: Swords have a 10/15/20% critical chance.",
                            "Effect: Critical hits add bonus damage based on weapon damage."), 0.623D, 0.596D),
                    perk(SkillPerk.ONE_HANDED_BONE_BREAKER, tooltip("Ranks: 3", "Requires: 30/60/90 One-handed",
                            "Bonus: Maces bypass 25/50/75% of armor mitigation.",
                            "Effect: Strong against heavily armored targets."), 0.533D, 0.592D),
                    perk(SkillPerk.ONE_HANDED_DUAL_FLURRY, tooltip("Ranks: 2", "Requires: 30/50 One-handed",
                            "Bonus: Dual wield attacks are 20/35% faster.",
                            "Effect: Rewards fighting with weapons in both hands."), 0.783D, 0.784D),
                    perk(SkillPerk.ONE_HANDED_DUAL_SAVAGERY, tooltip("Requires: 70 One-handed",
                            "Bonus: Dual-wield full-strength attacks consume stamina and deal +50% damage.",
                            "Effect: Converts stamina into high burst damage."), 0.687D, 0.301D),
                    perk(SkillPerk.ONE_HANDED_FIGHTING_STANCE, tooltip("Requires: 20 One-handed",
                            "Bonus: One-handed perk power attacks cost 25% less stamina.",
                            "Effect: Lets melee builds use stamina attacks more often."), 0.443D, 0.709D),
                    perk(SkillPerk.ONE_HANDED_CRITICAL_CHARGE, tooltip("Requires: 50 One-handed",
                            "Effect: Sprinting full-strength attacks consume stamina and deal 2x damage.",
                            "Utility: Opens fights with a high-impact charge."), 0.503D, 0.443D),
                    perk(SkillPerk.ONE_HANDED_SAVAGE_STRIKE, tooltip("Requires: 50 One-handed",
                            "Bonus: Standing power attacks deal +25% damage.",
                            "Effect: Consumes stamina when the perk attack triggers."), 0.370D, 0.433D),
                    perk(SkillPerk.ONE_HANDED_PARALYZING_STRIKE, tooltip("Requires: 100 One-handed",
                            "Effect: Backwards full-strength attacks have a 25% chance to paralyze.",
                            "Utility: Creates space when enemies push into melee range."), 0.443D, 0.064D),
                    perk(SkillPerk.ONE_HANDED_HACK_AND_SLASH, tooltip("Ranks: 3", "Requires: 30/60/90 One-handed",
                            "Bonus: War axes inflict increasing bleed damage.",
                            "Effect: Bleed keeps hurting the target after the hit lands."), 0.260D, 0.574D)),
                    links("Armsman", "Bladesman", "Armsman", "Bone Breaker", "Armsman", "Dual Flurry",
                            "Dual Flurry", "Dual Savagery", "Armsman", "Fighting Stance", "Fighting Stance",
                            "Critical Charge", "Fighting Stance", "Savage Strike", "Critical Charge",
                            "Paralyzing Strike", "Savage Strike", "Paralyzing Strike", "Armsman", "Hack and Slash")),
            tree(ModAttributes.SMITHING, 400, 210, perks(
                    perk(SkillPerk.SMITHING_STEEL_SMITHING, tooltip("Requires: 0 Smithing",
                            "Effect: Crafted or repaired steel and iron gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.365D, 0.819D),
                    perk(SkillPerk.SMITHING_ARCANE_BLACKSMITH, tooltip("Requires: 60 Smithing",
                            "Effect: Enchanted gear can receive the full smithing quality bonus.",
                            "Utility: Lets enchanted gear keep pace with crafted equipment."), 0.388D, 0.371D),
                    perk(SkillPerk.SMITHING_ELVEN_SMITHING, tooltip("Requires: 30 Smithing",
                            "Effect: Crafted or repaired elven gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.080D, 0.362D),
                    perk(SkillPerk.SMITHING_ADVANCED_ARMORS, tooltip("Requires: 50 Smithing",
                            "Effect: Crafted or repaired scaled and plate gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.108D, 0.257D),
                    perk(SkillPerk.SMITHING_GLASS_SMITHING, tooltip("Requires: 70 Smithing",
                            "Effect: Crafted or repaired glass gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.310D, 0.114D),
                    perk(SkillPerk.SMITHING_DRAGON_ARMOR, tooltip("Requires: 100 Smithing",
                            "Effect: Crafted or repaired dragon gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.505D, 0.114D),
                    perk(SkillPerk.SMITHING_DWARVEN_SMITHING, tooltip("Requires: 30 Smithing",
                            "Effect: Crafted or repaired dwarven gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.580D, 0.486D),
                    perk(SkillPerk.SMITHING_ORCISH_SMITHING, tooltip("Requires: 50 Smithing",
                            "Effect: Crafted or repaired orcish gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.795D, 0.310D),
                    perk(SkillPerk.SMITHING_EBONY_SMITHING, tooltip("Requires: 80 Smithing",
                            "Effect: Crafted or repaired ebony gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.963D, 0.314D),
                    perk(SkillPerk.SMITHING_DAEDRIC_SMITHING, tooltip("Requires: 90 Smithing",
                            "Effect: Crafted or repaired daedric gear gains smithing quality.",
                            "Bonus: Quality increases damage or armor rating."), 0.675D, 0.162D)),
                    links("Steel Smithing", "Arcane Blacksmith", "Steel Smithing", "Dwarven Smithing",
                            "Dwarven Smithing", "Orcish Smithing", "Orcish Smithing", "Ebony Smithing",
                            "Ebony Smithing", "Daedric Smithing", "Steel Smithing", "Elven Smithing",
                            "Elven Smithing", "Advanced Armors", "Advanced Armors", "Glass Smithing",
                            "Daedric Smithing", "Dragon Armor", "Glass Smithing", "Dragon Armor")),
            tree(ModAttributes.TWO_HANDED, 300, 404, perks(
                    perk(SkillPerk.TWO_HANDED_BARBARIAN, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Two-handed",
                            "Bonus: Two-handed weapons deal +20/40/60/80/100% damage.",
                            "Effect: Applies to greatswords, battleaxes, and warhammers."), 0.443D, 0.844D),
                    perk(SkillPerk.TWO_HANDED_LIMBSPLITTER, tooltip("Ranks: 3", "Requires: 30/60/90 Two-handed",
                            "Bonus: Battleaxes inflict increasing bleed damage.",
                            "Effect: Bleed keeps hurting the target after the hit lands."), 0.257D, 0.574D),
                    perk(SkillPerk.TWO_HANDED_CHAMPIONS_STANCE, tooltip("Requires: 20 Two-handed",
                            "Bonus: Two-handed perk power attacks cost 25% less stamina.",
                            "Effect: Keeps heavy weapons from exhausting stamina too quickly."), 0.450D, 0.661D),
                    perk(SkillPerk.TWO_HANDED_DEVASTATING_BLOW, tooltip("Requires: 50 Two-handed",
                            "Bonus: Standing power attacks deal +25% damage.",
                            "Effect: Consumes stamina when the perk attack triggers."), 0.510D, 0.453D),
                    perk(SkillPerk.TWO_HANDED_GREAT_CRITICAL_CHARGE, tooltip("Requires: 50 Two-handed",
                            "Effect: Sprinting full-strength attacks consume stamina and deal 2x damage.",
                            "Utility: Lets heavy weapons open fights with a crushing charge."), 0.377D, 0.458D),
                    perk(SkillPerk.TWO_HANDED_SWEEP, tooltip("Requires: 70 Two-handed",
                            "Effect: Sideways power attacks hit multiple enemies in front of you.",
                            "Utility: Adds crowd control to two-handed melee."), 0.460D, 0.260D),
                    perk(SkillPerk.TWO_HANDED_WARMASTER, tooltip("Requires: 100 Two-handed",
                            "Effect: Backwards full-strength attacks have a 25% chance to paralyze.",
                            "Utility: Gives heavy weapon users a defensive finisher."), 0.463D, 0.116D),
                    perk(SkillPerk.TWO_HANDED_DEEP_WOUNDS, tooltip("Ranks: 3", "Requires: 30/60/90 Two-handed",
                            "Bonus: Greatswords have a 10/15/20% critical chance.",
                            "Effect: Critical hits add bonus damage based on weapon damage."), 0.637D, 0.559D),
                    perk(SkillPerk.TWO_HANDED_SKULLCRUSHER, tooltip("Ranks: 3", "Requires: 30/60/90 Two-handed",
                            "Bonus: Warhammers bypass 25/50/75% of armor mitigation.",
                            "Effect: Strong against heavily armored targets."), 0.770D, 0.550D)),
                    links("Barbarian", "Champion's Stance", "Champion's Stance", "Devastating Blow",
                            "Champion's Stance", "Great Critical Charge", "Great Critical Charge", "Sweep",
                            "Devastating Blow", "Sweep", "Sweep", "Warmaster", "Barbarian", "Deep Wounds",
                            "Barbarian", "Limbsplitter", "Barbarian", "Skullcrusher")),
            tree(ModAttributes.ALTERATION, 300, 276, perks(
                    perk("Novice Alteration", "Requires: 0 Alteration", 0.450D, 0.855D),
                    perk("Alteration Dual Casting", "Requires: 20 Alteration", 0.337D, 0.641D),
                    perk("Apprentice Alteration", "Requires: 25 Alteration", 0.487D, 0.587D),
                    perk("Magic Resistance", "Ranks: 3\nRequires: 30/50/70 Alteration", 0.640D, 0.399D),
                    perk("Adept Alteration", "Requires: 50 Alteration", 0.493D, 0.351D),
                    perk("Expert Alteration", "Requires: 75 Alteration", 0.570D, 0.257D),
                    perk("Atronach", "Requires: 100 Alteration", 0.313D, 0.094D),
                    perk("Master Alteration", "Requires: 100 Alteration", 0.717D, 0.138D),
                    perk("Stability", "Requires: 70 Alteration", 0.393D, 0.239D),
                    perk("Mage Armor", "Ranks: 3\nRequires: 30/50/70 Alteration", 0.357D, 0.380D)),
                    links("Novice Alteration", "Alteration Dual Casting", "Novice Alteration", "Apprentice Alteration",
                            "Apprentice Alteration", "Magic Resistance", "Apprentice Alteration", "Adept Alteration",
                            "Adept Alteration", "Expert Alteration", "Expert Alteration", "Atronach",
                            "Expert Alteration", "Master Alteration", "Adept Alteration", "Stability",
                            "Apprentice Alteration", "Mage Armor")),
            tree(ModAttributes.CONJURATION, 400, 334, perks(
                    perk("Novice Conjuration", "Requires: 0 Conjuration", 0.540D, 0.886D),
                    perk("Apprentice Conjuration", "Requires: 25 Conjuration", 0.715D, 0.560D),
                    perk("Adept Conjuration", "Requires: 50 Conjuration", 0.740D, 0.380D),
                    perk("Expert Conjuration", "Requires: 75 Conjuration", 0.713D, 0.240D),
                    perk("Master Conjuration", "Requires: 100 Conjuration", 0.608D, 0.096D),
                    perk("Conjuration Dual Casting", "Requires: 20 Conjuration", 0.418D, 0.647D),
                    perk("Mystic Binding", "Requires: 20 Conjuration", 0.575D, 0.638D),
                    perk("Soul Stealer", "Requires: 30 Conjuration", 0.593D, 0.329D),
                    perk("Oblivion Binding", "Requires: 50 Conjuration", 0.585D, 0.243D),
                    perk("Necromancy", "Requires: 40 Conjuration", 0.360D, 0.305D),
                    perk("Dark Souls", "Requires: 70 Conjuration", 0.360D, 0.186D),
                    perk("Summoner", "Ranks: 2\nRequires: 30/70 Conjuration", 0.255D, 0.587D),
                    perk("Atromancy", "Requires: 40 Conjuration", 0.230D, 0.314D),
                    perk("Elemental Potency", "Requires: 80 Conjuration", 0.240D, 0.198D),
                    perk("Twin Souls", "Requires: 100 Conjuration", 0.375D, 0.120D)),
                    links("Novice Conjuration", "Apprentice Conjuration", "Apprentice Conjuration", "Adept Conjuration",
                            "Adept Conjuration", "Expert Conjuration", "Expert Conjuration", "Master Conjuration",
                            "Novice Conjuration", "Conjuration Dual Casting", "Novice Conjuration", "Mystic Binding",
                            "Mystic Binding", "Soul Stealer", "Soul Stealer", "Oblivion Binding",
                            "Novice Conjuration", "Necromancy", "Necromancy", "Dark Souls", "Novice Conjuration",
                            "Summoner", "Summoner", "Atromancy", "Atromancy", "Elemental Potency",
                            "Dark Souls", "Twin Souls", "Elemental Potency", "Twin Souls")),
            tree(ModAttributes.DESTRUCTION, 400, 333, perks(
                    perk("Novice Destruction", "Requires: 0 Destruction", 0.440D, 0.880D),
                    perk("Apprentice Destruction", "Requires: 25 Destruction", 0.593D, 0.643D),
                    perk("Adept Destruction", "Requires: 50 Destruction", 0.573D, 0.408D),
                    perk("Expert Destruction", "Requires: 75 Destruction", 0.623D, 0.270D),
                    perk("Master Destruction", "Requires: 100 Destruction", 0.618D, 0.090D),
                    perk("Rune Master", "Requires: 40 Destruction", 0.683D, 0.508D),
                    perk("Augmented Flames", "Ranks: 2\nRequires: 30/60 Destruction", 0.275D, 0.595D),
                    perk("Intense Flames", "Requires: 50 Destruction", 0.253D, 0.432D),
                    perk("Augmented Frost", "Ranks: 2\nRequires: 30/60 Destruction", 0.390D, 0.526D),
                    perk("Deep Freeze", "Requires: 60 Destruction", 0.370D, 0.330D),
                    perk("Augmented Shock", "Ranks: 2\nRequires: 30/60 Destruction", 0.480D, 0.520D),
                    perk("Disintegrate", "Requires: 70 Destruction", 0.475D, 0.279D),
                    perk("Destruction Dual Casting", "Requires: 20 Destruction", 0.678D, 0.787D),
                    perk("Impact", "Requires: 40 Destruction", 0.733D, 0.637D)),
                    links("Novice Destruction", "Apprentice Destruction", "Apprentice Destruction", "Adept Destruction",
                            "Adept Destruction", "Expert Destruction", "Expert Destruction", "Master Destruction",
                            "Apprentice Destruction", "Rune Master", "Novice Destruction", "Augmented Flames",
                            "Augmented Flames", "Intense Flames", "Novice Destruction", "Augmented Frost",
                            "Augmented Frost", "Deep Freeze", "Novice Destruction", "Augmented Shock",
                            "Augmented Shock", "Disintegrate", "Novice Destruction", "Destruction Dual Casting",
                            "Destruction Dual Casting", "Impact")),
            tree(ModAttributes.ENCHANTING, 400, 421, perks(
                    perk("Enchanter", "Ranks: 5\nRequires: 0/20/40/60/80 Enchanting", 0.393D, 0.855D),
                    perk("Fire Enchanter", "Requires: 30 Enchanting", 0.275D, 0.570D),
                    perk("Frost Enchanter", "Requires: 40 Enchanting", 0.295D, 0.401D),
                    perk("Storm Enchanter", "Requires: 50 Enchanting", 0.390D, 0.264D),
                    perk("Extra Effect", "Requires: 100 Enchanting", 0.558D, 0.138D),
                    perk("Insightful Enchanter", "Requires: 50 Enchanting", 0.495D, 0.561D),
                    perk("Corpus Enchanter", "Requires: 70 Enchanting", 0.595D, 0.385D),
                    perk("Soul Squeezer", "Requires: 20 Enchanting", 0.750D, 0.551D),
                    perk("Soul Siphon", "Requires: 40 Enchanting", 0.690D, 0.238D)),
                    links("Enchanter", "Fire Enchanter", "Fire Enchanter", "Frost Enchanter",
                            "Frost Enchanter", "Storm Enchanter", "Enchanter", "Insightful Enchanter",
                            "Insightful Enchanter", "Corpus Enchanter", "Corpus Enchanter", "Extra Effect",
                            "Storm Enchanter", "Extra Effect", "Enchanter", "Soul Squeezer",
                            "Soul Squeezer", "Soul Siphon")),
            tree(ModAttributes.ILLUSION, 400, 316, perks(
                    perk("Novice Illusion", "Requires: 0 Illusion", 0.540D, 0.858D),
                    perk("Animage", "Requires: 20 Illusion", 0.780D, 0.687D),
                    perk("Kindred Mage", "Requires: 40 Illusion", 0.643D, 0.475D),
                    perk("Quiet Casting", "Requires: 50 Illusion", 0.633D, 0.304D),
                    perk("Apprentice Illusion", "Requires: 25 Illusion", 0.315D, 0.595D),
                    perk("Adept Illusion", "Requires: 50 Illusion", 0.330D, 0.389D),
                    perk("Expert Illusion", "Requires: 75 Illusion", 0.320D, 0.282D),
                    perk("Master Illusion", "Requires: 100 Illusion", 0.430D, 0.149D),
                    perk("Hypnotic Gaze", "Requires: 30 Illusion", 0.515D, 0.532D),
                    perk("Aspect of Terror", "Requires: 50 Illusion", 0.445D, 0.339D),
                    perk("Rage", "Requires: 70 Illusion", 0.508D, 0.297D),
                    perk("Master of the Mind", "Requires: 90 Illusion", 0.575D, 0.165D),
                    perk("Illusion Dual Casting", "Requires: 20 Illusion", 0.198D, 0.835D)),
                    links("Novice Illusion", "Animage", "Animage", "Kindred Mage", "Kindred Mage", "Quiet Casting",
                            "Novice Illusion", "Apprentice Illusion", "Apprentice Illusion", "Adept Illusion",
                            "Adept Illusion", "Expert Illusion", "Expert Illusion", "Master Illusion",
                            "Novice Illusion", "Hypnotic Gaze", "Hypnotic Gaze", "Aspect of Terror",
                            "Aspect of Terror", "Rage", "Quiet Casting", "Master of the Mind", "Rage",
                            "Master of the Mind", "Novice Illusion", "Illusion Dual Casting")),
            tree(ModAttributes.RESTORATION, 400, 305, perks(
                    perk("Novice Restoration", "Requires: 0 Restoration", 0.548D, 0.862D),
                    perk("Apprentice Restoration", "Requires: 25 Restoration", 0.583D, 0.633D),
                    perk("Adept Restoration", "Requires: 50 Restoration", 0.530D, 0.393D),
                    perk("Expert Restoration", "Requires: 75 Restoration", 0.545D, 0.187D),
                    perk("Master Restoration", "Requires: 100 Restoration", 0.460D, 0.111D),
                    perk("Recovery", "Ranks: 2\nRequires: 30/60 Restoration", 0.805D, 0.580D),
                    perk("Avoid Death", "Requires: 90 Restoration", 0.875D, 0.426D),
                    perk("Regeneration", "Requires: 20 Restoration", 0.373D, 0.613D),
                    perk("Necromage", "Requires: 70 Restoration", 0.195D, 0.315D),
                    perk("Respite", "Requires: 40 Restoration", 0.138D, 0.541D),
                    perk("Restoration Dual Casting", "Requires: 20 Restoration", 0.735D, 0.702D),
                    perk("Ward Absorb", "Requires: 60 Restoration", 0.400D, 0.334D)),
                    links("Novice Restoration", "Apprentice Restoration", "Apprentice Restoration", "Adept Restoration",
                            "Adept Restoration", "Expert Restoration", "Expert Restoration", "Master Restoration",
                            "Novice Restoration", "Recovery", "Recovery", "Avoid Death", "Novice Restoration",
                            "Regeneration", "Regeneration", "Necromage", "Novice Restoration", "Respite",
                            "Novice Restoration", "Restoration Dual Casting", "Novice Restoration", "Ward Absorb")),
            tree(ModAttributes.ALCHEMY, 300, 301, perks(
                    perk(SkillPerk.ALCHEMY_ALCHEMIST, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Alchemy",
                            "Bonus: Ingredient effect magnitude is 20/40/60/80/100% stronger.",
                            "Effect: Useful duration also improves by 8% per rank where applicable."), 0.243D, 0.910D),
                    perk(SkillPerk.ALCHEMY_PHYSICIAN, tooltip("Requires: 20 Alchemy",
                            "Bonus: Restore Health, Magicka, and Stamina ingredient effects are 25% stronger.",
                            "Effect: Directly improves survival and resource recovery brews."), 0.697D, 0.841D),
                    perk(SkillPerk.ALCHEMY_BENEFACTOR, tooltip("Requires: 30 Alchemy",
                            "Bonus: Beneficial ingredient effects gain +25% magnitude.",
                            "Effect: Strengthens buffs, healing, and other positive mixtures."), 0.610D, 0.648D),
                    perk(SkillPerk.ALCHEMY_EXPERIMENTER, tooltip("Ranks: 3", "Requires: 50/70/90 Alchemy",
                            "Bonus: Eating ingredients reveals 2/3/4 effect entries.",
                            "Effect: Makes ingredient discovery much faster."), 0.573D, 0.495D),
                    perk(SkillPerk.ALCHEMY_PURITY, tooltip("Requires: 100 Alchemy",
                            "Effect: Harmful ingredient effects are suppressed when consumed.",
                            "Utility: Produces cleaner ingredient use for alchemists."), 0.507D, 0.083D),
                    perk(SkillPerk.ALCHEMY_POISONER, tooltip("Requires: 30 Alchemy",
                            "Bonus: Harmful ingredient effects gain +25% magnitude.",
                            "Effect: Makes hostile mixtures more dangerous."), 0.337D, 0.635D),
                    perk(SkillPerk.ALCHEMY_CONCENTRATED_POISON, tooltip("Requires: 60 Alchemy",
                            "Effect: Harmful ingredient effects last twice as long.",
                            "Utility: Extends poison-style pressure."), 0.357D, 0.482D),
                    perk(SkillPerk.ALCHEMY_GREEN_THUMB, tooltip("Requires: 70 Alchemy",
                            "Effect: Harvest extra drops from most plant and crop ingredient sources.",
                            "Utility: Speeds up gathering for potion-heavy characters."), 0.393D, 0.282D),
                    perk(SkillPerk.ALCHEMY_SNAKEBLOOD, tooltip("Requires: 80 Alchemy",
                            "Bonus: Gain a 50% chance to resist incoming poison.",
                            "Effect: Protects alchemists from hostile toxins and venom."), 0.550D, 0.239D)),
                    links("Alchemist", "Physician", "Physician", "Benefactor", "Benefactor", "Experimenter",
                            "Physician", "Poisoner", "Poisoner", "Concentrated Poison", "Concentrated Poison",
                            "Green Thumb", "Experimenter", "Snakeblood", "Concentrated Poison", "Snakeblood",
                            "Snakeblood", "Purity")),
            tree(ModAttributes.LIGHT_ARMOR, 300, 325, perks(
                    perk(SkillPerk.LIGHT_ARMOR_AGILE_DEFENDER, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Light Armor",
                            "Bonus: Light armor rating increases by 20/40/60/80/100%.",
                            "Effect: Shields are not counted in this armor bonus."), 0.613D, 0.908D),
                    perk(SkillPerk.LIGHT_ARMOR_CUSTOM_FIT, tooltip("Requires: 30 Light Armor",
                            "Bonus: Gain +25% armor rating while wearing all light armor.",
                            "Effect: Rewards committing to a full light set."), 0.523D, 0.609D),
                    perk(SkillPerk.LIGHT_ARMOR_MATCHING_SET, tooltip("Requires: 70 Light Armor",
                            "Bonus: Gain another +25% armor rating with a matched light set.",
                            "Effect: Rewards armor pieces from the same material family."), 0.647D, 0.157D),
                    perk(SkillPerk.LIGHT_ARMOR_UNHINDERED, tooltip("Requires: 50 Light Armor",
                            "Effect: Light armor no longer increases sprint stamina drain.",
                            "Utility: Keeps agile builds fast while armored."), 0.333D, 0.378D),
                    perk(SkillPerk.LIGHT_ARMOR_WIND_WALKER, tooltip("Requires: 60 Light Armor",
                            "Bonus: Stamina regenerates 50% faster while wearing all light armor.",
                            "Effect: Supports sprinting, dodging, and repeated power attacks."), 0.367D, 0.225D),
                    perk(SkillPerk.LIGHT_ARMOR_DEFT_MOVEMENT, tooltip("Requires: 100 Light Armor",
                            "Effect: 10% chance to avoid all damage from a melee attack.",
                            "Utility: Gives light armor a final evasive defense."), 0.513D, 0.080D)),
                    links("Agile Defender", "Custom Fit", "Custom Fit", "Matching Set", "Custom Fit", "Unhindered",
                            "Unhindered", "Wind Walker", "Wind Walker", "Deft Movement", "Matching Set",
                            "Deft Movement")),
            tree(ModAttributes.LOCKPICKING, 300, 275, perks(
                    perk("Novice Locks", "Requires: 0 Lockpicking", 0.433D, 0.920D),
                    perk("Apprentice Locks", "Requires: 25 Lockpicking", 0.577D, 0.673D),
                    perk("Adept Locks", "Requires: 50 Lockpicking", 0.680D, 0.433D),
                    perk("Expert Locks", "Requires: 75 Lockpicking", 0.707D, 0.295D),
                    perk("Locksmith", "Requires: 80 Lockpicking", 0.547D, 0.196D),
                    perk("Unbreakable", "Requires: 100 Lockpicking", 0.447D, 0.116D),
                    perk("Master Locks", "Requires: 100 Lockpicking", 0.740D, 0.080D),
                    perk("Golden Touch", "Requires: 60 Lockpicking", 0.503D, 0.367D),
                    perk("Treasure Hunter", "Requires: 70 Lockpicking", 0.380D, 0.262D),
                    perk("Quick Hands", "Requires: 40 Lockpicking", 0.410D, 0.527D),
                    perk("Wax Key", "Requires: 50 Lockpicking", 0.243D, 0.425D)),
                    links("Novice Locks", "Apprentice Locks", "Apprentice Locks", "Quick Hands", "Quick Hands",
                            "Wax Key", "Apprentice Locks", "Adept Locks", "Adept Locks", "Expert Locks",
                            "Adept Locks", "Golden Touch", "Golden Touch", "Treasure Hunter", "Expert Locks",
                            "Locksmith", "Locksmith", "Unbreakable", "Expert Locks", "Master Locks")),
            tree(ModAttributes.PICKPOCKET, 300, 286, perks(
                    perk("Light Fingers", "Ranks: 5\nRequires: 0/20/40/60/80 Pickpocket", 0.327D, 0.906D),
                    perk("Night Thief", "Requires: 30 Pickpocket", 0.443D, 0.661D),
                    perk("Cutpurse", "Requires: 40 Pickpocket", 0.540D, 0.378D),
                    perk("Keymaster", "Requires: 60 Pickpocket", 0.423D, 0.280D),
                    perk("Misdirection", "Requires: 70 Pickpocket", 0.587D, 0.126D),
                    perk("Perfect Touch", "Requires: 100 Pickpocket", 0.677D, 0.087D),
                    perk("Extra Pockets", "Requires: 50 Pickpocket", 0.683D, 0.374D),
                    perk("Poisoned", "Requires: 40 Pickpocket", 0.387D, 0.388D)),
                    links("Light Fingers", "Night Thief", "Night Thief", "Cutpurse", "Cutpurse", "Keymaster",
                            "Cutpurse", "Misdirection", "Misdirection", "Perfect Touch", "Night Thief",
                            "Extra Pockets", "Night Thief", "Poisoned")),
            tree(ModAttributes.SNEAK, 300, 254, perks(
                    perk(SkillPerk.SNEAK_STEALTH, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Sneak",
                            "Bonus: You are 20/25/30/35/40% harder to detect while sneaking.",
                            "Effect: Reduces enemy detection range before light and sound are applied."), 0.463D, 0.921D),
                    perk(SkillPerk.SNEAK_BACKSTAB, tooltip("Requires: 30 Sneak",
                            "Bonus: One-handed melee sneak attacks deal 6x damage.",
                            "Effect: Rewards getting close while undetected."), 0.657D, 0.657D),
                    perk(SkillPerk.SNEAK_DEADLY_AIM, tooltip("Requires: 40 Sneak",
                            "Bonus: Ranged projectile sneak attacks deal 3x damage.",
                            "Effect: Turns hidden opening shots into decisive strikes."), 0.683D, 0.386D),
                    perk(SkillPerk.SNEAK_ASSASSINS_BLADE, tooltip("Requires: 50 Sneak",
                            "Bonus: Dagger sneak attacks deal 15x damage.",
                            "Effect: Highest reward for reaching melee range unseen."), 0.593D, 0.327D),
                    perk(SkillPerk.SNEAK_MUFFLED_MOVEMENT, tooltip("Requires: 30 Sneak",
                            "Bonus: Armor makes 50% less movement noise.",
                            "Effect: Reduces sound-based detection while sneaking."), 0.223D, 0.638D),
                    perk(SkillPerk.SNEAK_LIGHT_FOOT, tooltip("Requires: 40 Sneak",
                            "Bonus: Player sounds are 50% quieter and armor noise is reduced.",
                            "Effect: Pressure plates and tripwires ignore your steps."), 0.327D, 0.358D),
                    perk(SkillPerk.SNEAK_SILENT_ROLL, tooltip("Requires: 50 Sneak",
                            "Effect: Sprinting while sneaking performs a short silent roll.",
                            "Cost: Rolls consume stamina and reduce movement noise."), 0.437D, 0.260D),
                    perk(SkillPerk.SNEAK_SILENCE, tooltip("Requires: 70 Sneak",
                            "Effect: Walking and running no longer affect detection.",
                            "Utility: Removes movement noise from the stealth equation."), 0.570D, 0.138D),
                    perk(SkillPerk.SNEAK_SHADOW_WARRIOR, tooltip("Requires: 100 Sneak",
                            "Effect: Entering crouch breaks nearby enemy targets.",
                            "Utility: Lets a master sneak vanish even after being found."), 0.773D, 0.083D)),
                    links("Stealth", "Backstab", "Backstab", "Deadly Aim", "Deadly Aim", "Assassin's Blade",
                            "Stealth", "Muffled Movement", "Muffled Movement", "Light Foot", "Light Foot",
                            "Silent Roll", "Silent Roll", "Silence", "Silence", "Shadow Warrior")),
            tree(ModAttributes.BARTER, 300, 286, perks(
                    perk(SkillPerk.BARTER_HAGGLING, tooltip("Ranks: 5", "Requires: 0/20/40/60/80 Speech",
                            "Bonus: Villager trade prices are 10/15/20/25/30% better.",
                            "Effect: Improves merchant costs when trading."), 0.317D, 0.927D),
                    perk(SkillPerk.BARTER_ALLURE, tooltip("Requires: 30 Speech",
                            "Bonus: Villager merchants offer another 10% discount.",
                            "Effect: Skyrim-style social charm adapted for merchant trading."), 0.380D, 0.668D),
                    perk(SkillPerk.BARTER_MERCHANT, tooltip("Requires: 50 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Utility: Adapts broad merchant access into better prices."), 0.330D, 0.406D),
                    perk(SkillPerk.BARTER_INVESTOR, tooltip("Requires: 70 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Effect: Completed trades can improve that offer by 1 emerald."), 0.287D, 0.262D),
                    perk(SkillPerk.BARTER_FENCE, tooltip("Requires: 90 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Utility: Adapts fence access into better merchant prices."), 0.250D, 0.143D),
                    perk(SkillPerk.BARTER_MASTER_TRADER, tooltip("Requires: 100 Speech",
                            "Bonus: Villager merchants offer another 15% discount.",
                            "Effect: Successful trades grant a small bonus XP reward."), 0.570D, 0.073D),
                    perk(SkillPerk.BARTER_BRIBERY, tooltip("Requires: 30 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Utility: Adapts bribery into lower merchant costs."), 0.593D, 0.654D),
                    perk(SkillPerk.BARTER_PERSUASION, tooltip("Requires: 50 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Effect: Adapts persuasion into peaceful trade value."), 0.713D, 0.385D),
                    perk(SkillPerk.BARTER_INTIMIDATION, tooltip("Requires: 70 Speech",
                            "Bonus: Villager merchants offer another 5% discount.",
                            "Effect: Adapts intimidation into stronger merchant leverage."), 0.763D, 0.224D)),
                    links("Haggling", "Allure", "Allure", "Merchant", "Merchant", "Investor", "Investor",
                            "Fence", "Fence", "Master Trader", "Haggling", "Bribery", "Bribery", "Persuasion",
                            "Persuasion", "Intimidation"))
    );
    private static final List<SkillTreeDefinition> TREES_WITHOUT_EPIC_FIGHT = treesWithoutEpicFight();

    private static int lastTreeIndex;

    private int treeIndex = lastTreeIndex;
    private int previousTreeIndex = lastTreeIndex;
    private int transitionTreeOffset;
    private long treeTransitionStartMillis;
    private int selectedNodeIndex = -1;

    public SkillTreeScreen() {
        super(Component.translatable("screen.thuumcraft.skill_trees"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        normalizeTreeIndices();
        renderSkyBackground(guiGraphics);
        float transitionProgress = updateTreeTransitionProgress();
        boolean transitioning = isTreeTransitioning();
        renderTreeNavigation(guiGraphics, transitionProgress, transitioning);
        renderTreeCarousel(guiGraphics, mouseX, mouseY, transitionProgress, transitioning);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (!transitioning) {
            renderHoveredNodeTooltip(guiGraphics, currentTree(), mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_A) {
            previousTree();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D) {
            nextTree();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int selectedTree = clickedNavigationTree(mouseX, mouseY);
            if (selectedTree >= 0) {
                setTreeIndex(selectedTree);
                return true;
            }

            int lowerNavigationY = this.height - 32;
            if (mouseY >= lowerNavigationY && mouseX < this.width / 2.0D) {
                previousTree();
                return true;
            }
            if (mouseY >= lowerNavigationY && mouseX >= this.width / 2.0D) {
                nextTree();
                return true;
            }

            SkillTreeDefinition tree = currentTree();
            if (isTreeTransitioning()) {
                return true;
            }
            selectedNodeIndex = hoveredNode(tree, treeBounds(tree), (int) mouseX, (int) mouseY);
            if (selectedNodeIndex >= 0) {
                upgradeNodeIfAvailable(treeIndex, selectedNodeIndex);
            }
            return selectedNodeIndex >= 0;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta > 0.0D) {
            previousTree();
            return true;
        }
        if (delta < 0.0D) {
            nextTree();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderSkyBackground(GuiGraphics guiGraphics) {
        guiGraphics.blit(BACKGROUND, 0, 0, this.width, this.height, 0.0F, 0.0F,
                BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        guiGraphics.fill(0, 0, this.width, this.height, 0xA8040912);
        guiGraphics.fillGradient(0, 0, this.width, this.height / 3, 0xD8000000, 0x22000000);
        guiGraphics.fillGradient(0, this.height * 2 / 3, this.width, this.height, 0x22000000, 0xD8000000);
    }

    private void renderTreeNavigation(GuiGraphics guiGraphics, float transitionProgress, boolean transitioning) {
        if (transitioning) {
            renderTransitionedHeading(guiGraphics, transitionProgress);
        } else {
            SkillTreeDefinition tree = currentTree();
            guiGraphics.drawCenteredString(this.font, skillTitle(tree), this.width / 2, 18, TEXT_COLOR);
        }
        guiGraphics.drawCenteredString(this.font, "Level " + ClientSkillPerkState.playerLevel(),
                this.width / 2, 31, TEXT_COLOR);
        guiGraphics.drawCenteredString(this.font, "Perk Points " + ClientSkillPerkState.perkPoints(),
                this.width / 2, 42, MUTED_TEXT_COLOR);
        renderBottomSkillStrip(guiGraphics, transitionProgress, transitioning);
        guiGraphics.drawCenteredString(this.font, Component.literal("<"), this.width / 2 - 118, this.height - 18, DIM_TEXT_COLOR);
        guiGraphics.drawCenteredString(this.font, Component.literal(">"), this.width / 2 + 118, this.height - 18, DIM_TEXT_COLOR);
    }

    private void renderTransitionedHeading(GuiGraphics guiGraphics, float transitionProgress) {
        int direction = Integer.signum(transitionTreeOffset);
        int slide = Math.min(36, Math.max(18, this.width / 18));
        List<SkillTreeDefinition> trees = availableTrees();
        SkillTreeDefinition previousTree = trees.get(wrapTreeIndex(previousTreeIndex));
        SkillTreeDefinition tree = currentTree();
        Component previousHeading = skillTitle(previousTree);
        Component heading = skillTitle(tree);

        int previousX = this.width / 2 - Math.round(direction * slide * transitionProgress);
        int currentX = this.width / 2 + Math.round(direction * slide * (1.0F - transitionProgress));
        guiGraphics.drawCenteredString(this.font, previousHeading, previousX, 18,
                withAlpha(TEXT_COLOR, 1.0F - transitionProgress));
        guiGraphics.drawCenteredString(this.font, heading, currentX, 18, withAlpha(TEXT_COLOR, transitionProgress));
    }

    private void renderBottomSkillStrip(GuiGraphics guiGraphics, float transitionProgress, boolean transitioning) {
        int centerX = this.width / 2;
        int y = this.height - 31;
        int spacing = Math.max(58, Math.min(86, this.width / VISIBLE_NAVIGATION_TREES));
        int middle = VISIBLE_NAVIGATION_TREES / 2;
        int baseIndex = transitioning ? previousTreeIndex : treeIndex;
        float animatedOffset = transitioning ? transitionTreeOffset * transitionProgress : 0.0F;
        int extraItems = transitioning ? Math.abs(transitionTreeOffset) : 0;
        List<SkillTreeDefinition> trees = availableTrees();

        for (int offset = -middle - extraItems; offset <= middle + extraItems; offset++) {
            float visualOffset = offset - animatedOffset;
            if (Math.abs(visualOffset) > middle + 0.95F) {
                continue;
            }

            int index = wrapTreeIndex(baseIndex + offset);
            SkillTreeDefinition tree = trees.get(index);
            int x = centerX + Math.round(visualOffset * spacing);
            int color = navigationColor(visualOffset);
            guiGraphics.drawCenteredString(this.font, skillTitle(tree), x, y, color);
        }
    }

    private void renderTreeCarousel(GuiGraphics guiGraphics, int mouseX, int mouseY, float transitionProgress,
                                    boolean transitioning) {
        int baseIndex = transitioning ? previousTreeIndex : treeIndex;
        float animatedOffset = transitioning ? transitionTreeOffset * transitionProgress : 0.0F;
        int extraItems = transitioning ? Math.abs(transitionTreeOffset) : 0;
        int maximumOffset = VISIBLE_SIDE_TREES + extraItems;
        float spacing = treeCarouselSpacing(treeBounds(currentTree()));
        List<SkillTreeDefinition> trees = availableTrees();

        for (int layer = VISIBLE_SIDE_TREES + 1; layer >= 0; layer--) {
            for (int offset = -maximumOffset; offset <= maximumOffset; offset++) {
                float visualOffset = offset - animatedOffset;
                float distance = Math.abs(visualOffset);
                if (distance > VISIBLE_SIDE_TREES + 0.65F || Math.round(distance) != layer) {
                    continue;
                }

                boolean centered = !transitioning && offset == 0;
                float scale = treeScale(distance);
                float alpha = treeAlpha(distance);
                float xOffset = visualOffset * spacing;
                float yOffset = Math.min(26.0F, distance * 14.0F);
                int renderedTreeIndex = wrapTreeIndex(baseIndex + offset);
                SkillTreeDefinition tree = trees.get(renderedTreeIndex);
                TreeBounds bounds = treeBounds(tree);
                renderTree(guiGraphics, renderedTreeIndex, tree, bounds, mouseX, mouseY, xOffset, yOffset, scale,
                        alpha, centered);
            }
        }
    }

    private void renderTree(GuiGraphics guiGraphics, int renderedTreeIndex, SkillTreeDefinition tree, TreeBounds bounds,
                            int mouseX, int mouseY, float xOffset, float yOffset, float scale, float alpha,
                            boolean interactive) {
        int transformedMouseX = Math.round((mouseX - this.width / 2.0F - xOffset) / scale + this.width / 2.0F);
        int transformedMouseY = Math.round((mouseY - this.height / 2.0F - yOffset) / scale + this.height / 2.0F);
        int hoveredNode = interactive ? hoveredNode(tree, bounds, transformedMouseX, transformedMouseY) : -1;
        int nodeGlowColor = withAlpha(NODE_GLOW_COLOR, alpha);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.width / 2.0F + xOffset, this.height / 2.0F + yOffset, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.pose().translate(-this.width / 2.0F, -this.height / 2.0F, 0.0F);
        for (Edge edge : tree.edges()) {
            Node from = tree.nodes().get(edge.from());
            Node to = tree.nodes().get(edge.to());
            int fromX = bounds.x(from.x());
            int fromY = bounds.y(from.y());
            int toX = bounds.x(to.x());
            int toY = bounds.y(to.y());
            float edgeAlpha = edgeAlpha(renderedTreeIndex, edge, alpha);
            drawLine(guiGraphics, fromX, fromY, toX, toY, withAlpha(LINE_GLOW_COLOR, edgeAlpha), 1);
            drawLine(guiGraphics, fromX, fromY, toX, toY, withAlpha(LINE_COLOR, edgeAlpha), 0);
        }

        for (int i = 0; i < tree.nodes().size(); i++) {
            Node node = tree.nodes().get(i);
            int x = bounds.x(node.x());
            int y = bounds.y(node.y());
            int rank = rankForNode(renderedTreeIndex, i);
            int color = nodeColor(renderedTreeIndex, i, alpha, interactive && i == hoveredNode);
            int glowColor = rank > 0 || canUpgradeNode(renderedTreeIndex, i) ? nodeGlowColor
                    : withAlpha(NODE_GLOW_COLOR, alpha * 0.3F);
            int coreColor = rank > 0 ? withAlpha(0xFFFFFFFF, alpha) : withAlpha(0xFFFFFFFF, alpha * 0.45F);
            drawNode(guiGraphics, x, y, color, glowColor, coreColor);
            drawRankPips(guiGraphics, x, y, node.maximumRank(), rank, alpha);
        }
        if (interactive) {
            renderPerkLabels(guiGraphics, renderedTreeIndex, tree, bounds, alpha);
        }
        guiGraphics.pose().popPose();
    }

    private void renderPerkLabels(GuiGraphics guiGraphics, int renderedTreeIndex, SkillTreeDefinition tree,
                                  TreeBounds bounds, float alpha) {
        for (int i = 0; i < tree.nodes().size(); i++) {
            Node node = tree.nodes().get(i);
            int x = bounds.x(node.x());
            int y = bounds.y(node.y());
            int textWidth = this.font.width(node.name());
            int labelX = Math.max(bounds.left(), Math.min(x - textWidth / 2, bounds.left() + bounds.width() - textWidth));
            int labelY = y + 9;
            if (labelY > bounds.top() + bounds.height() - 8) {
                labelY = y - 17;
            }
            int color = rankForNode(renderedTreeIndex, i) > 0 || canUpgradeNode(renderedTreeIndex, i)
                    ? PERK_LABEL_COLOR : PERK_LABEL_LOCKED_COLOR;
            guiGraphics.drawString(this.font, Component.literal(node.name()), labelX, labelY,
                    withAlpha(color, alpha), false);
        }
    }

    private void drawRankPips(GuiGraphics guiGraphics, int x, int y, int maximumRank, int currentRank, float alpha) {
        if (maximumRank <= 1) {
            return;
        }

        int startX = x - maximumRank * 2;
        int pipY = y + 8;
        for (int rank = 0; rank < maximumRank; rank++) {
            int color = rank < currentRank ? withAlpha(NODE_SELECTED_COLOR, alpha)
                    : withAlpha(NODE_LOCKED_COLOR, alpha * 0.75F);
            int pipX = startX + rank * 4;
            guiGraphics.fill(pipX, pipY, pipX + 2, pipY + 2, color);
        }
    }

    private void renderHoveredNodeTooltip(GuiGraphics guiGraphics, SkillTreeDefinition tree, int mouseX, int mouseY) {
        int hoveredNode = hoveredNode(tree, treeBounds(tree), mouseX, mouseY);
        if (hoveredNode < 0) {
            return;
        }

        Node node = tree.nodes().get(hoveredNode);
        int currentRank = rankForNode(treeIndex, hoveredNode);
        int maximumRank = node.maximumRank();
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(formatTooltipTitle(node.name()));
        if (maximumRank > 1) {
            lines.add(formatTooltipRank(currentRank, maximumRank));
        }
        for (String detail : node.tooltip().split("\n")) {
            if (!detail.isBlank() && !detail.startsWith("Ranks: ") && !detail.startsWith("Requires: ")) {
                addTooltipDetailLine(lines, detail);
            }
        }
        lines.add(formatTooltipStatus(nodeStatus(treeIndex, hoveredNode)));

        int tooltipWidth = 0;
        for (Component line : lines) {
            tooltipWidth = Math.max(tooltipWidth, this.font.width(line));
        }

        int lineHeight = 10;
        int tooltipHeight = lines.size() * lineHeight + 7;
        int x = Math.max(4, Math.min(mouseX + 12, this.width - tooltipWidth - 12));
        int y = Math.max(4, Math.min(mouseY + 12, this.height - tooltipHeight - 8));

        guiGraphics.fill(x - 4, y - 4, x + tooltipWidth + 4, y + tooltipHeight - 2, TOOLTIP_BACKGROUND_COLOR);
        guiGraphics.fill(x - 4, y - 4, x + tooltipWidth + 4, y - 3, TOOLTIP_BORDER_COLOR);
        guiGraphics.fill(x - 4, y + tooltipHeight - 3, x + tooltipWidth + 4, y + tooltipHeight - 2, TOOLTIP_BORDER_COLOR);
        guiGraphics.fill(x - 4, y - 4, x - 3, y + tooltipHeight - 2, TOOLTIP_BORDER_COLOR);
        guiGraphics.fill(x + tooltipWidth + 3, y - 4, x + tooltipWidth + 4, y + tooltipHeight - 2, TOOLTIP_BORDER_COLOR);

        for (int i = 0; i < lines.size(); i++) {
            int color = i == 0 ? NODE_HOVER_COLOR : TEXT_COLOR;
            guiGraphics.drawString(this.font, lines.get(i), x, y + i * lineHeight, color);
        }
    }

    private void addTooltipDetailLine(ArrayList<Component> lines, String detail) {
        if (this.font.width(detail) <= TOOLTIP_WRAP_WIDTH) {
            lines.add(formatTooltipDetail(detail, false));
            return;
        }

        StringBuilder line = new StringBuilder();
        boolean continuation = false;
        for (String word : detail.split(" ")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (line.length() > 0 && this.font.width(candidate) > TOOLTIP_WRAP_WIDTH) {
                lines.add(formatTooltipDetail(line.toString(), continuation));
                continuation = true;
                line.setLength(0);
                line.append(word);
            } else {
                if (line.length() > 0) {
                    line.append(' ');
                }
                line.append(word);
            }
        }
        if (line.length() > 0) {
            lines.add(formatTooltipDetail(line.toString(), continuation));
        }
    }

    private static Component formatTooltipTitle(String title) {
        return Component.literal(title).withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE);
    }

    private static Component formatTooltipRank(int currentRank, int maximumRank) {
        return Component.literal("Rank: " + currentRank + "/" + maximumRank);
    }

    private static Component formatTooltipDetail(String detail, boolean continuation) {
        return Component.literal(continuation ? "  " + detail : detail);
    }

    private static Component formatTooltipStatus(String status) {
        if (status.equals("Unlocked")) {
            return Component.literal(status).withStyle(ChatFormatting.GREEN);
        }
        if (status.equals("Available")) {
            return Component.literal(status).withStyle(ChatFormatting.GOLD);
        }
        if (status.equals("Locked") || status.startsWith("Requires") || status.contains("Locked")) {
            return Component.literal(status).withStyle(ChatFormatting.RED);
        }
        if (status.equals("Not implemented yet")) {
            return Component.literal(status).withStyle(ChatFormatting.DARK_GRAY);
        }
        return Component.literal(status).withStyle(ChatFormatting.GRAY);
    }

    private int nodeColor(int renderedTreeIndex, int nodeIndex, float alpha, boolean hovered) {
        Node node = availableTrees().get(renderedTreeIndex).nodes().get(nodeIndex);
        if (rankForNode(renderedTreeIndex, nodeIndex) >= node.maximumRank()) {
            return withAlpha(NODE_SELECTED_COLOR, alpha);
        }
        if (canUpgradeNode(renderedTreeIndex, nodeIndex)) {
            return withAlpha(hovered ? NODE_HOVER_COLOR : NODE_AVAILABLE_COLOR, alpha);
        }
        return withAlpha(NODE_LOCKED_COLOR, alpha);
    }

    private float edgeAlpha(int renderedTreeIndex, Edge edge, float alpha) {
        int fromRank = rankForNode(renderedTreeIndex, edge.from());
        int toRank = rankForNode(renderedTreeIndex, edge.to());
        if (fromRank > 0 && toRank > 0) {
            return alpha;
        }
        if (fromRank > 0 || canUpgradeNode(renderedTreeIndex, edge.to())) {
            return alpha * 0.7F;
        }
        return alpha * 0.28F;
    }

    private String nodeStatus(int treeIndex, int nodeIndex) {
        SkillTreeDefinition tree = availableTrees().get(treeIndex);
        Node node = tree.nodes().get(nodeIndex);
        if (node.perk() == null) {
            return "Not implemented yet";
        }
        int rank = rankForNode(treeIndex, nodeIndex);
        if (rank >= node.maximumRank()) {
            return "Unlocked";
        }
        if (canUpgradeNode(treeIndex, nodeIndex)) {
            return "Available";
        }
        int requiredSkill = node.requiredSkillForRank(rank);
        int skillValue = skillValue(tree);
        if (skillValue < requiredSkill) {
            String prefix = rank > 0 ? "Rank " + rank + " unlocked. " : "";
            return prefix + "Locked";
        }
        if (!prerequisitesMet(treeIndex, nodeIndex, node)) {
            String prefix = rank > 0 ? "Rank " + rank + " unlocked. " : "";
            return prefix + "Requires prerequisite perk";
        }
        if (node.perk() != null && ClientSkillPerkState.perkPoints() <= 0) {
            return "Requires perk point";
        }
        return "Locked";
    }

    private void upgradeNodeIfAvailable(int treeIndex, int nodeIndex) {
        if (!canUpgradeNode(treeIndex, nodeIndex)) {
            return;
        }

        Node node = availableTrees().get(treeIndex).nodes().get(nodeIndex);
        if (node.perk() != null) {
            ModMessages.sendToServer(new ServerboundUnlockPerkPacket(node.perk().id()));
        }
    }

    private boolean canUpgradeNode(int treeIndex, int nodeIndex) {
        SkillTreeDefinition tree = availableTrees().get(treeIndex);
        Node node = tree.nodes().get(nodeIndex);
        if (node.perk() == null) {
            return false;
        }
        int currentRank = rankForNode(treeIndex, nodeIndex);
        return currentRank < node.maximumRank()
                && ClientSkillPerkState.perkPoints() > 0
                && skillValue(tree) >= node.requiredSkillForRank(currentRank)
                && prerequisitesMet(treeIndex, nodeIndex, node);
    }

    private boolean prerequisitesMet(int treeIndex, int nodeIndex, Node node) {
        SkillTreeDefinition tree = availableTrees().get(treeIndex);
        for (String prerequisite : node.prerequisites()) {
            int prerequisiteIndex = findNodeIndex(tree.nodes(), prerequisite);
            if (prerequisiteIndex >= 0 && rankForNode(treeIndex, prerequisiteIndex) > 0) {
                return true;
            }
        }
        if (!node.prerequisites().isEmpty()) {
            return false;
        }

        boolean hasIncomingEdge = false;
        for (Edge edge : tree.edges()) {
            if (edge.to() == nodeIndex) {
                hasIncomingEdge = true;
                if (rankForNode(treeIndex, edge.from()) > 0) {
                    return true;
                }
            }
        }
        if (hasIncomingEdge) {
            return false;
        }
        return true;
    }

    private int rankForNode(int treeIndex, int nodeIndex) {
        Node node = availableTrees().get(treeIndex).nodes().get(nodeIndex);
        return node.perk() == null ? 0 : ClientSkillPerkState.rank(node.perk());
    }

    private void drawNode(GuiGraphics guiGraphics, int x, int y, int color, int glowColor, int coreColor) {
        guiGraphics.fill(x - 5, y, x + 6, y + 1, glowColor);
        guiGraphics.fill(x, y - 5, x + 1, y + 6, glowColor);
        guiGraphics.fill(x - 3, y, x + 4, y + 1, color);
        guiGraphics.fill(x, y - 3, x + 1, y + 4, color);
        guiGraphics.fill(x - 1, y - 1, x + 2, y + 2, coreColor);
    }

    private void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color, int radius) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int error = dx - dy;
        int x = x1;
        int y = y1;

        while (true) {
            guiGraphics.fill(x - radius, y - radius, x + radius + 1, y + radius + 1, color);
            if (x == x2 && y == y2) {
                return;
            }

            int doubledError = error * 2;
            if (doubledError > -dy) {
                error -= dy;
                x += sx;
            }
            if (doubledError < dx) {
                error += dx;
                y += sy;
            }
        }
    }

    private int hoveredNode(SkillTreeDefinition tree, TreeBounds bounds, int mouseX, int mouseY) {
        for (int i = 0; i < tree.nodes().size(); i++) {
            Node node = tree.nodes().get(i);
            int x = bounds.x(node.x());
            int y = bounds.y(node.y());
            int dx = mouseX - x;
            int dy = mouseY - y;
            if (dx * dx + dy * dy <= NODE_PICK_RADIUS * NODE_PICK_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private TreeBounds treeBounds(SkillTreeDefinition tree) {
        int maximumWidth = Math.min(Math.max(this.width - 128, 170), 440);
        int maximumHeight = Math.min(Math.max(this.height - 96, 126), 300);
        double sourceAspectRatio = tree.sourceWidth() / (double) tree.sourceHeight();
        int treeWidth = maximumWidth;
        int treeHeight = (int) Math.round(treeWidth / sourceAspectRatio);
        if (treeHeight > maximumHeight) {
            treeHeight = maximumHeight;
            treeWidth = (int) Math.round(treeHeight * sourceAspectRatio);
        }

        int left = this.width / 2 - treeWidth / 2;
        int top = this.height / 2 - treeHeight / 2 + 4;
        return new TreeBounds(left, top, treeWidth, treeHeight);
    }

    private float treeCarouselSpacing(TreeBounds bounds) {
        float wideSpacing = Math.min(bounds.width() + 72.0F, this.width * 0.4375F);
        return Math.max(bounds.width() * 0.75F, wideSpacing);
    }

    private float treeScale(float distance) {
        return Math.max(0.62F, 1.0F - distance * 0.18F);
    }

    private float treeAlpha(float distance) {
        return Math.max(0.22F, 1.0F - distance * 0.36F);
    }

    private static List<SkillTreeDefinition> availableTrees() {
        return ClientSkillPerkState.meleeSkillTreesEnabled() ? TREES : TREES_WITHOUT_EPIC_FIGHT;
    }

    private static List<SkillTreeDefinition> treesWithoutEpicFight() {
        ArrayList<SkillTreeDefinition> filteredTrees = new ArrayList<>(TREES.size() - 2);
        for (SkillTreeDefinition tree : TREES) {
            if (!isEpicFightTree(tree)) {
                filteredTrees.add(tree);
            }
        }
        return List.copyOf(filteredTrees);
    }

    private static boolean isEpicFightTree(SkillTreeDefinition tree) {
        return tree.attribute().equals(ModAttributes.ONE_HANDED)
                || tree.attribute().equals(ModAttributes.TWO_HANDED);
    }

    private void normalizeTreeIndices() {
        int normalizedTreeIndex = wrapTreeIndex(treeIndex);
        int normalizedPreviousTreeIndex = wrapTreeIndex(previousTreeIndex);
        if (treeIndex != normalizedTreeIndex || previousTreeIndex != normalizedPreviousTreeIndex) {
            treeIndex = normalizedTreeIndex;
            previousTreeIndex = normalizedPreviousTreeIndex;
            if (treeIndex == previousTreeIndex) {
                transitionTreeOffset = 0;
                treeTransitionStartMillis = 0L;
            }
        }
        lastTreeIndex = treeIndex;
    }

    private SkillTreeDefinition currentTree() {
        normalizeTreeIndices();
        return availableTrees().get(treeIndex);
    }

    private void previousTree() {
        setTreeIndex(wrapTreeIndex(treeIndex - 1));
    }

    private void nextTree() {
        setTreeIndex(wrapTreeIndex(treeIndex + 1));
    }

    private void setTreeIndex(int treeIndex) {
        int nextTreeIndex = wrapTreeIndex(treeIndex);
        int currentTreeIndex = wrapTreeIndex(this.treeIndex);
        if (nextTreeIndex == currentTreeIndex) {
            return;
        }

        this.previousTreeIndex = currentTreeIndex;
        this.transitionTreeOffset = shortestWrappedDistance(this.previousTreeIndex, nextTreeIndex);
        this.treeTransitionStartMillis = Util.getMillis();
        this.treeIndex = nextTreeIndex;
        lastTreeIndex = this.treeIndex;
        selectedNodeIndex = -1;
    }

    private int clickedNavigationTree(double mouseX, double mouseY) {
        int y = this.height - 31;
        if (mouseY < y - 8 || mouseY > y + 12) {
            return -1;
        }

        int centerX = this.width / 2;
        int spacing = Math.max(58, Math.min(86, this.width / VISIBLE_NAVIGATION_TREES));
        int middle = VISIBLE_NAVIGATION_TREES / 2;
        for (int offset = -middle; offset <= middle; offset++) {
            int x = centerX + offset * spacing;
            if (Math.abs(mouseX - x) <= spacing / 2.0D) {
                return wrapTreeIndex(treeIndex + offset);
            }
        }
        return -1;
    }

    private int wrapTreeIndex(int index) {
        int treeCount = availableTrees().size();
        return (index % treeCount + treeCount) % treeCount;
    }

    private int shortestWrappedDistance(int from, int to) {
        int forward = wrapTreeIndex(to - from);
        int treeCount = availableTrees().size();
        int backward = forward - treeCount;
        return forward <= treeCount / 2 ? forward : backward;
    }

    private boolean isTreeTransitioning() {
        return treeTransitionStartMillis > 0L && previousTreeIndex != treeIndex;
    }

    private float updateTreeTransitionProgress() {
        if (!isTreeTransitioning()) {
            return 1.0F;
        }

        float progress = (Util.getMillis() - treeTransitionStartMillis) / (float) TREE_TRANSITION_MILLIS;
        if (progress >= 1.0F) {
            previousTreeIndex = treeIndex;
            transitionTreeOffset = 0;
            treeTransitionStartMillis = 0L;
            return 1.0F;
        }

        return smoothStep(Math.max(0.0F, Math.min(1.0F, progress)));
    }

    private float smoothStep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

    private int navigationColor(float visualOffset) {
        float distance = Math.abs(visualOffset);
        if (distance < 1.0F) {
            return lerpColor(TEXT_COLOR, MUTED_TEXT_COLOR, distance);
        }
        if (distance < 2.0F) {
            return lerpColor(MUTED_TEXT_COLOR, DIM_TEXT_COLOR, distance - 1.0F);
        }

        return withAlpha(DIM_TEXT_COLOR, Math.max(0.0F, 1.0F - (distance - 2.0F)));
    }

    private int lerpColor(int from, int to, float amount) {
        float clampedAmount = Math.max(0.0F, Math.min(1.0F, amount));
        int alpha = lerpChannel(from >>> 24, to >>> 24, clampedAmount);
        int red = lerpChannel(from >>> 16, to >>> 16, clampedAmount);
        int green = lerpChannel(from >>> 8, to >>> 8, clampedAmount);
        int blue = lerpChannel(from, to, clampedAmount);
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    private int lerpChannel(int from, int to, float amount) {
        return Math.round((from & 0xFF) + ((to & 0xFF) - (from & 0xFF)) * amount);
    }

    private int withAlpha(int color, float alphaMultiplier) {
        int alpha = color >>> 24 & 0xFF;
        int adjustedAlpha = Math.round(alpha * Math.max(0.0F, Math.min(1.0F, alphaMultiplier)));
        return color & 0x00FFFFFF | adjustedAlpha << 24;
    }

    private int skillValue(SkillTreeDefinition tree) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return 0;
        }

        AttributeInstance instance = this.minecraft.player.getAttribute(tree.attribute().get());
        return instance == null ? 0 : (int) Math.round(instance.getValue());
    }

    private Component skillTitle(SkillTreeDefinition tree) {
        return tree.displayName().copy()
                .append(" ")
                .append(Component.literal(String.valueOf(skillValue(tree))).withStyle(ChatFormatting.BOLD));
    }

    private static SkillTreeDefinition tree(RegistryObject<Attribute> attribute, int sourceWidth, int sourceHeight,
                                            List<Node> nodes, List<EdgeDefinition> edges) {
        return new SkillTreeDefinition(attribute, sourceWidth, sourceHeight, nodes, indexedEdges(nodes, edges));
    }

    private static List<Node> perks(Node... nodes) {
        return List.of(nodes);
    }

    private static Node perk(String name, String tooltip, double x, double y) {
        return new Node(null, name, tooltip, skillRequirements(tooltip), prerequisites(tooltip), x, y);
    }

    private static Node perk(SkillPerk perk, String tooltip, double x, double y) {
        return new Node(perk, perk.displayName(), tooltip, skillRequirements(tooltip), prerequisites(tooltip), x, y);
    }

    private static String tooltip(String... lines) {
        return String.join("\n", lines);
    }

    private static List<EdgeDefinition> links(String... names) {
        if (names.length % 2 != 0) {
            throw new IllegalArgumentException("Edge names must be from/to pairs");
        }

        ArrayList<EdgeDefinition> edges = new ArrayList<>(names.length / 2);
        for (int i = 0; i < names.length; i += 2) {
            edges.add(new EdgeDefinition(names[i], names[i + 1]));
        }
        return List.copyOf(edges);
    }

    private static List<Edge> indexedEdges(List<Node> nodes, List<EdgeDefinition> edges) {
        ArrayList<Edge> indexedEdges = new ArrayList<>(edges.size());
        for (EdgeDefinition edge : edges) {
            indexedEdges.add(new Edge(nodeIndex(nodes, edge.from()), nodeIndex(nodes, edge.to())));
        }
        return List.copyOf(indexedEdges);
    }

    private static int nodeIndex(List<Node> nodes, String name) {
        int index = findNodeIndex(nodes, name);
        if (index >= 0) {
            return index;
        }
        throw new IllegalArgumentException("Unknown perk node: " + name);
    }

    private static int findNodeIndex(List<Node> nodes, String name) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).name().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private static int[] skillRequirements(String tooltip) {
        String skillLine = tooltipLine(tooltip, "Requires: ");
        if (skillLine.isEmpty()) {
            return new int[]{0};
        }

        ArrayList<Integer> requirements = new ArrayList<>();
        for (String token : skillLine.split("[^0-9]+")) {
            if (!token.isBlank()) {
                requirements.add(Integer.parseInt(token));
            }
        }

        if (requirements.isEmpty()) {
            return new int[]{0};
        }

        int[] values = new int[requirements.size()];
        for (int i = 0; i < requirements.size(); i++) {
            values[i] = requirements.get(i);
        }
        return values;
    }

    private static List<String> prerequisites(String tooltip) {
        String prerequisiteLine = tooltipLine(tooltip, "Requires: ");
        if (prerequisiteLine.isEmpty() || isSkillRequirementLine(prerequisiteLine)) {
            return List.of();
        }

        ArrayList<String> prerequisites = new ArrayList<>();
        for (String prerequisite : prerequisiteLine.split("\\s+or\\s+")) {
            String trimmedPrerequisite = prerequisite.trim();
            if (!trimmedPrerequisite.isEmpty()) {
                prerequisites.add(trimmedPrerequisite);
            }
        }
        return List.copyOf(prerequisites);
    }

    private static boolean isSkillRequirementLine(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static String tooltipLine(String tooltip, String prefix) {
        for (String line : tooltip.split("\n")) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        return "";
    }

    private record SkillTreeDefinition(RegistryObject<Attribute> attribute, int sourceWidth, int sourceHeight,
                                       List<Node> nodes, List<Edge> edges) {
        private Component displayName() {
            return Component.translatable(attribute.get().getDescriptionId());
        }
    }

    private record Node(SkillPerk perk, String name, String tooltip, int[] skillRequirements,
                        List<String> prerequisites, double x, double y) {
        private int maximumRank() {
            return skillRequirements.length;
        }

        private int requiredSkillForRank(int rank) {
            int clampedRank = Math.max(0, Math.min(rank, skillRequirements.length - 1));
            return skillRequirements[clampedRank];
        }
    }

    private record Edge(int from, int to) {
    }

    private record EdgeDefinition(String from, String to) {
    }

    private record TreeBounds(int left, int top, int width, int height) {
        private int x(double normalizedX) {
            return left + (int) Math.round(normalizedX * width);
        }

        private int y(double normalizedY) {
            return top + (int) Math.round(normalizedY * height);
        }
    }

}
