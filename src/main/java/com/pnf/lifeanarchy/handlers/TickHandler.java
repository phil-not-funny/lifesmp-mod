package com.pnf.lifeanarchy.handlers;

import com.pnf.lifeanarchy.managers.SpawnProtectionManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

public class TickHandler implements ServerTickEvents.StartWorldTick {

    @Override
    public void onStartTick(ServerWorld world) {
        SpawnProtectionManager.tick(world);
    }
}
