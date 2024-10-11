package net.reimaden.unkindled.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.reimaden.unkindled.util.FurnaceUtil;
import net.reimaden.unkindled.util.Igniter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$8")
public abstract class FlintAndSteelDispenserBehaviorMixin extends OptionalDispenseItemBehavior {
    @Inject(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior$8;isSuccess()Z"
            )
    )
    private void unkindled$useOnFurnaces(CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 0) BlockState state,
                                         @Local(ordinal = 0) ServerLevel level, @Local(ordinal = 0) BlockPos pos) {
        if (FurnaceUtil.canBeLit(state) && level.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity furnace) {
            level.setBlockAndUpdate(pos, state.setValue(AbstractFurnaceBlock.LIT, true));
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            ((Igniter) furnace).unkindled$setIgnited(true);

            this.setSuccess(true);
        }
    }
}
