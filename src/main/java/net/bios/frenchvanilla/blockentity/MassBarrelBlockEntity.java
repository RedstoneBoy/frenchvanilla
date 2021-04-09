package net.bios.frenchvanilla.blockentity;

import net.bios.frenchvanilla.Texts;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class MassBarrelBlockEntity extends BlockEntity {
    // Invariants:
    // count == 0 <==> itemType.isEmpty()
    // itemType.count() == 1
    private Optional<ItemStack> itemType;
    private int count;

    public MassBarrelBlockEntity() {
        super(BlockEntities.MASS_BARREL_BLOCK_ENTITY);

        this.itemType = Optional.empty();
        this.count = 0;
    }

    public boolean hasItemType() {
        return this.itemType.isPresent();
    }

    public Optional<ItemStack> getItemType() {
        return this.itemType;
    }

    public boolean isCorrectItemType(ItemStack stack) {
        if (this.itemType.isPresent()) {
            return ItemStack.areItemsEqual(this.itemType.get(), stack) && ItemStack.areTagsEqual(this.itemType.get(), stack);
        } else {
            return true;
        }
    }

    public int getItemCount() {
        return this.count;
    }

    public int getMaxItemCount() {
        return 65536;
    }

    public ItemStack removeOne() {
        if (this.itemType.isPresent()) {
            ItemStack stack = this.itemType.get().copy();
            stack.setCount(1);
            this.count -= 1;
            if (this.count <= 0) {
                this.itemType = Optional.empty();
                this.count = 0;
            }

            this.markDirty();

            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack removeStack() {
        if (this.itemType.isPresent()) {
            ItemStack stack = this.itemType.get().copy();
            int toRemove = Math.min(this.count, stack.getMaxCount());
            stack.setCount(toRemove);
            this.count -= toRemove;
            if (this.count <= 0) {
                this.itemType = Optional.empty();
                this.count = 0;
            }

            this.markDirty();

            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack insert(ItemStack stack) {
        if (this.itemType.isPresent()) {
            if (this.isCorrectItemType(stack)) {
                int toInsert = Math.min(stack.getCount(), this.getMaxItemCount() - this.getItemCount());

                ItemStack ret = stack.copy();
                ret.setCount(stack.getCount() - toInsert);

                this.count += toInsert;

                this.markDirty();

                return ret;
            } else {
                return stack;
            }
        } else {
            ItemStack newItemType = stack.copy();
            newItemType.setCount(1);
            int toInsert = stack.getCount() > this.getMaxItemCount() ? this.getMaxItemCount() - stack.getCount() : stack.getCount();
            this.itemType = Optional.of(newItemType);
            this.count = toInsert;

            ItemStack ret = stack.copy();
            ret.setCount(stack.getCount() - toInsert);

            this.markDirty();

            return ret;
        }
    }

    public void dropItems(World world, BlockPos pos) {
        if (!this.itemType.isPresent()) return;

        ItemStack stack = this.itemType.get();

        while (this.getItemCount() > 0) {
            int toDrop = Math.min(this.getItemCount(), this.itemType.get().getMaxCount());
            this.count -= toDrop;

            ItemStack spawn = stack.copy();
            spawn.setCount(toDrop);

            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), spawn));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if (this.itemType.isPresent()) {
            CompoundTag itemTag = new CompoundTag();
            this.itemType.get().toTag(itemTag);
            tag.put("itemType", itemTag);
            tag.putInt("count", this.count);
        }

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        if (tag.contains("itemType")) {
            this.itemType = Optional.of(ItemStack.fromTag(tag.getCompound("itemType")));
            this.count = tag.getInt("count");
        } else {
            this.itemType = Optional.empty();
            this.count = 0;
        }
    }

    public Text getText() {
        if (!this.itemType.isPresent()) {
            return Texts.EMPTY;
        }
        return new LiteralText("")
                .append(itemType.get().getName())
                .append(String.format(": %d / %d", this.getItemCount(), this.getMaxItemCount()));
    }
}
