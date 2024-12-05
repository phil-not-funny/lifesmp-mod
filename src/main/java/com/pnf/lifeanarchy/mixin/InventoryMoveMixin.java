package com.pnf.lifeanarchy.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pnf.lifeanarchy.managers.CommandManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(PlayerInventory.class)
public class InventoryMoveMixin {
    @Inject(method = "setStack", at = @At("HEAD"), cancellable = true)
    private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        PlayerInventory inventory = (PlayerInventory) (Object) this;
        PlayerEntity player = inventory.player;
        if (slot == 39 && !stack.isEmpty() && !player.getServer().getGameRules().getBoolean(CommandManager.GR_ALLOW_HELMETS)) {
            player.sendMessage(Text.literal("Wearing helmets is not allowed!").formatted(Formatting.RED), false);
            inventory.setStack(slot, ItemStack.EMPTY);
            ci.cancel();
        }
    }
}
