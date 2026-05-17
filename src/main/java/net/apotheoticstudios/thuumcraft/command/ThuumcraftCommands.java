package net.apotheoticstudios.thuumcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.apotheoticstudios.thuumcraft.skill.SkillProgression;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Collection;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class ThuumcraftCommands {
    private static final int MAX_COMMAND_AMOUNT = 1_000_000;
    private static final SimpleCommandExceptionType SKILL_SYSTEM_DISABLED = new SimpleCommandExceptionType(
            Component.literal("The Thuumcraft skill system is disabled."));
    private static final DynamicCommandExceptionType UNKNOWN_SKILL = new DynamicCommandExceptionType(
            skill -> Component.literal("Unknown skill: " + skill));
    private static final DynamicCommandExceptionType DISABLED_SKILL = new DynamicCommandExceptionType(
            skill -> Component.literal(skill + " skill is disabled."));
    private static final SuggestionProvider<CommandSourceStack> SKILL_SUGGESTIONS = (context, builder) ->
            SharedSuggestionProvider.suggest(Arrays.stream(SkillProgression.Skill.values())
                    .filter(SkillPerk::isSkillEnabled)
                    .map(SkillProgression.Skill::id)
                    .toList(), builder);

    private ThuumcraftCommands() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal(Thuumcraft.MOD_ID)
                .requires(source -> source.hasPermission(2))
                .then(playerLevels())
                .then(skillLevels())
                .then(perkPoints())
                .then(resetSkillTrees())
                .then(resetPerkPoints())
                .then(resetSkillLevels())
                .then(resetAllSkillProgress()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> playerLevels() {
        return Commands.literal("add_character_levels")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, MAX_COMMAND_AMOUNT))
                                .executes(ThuumcraftCommands::givePlayerLevels)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> skillLevels() {
        return Commands.literal("add_skill_levels")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .suggests(SKILL_SUGGESTIONS)
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, MAX_COMMAND_AMOUNT))
                                        .executes(ThuumcraftCommands::giveSkillLevels))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> perkPoints() {
        return Commands.literal("add_perk_points")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, MAX_COMMAND_AMOUNT))
                                .executes(ThuumcraftCommands::givePerkPoints)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> resetSkillTrees() {
        return Commands.literal("reset_skill_trees")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ThuumcraftCommands::resetSkillTrees));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> resetPerkPoints() {
        return Commands.literal("reset_perk_points")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ThuumcraftCommands::resetPerkPoints));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> resetSkillLevels() {
        return Commands.literal("reset_skill_levels")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ThuumcraftCommands::resetAllSkillLevels)
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .suggests(SKILL_SUGGESTIONS)
                                .executes(ThuumcraftCommands::resetOneSkillLevel)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> resetAllSkillProgress() {
        return Commands.literal("reset_all_skill_progress")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ThuumcraftCommands::resetAllSkillProgress));
    }

    private static int givePlayerLevels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int applied = 0;
        for (ServerPlayer player : targets) {
            applied += SkillPerk.addPlayerLevels(player, amount);
        }
        sendSuccess(context.getSource(), "character level(s)", applied, targets);
        return applied;
    }

    private static int giveSkillLevels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        SkillProgression.Skill skill = parseEnabledSkill(StringArgumentType.getString(context, "skill"));
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int applied = 0;
        for (ServerPlayer player : targets) {
            applied += SkillProgression.addLevels(player, skill, amount);
        }
        sendSuccess(context.getSource(), skill.displayName() + " skill level(s)", applied, targets);
        return applied;
    }

    private static int givePerkPoints(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int applied = 0;
        for (ServerPlayer player : targets) {
            applied += SkillPerk.addPerkPoints(player, amount);
        }
        sendSuccess(context.getSource(), "perk point(s)", applied, targets);
        return applied;
    }

    private static int resetSkillTrees(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int removed = 0;
        for (ServerPlayer player : targets) {
            removed += SkillPerk.resetSkillTrees(player);
        }
        sendResetSuccess(context.getSource(), "skill tree rank(s)", removed, targets);
        return removed;
    }

    private static int resetPerkPoints(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int removed = 0;
        for (ServerPlayer player : targets) {
            removed += SkillPerk.resetPerkPoints(player);
        }
        sendResetSuccess(context.getSource(), "available perk point(s)", removed, targets);
        return removed;
    }

    private static int resetAllSkillLevels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int removed = 0;
        for (ServerPlayer player : targets) {
            removed += SkillProgression.resetAll(player);
            SkillPerk.syncCommandGrantedSkillLevels(player);
        }
        sendResetSuccess(context.getSource(), "skill level(s)", removed, targets);
        return removed;
    }

    private static int resetOneSkillLevel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        SkillProgression.Skill skill = parseEnabledSkill(StringArgumentType.getString(context, "skill"));
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int removed = 0;
        for (ServerPlayer player : targets) {
            removed += SkillProgression.reset(player, skill);
            SkillPerk.syncCommandGrantedSkillLevels(player);
        }
        sendResetSuccess(context.getSource(), skill.displayName() + " skill level(s)", removed, targets);
        return removed;
    }

    private static int resetAllSkillProgress(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        requireSkillSystemEnabled();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        int removed = 0;
        for (ServerPlayer player : targets) {
            removed += SkillPerk.resetAllSkillProgress(player);
        }
        sendResetSuccess(context.getSource(), "skill progress value(s)", removed, targets);
        return removed;
    }

    private static void requireSkillSystemEnabled() throws CommandSyntaxException {
        if (!SkillPerk.isSystemEnabled()) {
            throw SKILL_SYSTEM_DISABLED.create();
        }
    }

    private static SkillProgression.Skill parseEnabledSkill(String id) throws CommandSyntaxException {
        SkillProgression.Skill skill = SkillProgression.Skill.fromId(id);
        if (skill == null) {
            throw UNKNOWN_SKILL.create(id);
        }
        if (!SkillPerk.isSkillEnabled(skill)) {
            throw DISABLED_SKILL.create(skill.displayName());
        }
        return skill;
    }

    private static void sendSuccess(CommandSourceStack source, String label, int amount,
                                    Collection<ServerPlayer> targets) {
        source.sendSuccess(() -> Component.literal("Added " + amount + " " + label + " across "
                + targets.size() + " player(s)."), true);
    }

    private static void sendResetSuccess(CommandSourceStack source, String label, int amount,
                                         Collection<ServerPlayer> targets) {
        source.sendSuccess(() -> Component.literal("Reset " + amount + " " + label + " across "
                + targets.size() + " player(s)."), true);
    }
}
