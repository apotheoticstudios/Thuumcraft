package net.apotheoticstudios.thuumcraft.client.gui;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
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
    private static final int VISIBLE_NAVIGATION_TREES = 5;
    private static final int VISIBLE_SIDE_TREES = 2;
    private static final long TREE_TRANSITION_MILLIS = 220L;

    private static final List<SkillTreeDefinition> TREES = List.of(
            tree(ModAttributes.ARCHERY, 400, 487, perks(
                    perk("Overdraw", "Ranks: 5\nSkill: 0/20/40/60/80 Archery", 0.730D, 0.749D),
                    perk("Critical Shot", "Ranks: 3\nSkill: 30/60/90 Archery\nRequires: Overdraw", 0.668D, 0.497D),
                    perk("Eagle Eye", "Skill: 30 Archery\nRequires: Overdraw", 0.400D, 0.550D),
                    perk("Steady Hand", "Ranks: 2\nSkill: 40/60 Archery\nRequires: Eagle Eye", 0.510D, 0.499D),
                    perk("Power Shot", "Skill: 50 Archery\nRequires: Eagle Eye", 0.323D, 0.396D),
                    perk("Hunter's Discipline", "Skill: 50 Archery\nRequires: Critical Shot", 0.560D, 0.308D),
                    perk("Ranger", "Skill: 60 Archery\nRequires: Hunter's Discipline", 0.528D, 0.234D),
                    perk("Quick Shot", "Skill: 70 Archery\nRequires: Power Shot", 0.330D, 0.199D),
                    perk("Bullseye", "Skill: 100 Archery\nRequires: Ranger or Quick Shot", 0.443D, 0.142D)),
                    links("Overdraw", "Critical Shot", "Critical Shot", "Hunter's Discipline",
                            "Hunter's Discipline", "Ranger", "Overdraw", "Eagle Eye", "Eagle Eye", "Power Shot",
                            "Power Shot", "Quick Shot", "Eagle Eye", "Steady Hand", "Ranger", "Bullseye",
                            "Quick Shot", "Bullseye")),
            tree(ModAttributes.BLOCK, 300, 275, perks(
                    perk("Shield Wall", "Ranks: 5\nSkill: 0/20/40/60/80 Block", 0.527D, 0.862D),
                    perk("Deflect Arrows", "Skill: 30 Block\nRequires: Shield Wall", 0.093D, 0.629D),
                    perk("Block Runner", "Skill: 70 Block\nRequires: Elemental Protection", 0.340D, 0.175D),
                    perk("Elemental Protection", "Skill: 50 Block\nRequires: Deflect Arrows", 0.217D, 0.262D),
                    perk("Quick Reflexes", "Skill: 30 Block\nRequires: Shield Wall", 0.427D, 0.545D),
                    perk("Power Bash", "Skill: 30 Block\nRequires: Shield Wall", 0.887D, 0.615D),
                    perk("Deadly Bash", "Skill: 50 Block\nRequires: Power Bash", 0.867D, 0.356D),
                    perk("Disarming Bash", "Skill: 70 Block\nRequires: Deadly Bash", 0.787D, 0.164D),
                    perk("Shield Charge", "Skill: 100 Block\nRequires: Disarming Bash or Block Runner", 0.527D, 0.102D)),
                    links("Shield Wall", "Deflect Arrows", "Deflect Arrows", "Elemental Protection",
                            "Elemental Protection", "Block Runner", "Shield Wall", "Power Bash", "Power Bash",
                            "Deadly Bash", "Deadly Bash", "Disarming Bash", "Disarming Bash", "Shield Charge",
                            "Block Runner", "Shield Charge", "Shield Wall", "Quick Reflexes")),
            tree(ModAttributes.HEAVY_ARMOR, 300, 292, perks(
                    perk("Juggernaut", "Ranks: 5\nSkill: 0/20/40/60/80 Heavy Armor", 0.503D, 0.901D),
                    perk("Fists of Steel", "Skill: 30 Heavy Armor\nRequires: Juggernaut", 0.277D, 0.685D),
                    perk("Cushioned", "Skill: 50 Heavy Armor\nRequires: Fists of Steel", 0.157D, 0.479D),
                    perk("Conditioning", "Skill: 70 Heavy Armor\nRequires: Cushioned", 0.167D, 0.247D),
                    perk("Well Fitted", "Skill: 30 Heavy Armor\nRequires: Juggernaut", 0.727D, 0.668D),
                    perk("Tower of Strength", "Skill: 50 Heavy Armor\nRequires: Well Fitted", 0.790D, 0.438D),
                    perk("Matching Set", "Skill: 70 Heavy Armor\nRequires: Tower of Strength", 0.850D, 0.301D),
                    perk("Reflect Blows", "Skill: 100 Heavy Armor\nRequires: Matching Set", 0.800D, 0.072D)),
                    links("Juggernaut", "Fists of Steel", "Fists of Steel", "Cushioned", "Cushioned",
                            "Conditioning", "Juggernaut", "Well Fitted", "Well Fitted", "Tower of Strength",
                            "Tower of Strength", "Matching Set", "Matching Set", "Reflect Blows")),
            tree(ModAttributes.ONE_HANDED, 300, 282, perks(
                    perk("Armsman", "Ranks: 5\nSkill: 0/20/40/60/80 One-handed", 0.437D, 0.929D),
                    perk("Bladesman", "Ranks: 3\nSkill: 30/60/90 One-handed\nRequires: Armsman", 0.623D, 0.596D),
                    perk("Bone Breaker", "Ranks: 3\nSkill: 30/60/90 One-handed\nRequires: Armsman", 0.533D, 0.592D),
                    perk("Dual Flurry", "Ranks: 2\nSkill: 30/50 One-handed\nRequires: Armsman", 0.783D, 0.784D),
                    perk("Dual Savagery", "Skill: 70 One-handed\nRequires: Dual Flurry", 0.687D, 0.301D),
                    perk("Fighting Stance", "Skill: 20 One-handed\nRequires: Armsman", 0.443D, 0.709D),
                    perk("Critical Charge", "Skill: 50 One-handed\nRequires: Fighting Stance", 0.503D, 0.443D),
                    perk("Savage Strike", "Skill: 50 One-handed\nRequires: Fighting Stance", 0.370D, 0.433D),
                    perk("Paralyzing Strike", "Skill: 100 One-handed\nRequires: Critical Charge or Savage Strike", 0.443D, 0.064D),
                    perk("Hack and Slash", "Ranks: 3\nSkill: 30/60/90 One-handed\nRequires: Armsman", 0.260D, 0.574D)),
                    links("Armsman", "Bladesman", "Armsman", "Bone Breaker", "Armsman", "Dual Flurry",
                            "Dual Flurry", "Dual Savagery", "Armsman", "Fighting Stance", "Fighting Stance",
                            "Critical Charge", "Fighting Stance", "Savage Strike", "Critical Charge",
                            "Paralyzing Strike", "Savage Strike", "Paralyzing Strike", "Armsman", "Hack and Slash")),
            tree(ModAttributes.SMITHING, 400, 210, perks(
                    perk("Steel Smithing", "Skill: 0 Smithing", 0.365D, 0.819D),
                    perk("Arcane Blacksmith", "Skill: 60 Smithing\nRequires: Steel Smithing", 0.388D, 0.371D),
                    perk("Elven Smithing", "Skill: 30 Smithing\nRequires: Steel Smithing", 0.080D, 0.362D),
                    perk("Advanced Armors", "Skill: 50 Smithing\nRequires: Elven Smithing", 0.108D, 0.257D),
                    perk("Glass Smithing", "Skill: 70 Smithing\nRequires: Advanced Armors", 0.310D, 0.114D),
                    perk("Dragon Armor", "Skill: 100 Smithing\nRequires: Daedric Smithing or Glass Smithing", 0.505D, 0.114D),
                    perk("Dwarven Smithing", "Skill: 30 Smithing\nRequires: Steel Smithing", 0.580D, 0.486D),
                    perk("Orcish Smithing", "Skill: 50 Smithing\nRequires: Dwarven Smithing", 0.795D, 0.310D),
                    perk("Ebony Smithing", "Skill: 80 Smithing\nRequires: Orcish Smithing", 0.963D, 0.314D),
                    perk("Daedric Smithing", "Skill: 90 Smithing\nRequires: Ebony Smithing", 0.675D, 0.162D)),
                    links("Steel Smithing", "Arcane Blacksmith", "Steel Smithing", "Dwarven Smithing",
                            "Dwarven Smithing", "Orcish Smithing", "Orcish Smithing", "Ebony Smithing",
                            "Ebony Smithing", "Daedric Smithing", "Steel Smithing", "Elven Smithing",
                            "Elven Smithing", "Advanced Armors", "Advanced Armors", "Glass Smithing",
                            "Daedric Smithing", "Dragon Armor", "Glass Smithing", "Dragon Armor")),
            tree(ModAttributes.TWO_HANDED, 300, 404, perks(
                    perk("Barbarian", "Ranks: 5\nSkill: 0/20/40/60/80 Two-handed", 0.443D, 0.844D),
                    perk("Limbsplitter", "Ranks: 3\nSkill: 30/60/90 Two-handed\nRequires: Barbarian", 0.257D, 0.574D),
                    perk("Champion's Stance", "Skill: 20 Two-handed\nRequires: Barbarian", 0.450D, 0.661D),
                    perk("Devastating Blow", "Skill: 50 Two-handed\nRequires: Champion's Stance", 0.510D, 0.453D),
                    perk("Great Critical Charge", "Skill: 50 Two-handed\nRequires: Champion's Stance", 0.377D, 0.458D),
                    perk("Sweep", "Skill: 70 Two-handed\nRequires: Great Critical Charge or Devastating Blow", 0.460D, 0.260D),
                    perk("Warmaster", "Skill: 100 Two-handed\nRequires: Sweep", 0.463D, 0.116D),
                    perk("Deep Wounds", "Ranks: 3\nSkill: 30/60/90 Two-handed\nRequires: Barbarian", 0.637D, 0.559D),
                    perk("Skullcrusher", "Ranks: 3\nSkill: 30/60/90 Two-handed\nRequires: Barbarian", 0.770D, 0.550D)),
                    links("Barbarian", "Champion's Stance", "Champion's Stance", "Devastating Blow",
                            "Champion's Stance", "Great Critical Charge", "Great Critical Charge", "Sweep",
                            "Devastating Blow", "Sweep", "Sweep", "Warmaster", "Barbarian", "Deep Wounds",
                            "Barbarian", "Limbsplitter", "Barbarian", "Skullcrusher")),
            tree(ModAttributes.ALTERATION, 300, 276, perks(
                    perk("Novice Alteration", "Skill: 0 Alteration", 0.450D, 0.855D),
                    perk("Alteration Dual Casting", "Skill: 20 Alteration\nRequires: Novice Alteration", 0.337D, 0.641D),
                    perk("Apprentice Alteration", "Skill: 25 Alteration\nRequires: Novice Alteration", 0.487D, 0.587D),
                    perk("Magic Resistance", "Ranks: 3\nSkill: 30/50/70 Alteration\nRequires: Apprentice Alteration", 0.640D, 0.399D),
                    perk("Adept Alteration", "Skill: 50 Alteration\nRequires: Apprentice Alteration", 0.493D, 0.351D),
                    perk("Expert Alteration", "Skill: 75 Alteration\nRequires: Adept Alteration", 0.570D, 0.257D),
                    perk("Atronach", "Skill: 100 Alteration\nRequires: Expert Alteration", 0.313D, 0.094D),
                    perk("Master Alteration", "Skill: 100 Alteration\nRequires: Expert Alteration", 0.717D, 0.138D),
                    perk("Stability", "Skill: 70 Alteration\nRequires: Adept Alteration", 0.393D, 0.239D),
                    perk("Mage Armor", "Ranks: 3\nSkill: 30/50/70 Alteration\nRequires: Apprentice Alteration", 0.357D, 0.380D)),
                    links("Novice Alteration", "Alteration Dual Casting", "Novice Alteration", "Apprentice Alteration",
                            "Apprentice Alteration", "Magic Resistance", "Apprentice Alteration", "Adept Alteration",
                            "Adept Alteration", "Expert Alteration", "Expert Alteration", "Atronach",
                            "Expert Alteration", "Master Alteration", "Adept Alteration", "Stability",
                            "Apprentice Alteration", "Mage Armor")),
            tree(ModAttributes.CONJURATION, 400, 334, perks(
                    perk("Novice Conjuration", "Skill: 0 Conjuration", 0.540D, 0.886D),
                    perk("Apprentice Conjuration", "Skill: 25 Conjuration\nRequires: Novice Conjuration", 0.715D, 0.560D),
                    perk("Adept Conjuration", "Skill: 50 Conjuration\nRequires: Apprentice Conjuration", 0.740D, 0.380D),
                    perk("Expert Conjuration", "Skill: 75 Conjuration\nRequires: Adept Conjuration", 0.713D, 0.240D),
                    perk("Master Conjuration", "Skill: 100 Conjuration\nRequires: Expert Conjuration", 0.608D, 0.096D),
                    perk("Conjuration Dual Casting", "Skill: 20 Conjuration\nRequires: Novice Conjuration", 0.418D, 0.647D),
                    perk("Mystic Binding", "Skill: 20 Conjuration\nRequires: Novice Conjuration", 0.575D, 0.638D),
                    perk("Soul Stealer", "Skill: 30 Conjuration\nRequires: Mystic Binding", 0.593D, 0.329D),
                    perk("Oblivion Binding", "Skill: 50 Conjuration\nRequires: Soul Stealer", 0.585D, 0.243D),
                    perk("Necromancy", "Skill: 40 Conjuration\nRequires: Novice Conjuration", 0.360D, 0.305D),
                    perk("Dark Souls", "Skill: 70 Conjuration\nRequires: Necromancy", 0.360D, 0.186D),
                    perk("Summoner", "Ranks: 2\nSkill: 30/70 Conjuration\nRequires: Novice Conjuration", 0.255D, 0.587D),
                    perk("Atromancy", "Skill: 40 Conjuration\nRequires: Summoner", 0.230D, 0.314D),
                    perk("Elemental Potency", "Skill: 80 Conjuration\nRequires: Atromancy", 0.240D, 0.198D),
                    perk("Twin Souls", "Skill: 100 Conjuration\nRequires: Dark Souls or Elemental Potency", 0.375D, 0.120D)),
                    links("Novice Conjuration", "Apprentice Conjuration", "Apprentice Conjuration", "Adept Conjuration",
                            "Adept Conjuration", "Expert Conjuration", "Expert Conjuration", "Master Conjuration",
                            "Novice Conjuration", "Conjuration Dual Casting", "Novice Conjuration", "Mystic Binding",
                            "Mystic Binding", "Soul Stealer", "Soul Stealer", "Oblivion Binding",
                            "Novice Conjuration", "Necromancy", "Necromancy", "Dark Souls", "Novice Conjuration",
                            "Summoner", "Summoner", "Atromancy", "Atromancy", "Elemental Potency",
                            "Dark Souls", "Twin Souls", "Elemental Potency", "Twin Souls")),
            tree(ModAttributes.DESTRUCTION, 400, 333, perks(
                    perk("Novice Destruction", "Skill: 0 Destruction", 0.440D, 0.880D),
                    perk("Apprentice Destruction", "Skill: 25 Destruction\nRequires: Novice Destruction", 0.593D, 0.643D),
                    perk("Adept Destruction", "Skill: 50 Destruction\nRequires: Apprentice Destruction", 0.573D, 0.408D),
                    perk("Expert Destruction", "Skill: 75 Destruction\nRequires: Adept Destruction", 0.623D, 0.270D),
                    perk("Master Destruction", "Skill: 100 Destruction\nRequires: Expert Destruction", 0.618D, 0.090D),
                    perk("Rune Master", "Skill: 40 Destruction\nRequires: Apprentice Destruction", 0.683D, 0.508D),
                    perk("Augmented Flames", "Ranks: 2\nSkill: 30/60 Destruction\nRequires: Novice Destruction", 0.275D, 0.595D),
                    perk("Intense Flames", "Skill: 50 Destruction\nRequires: Augmented Flames", 0.253D, 0.432D),
                    perk("Augmented Frost", "Ranks: 2\nSkill: 30/60 Destruction\nRequires: Novice Destruction", 0.390D, 0.526D),
                    perk("Deep Freeze", "Skill: 60 Destruction\nRequires: Augmented Frost", 0.370D, 0.330D),
                    perk("Augmented Shock", "Ranks: 2\nSkill: 30/60 Destruction\nRequires: Novice Destruction", 0.480D, 0.520D),
                    perk("Disintegrate", "Skill: 70 Destruction\nRequires: Augmented Shock", 0.475D, 0.279D),
                    perk("Destruction Dual Casting", "Skill: 20 Destruction\nRequires: Novice Destruction", 0.678D, 0.787D),
                    perk("Impact", "Skill: 40 Destruction\nRequires: Destruction Dual Casting", 0.733D, 0.637D)),
                    links("Novice Destruction", "Apprentice Destruction", "Apprentice Destruction", "Adept Destruction",
                            "Adept Destruction", "Expert Destruction", "Expert Destruction", "Master Destruction",
                            "Apprentice Destruction", "Rune Master", "Novice Destruction", "Augmented Flames",
                            "Augmented Flames", "Intense Flames", "Novice Destruction", "Augmented Frost",
                            "Augmented Frost", "Deep Freeze", "Novice Destruction", "Augmented Shock",
                            "Augmented Shock", "Disintegrate", "Novice Destruction", "Destruction Dual Casting",
                            "Destruction Dual Casting", "Impact")),
            tree(ModAttributes.ENCHANTING, 400, 421, perks(
                    perk("Enchanter", "Ranks: 5\nSkill: 0/20/40/60/80 Enchanting", 0.393D, 0.855D),
                    perk("Fire Enchanter", "Skill: 30 Enchanting\nRequires: Enchanter", 0.275D, 0.570D),
                    perk("Frost Enchanter", "Skill: 40 Enchanting\nRequires: Fire Enchanter", 0.295D, 0.401D),
                    perk("Storm Enchanter", "Skill: 50 Enchanting\nRequires: Frost Enchanter", 0.390D, 0.264D),
                    perk("Extra Effect", "Skill: 100 Enchanting\nRequires: Corpus Enchanter or Storm Enchanter", 0.558D, 0.138D),
                    perk("Insightful Enchanter", "Skill: 50 Enchanting\nRequires: Enchanter", 0.495D, 0.561D),
                    perk("Corpus Enchanter", "Skill: 70 Enchanting\nRequires: Insightful Enchanter", 0.595D, 0.385D),
                    perk("Soul Squeezer", "Skill: 20 Enchanting\nRequires: Enchanter", 0.750D, 0.551D),
                    perk("Soul Siphon", "Skill: 40 Enchanting\nRequires: Soul Squeezer", 0.690D, 0.238D)),
                    links("Enchanter", "Fire Enchanter", "Fire Enchanter", "Frost Enchanter",
                            "Frost Enchanter", "Storm Enchanter", "Enchanter", "Insightful Enchanter",
                            "Insightful Enchanter", "Corpus Enchanter", "Corpus Enchanter", "Extra Effect",
                            "Storm Enchanter", "Extra Effect", "Enchanter", "Soul Squeezer",
                            "Soul Squeezer", "Soul Siphon")),
            tree(ModAttributes.ILLUSION, 400, 316, perks(
                    perk("Novice Illusion", "Skill: 0 Illusion", 0.540D, 0.858D),
                    perk("Animage", "Skill: 20 Illusion\nRequires: Novice Illusion", 0.780D, 0.687D),
                    perk("Kindred Mage", "Skill: 40 Illusion\nRequires: Animage", 0.643D, 0.475D),
                    perk("Quiet Casting", "Skill: 50 Illusion\nRequires: Kindred Mage", 0.633D, 0.304D),
                    perk("Apprentice Illusion", "Skill: 25 Illusion\nRequires: Novice Illusion", 0.315D, 0.595D),
                    perk("Adept Illusion", "Skill: 50 Illusion\nRequires: Apprentice Illusion", 0.330D, 0.389D),
                    perk("Expert Illusion", "Skill: 75 Illusion\nRequires: Adept Illusion", 0.320D, 0.282D),
                    perk("Master Illusion", "Skill: 100 Illusion\nRequires: Expert Illusion", 0.430D, 0.149D),
                    perk("Hypnotic Gaze", "Skill: 30 Illusion\nRequires: Novice Illusion", 0.515D, 0.532D),
                    perk("Aspect of Terror", "Skill: 50 Illusion\nRequires: Hypnotic Gaze", 0.445D, 0.339D),
                    perk("Rage", "Skill: 70 Illusion\nRequires: Aspect of Terror", 0.508D, 0.297D),
                    perk("Master of the Mind", "Skill: 90 Illusion\nRequires: Quiet Casting or Rage", 0.575D, 0.165D),
                    perk("Illusion Dual Casting", "Skill: 20 Illusion\nRequires: Novice Illusion", 0.198D, 0.835D)),
                    links("Novice Illusion", "Animage", "Animage", "Kindred Mage", "Kindred Mage", "Quiet Casting",
                            "Novice Illusion", "Apprentice Illusion", "Apprentice Illusion", "Adept Illusion",
                            "Adept Illusion", "Expert Illusion", "Expert Illusion", "Master Illusion",
                            "Novice Illusion", "Hypnotic Gaze", "Hypnotic Gaze", "Aspect of Terror",
                            "Aspect of Terror", "Rage", "Quiet Casting", "Master of the Mind", "Rage",
                            "Master of the Mind", "Novice Illusion", "Illusion Dual Casting")),
            tree(ModAttributes.RESTORATION, 400, 305, perks(
                    perk("Novice Restoration", "Skill: 0 Restoration", 0.548D, 0.862D),
                    perk("Apprentice Restoration", "Skill: 25 Restoration\nRequires: Novice Restoration", 0.583D, 0.633D),
                    perk("Adept Restoration", "Skill: 50 Restoration\nRequires: Apprentice Restoration", 0.530D, 0.393D),
                    perk("Expert Restoration", "Skill: 75 Restoration\nRequires: Adept Restoration", 0.545D, 0.187D),
                    perk("Master Restoration", "Skill: 100 Restoration\nRequires: Expert Restoration", 0.460D, 0.111D),
                    perk("Recovery", "Ranks: 2\nSkill: 30/60 Restoration\nRequires: Novice Restoration", 0.805D, 0.580D),
                    perk("Avoid Death", "Skill: 90 Restoration\nRequires: Recovery", 0.875D, 0.426D),
                    perk("Regeneration", "Skill: 20 Restoration\nRequires: Novice Restoration", 0.373D, 0.613D),
                    perk("Necromage", "Skill: 70 Restoration\nRequires: Regeneration", 0.195D, 0.315D),
                    perk("Respite", "Skill: 40 Restoration\nRequires: Novice Restoration", 0.138D, 0.541D),
                    perk("Restoration Dual Casting", "Skill: 20 Restoration\nRequires: Novice Restoration", 0.735D, 0.702D),
                    perk("Ward Absorb", "Skill: 60 Restoration\nRequires: Novice Restoration", 0.400D, 0.334D)),
                    links("Novice Restoration", "Apprentice Restoration", "Apprentice Restoration", "Adept Restoration",
                            "Adept Restoration", "Expert Restoration", "Expert Restoration", "Master Restoration",
                            "Novice Restoration", "Recovery", "Recovery", "Avoid Death", "Novice Restoration",
                            "Regeneration", "Regeneration", "Necromage", "Novice Restoration", "Respite",
                            "Novice Restoration", "Restoration Dual Casting", "Novice Restoration", "Ward Absorb")),
            tree(ModAttributes.ALCHEMY, 300, 301, perks(
                    perk("Alchemist", "Ranks: 5\nSkill: 0/20/40/60/80 Alchemy", 0.243D, 0.910D),
                    perk("Physician", "Skill: 20 Alchemy\nRequires: Alchemist", 0.697D, 0.841D),
                    perk("Benefactor", "Skill: 30 Alchemy\nRequires: Physician", 0.610D, 0.648D),
                    perk("Experimenter", "Ranks: 3\nSkill: 50/70/90 Alchemy\nRequires: Benefactor", 0.573D, 0.495D),
                    perk("Purity", "Skill: 100 Alchemy\nRequires: Snakeblood", 0.507D, 0.083D),
                    perk("Poisoner", "Skill: 30 Alchemy\nRequires: Physician", 0.337D, 0.635D),
                    perk("Concentrated Poison", "Skill: 60 Alchemy\nRequires: Poisoner", 0.357D, 0.482D),
                    perk("Green Thumb", "Skill: 70 Alchemy\nRequires: Concentrated Poison", 0.393D, 0.282D),
                    perk("Snakeblood", "Skill: 80 Alchemy\nRequires: Experimenter or Concentrated Poison", 0.550D, 0.239D)),
                    links("Alchemist", "Physician", "Physician", "Benefactor", "Benefactor", "Experimenter",
                            "Physician", "Poisoner", "Poisoner", "Concentrated Poison", "Concentrated Poison",
                            "Green Thumb", "Experimenter", "Snakeblood", "Concentrated Poison", "Snakeblood",
                            "Snakeblood", "Purity")),
            tree(ModAttributes.LIGHT_ARMOR, 300, 325, perks(
                    perk("Agile Defender", "Ranks: 5\nSkill: 0/20/40/60/80 Light Armor", 0.613D, 0.908D),
                    perk("Custom Fit", "Skill: 30 Light Armor\nRequires: Agile Defender", 0.523D, 0.609D),
                    perk("Matching Set", "Skill: 70 Light Armor\nRequires: Custom Fit", 0.647D, 0.157D),
                    perk("Unhindered", "Skill: 50 Light Armor\nRequires: Custom Fit", 0.333D, 0.378D),
                    perk("Wind Walker", "Skill: 60 Light Armor\nRequires: Unhindered", 0.367D, 0.225D),
                    perk("Deft Movement", "Skill: 100 Light Armor\nRequires: Wind Walker or Matching Set", 0.513D, 0.080D)),
                    links("Agile Defender", "Custom Fit", "Custom Fit", "Matching Set", "Custom Fit", "Unhindered",
                            "Unhindered", "Wind Walker", "Wind Walker", "Deft Movement", "Matching Set",
                            "Deft Movement")),
            tree(ModAttributes.LOCKPICKING, 300, 275, perks(
                    perk("Novice Locks", "Skill: 0 Lockpicking", 0.433D, 0.920D),
                    perk("Apprentice Locks", "Skill: 25 Lockpicking\nRequires: Novice Locks", 0.577D, 0.673D),
                    perk("Adept Locks", "Skill: 50 Lockpicking\nRequires: Apprentice Locks", 0.680D, 0.433D),
                    perk("Expert Locks", "Skill: 75 Lockpicking\nRequires: Adept Locks", 0.707D, 0.295D),
                    perk("Locksmith", "Skill: 80 Lockpicking\nRequires: Expert Locks", 0.547D, 0.196D),
                    perk("Unbreakable", "Skill: 100 Lockpicking\nRequires: Locksmith", 0.447D, 0.116D),
                    perk("Master Locks", "Skill: 100 Lockpicking\nRequires: Expert Locks", 0.740D, 0.080D),
                    perk("Golden Touch", "Skill: 60 Lockpicking\nRequires: Adept Locks", 0.503D, 0.367D),
                    perk("Treasure Hunter", "Skill: 70 Lockpicking\nRequires: Golden Touch", 0.380D, 0.262D),
                    perk("Quick Hands", "Skill: 40 Lockpicking\nRequires: Apprentice Locks", 0.410D, 0.527D),
                    perk("Wax Key", "Skill: 50 Lockpicking\nRequires: Quick Hands", 0.243D, 0.425D)),
                    links("Novice Locks", "Apprentice Locks", "Apprentice Locks", "Quick Hands", "Quick Hands",
                            "Wax Key", "Apprentice Locks", "Adept Locks", "Adept Locks", "Expert Locks",
                            "Adept Locks", "Golden Touch", "Golden Touch", "Treasure Hunter", "Expert Locks",
                            "Locksmith", "Locksmith", "Unbreakable", "Expert Locks", "Master Locks")),
            tree(ModAttributes.PICKPOCKET, 300, 286, perks(
                    perk("Light Fingers", "Ranks: 5\nSkill: 0/20/40/60/80 Pickpocket", 0.327D, 0.906D),
                    perk("Night Thief", "Skill: 30 Pickpocket\nRequires: Light Fingers", 0.443D, 0.661D),
                    perk("Cutpurse", "Skill: 40 Pickpocket\nRequires: Night Thief", 0.540D, 0.378D),
                    perk("Keymaster", "Skill: 60 Pickpocket\nRequires: Cutpurse", 0.423D, 0.280D),
                    perk("Misdirection", "Skill: 70 Pickpocket\nRequires: Cutpurse", 0.587D, 0.126D),
                    perk("Perfect Touch", "Skill: 100 Pickpocket\nRequires: Misdirection", 0.677D, 0.087D),
                    perk("Extra Pockets", "Skill: 50 Pickpocket\nRequires: Night Thief", 0.683D, 0.374D),
                    perk("Poisoned", "Skill: 40 Pickpocket\nRequires: Night Thief", 0.387D, 0.388D)),
                    links("Light Fingers", "Night Thief", "Night Thief", "Cutpurse", "Cutpurse", "Keymaster",
                            "Cutpurse", "Misdirection", "Misdirection", "Perfect Touch", "Night Thief",
                            "Extra Pockets", "Night Thief", "Poisoned")),
            tree(ModAttributes.SNEAK, 300, 254, perks(
                    perk("Stealth", "Ranks: 5\nSkill: 0/20/40/60/80 Sneak", 0.463D, 0.921D),
                    perk("Backstab", "Skill: 30 Sneak\nRequires: Stealth", 0.657D, 0.657D),
                    perk("Deadly Aim", "Skill: 40 Sneak\nRequires: Backstab", 0.683D, 0.386D),
                    perk("Assassin's Blade", "Skill: 50 Sneak\nRequires: Deadly Aim", 0.593D, 0.327D),
                    perk("Muffled Movement", "Skill: 30 Sneak\nRequires: Stealth", 0.223D, 0.638D),
                    perk("Light Foot", "Skill: 40 Sneak\nRequires: Muffled Movement", 0.327D, 0.358D),
                    perk("Silent Roll", "Skill: 50 Sneak\nRequires: Light Foot", 0.437D, 0.260D),
                    perk("Silence", "Skill: 70 Sneak\nRequires: Silent Roll", 0.570D, 0.138D),
                    perk("Shadow Warrior", "Skill: 100 Sneak\nRequires: Silence", 0.773D, 0.083D)),
                    links("Stealth", "Backstab", "Backstab", "Deadly Aim", "Deadly Aim", "Assassin's Blade",
                            "Stealth", "Muffled Movement", "Muffled Movement", "Light Foot", "Light Foot",
                            "Silent Roll", "Silent Roll", "Silence", "Silence", "Shadow Warrior")),
            tree(ModAttributes.BARTER, 300, 286, perks(
                    perk("Haggling", "Ranks: 5\nSkill: 0/20/40/60/80 Speech", 0.317D, 0.927D),
                    perk("Allure", "Skill: 30 Speech\nRequires: Haggling", 0.380D, 0.668D),
                    perk("Merchant", "Skill: 50 Speech\nRequires: Allure", 0.330D, 0.406D),
                    perk("Investor", "Skill: 70 Speech\nRequires: Merchant", 0.287D, 0.262D),
                    perk("Fence", "Skill: 90 Speech\nRequires: Investor", 0.250D, 0.143D),
                    perk("Master Trader", "Skill: 100 Speech\nRequires: Fence", 0.570D, 0.073D),
                    perk("Bribery", "Skill: 30 Speech\nRequires: Haggling", 0.593D, 0.654D),
                    perk("Persuasion", "Skill: 50 Speech\nRequires: Bribery", 0.713D, 0.385D),
                    perk("Intimidation", "Skill: 70 Speech\nRequires: Persuasion", 0.763D, 0.224D)),
                    links("Haggling", "Allure", "Allure", "Merchant", "Merchant", "Investor", "Investor",
                            "Fence", "Fence", "Master Trader", "Haggling", "Bribery", "Bribery", "Persuasion",
                            "Persuasion", "Intimidation"))
    );

    private static final List<int[]> PERK_RANKS = createPerkRanks();

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
        renderBottomSkillStrip(guiGraphics, transitionProgress, transitioning);
        guiGraphics.drawCenteredString(this.font, Component.literal("<"), this.width / 2 - 118, this.height - 18, DIM_TEXT_COLOR);
        guiGraphics.drawCenteredString(this.font, Component.literal(">"), this.width / 2 + 118, this.height - 18, DIM_TEXT_COLOR);
    }

    private void renderTransitionedHeading(GuiGraphics guiGraphics, float transitionProgress) {
        int direction = Integer.signum(transitionTreeOffset);
        int slide = Math.min(36, Math.max(18, this.width / 18));
        SkillTreeDefinition previousTree = TREES.get(previousTreeIndex);
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

        for (int offset = -middle - extraItems; offset <= middle + extraItems; offset++) {
            float visualOffset = offset - animatedOffset;
            if (Math.abs(visualOffset) > middle + 0.95F) {
                continue;
            }

            int index = wrapTreeIndex(baseIndex + offset);
            SkillTreeDefinition tree = TREES.get(index);
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
                SkillTreeDefinition tree = TREES.get(renderedTreeIndex);
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
        int[] ranks = PERK_RANKS.get(renderedTreeIndex);
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
            int color = nodeColor(renderedTreeIndex, i, alpha, interactive && i == hoveredNode);
            int glowColor = ranks[i] > 0 || canUpgradeNode(renderedTreeIndex, i) ? nodeGlowColor
                    : withAlpha(NODE_GLOW_COLOR, alpha * 0.3F);
            int coreColor = ranks[i] > 0 ? withAlpha(0xFFFFFFFF, alpha) : withAlpha(0xFFFFFFFF, alpha * 0.45F);
            drawNode(guiGraphics, x, y, color, glowColor, coreColor);
            drawRankPips(guiGraphics, x, y, node.maximumRank(), ranks[i], alpha);
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
            int color = PERK_RANKS.get(renderedTreeIndex)[i] > 0 || canUpgradeNode(renderedTreeIndex, i)
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
        int currentRank = PERK_RANKS.get(treeIndex)[hoveredNode];
        int maximumRank = node.maximumRank();
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(Component.literal(node.name()));
        lines.add(Component.literal("Rank: " + currentRank + "/" + maximumRank));
        for (String detail : node.tooltip().split("\n")) {
            if (!detail.isBlank()) {
                lines.add(Component.literal(detail));
            }
        }
        lines.add(Component.literal(nodeStatus(treeIndex, hoveredNode)));

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

    private int nodeColor(int renderedTreeIndex, int nodeIndex, float alpha, boolean hovered) {
        int[] ranks = PERK_RANKS.get(renderedTreeIndex);
        Node node = TREES.get(renderedTreeIndex).nodes().get(nodeIndex);
        if (ranks[nodeIndex] >= node.maximumRank()) {
            return withAlpha(NODE_SELECTED_COLOR, alpha);
        }
        if (canUpgradeNode(renderedTreeIndex, nodeIndex)) {
            return withAlpha(hovered ? NODE_HOVER_COLOR : NODE_AVAILABLE_COLOR, alpha);
        }
        return withAlpha(NODE_LOCKED_COLOR, alpha);
    }

    private float edgeAlpha(int renderedTreeIndex, Edge edge, float alpha) {
        int[] ranks = PERK_RANKS.get(renderedTreeIndex);
        if (ranks[edge.from()] > 0 && ranks[edge.to()] > 0) {
            return alpha;
        }
        if (ranks[edge.from()] > 0 || canUpgradeNode(renderedTreeIndex, edge.to())) {
            return alpha * 0.7F;
        }
        return alpha * 0.28F;
    }

    private String nodeStatus(int treeIndex, int nodeIndex) {
        Node node = TREES.get(treeIndex).nodes().get(nodeIndex);
        int rank = PERK_RANKS.get(treeIndex)[nodeIndex];
        if (rank >= node.maximumRank()) {
            return "Unlocked";
        }
        if (canUpgradeNode(treeIndex, nodeIndex)) {
            return "Available";
        }
        int requiredSkill = node.requiredSkillForRank(rank);
        int skillValue = skillValue(TREES.get(treeIndex));
        if (skillValue < requiredSkill) {
            String prefix = rank > 0 ? "Rank " + rank + " unlocked. " : "";
            return prefix + "Requires " + requiredSkill + " skill";
        }
        if (!prerequisitesMet(treeIndex, node)) {
            String prefix = rank > 0 ? "Rank " + rank + " unlocked. " : "";
            return prefix + "Requires prerequisite perk";
        }
        return "Locked";
    }

    private void upgradeNodeIfAvailable(int treeIndex, int nodeIndex) {
        if (!canUpgradeNode(treeIndex, nodeIndex)) {
            return;
        }

        PERK_RANKS.get(treeIndex)[nodeIndex]++;
    }

    private boolean canUpgradeNode(int treeIndex, int nodeIndex) {
        SkillTreeDefinition tree = TREES.get(treeIndex);
        Node node = tree.nodes().get(nodeIndex);
        int currentRank = PERK_RANKS.get(treeIndex)[nodeIndex];
        return currentRank < node.maximumRank()
                && skillValue(tree) >= node.requiredSkillForRank(currentRank)
                && prerequisitesMet(treeIndex, node);
    }

    private boolean prerequisitesMet(int treeIndex, Node node) {
        if (node.prerequisites().isEmpty()) {
            return true;
        }

        int[] ranks = PERK_RANKS.get(treeIndex);
        SkillTreeDefinition tree = TREES.get(treeIndex);
        for (String prerequisite : node.prerequisites()) {
            int prerequisiteIndex = nodeIndex(tree.nodes(), prerequisite);
            if (ranks[prerequisiteIndex] > 0) {
                return true;
            }
        }
        return false;
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

    private SkillTreeDefinition currentTree() {
        return TREES.get(treeIndex);
    }

    private void previousTree() {
        setTreeIndex(wrapTreeIndex(treeIndex - 1));
    }

    private void nextTree() {
        setTreeIndex(wrapTreeIndex(treeIndex + 1));
    }

    private void setTreeIndex(int treeIndex) {
        int nextTreeIndex = wrapTreeIndex(treeIndex);
        if (nextTreeIndex == this.treeIndex) {
            return;
        }

        this.previousTreeIndex = this.treeIndex;
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
        return (index % TREES.size() + TREES.size()) % TREES.size();
    }

    private int shortestWrappedDistance(int from, int to) {
        int forward = wrapTreeIndex(to - from);
        int backward = forward - TREES.size();
        return forward <= TREES.size() / 2 ? forward : backward;
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
        return Component.literal(String.valueOf(skillValue(tree))).withStyle(ChatFormatting.BOLD)
                .append(" ")
                .append(tree.displayName());
    }

    private static List<int[]> createPerkRanks() {
        ArrayList<int[]> ranks = new ArrayList<>(TREES.size());
        for (SkillTreeDefinition tree : TREES) {
            ranks.add(new int[tree.nodes().size()]);
        }
        return List.copyOf(ranks);
    }

    private static SkillTreeDefinition tree(RegistryObject<Attribute> attribute, int sourceWidth, int sourceHeight,
                                            List<Node> nodes, List<EdgeDefinition> edges) {
        return new SkillTreeDefinition(attribute, sourceWidth, sourceHeight, nodes, indexedEdges(nodes, edges));
    }

    private static List<Node> perks(Node... nodes) {
        return List.of(nodes);
    }

    private static Node perk(String name, String tooltip, double x, double y) {
        return new Node(name, tooltip, skillRequirements(tooltip), prerequisites(tooltip), x, y);
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
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).name().equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown perk node: " + name);
    }

    private static int[] skillRequirements(String tooltip) {
        String skillLine = tooltipLine(tooltip, "Skill: ");
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
        if (prerequisiteLine.isEmpty()) {
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

    private record Node(String name, String tooltip, int[] skillRequirements, List<String> prerequisites, double x,
                        double y) {
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
