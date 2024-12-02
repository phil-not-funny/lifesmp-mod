package com.pnf.lifeanarchy.misc;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pnf.lifeanarchy.commands.BoogeyCommand;
import com.pnf.lifeanarchy.commands.GiveLifeCommand;
import com.pnf.lifeanarchy.commands.SetLivesCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules;

public class CommandUtils {
	public static final GameRules.Key<GameRules.IntRule> GR_START_LIVES = GameRuleRegistry.register("startLives", Category.PLAYER, GameRuleFactory.createIntRule(3));
	
	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("setlives").requires(source -> source.hasPermissionLevel(1))
					.then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager
							.argument("lives", IntegerArgumentType.integer()).executes(SetLivesCommand::run))));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, enviroment) -> {
			dispatcher.register(CommandManager.literal("boogeyman").requires(source -> source.hasPermissionLevel(1))
					.then(CommandManager.argument("executes", StringArgumentType.word())
							.suggests(BoogeyCommand::getSuggestions).executes(BoogeyCommand::runStartEnd).then(CommandManager
									.argument("player", EntityArgumentType.player()).executes(BoogeyCommand::runCure))));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("givelife").then(
					CommandManager.argument("player", EntityArgumentType.player()).executes(GiveLifeCommand::run)));
		});
	}
}
