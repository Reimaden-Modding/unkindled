package net.reimaden.unkindled.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.reimaden.unkindled.Unkindled;
import net.reimaden.unkindled.util.Igniter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements Igniter {
    @Unique
    private boolean unkindled$ignited = false;

    @Override
    public void unkindled$setIgnited(boolean ignited) {
        this.unkindled$ignited = ignited;
    }

    @Override
    public boolean unkindled$isIgnited() {
        return this.unkindled$ignited;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE:FIRST",
                    target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;canAcceptRecipeOutput(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/recipe/RecipeEntry;Lnet/minecraft/util/collection/DefaultedList;I)Z"
            )
    )
    private static boolean unkindled$requiresIgniting(boolean original, World world, BlockPos pos, BlockState state,
                                                      AbstractFurnaceBlockEntity blockEntity, @Local(ordinal = 0) ItemStack stack) {
        if (!state.isIn(Unkindled.NEEDS_IGNITING) || stack.isIn(Unkindled.SELF_IGNITING_FUEL)) {
            return original;
        }
        return original && ((Igniter) blockEntity).unkindled$isIgnited();
    }

    @Inject(
            method = "tick",
            at = @At(
                    "TAIL"
            )
    )
    private static void unkindled$setUnignited(World world, BlockPos pos, BlockState state,
                                               AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        if (!state.get(AbstractFurnaceBlock.LIT)) {
            if (((AbstractFurnaceBlockEntityAccessor) blockEntity).unkindled$cookTime() > 0) {
                return;
            }
            ((Igniter) blockEntity).unkindled$setIgnited(false);
        } else if (!((AbstractFurnaceBlockEntityAccessor) blockEntity).unkindled$isBurning()) {
            state = state.with(AbstractFurnaceBlock.LIT, false);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
    }

    @Inject(
            method = "readNbt",
            at = @At(
                    "TAIL"
            )
    )
    private void unkindled$readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        this.unkindled$ignited = nbt.getBoolean("Ignited");
    }

    @Inject(
            method = "writeNbt",
            at = @At(
                    "TAIL"
            )
    )
    private void unkindled$writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        nbt.putBoolean("Ignited", this.unkindled$ignited);
    }
}
