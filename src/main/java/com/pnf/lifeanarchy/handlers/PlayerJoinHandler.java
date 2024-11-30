package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.misc.ScoreboardUtils;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerJoinHandler implements Join {

	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		int lives;
		if ((lives = PlayerDataManager.loadPlayerInt(handler.player, "lives")) != -1) {
			Lifeanarchy.LOGGER.info(handler.player.getName().getString() + " has " + lives + " lives");
		} else {
			lives = ModConfigManager.loadInt("startlives") != -1 ? ModConfigManager.loadInt("startlives") : 4;
			Lifeanarchy.LOGGER.info(handler.player.getName().getString() + " is new Player. Setting " + lives + " lives");
			PlayerDataManager.savePlayerint(handler.player, lives, "lives");
		}
		ScoreboardUtils.updatePlayerTeam(handler.player);
		handler.player.sendMessage(Text.literal("")
				.append(Text.literal("Welcome to the Life-SMP Mod!").formatted(Formatting.UNDERLINE))
				.append(Text.literal("\nEverytime you die, you lose a Life.\n")).append("When you lose your ")
				.append(Text.literal("last life").formatted(Formatting.BOLD).formatted(Formatting.GRAY))
				.append(", you enter ")
				.append(Text.literal("Spectator").formatted(Formatting.ITALIC).formatted(Formatting.GRAY))
				.append(" and are unable to play anymore.\n")
				.append(Text.literal("IMPORTANT: ").formatted(Formatting.RED))
				.append("You may not not kill anyone below you at random!\n")
				.append("Deathmatches must be initiated by them ")
				.append(Text.literal("first").formatted(Formatting.BOLD).formatted(Formatting.GRAY)).append("."),
				false);

	}

}
