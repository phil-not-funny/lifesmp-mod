package com.pnf.lifeanarchy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pnf.lifeanarchy.commands.GiveLifeCommand;
import com.pnf.lifeanarchy.commands.SetLivesCommand;
import com.pnf.lifeanarchy.commands.BoogeyCommand;
import com.pnf.lifeanarchy.commands.autocompletes.BoogeyCommandArgument;
import com.pnf.lifeanarchy.handlers.EntityDeathHandler;
import com.pnf.lifeanarchy.handlers.PlayerJoinHandler;

public class Lifeanarchy implements ModInitializer {
	public static final String MOD_ID = "lifeanarchy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Lifeanarchy is loading");
		registerEvents();
		registerCommands();
	}

	private void registerEvents() {
		ServerLivingEntityEvents.AFTER_DEATH.register(new EntityDeathHandler());
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler());
	}

	private void registerCommands() {
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