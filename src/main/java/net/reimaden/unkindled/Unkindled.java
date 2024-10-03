package net.reimaden.unkindled;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import net.reimaden.unkindled.util.FurnaceUtil;
import net.reimaden.unkindled.util.Igniter;

public class Unkindled implements ModInitializer {
    public static final String MOD_ID = "unkindled";
    public static final TagKey<Block> NEEDS_IGNITING = TagKey.of(RegistryKeys.BLOCK, id("needs_igniting"));
    public static final TagKey<Item> SELF_IGNITING_FUEL = TagKey.of(RegistryKeys.ITEM, id("self_igniting_fuel"));

    @Override
    public void onInitialize() {
        // Event to allow lighting furnaces with any igniter tool
        // "c:tools/igniter"
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            final BlockPos pos = hitResult.getBlockPos();
            final BlockState state = world.getBlockState(pos);
            if (!FurnaceUtil.canBeLit(state) || !player.isSneaking() || player.isSpectator()) return ActionResult.PASS;

            final ItemStack stack = player.getStackInHand(hand);
            final boolean isFireCharge = stack.isOf(Items.FIRE_CHARGE);

            if (stack.isIn(ConventionalItemTags.IGNITER_TOOLS) || isFireCharge) {
                final Random random = world.getRandom();
                final SoundEvent sound = isFireCharge ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
                float pitch = isFireCharge ? (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F : random.nextFloat() * 0.4F + 0.8F;

                world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, pitch);
                world.setBlockState(pos, state.with(AbstractFurnaceBlock.LIT, true), Block.NOTIFY_ALL_AND_REDRAW);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                final BlockEntity blockEntity = world.getBlockEntity(pos);
                // Sanity check
                if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) {
                    ((Igniter) furnace).unkindled$setIgnited(true);
                }

                if (isFireCharge) {
                    stack.decrementUnlessCreative(1, player);
                } else {
                    stack.damage(1, player, LivingEntity.getSlotForHand(hand));
                }

                return ActionResult.success(world.isClient());
            }

            return ActionResult.PASS;
        });
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
