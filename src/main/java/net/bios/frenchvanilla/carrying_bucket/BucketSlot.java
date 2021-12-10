package net.bios.frenchvanilla.carrying_bucket;

import net.bios.frenchvanilla.Components;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BucketSlot extends Slot {
    public BucketSlot(Inventory inventory, int index) {
        super(inventory, index, 0, 0);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem().canBeNested() && (!Components.CARRYING_BUCKET.isProvidedBy(stack) || !Components.CARRYING_BUCKET.get(stack).isCarryingBucket());
    }
}
