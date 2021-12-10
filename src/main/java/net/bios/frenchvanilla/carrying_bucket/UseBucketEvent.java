package net.bios.frenchvanilla.carrying_bucket;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.bios.frenchvanilla.Components;
import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UseBucketEvent implements UseItemCallback {
    public static void register() {
        UseItemCallback.EVENT.register(new UseBucketEvent());
    }

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (world.isClient
                || !FrenchVanilla.config.carryingBucket
                || !hand.equals(Hand.MAIN_HAND)
                || !Components.CARRYING_BUCKET.isProvidedBy(player.getStackInHand(hand))
                || !Components.CARRYING_BUCKET.get(player.getStackInHand(hand)).isCarryingBucket())
            return TypedActionResult.pass(ItemStack.EMPTY);

        ItemStack stack = player.getStackInHand(hand);
        player.setStackInHand(hand, ItemStack.EMPTY);
        Inventory inv = Components.CARRYING_BUCKET.get(stack).getInventory();
        SimpleGui gui = new BucketGui(ScreenHandlerType.GENERIC_9X1, (ServerPlayerEntity) player, stack);
        gui.setTitle(stack.getName());
        for (int i = 0; i < inv.size(); i++) {
            gui.setSlotRedirect(i, new BucketSlot(inv, i));
        }

        gui.open();

        return TypedActionResult.consume(ItemStack.EMPTY);
    }
}
