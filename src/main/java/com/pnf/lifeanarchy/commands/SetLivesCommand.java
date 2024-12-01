package com.pnf.lifeanarchy.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.misc.LifeCycleUtils;
import com.pnf.lifeanarchy.misc.ScoreboardUtils;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class SetLivesCommand {

	public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		int number = IntegerArgumentType.getInteger(context, "lives");
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

		LifeCycleUtils.setLives(player, number);

		context.getSource().sendFeedback(() -> Text.literal("Lives are now set to " + number), true);
		return 1;
	}

}
