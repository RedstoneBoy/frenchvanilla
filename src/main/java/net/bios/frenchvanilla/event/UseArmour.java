package net.bios.frenchvanilla.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UseArmour implements UseItemCallback {
    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (world.isClient
                || hand != Hand.MAIN_HAND
                || player.getStackInHand(hand).isEmpty()
                || !(player.getStackInHand(hand).getItem() instanceof Wearable))
            return TypedActionResult.pass(ItemStack.EMPTY);

        EquipmentSlot equipSlot;
        if (player.getStackInHand(hand).getItem() instanceof ArmorItem) {
            equipSlot = ((ArmorItem) player.getStackInHand(hand).getItem()).getSlotType();
        } else if (player.getStackInHand(hand).getItem() instanceof ElytraItem) {
            equipSlot = EquipmentSlot.CHEST;
        } else {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        if (player.getEquippedStack(equipSlot).isEmpty()) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        ItemStack oldArmour = player.inventory.armor.get(equipSlot.getEntitySlotId());
        ItemStack newArmour = player.getStackInHand(hand);

        player.inventory.armor.set(equipSlot.getEntitySlotId(), newArmour);
        player.setStackInHand(hand, oldArmour);

        return TypedActionResult.success(ItemStack.EMPTY);
    }
}
