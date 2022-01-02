package net.bios.frenchvanilla.unpath;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShovelItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class UsePathEvent implements UseBlockCallback {
    public static void register() {
        UseBlockCallback.EVENT.register(new UsePathEvent());
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient
                || !FrenchVanilla.config.unpath.value
                || !world.getBlockState(hitResult.getBlockPos()).getBlock().equals(Blocks.DIRT_PATH)
                || !(player.getStackInHand(hand).getItem() instanceof ShovelItem))
            return ActionResult.PASS;

        world.setBlockState(hitResult.getBlockPos(), Blocks.DIRT.getDefaultState());
        world.playSound(null, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.MASTER, 1.0f, 1.0f);

        return ActionResult.SUCCESS;
    }
}
