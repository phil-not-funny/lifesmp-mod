package com.pnf.lifeanarchy.mixin;

import com.pnf.lifeanarchy.misc.CommandUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class PlayerArmorEquipMixin {
    @Inject(method = "onEquipStack", at = @At("HEAD"), cancellable = true)
    private void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        if ((Object) this instanceof PlayerEntity player) {
            if (slot == EquipmentSlot.HEAD && !newStack.isEmpty()
                    && !player.getServer().getGameRules().getBoolean(CommandUtils.GR_ALLOW_HELMETS)) {
                player.sendMessage(Text.literal("Wearing helmets is not allowed!").formatted(Formatting.RED), false);
                player.getInventory().setStack(39, ItemStack.EMPTY);
                ci.cancel();
            }
        }
    }
}
