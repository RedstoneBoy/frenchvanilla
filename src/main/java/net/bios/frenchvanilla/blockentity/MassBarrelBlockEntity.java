package net.bios.frenchvanilla.blockentity;

import net.bios.frenchvanilla.Texts;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MassBarrelBlockEntity extends BlockEntity implements Inventory, SidedInventory, Clearable, Tickable {
    private Optional<ItemStack> itemType;
    private int count;

    private ItemStack fullStack;
    private ItemStack emptyStack;

    public MassBarrelBlockEntity() {
        super(BlockEntities.MASS_BARREL_BLOCK_ENTITY);

        this.clear();
    }

    public boolean hasItemType() {
        return this.itemType.isPresent();
    }

    public Optional<ItemStack> getItemType() {
        return this.itemType;
    }

    public boolean isCorrectItemType(ItemStack stack) {
        return this.itemType.map(itemStack -> ItemStack.areItemsEqual(itemStack, stack) && ItemStack.areTagsEqual(itemStack, stack)).orElse(true);
    }

    public int getItemCount() {
        return this.itemType.isPresent() ? this.count + this.fullStack.getCount() + this.emptyStack.getCount() : 0;
    }

    public int getMaxItemCount() {
        return 1024 * this.itemType.map(ItemStack::getMaxCount).orElse(1);
    }

    public ItemStack removeOne() {
        if (this.itemType.isPresent()) {
            ItemStack stack = this.itemType.get().copy();
            stack.setCount(1);
            if (this.emptyStack.getCount() > 0) {
                this.emptyStack.decrement(1);
            } else if (this.fullStack.getCount() > 0) {
                this.fullStack.decrement(1);
            } else {
                this.count -= 1;
            }
            if (this.getItemCount() <= 0) {
                this.clear();
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
            int toRemove = Math.min(this.getItemCount(), stack.getMaxCount());
            stack.setCount(toRemove);
            if (this.emptyStack.getCount() > 0) {
                if (toRemove >= this.emptyStack.getCount()) {
                    toRemove -= this.emptyStack.getCount();
                    this.emptyStack.setCount(0);
                } else {
                    this.emptyStack.decrement(toRemove);
                    toRemove = 0;
                }
            }
            if (toRemove > 0 && this.fullStack.getCount() > 0) {
                if (toRemove >= this.fullStack.getCount()) {
                    toRemove -= this.fullStack.getCount();
                    this.fullStack.setCount(0);
                } else {
                    this.fullStack.decrement(toRemove);
                    toRemove = 0;
                }
            }
            if (toRemove > 0) {
                this.count = Math.max(this.count - toRemove, 0);
            }
            if (this.getItemCount() <= 0) {
                this.clear();
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
            this.fullStack = newItemType.copy();
            this.fullStack.setCount(0);
            this.emptyStack = newItemType.copy();
            this.emptyStack.setCount(0);

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

        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.NETHER_STAR, 1)));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if (this.itemType.isPresent()) {
            CompoundTag itemTag = new CompoundTag();
            this.itemType.get().toTag(itemTag);
            tag.put("itemType", itemTag);
            tag.putInt("count", this.getItemCount());
        }

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        this.clear();

        if (tag.contains("itemType")) {
            this.itemType = Optional.of(ItemStack.fromTag(tag.getCompound("itemType")));
            this.count = tag.getInt("count");
            this.fullStack = this.itemType.get().copy();
            this.fullStack.setCount(0);
            this.emptyStack = this.itemType.get().copy();
            this.emptyStack.setCount(0);
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

    @Override
    public void clear() {
        this.itemType = Optional.empty();
        this.count = 0;
        this.fullStack = ItemStack.EMPTY;
        this.emptyStack = ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        if (this.itemType.isPresent()) {
            if (this.emptyStack.getCount() > 0) {
                this.count += this.emptyStack.getCount();
                this.emptyStack.setCount(0);
            }

            if (this.fullStack.getCount() < this.itemType.get().getMaxCount()) {
                int toTransfer = Math.min(this.count, this.itemType.get().getMaxCount() - this.fullStack.getCount());
                this.count -= toTransfer;
                this.fullStack.increment(toTransfer);
            }

            if (this.getItemCount() <= 0) {
                this.clear();
            }
        }
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return !this.hasItemType();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == 0) return this.fullStack;
        if (slot == 1) return this.emptyStack;
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot == 0) {
            ItemStack stack = this.fullStack.copy();
            int toRemove = Math.min(amount, this.fullStack.getCount());
            stack.setCount(toRemove);
            this.fullStack.decrement(toRemove);
            return stack;
        } else if (slot == 1) {
            ItemStack stack = this.emptyStack.copy();
            int toRemove = Math.min(amount, this.emptyStack.getCount());
            stack.setCount(toRemove);
            this.emptyStack.decrement(toRemove);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot == 0) {
            ItemStack stack = this.fullStack.copy();
            this.fullStack.setCount(0);
            return stack;
        } else if (slot == 1) {
            ItemStack stack = this.emptyStack.copy();
            this.emptyStack.setCount(0);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (this.itemType.isPresent()) {
            if (this.isCorrectItemType(stack)) {
                if (slot == 0) this.fullStack = stack.copy();
                else if (slot == 1) this.emptyStack = stack.copy();
            }
        } else {
            this.insert(stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return !this.itemType.isPresent() || (this.isCorrectItemType(stack) && stack.getCount() < this.getMaxItemCount() - this.getItemCount());
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
