package com.pnf.lifeanarchy.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;

public class StartBoogeyCommand {
	
	public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return 1;
	}
	
	public static int runArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return 1;
	}
}
