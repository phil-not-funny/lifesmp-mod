package com.pnf.lifeanarchy.managers;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.misc.PlayerDamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDamageManager {
    private static final HashMap<PlayerDamageTracker, Long> trackedPlayers = new HashMap<>();
    private static final long TRACK_TIME = 5000; // 5 seconds

    @Nullable
    public static ServerPlayerEntity getDeathCauser(PlayerEntity p) {
        PlayerDamageTracker t = getTracker(p);
        if(t == null) return null;
        Lifeanarchy.LOGGER.info("Last Attacker acc. tracker: {}", t.getLastAttacker());
        return p.getServer().getPlayerManager().getPlayerList()
                .stream()
                .filter(other -> other.getUuidAsString().equals(t.getLastAttacker()))
                .findFirst()
                .orElse(null);
    }

    public static void addTracker(PlayerDamageTracker t) {
        trackedPlayers.put(t, System.currentTimeMillis() + TRACK_TIME);
    }

    @Nullable
    public static PlayerDamageTracker getTracker(PlayerEntity e) {
        for (PlayerDamageTracker t : trackedPlayers.keySet()) {
            if (e.getUuidAsString().equals(t.getUuid())) return t;
        }
        return null;
    }

    public static void resetTracker(PlayerDamageTracker t) {
        trackedPlayers.put(t, TRACK_TIME);
    }

    public static boolean isTracked(PlayerDamageTracker t) {
        return trackedPlayers.containsKey(t) &&
                System.currentTimeMillis() <= trackedPlayers.get(t);
    }

    public static void tick(ServerWorld world) {
        long now = System.currentTimeMillis();
        trackedPlayers.entrySet().removeIf(entry -> now > entry.getValue());
    }
}
