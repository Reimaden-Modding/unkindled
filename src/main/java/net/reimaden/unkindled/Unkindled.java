package net.reimaden.unkindled;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.reimaden.unkindled.util.FurnaceUtil;
import net.reimaden.unkindled.util.Igniter;

@Mod(Unkindled.MOD_ID)
public class Unkindled {
    public static final String MOD_ID = "unkindled";
    public static final TagKey<Block> NEEDS_IGNITING = BlockTags.create(id("needs_igniting"));
    public static final TagKey<Item> SELF_IGNITING_FUEL = ItemTags.create(id("self_igniting_fuel"));

    public Unkindled() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUseItemOnBlock(UseItemOnBlockEvent event) {
        // Event to allow lighting furnaces with any igniter tool
        // "c:tools/igniter"
        final BlockPos pos = event.getPos();
        final Level level = event.getLevel();
        final BlockState state = level.getBlockState(pos);
        final Player player = event.getPlayer();

        if (player == null || !FurnaceUtil.canBeLit(state) || !player.isShiftKeyDown()) return;

        final InteractionHand hand = event.getHand();
        final ItemStack stack = player.getItemInHand(hand);
        final boolean isFireCharge = stack.is(Items.FIRE_CHARGE);

        if (stack.is(Tags.Items.TOOLS_IGNITER) || isFireCharge) {
            final RandomSource random = level.getRandom();
            final SoundEvent sound = isFireCharge ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            final float pitch = isFireCharge ? (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F : random.nextFloat() * 0.4F + 0.8F;

            level.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, true), Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            // Sanity check
            if (level.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity furnace) {
                ((Igniter) furnace).unkindled$setIgnited(true);
            }

            if (isFireCharge) {
                stack.consume(1, player);
            } else {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }

            event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide()));
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
