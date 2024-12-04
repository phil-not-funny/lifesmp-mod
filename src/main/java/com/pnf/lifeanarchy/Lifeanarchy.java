package com.pnf.lifeanarchy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.mixin.event.interaction.ServerPlayerEntityMixin;
import net.minecraft.server.network.ServerPlayerEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pnf.lifeanarchy.handlers.EntityDeathHandler;
import com.pnf.lifeanarchy.handlers.PlayerJoinHandler;
import com.pnf.lifeanarchy.misc.CommandUtils;

public class Lifeanarchy implements ModInitializer {
	public static final String MOD_ID = "lifeanarchy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Lifeanarchy is loading");
		registerEvents();
		CommandUtils.registerCommands();;
	}

	private void registerEvents() {
		ServerLivingEntityEvents.AFTER_DEATH.register(new EntityDeathHandler());
		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler());
	}

	
}