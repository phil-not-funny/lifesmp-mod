package com.pnf.lifeanarchy.managers;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pnf.lifeanarchy.commands.BoogeyCommand;
import com.pnf.lifeanarchy.commands.GiveLifeCommand;
import com.pnf.lifeanarchy.commands.SetLivesCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.IntRule;
import net.minecraft.world.GameRules.Key;

public abstract class CommandManager {
	public static final Key<IntRule> GR_START_LIVES = GameRuleRegistry.register("lifesmp_startLives", Category.PLAYER, GameRuleFactory.createIntRule(3));
	public static final Key<IntRule> GR_BOOGEY_TIMER = GameRuleRegistry.register("lifesmp_boogeyTimer", Category.PLAYER, GameRuleFactory.createIntRule(5));
	public static final Key<IntRule> GR_BOOGEY_FAIL_PENALTY = GameRuleRegistry.register("lifesmp_boogeyFailPenalty", Category.PLAYER, GameRuleFactory.createIntRule(999));
	public static final Key<DoubleRule> GR_BOOGEY_SECOND_PROBABILITY = GameRuleRegistry.register("lifesmp_boogeySecondProbability", Category.PLAYER, GameRuleFactory.createDoubleRule(0.2));
	public static final Key<BooleanRule> GR_ALLOW_GIVELIFE_TO_GREYS = GameRuleRegistry.register("lifesmp_allowGiveLifeToGreys", Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final Key<BooleanRule> GR_ALLOW_HELMETS = GameRuleRegistry.register("lifesmp_allowHelmets", Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final Key<BooleanRule> GR_ENABLE_SPAWN_PROTECTION = GameRuleRegistry.register("lifesmp_enableSpawnProtection", Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final Key<BooleanRule> GR_ENABLE_LIFESTEAL = GameRuleRegistry.register("lifesmp_enableLifesteal", Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final Key<IntRule> GR_SPAWN_PROTECTION_TIME = GameRuleRegistry.register("lifesmp_spawnProtectionDuration", Category.PLAYER, GameRuleFactory.createIntRule(8));

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(net.minecraft.server.command.CommandManager.literal("setlives").requires(source -> source.hasPermissionLevel(1))
					.then(net.minecraft.server.command.CommandManager.argument("player", EntityArgumentType.player()).then(net.minecraft.server.command.CommandManager
							.argument("lives", IntegerArgumentType.integer()).executes(SetLivesCommand::run))));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, enviroment) -> {
			dispatcher.register(net.minecraft.server.command.CommandManager.literal("boogeyman").requires(source -> source.hasPermissionLevel(1))
					.then(net.minecraft.server.command.CommandManager.argument("executes", StringArgumentType.word())
							.suggests(BoogeyCommand::getSuggestions).executes(BoogeyCommand::runStartEnd).then(net.minecraft.server.command.CommandManager
									.argument("player", EntityArgumentType.player()).executes(BoogeyCommand::runCure))));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(net.minecraft.server.command.CommandManager.literal("givelife").then(
					net.minecraft.server.command.CommandManager.argument("player", EntityArgumentType.player()).executes(GiveLifeCommand::run)));
		});
	}
}
