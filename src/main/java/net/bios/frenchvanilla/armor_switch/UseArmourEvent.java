package net.bios.frenchvanilla.armor_switch;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.bios.frenchvanilla.Components.PLAYER_CONFIG;

public class UseArmourEvent implements UseItemCallback {
    public static void register() {
        UseItemCallback.EVENT.register(new UseArmourEvent());
    }

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (world.isClient
                || !FrenchVanilla.config.armorSwapping.value
                || !PLAYER_CONFIG.get(player).config().armorSwapping.value
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

        ItemStack oldArmour = player.getInventory().armor.get(equipSlot.getEntitySlotId());
        ItemStack newArmour = player.getStackInHand(hand);

        player.getInventory().armor.set(equipSlot.getEntitySlotId(), newArmour);
        player.setStackInHand(hand, oldArmour);

        player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1.0f, 1.0f);

        return TypedActionResult.success(ItemStack.EMPTY);
    }
}
