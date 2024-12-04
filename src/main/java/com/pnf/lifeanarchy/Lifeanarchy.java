package com.pnf.lifeanarchy;

import com.pnf.lifeanarchy.managers.EventManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pnf.lifeanarchy.handlers.EntityDeathHandler;
import com.pnf.lifeanarchy.handlers.PlayerJoinHandler;
import com.pnf.lifeanarchy.managers.CommandManager;

public class Lifeanarchy implements ModInitializer {
	public static final String MOD_ID = "lifeanarchy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Lifeanarchy is loading");
		EventManager.registerEvents();
		CommandManager.registerCommands();;
	}
}