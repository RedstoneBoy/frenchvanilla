package net.bios.frenchvanilla.event;

import net.bios.frenchvanilla.blockentity.MassBarrelBlockEntity;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BreakBarrel implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient) return;

        if (state.getBlock().is(Blocks.BARREL)
                && blockEntity != null
                && blockEntity instanceof MassBarrelBlockEntity) {
            MassBarrelBlockEntity massBarrel = (MassBarrelBlockEntity) blockEntity;

            massBarrel.dropItems(world, pos);
        }
    }
}
