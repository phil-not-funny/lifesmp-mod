package com.pnf.lifeanarchy.mixin;

import com.pnf.lifeanarchy.managers.CommandManager;
import com.pnf.lifeanarchy.managers.SpawnProtectionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerAttackMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        if (target.getServer().getGameRules().getBoolean(CommandManager.GR_ENABLE_SPAWN_PROTECTION)
                && SpawnProtectionManager.isProtected((PlayerEntity) (Object) this)) {
            ((PlayerEntity) (Object) this).sendMessage(
                    Text.literal("You cannot attack during spawn protection!").formatted(Formatting.YELLOW), true);
            ci.cancel();
        }
    }
}
