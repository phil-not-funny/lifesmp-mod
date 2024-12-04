package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.managers.CommandManager;
import com.pnf.lifeanarchy.managers.SpawnProtectionManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerRespawnHandler implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean b) {
        if (newPlayer.getServer().getGameRules().getBoolean(CommandManager.GR_ENABLE_SPAWN_PROTECTION)) {
            SpawnProtectionManager.addProtection(newPlayer, newPlayer.getServer().getGameRules().getInt(CommandManager.GR_SPAWN_PROTECTION_TIME));
            newPlayer.sendMessage(Text.literal("You are under spawn protection for " + newPlayer.getServer().getGameRules().getInt(CommandManager.GR_SPAWN_PROTECTION_TIME) + " seconds!").formatted(Formatting.GREEN), true);
        }
    }
}
