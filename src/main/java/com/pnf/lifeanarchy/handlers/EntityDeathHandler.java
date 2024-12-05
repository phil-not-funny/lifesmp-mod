package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.managers.CommandManager;
import com.pnf.lifeanarchy.managers.PlayerDamageManager;
import com.pnf.lifeanarchy.misc.LifeCycleUtils;

import com.pnf.lifeanarchy.misc.MessageUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class EntityDeathHandler implements AfterDeath {
	@Override
	public void afterDeath(LivingEntity entity, DamageSource damageSource) {
		if (entity instanceof ServerPlayerEntity player) {
			ServerPlayerEntity causer = (ServerPlayerEntity) PlayerDamageManager.getDeathCauser(player);
			if(causer != null) {
				Lifeanarchy.LOGGER.info("Causer was {}", causer.getName().getString());

				if(player.getServer().getGameRules().getBoolean(CommandManager.GR_ENABLE_LIFESTEAL)) {
					LifeCycleUtils.setLives(causer, PlayerDataManager.loadPlayerInt(causer, "lives") + 1);
					MessageUtils.displayBigTitle(causer, "+1", Formatting.GREEN);
				}

				//boogeyman
				if(ModConfigManager.loadPlayerList("boogeymen").contains(causer.getUuidAsString())) {
					LifeCycleUtils.cureBoogey(causer);
					Lifeanarchy.LOGGER.info("The Boogeyman got cured by killing someone!");
				}
			}
			
			// set lives and everything else that has to do with it
			int lives = PlayerDataManager.loadPlayerInt(player, "lives") - 1;
			LifeCycleUtils.setLives(player, lives);
			
		}
	}

}
