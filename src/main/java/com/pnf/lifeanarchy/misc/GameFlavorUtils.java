package com.pnf.lifeanarchy.misc;

import java.util.function.Predicate;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class GameFlavorUtils {

	public static void broadcastSound(MinecraftServer server, SoundEvent sound) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			player.playSoundToPlayer(sound, SoundCategory.AMBIENT, 1.0F, 1.0F);
		}
	}

	public static void broadcastSound(MinecraftServer server, SoundEvent success,
			Predicate<ServerPlayerEntity> predicate, SoundEvent failure) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (predicate.test(player))
				player.playSoundToPlayer(success, SoundCategory.AMBIENT, 1.0F, 1.0F);
			else if (failure != null)
				player.playSoundToPlayer(failure, SoundCategory.AMBIENT, 1.0F, 1.0F);
		}
	}
	
	public static void playSound(ServerPlayerEntity p, SoundEvent s) {
		p.playSoundToPlayer(s, SoundCategory.AMBIENT, 1.0F, 1.0F);
	}
	
	public static void playSound(ServerPlayerEntity p, SoundEvent s, float volume, float pitch) {
		p.playSoundToPlayer(s, SoundCategory.AMBIENT, volume, pitch);
	}
}
