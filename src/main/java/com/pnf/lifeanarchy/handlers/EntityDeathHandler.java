package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.misc.LifeCycleUtils;
import com.pnf.lifeanarchy.misc.MessageUtils;
import com.pnf.lifeanarchy.misc.ScoreboardUtils;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class EntityDeathHandler implements AfterDeath {
	@Override
	public void afterDeath(LivingEntity entity, DamageSource damageSource) {
		if (entity instanceof ServerPlayerEntity player) {
			int lives = PlayerDataManager.loadPlayerInt(player, "lives") - 1;
			Lifeanarchy.LOGGER.info("Player " + player.getName() + " died by " + damageSource.getName() + " and lost one Life");
			
			// set lives
			PlayerDataManager.savePlayerint(player, lives, "lives");
			ScoreboardUtils.updatePlayerTeam(player);
			
			//boogeyman
			if(damageSource.getAttacker() instanceof ServerPlayerEntity killer) {
				if(ModConfigManager.loadPlayerList("boogeymen").contains(killer.getUuidAsString())) {
					LifeCycleUtils.cureBoogey(killer);
					Lifeanarchy.LOGGER.info("The Boogeyman got cured by killing someone!");
				}
			}
			
			if (lives == 1) {
				player.sendMessage(Text.literal("You are now on ").append(Text.literal("your Last Life!").formatted(Formatting.RED)).append("\nOne more death an you're ").append(Text.literal("out!\n").formatted(Formatting.GRAY)).append(Text.literal("Alliences are now broken and you become hostile to other players!").formatted(Formatting.BOLD)));
			}
			else if (lives == 0) {
				player.changeGameMode(GameMode.SPECTATOR);
				MessageUtils.displayTitle(player, "You're Out!", Formatting.RED);
			}
		}
	}

}
