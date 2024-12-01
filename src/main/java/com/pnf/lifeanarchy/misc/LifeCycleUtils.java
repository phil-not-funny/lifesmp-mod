package com.pnf.lifeanarchy.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;
import com.pnf.lifeanarchy.data.PlayerDataManager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class LifeCycleUtils {
	public static void setLives(ServerPlayerEntity player, int lives) {
		PlayerDataManager.savePlayerint(player, lives, "lives");
		ScoreboardUtils.updatePlayerTeam(player);
		if (lives > 0)
			player.changeGameMode(GameMode.SURVIVAL);

		if (lives == 1) {
			player.sendMessage(Text.literal("You are now on ")
					.append(Text.literal("your Last Life!").formatted(Formatting.RED))
					.append("\nOne more death an you're ").append(Text.literal("out!\n").formatted(Formatting.GRAY))
					.append(Text.literal("Alliences are now broken and you become hostile to other players!")
							.formatted(Formatting.BOLD)));
		} else if (lives == 0) {
			// spawn lightning
			LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, player.getWorld());
			lightning.setPosition(player.getPos());
			lightning.setCosmetic(true);
			player.getWorld().spawnEntity(lightning);

			player.changeGameMode(GameMode.SPECTATOR);
			MessageUtils.displayTitle(player, "You're Out!", Formatting.RED);
		}
	}

	public static int getMembersWithPredicateSize(MinecraftServer server, Predicate<ServerPlayerEntity> predicate) {
		int retrn = 0;
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (predicate.test(player))
				retrn++;
		}
		return retrn;
	}

	public static List<ServerPlayerEntity> chooseBoogey(MinecraftServer server, boolean forceBoogey) {
		List<ServerPlayerEntity> boogeymen = new ArrayList<>();
		Random random = new Random();

		ArrayList<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
		Collections.shuffle(players);
		double probability = 1.0;

		if (getMembersWithPredicateSize(server, (p) -> PlayerDataManager.loadPlayerInt(p, "lives") > 1) > 1
				|| forceBoogey) {
			for (ServerPlayerEntity player : players) {
				if (random.nextDouble() <= probability
						&& (PlayerDataManager.loadPlayerInt(player, "lives") > 1 || forceBoogey)) {
					boogeymen.add(player);
					probability *= 0.2;
				}
			}
			Lifeanarchy.LOGGER.info("Boogeyman selection complete. Total selected: " + boogeymen.size());
			ModConfigManager.savePlayerList("boogeymen", boogeymen.stream().map(b -> b.getUuidAsString()).toList());
		} else
			Lifeanarchy.LOGGER.error("No players eligable for boogeyman!");
		return boogeymen;

	}

	public static boolean cureBoogey(ServerPlayerEntity player) {
		ArrayList<String> boogeymen;
		if ((boogeymen = new ArrayList<>(ModConfigManager.loadPlayerList("boogeymen")))
				.contains(player.getUuidAsString())) {
			boogeymen.remove(player.getUuidAsString());
			MessageUtils.displayTitle(player, "You're cured!", Formatting.GREEN);
			GameFlavorUtils.playSound(player, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), 1.0F, 0.9F);
			ModConfigManager.savePlayerList("boogeymen", boogeymen);
			return true;
		}
		return false;
	}

	public static boolean endBoogeyPhase(MinecraftServer server) {
		List<String> boogeymen = new ArrayList<>(ModConfigManager.loadPlayerList("boogeymen"));
		if (boogeymen.isEmpty())
			return false;
		for (String uuid : boogeymen) {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (player.getUuidAsString().equals(uuid)) {
					MessageUtils.displayTitle(player, "You failed!", Formatting.RED);
					GameFlavorUtils.playSound(player, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), 1.0F, 0.15F);
					setLives(player, 1);
					break;
				}
			}
		}
		ModConfigManager.savePlayerList("boogeymen", Collections.emptyList());
		return true;
	}

	public static void startBoogeyPhase(MinecraftServer server, long minutes) {
		// DEFINITIONS
		final Timer timer = new Timer();
		final long ONE_SECOND = 1000L;
		final long ONE_MINUTE = 60 * ONE_SECOND;
		final long TIMER_DURATION = minutes == -1 ? 5 * ONE_MINUTE + ONE_SECOND : minutes * ONE_MINUTE + ONE_SECOND;
		final List<ServerPlayerEntity> boogeymen = new ArrayList<>();

		long startTime = System.currentTimeMillis();
		long endTime = startTime + TIMER_DURATION;

		// MAIN COUNTDOWN
		timer.scheduleAtFixedRate(new TimerTask() {
			private boolean lastWarning = false;
			private boolean oneMinWarning = false;
			private boolean firstWarning = false;

			@Override
			public void run() {
				long remainingTime = endTime - System.currentTimeMillis();

				// BOOGEYMAN 3-second REVEAL COUNTDOWN
				if (remainingTime <= 0) {
					Timer countdownTimer = new Timer();
					countdownTimer.scheduleAtFixedRate(new TimerTask() {
						private int countdown = 3;

						@Override
						public void run() {
							if (countdown > 0) { // 3,2,1
								MessageUtils.broadcastTitle(server,
										Text.literal(String.valueOf(countdown)).formatted(Formatting.RED), p -> true);
								GameFlavorUtils.broadcastSound(server, SoundEvents.BLOCK_NOTE_BLOCK_PLING.value());
								countdown--;
							} else { // REVEAL
								countdownTimer.cancel();
								MessageUtils.broadcastTitle(server,
										Text.literal("You are THE Boogeyman").formatted(Formatting.RED),
										boogeymen::contains,
										Text.literal("You are NOT the Boogeyman").formatted(Formatting.GREEN));
								MessageUtils.broadcastMessage(server, Text.literal("")
										.append(Text.literal("You are the Boogeyman.").formatted(Formatting.RED))
										.append(Text.literal("\nSecure a kill on a ")
												.append(Text.literal("Green-").formatted(Formatting.GREEN))
												.append(" or ")
												.append(Text.literal("Yellow-").formatted(Formatting.YELLOW))
												.append("named player in order to be cured.\nShould you fail to do so, you will drop down to your ")
												.append(Text.literal("last life.").formatted(Formatting.RED))),
										boogeymen::contains);
								GameFlavorUtils.broadcastSound(server, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
										boogeymen::contains, SoundEvents.BLOCK_NOTE_BLOCK_PLING.value());
							}
						}
					}, 0, ONE_SECOND);
					timer.cancel();
					return;
				}

				int minutesLeft = (int) (remainingTime / ONE_MINUTE);
				if (!firstWarning) { // FIRST WARNING AT START
					MessageUtils.broadcastMessage(server, Text.literal("The boogeyman is being chosen in " + minutesLeft
							+ " minute" + (minutesLeft > 1 ? "s." : ".")).formatted(Formatting.RED));
					GameFlavorUtils.broadcastSound(server, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER);
					firstWarning = true;
				} else if (remainingTime <= ONE_MINUTE && !oneMinWarning) { // ONE-MIN WARNING
					MessageUtils.broadcastMessage(server,
							Text.literal("The boogeyman is being chosen in 1 minute.").formatted(Formatting.RED));
					GameFlavorUtils.broadcastSound(server, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER);
					oneMinWarning = true;

				} else if (remainingTime <= 10 * ONE_SECOND && !lastWarning) { // BRIEF WARNING BEFORE COUNTDOWN
					MessageUtils.broadcastMessage(server,
							Text.literal("The boogeyman is about to be chosen.").formatted(Formatting.RED));
					boogeymen.addAll(chooseBoogey(server));
					lastWarning = true;
				}
			}
		}, 0, ONE_SECOND);
	}

	// METHOD HELPERS FOR OVERLOADS
	public static List<ServerPlayerEntity> chooseBoogey(MinecraftServer server) {
		return chooseBoogey(server, false);
	}

	public static void startBoogeyPhase(MinecraftServer server) {
		startBoogeyPhase(server, -1);
	}
}
