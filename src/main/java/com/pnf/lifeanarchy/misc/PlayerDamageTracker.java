package com.pnf.lifeanarchy.misc;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.managers.PlayerDamageManager;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerDamageTracker {
    private String uuid;

    private String lastAttacker;

    public PlayerDamageTracker(PlayerEntity p) {
        this.uuid = p.getUuidAsString();
    }

    public void setAttacker(PlayerEntity e) {
        lastAttacker = e.getUuidAsString();
        if (PlayerDamageManager.isTracked(this))
            PlayerDamageManager.resetTracker(this);
        else
            PlayerDamageManager.addTracker(this);
    }

    public String getLastAttacker() {
        return lastAttacker;
    }

    public String getUuid() {
        return uuid;
    }
}
