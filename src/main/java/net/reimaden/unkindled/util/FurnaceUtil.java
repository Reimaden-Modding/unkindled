package net.reimaden.unkindled.util;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.reimaden.unkindled.Unkindled;

public class FurnaceUtil {
    public static boolean canBeLit(BlockState state) {
        return state.isIn(Unkindled.NEEDS_IGNITING, s -> s.contains(AbstractFurnaceBlock.LIT) && !state.get(AbstractFurnaceBlock.LIT));
    }
}
