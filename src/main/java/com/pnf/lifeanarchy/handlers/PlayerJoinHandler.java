package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.misc.MessageUtils;
import com.pnf.lifeanarchy.misc.ScoreboardUtils;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
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
			Lifeanarchy.LOGGER.info(handler.player.getName().getString() + " is new Player. Setting 4 lives");
			PlayerDataManager.savePlayerint(handler.player, 4, "lives");
		}
		ScoreboardUtils.updatePlayerTeam(handler.player);
		handler.player.sendMessage(
			    Text.literal("Welcome to the Life-SMP Mod!")
			         // Underline only this part
			        .append(Text.literal("\nEverytime you die, you lose a Life.\n")) // Reset formatting here
			        .append("When you lose your ")
			        .append(Text.literal("last life").formatted(Formatting.BOLD).formatted(Formatting.GRAY)) // Bold and Gray
			        .append(", you enter ")
			        .append(Text.literal("Spectator").formatted(Formatting.ITALIC).formatted(Formatting.GRAY)) // Italic and Gray
			        .append(" and are unable to play anymore.\n")
			        .append(Text.literal("IMPORTANT: ").formatted(Formatting.RED))
			        .append(Text.literal("Green").formatted(Formatting.GREEN))
			        .append("- and ")
			        .append(Text.literal("Yellow").formatted(Formatting.YELLOW))
			        .append("-names may not kill ")
			        .append(Text.literal("Red").formatted(Formatting.RED))
			        .append("-names at random! Deathmatches must be initiated by ")
			        .append(Text.literal("Red").formatted(Formatting.RED))
			        .append("-names ")
			        .append(Text.literal("first").formatted(Formatting.BOLD).formatted(Formatting.GRAY)) // Bold and Gray
			        .append(".")
			    , false); // send message

	}

}
