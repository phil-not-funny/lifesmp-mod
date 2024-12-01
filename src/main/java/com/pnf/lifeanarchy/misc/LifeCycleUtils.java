package com.pnf.lifeanarchy.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LifeCycleUtils {
	public static List<ServerPlayerEntity> chooseBoogey(MinecraftServer server) {
		List<ServerPlayerEntity> boogeymen = new ArrayList<>();
		Random random = new Random();

		ArrayList<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
		Collections.shuffle(players);
		double probability = 1.0;

		for (ServerPlayerEntity player : players) {
			if (random.nextDouble() <= probability) {
				boogeymen.add(player);
				probability *= 0.2;
			}
		}
		Lifeanarchy.LOGGER.info("Boogeyman selection complete. Total selected: " + boogeymen.size());
		ModConfigManager.savePlayerList("boogeymen", boogeymen.stream().map(b -> b.getUuidAsString()).toList());
		return boogeymen;
	}

	public static boolean cureBoogey(ServerPlayerEntity player) {
		ArrayList<String> boogeymen;
		if ((boogeymen = new ArrayList<>(ModConfigManager.loadPlayerList("boogeymen")))
				.contains(player.getUuidAsString())) {
			boogeymen.remove(player.getUuidAsString());
			MessageUtils.displayTitle(player, "You're cured!", Formatting.GREEN);
			GameFlavorUtils.playSound(player, SoundEvents.BLOCK_NOTE_BLOCK_BANJO.value());
			ModConfigManager.savePlayerList("boogeymen", boogeymen);
			return true;
		}
		return false;
	}

	public static void startBoogeyPhase(MinecraftServer server, long minutes) {
		final Timer timer = new Timer();
		final long ONE_SECOND = 1000L;
		final long ONE_MINUTE = 60 * ONE_SECOND;
		final long TIMER_DURATION = minutes == -1 ? 5 * ONE_MINUTE + ONE_SECOND: minutes * ONE_MINUTE + ONE_SECOND;
		final List<ServerPlayerEntity> boogeymen = new ArrayList<>();

		long startTime = System.currentTimeMillis();
		long endTime = startTime + TIMER_DURATION;

		timer.scheduleAtFixedRate(new TimerTask() {
			private boolean lastWarning = false;
			private boolean oneMinWarning = false;

			@Override
			public void run() {
				long remainingTime = endTime - System.currentTimeMillis();

				if (remainingTime <= 0) {
					timer.cancel();
					MessageUtils.broadcastTitle(server, Text.literal("You are THE Boogeyman").formatted(Formatting.RED),
							boogeymen::contains, Text.literal("You are NOT the Boogeyman").formatted(Formatting.GREEN));
					GameFlavorUtils.broadcastSound(server, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
							boogeymen::contains, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value());
					return;
				}

				int minutesLeft = (int) (remainingTime / ONE_MINUTE);
				if (minutesLeft == 5 || (minutesLeft == 1 && !oneMinWarning)) {
					MessageUtils.broadcastMessage(server, Text.literal("The boogeyman is being chosen in " + minutesLeft
							+ " minute" + (minutesLeft > 1 ? "s." : ".")).formatted(Formatting.RED));
					GameFlavorUtils.broadcastSound(server, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER);
					oneMinWarning = true;

				} else if (remainingTime <= 10 * ONE_SECOND && !lastWarning) {
					MessageUtils.broadcastMessage(server,
							Text.literal("The boogeyman is about to be chosen.").formatted(Formatting.RED));
					boogeymen.addAll(chooseBoogey(server));
					lastWarning = true;
				}
			}
		}, 0, ONE_SECOND);
	}
}
