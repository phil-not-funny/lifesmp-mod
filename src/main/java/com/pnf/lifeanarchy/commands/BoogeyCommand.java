package com.pnf.lifeanarchy.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Lifecycle;
import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.commands.autocompletes.BoogeyCommandArgument;
import com.pnf.lifeanarchy.misc.LifeCycleUtils;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BoogeyCommand {

	public static int runStartEnd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String method = StringArgumentType.getString(context, "executes");
		if (BoogeyCommandArgument.start.match(method)) {
			LifeCycleUtils.startBoogeyPhase(context.getSource().getServer());
			context.getSource().sendFeedback(() -> Text.literal("Boogeyman-phase is now starting..."), true);
		} else if (BoogeyCommandArgument.end.match(method)) {
			boolean succeeded = LifeCycleUtils.endBoogeyPhase(context.getSource().getServer());
			if (succeeded)
				context.getSource().sendFeedback(() -> Text.literal("All remaining boogeymens have been punished."),
						true);
			else
				context.getSource().sendFeedback(() -> Text.literal("No boogeymen remaining. No punishments."), true);
		} else if (BoogeyCommandArgument.cure.match(method)) {
			context.getSource().sendError(Text.literal("Please enter a Player to be CURED!"));
			return 0;
		}
		return 1;
	}

	public static int runCure(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String method = StringArgumentType.getString(context, "executes");
		if (BoogeyCommandArgument.start.match(method) || BoogeyCommandArgument.end.match(method)) {
			context.getSource().sendError(Text.literal("Player is an invalid argument for START/END!"));
			return 0;
		} else if (BoogeyCommandArgument.cure.match(method)) {
			ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			if (LifeCycleUtils.cureBoogey(player))
				context.getSource().sendFeedback(
						() -> Text.literal("Player " + player.getName().getString() + " is now cured"), true);
			else
				context.getSource().sendError(Text.literal("This player is not a boogeyman!"));
		}
		return 1;
	}

	public static CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context,
			SuggestionsBuilder builder) throws CommandSyntaxException {
		for (BoogeyCommandArgument arg : BoogeyCommandArgument.values()) {
			builder.suggest(arg.toString());
		}
		return builder.buildFuture();
	}
}
