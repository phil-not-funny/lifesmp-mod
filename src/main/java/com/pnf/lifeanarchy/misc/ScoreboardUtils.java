package com.pnf.lifeanarchy.misc;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.PlayerDataManager;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ScoreboardUtils {
	public static void updatePlayerTeam(ServerPlayerEntity p) {
		MinecraftServer server = p.getServer();
		if (server == null)
			return;

		int lives = PlayerDataManager.loadPlayerInt(p, "lives");

		ServerScoreboard sb = server.getScoreboard();
		String teamName = Integer.toString(lives);
		Formatting teamColor = getColorFromValue(lives);

		Team team = sb.getTeam(teamName);
		if (team == null) {
			team = sb.addTeam(teamName);
			team.setColor(teamColor);
			//team.setPrefix(Text.literal(getColorFromValueAsString(lives)));
			//team.setSuffix(Text.literal("§r"));
		}

		String playername = p.getName().getString();
		removePlayerFromOtherTeams(playername, sb);
		sb.addScoreHolderToTeam(playername, team);
		sb.updateScoreboardTeamAndPlayers(team);
	}

	private static void removePlayerFromOtherTeams(String playerName, ServerScoreboard scoreboard) {
		for (Team team : scoreboard.getTeams()) {
			if (team.getPlayerList().contains(playerName)) {
				scoreboard.removeScoreHolderFromTeam(playerName, team);
			}
		}
	}

	public static Formatting getColorFromValue(int lives) {
		if (lives > 3)
			return Formatting.DARK_GREEN;
		else if (lives == 3)
			return Formatting.GREEN;
		else if (lives == 2)
			return Formatting.YELLOW;
		else if (lives == 1)
			return Formatting.RED;
		else
			return Formatting.GRAY;
	}

	private static String getColorFromValueAsString(int lives) {
		if (lives > 3)
			return "§2";
		else if (lives == 3)
			return "§a";
		else if (lives == 2)
			return "§e";
		else if (lives == 1)
			return "§c";
		else
			return "§7";
	}
}
