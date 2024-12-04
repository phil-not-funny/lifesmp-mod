package com.pnf.lifeanarchy.misc;

import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageUtils {

	public static void displayTitle(ServerPlayerEntity p, String msg, Formatting format) {
		p.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("")));
		p.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(msg).formatted(format)));
	}

	public static void broadcastTitle(MinecraftServer s, Text success, Predicate<ServerPlayerEntity> predicate) {
		for (ServerPlayerEntity p : s.getPlayerManager().getPlayerList()) {
			if (predicate.test(p)) {
				p.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("")));
				p.networkHandler.sendPacket(new SubtitleS2CPacket(success));
			}
		}
	}
	
	public static void broadcastTitle(MinecraftServer s, Text success, Predicate<ServerPlayerEntity> predicate, Text failure) {
		for (ServerPlayerEntity p : s.getPlayerManager().getPlayerList()) {
			p.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("")));
			if (predicate.test(p)) {
				p.networkHandler.sendPacket(new SubtitleS2CPacket(success));
			} else {
				p.networkHandler.sendPacket(new SubtitleS2CPacket(failure));
			}
		}
	}

	public static void sendActionbar(PlayerEntity p, Text message) {

	}

	public static void broadcastMessage(MinecraftServer s, Text message) {
		for (ServerPlayerEntity p : s.getPlayerManager().getPlayerList()) {
			p.sendMessage(message);
		}
	}

	public static void broadcastMessage(MinecraftServer s, Text success, Predicate<ServerPlayerEntity> predicate) {
		for (ServerPlayerEntity p : s.getPlayerManager().getPlayerList()) {
			if (predicate.test(p)) {
				p.sendMessage(success);
			}
		}
	}

}
