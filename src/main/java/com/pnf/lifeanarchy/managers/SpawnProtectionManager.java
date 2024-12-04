package com.pnf.lifeanarchy.managers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.UUID;

public abstract class SpawnProtectionManager {
    private static final HashMap<UUID, Long> protectedPlayers = new HashMap<>();

    public static void addProtection(PlayerEntity player, int seconds) {
        protectedPlayers.put(player.getUuid(), System.currentTimeMillis() + seconds * 1000);
    }

    public static boolean isProtected(PlayerEntity player) {
        return protectedPlayers.containsKey(player.getUuid()) &&
                System.currentTimeMillis() <= protectedPlayers.get(player.getUuid());
    }

    public static void tick(ServerWorld world) {
        long now = System.currentTimeMillis();
        protectedPlayers.entrySet().removeIf(entry -> now > entry.getValue());
    }
}
