package net.reimaden.unkindled.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.reimaden.unkindled.util.FurnaceUtil;
import net.reimaden.unkindled.util.Igniter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$17")
public abstract class FlintAndSteelDispenserBehaviorMixin {
    @ModifyExpressionValue(
            method = "dispenseSilently",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/CandleCakeBlock;canBeLit(Lnet/minecraft/block/BlockState;)Z"
            )
    )
    private boolean unkindled$allowUsingOnFurnaces(boolean original, @Local BlockState state) {
        return original || FurnaceUtil.canBeLit(state);
    }

    @Inject(
            method = "dispenseSilently",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/event/GameEvent;BLOCK_CHANGE:Lnet/minecraft/registry/entry/RegistryEntry$Reference;"
            )
    )
    private void unkindled$setIgnited(CallbackInfoReturnable<ItemStack> cir, @Local ServerWorld world, @Local BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) {
            ((Igniter) furnace).unkindled$setIgnited(true);
        }
    }

}
