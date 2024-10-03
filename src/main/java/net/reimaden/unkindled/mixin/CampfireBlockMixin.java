package net.reimaden.unkindled.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.reimaden.unkindled.Unkindled;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BlockWithEntity {
    protected CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = "getPlacementState",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;"
            )
    )
    private Boolean unkindled$isUnlitByDefault(Boolean original) {
        return original && !this.getDefaultState().isIn(Unkindled.NEEDS_IGNITING);
    }
}
