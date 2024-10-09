package net.reimaden.unkindled.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.reimaden.unkindled.Unkindled;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BaseEntityBlock {
    protected CampfireBlockMixin(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = "getStateForPlacement",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;"
            )
    )
    private Boolean unkindled$isUnlitByDefault(Boolean original) {
        return original && !this.defaultBlockState().is(Unkindled.NEEDS_IGNITING);
    }
}
