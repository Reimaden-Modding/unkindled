package net.reimaden.unkindled.util;

import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.reimaden.unkindled.Unkindled;

public class FurnaceUtil {
    public static boolean canBeLit(BlockState state) {
        return state.is(Unkindled.NEEDS_IGNITING, s -> s.hasProperty(AbstractFurnaceBlock.LIT) && !state.getValue(AbstractFurnaceBlock.LIT));
    }
}
