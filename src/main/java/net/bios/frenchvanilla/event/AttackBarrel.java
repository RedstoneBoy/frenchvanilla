package net.bios.frenchvanilla.event;

import net.bios.frenchvanilla.blockentity.MassBarrelBlockEntity;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AttackBarrel implements AttackBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        if (world.isClient) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

        if (world.getBlockState(pos).isOf(Blocks.BARREL)
                && world.getBlockEntity(pos) != null
                && world.getBlockEntity(pos) instanceof MassBarrelBlockEntity) {
            MassBarrelBlockEntity massBarrel = (MassBarrelBlockEntity) world.getBlockEntity(pos);

            ItemStack newStack;

            if (player.isSneaking()) {
                newStack = massBarrel.removeStack();
            } else {
                newStack = massBarrel.removeOne();
            }

            player.inventory.offerOrDrop(world, newStack);

            player.sendMessage(massBarrel.getText(), true);
        }
        return ActionResult.PASS;
    }
}
