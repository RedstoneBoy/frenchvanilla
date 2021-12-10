package net.bios.frenchvanilla.carrying_bucket;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.bios.frenchvanilla.NbtIds;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class CarryingBucketItemComponent extends ItemComponent {
    public CarryingBucketItemComponent(ItemStack stack) {
        super(stack);
    }

    public boolean isCarryingBucket() {
        return this.getRootTag() != null;
    }

    public Inventory getInventory() {
        NbtList itemsNbt = this.getList(NbtIds.CARRYING_BUCKET_ITEMS, NbtElement.COMPOUND_TYPE);
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            if (i < itemsNbt.size()) {
                items[i] = ItemStack.fromNbt(itemsNbt.getCompound(i));
            } else {
                items[i] = ItemStack.EMPTY;
            }
        }
        SimpleInventory inventory = new SimpleInventory(items);
        inventory.addListener(this::setItems);
        return inventory;
    }

    private void setItems(Inventory inv) {
        NbtList itemsNbt = new NbtList();
        for (int i = 0; i < 9; i++) {
            itemsNbt.add(inv.getStack(i).writeNbt(new NbtCompound()));
        }

        this.putList(NbtIds.CARRYING_BUCKET_ITEMS, itemsNbt);
    }
}
