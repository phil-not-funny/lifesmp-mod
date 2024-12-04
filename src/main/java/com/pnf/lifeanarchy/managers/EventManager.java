package com.pnf.lifeanarchy.managers;

import com.pnf.lifeanarchy.handlers.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public abstract class EventManager {
    public static void registerEvents() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new EntityDeathHandler());
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoinHandler());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnHandler());

        ServerTickEvents.START_WORLD_TICK.register(new TickHandler());
    }
}
