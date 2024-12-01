package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.data.ModConfigManager;
import com.pnf.lifeanarchy.data.PlayerDataManager;
import com.pnf.lifeanarchy.misc.LifeCycleUtils;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class EntityDeathHandler implements AfterDeath {
	@Override
	public void afterDeath(LivingEntity entity, DamageSource damageSource) {
		if (entity instanceof ServerPlayerEntity player) {
			//boogeyman
			if(damageSource.getAttacker() instanceof ServerPlayerEntity killer) {
				if(ModConfigManager.loadPlayerList("boogeymen").contains(killer.getUuidAsString())) {
					LifeCycleUtils.cureBoogey(killer);
					Lifeanarchy.LOGGER.info("The Boogeyman got cured by killing someone!");
				}
			}
			
			// set lives and everything else that has to do with it
			int lives = PlayerDataManager.loadPlayerInt(player, "lives") - 1;
			LifeCycleUtils.setLives(player, lives);
			
		}
	}

}
