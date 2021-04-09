package net.bios.frenchvanilla.event;

import net.bios.frenchvanilla.Texts;
import net.bios.frenchvanilla.blockentity.MassBarrelBlockEntity;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class UseBarrel implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;
        if (hitResult.getType() != HitResult.Type.BLOCK) return ActionResult.PASS;
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

        if (world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.BARREL)
                && world.getBlockEntity(hitResult.getBlockPos()) != null
        ) {
            if (world.getBlockEntity(hitResult.getBlockPos()) instanceof BarrelBlockEntity
                    && !player.getStackInHand(hand).isEmpty()
                    && player.isSneaking()
                    && player.getStackInHand(hand).getItem() == Items.DIAMOND_HOE
            ) {
                world.removeBlockEntity(hitResult.getBlockPos());

                world.setBlockEntity(hitResult.getBlockPos(), new MassBarrelBlockEntity());

                world.playSound(
                        null,
                        hitResult.getBlockPos(),
                        SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                        SoundCategory.BLOCKS,
                        1.0f,
                        2.0f
                );

                return ActionResult.SUCCESS;
            } else if (world.getBlockEntity(hitResult.getBlockPos()) instanceof MassBarrelBlockEntity
                    && !player.isSneaking()
                    && !player.getStackInHand(hand).isEmpty()) {
                MassBarrelBlockEntity massBarrel = (MassBarrelBlockEntity) world.getBlockEntity(hitResult.getBlockPos());
                if (massBarrel.isCorrectItemType(player.getStackInHand(hand))) {
                    ItemStack newHandStack = massBarrel.insert(player.getStackInHand(hand));
                    player.setStackInHand(hand, newHandStack);

                    player.sendMessage(massBarrel.getText(), true);

                    return ActionResult.SUCCESS;
                } else {
                    player.sendMessage(new LiteralText("")
                            .append(Texts.WRONG_ITEM)
                            .append(massBarrel.getItemType().get().getName()), true);

                    return ActionResult.FAIL;
                }
            } else if (world.getBlockEntity(hitResult.getBlockPos()) instanceof MassBarrelBlockEntity
                    && player.getStackInHand(hand).isEmpty()) {
                MassBarrelBlockEntity massBarrel = (MassBarrelBlockEntity) world.getBlockEntity(hitResult.getBlockPos());

                if (player.isSneaking() && massBarrel.hasItemType()) {
                    for (int i = 0; i < 36; i++) {
                        if (massBarrel.isCorrectItemType(player.inventory.getStack(i))) {
                            ItemStack oldStack = player.inventory.removeStack(i);
                            ItemStack newStack = massBarrel.insert(oldStack);
                            player.inventory.setStack(i, newStack);
                        }
                    }
                }

                player.sendMessage(massBarrel.getText(), true);
            }
        }

        return ActionResult.PASS;
    }
}
