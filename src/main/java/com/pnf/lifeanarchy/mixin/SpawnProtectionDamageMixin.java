package com.pnf.lifeanarchy.mixin;

import com.pnf.lifeanarchy.Lifeanarchy;
import com.pnf.lifeanarchy.managers.CommandManager;
import com.pnf.lifeanarchy.managers.SpawnProtectionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class SpawnProtectionDamageMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof PlayerEntity player && SpawnProtectionManager.isProtected(player)
                && world.getServer().getGameRules().getBoolean(CommandManager.GR_ENABLE_SPAWN_PROTECTION)) {
            player.sendMessage(Text.literal("You are under spawn protection!").formatted(Formatting.YELLOW), true);
            if (source.getAttacker() instanceof PlayerEntity attacker)
                attacker.sendMessage(Text.literal("This player has spawn protection!").formatted(Formatting.YELLOW), true);
            cir.setReturnValue(false);
        }
    }
}