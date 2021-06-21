package net.bios.frenchvanilla.deathlock;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public final class DeathLock {
    private final Identifier dimension;
    private final BlockPos position;
    private final UUID ownerId;
    private final ArrayList<ItemStack> stacks;

    public DeathLock(Identifier dimension, BlockPos position, UUID ownerId,
                     Collection<ItemStack> stacks) {
        this.dimension = dimension;
        this.position = position;
        this.ownerId = ownerId;
        this.stacks = new ArrayList<>(stacks);
    }

    public static DeathLock readFromNbt(NbtCompound tag) {
        Identifier dimension = new Identifier(tag.getString("dimension"));
        BlockPos position = new BlockPos(
                tag.getInt("x"),
                tag.getInt("y"),
                tag.getInt("z")
        );
        UUID ownerId = tag.getUuid("owner");
        NbtList items = tag.getList("items", NbtElement.COMPOUND_TYPE);
        ArrayList<ItemStack> stacks = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            stacks.add(i, ItemStack.fromNbt(items.getCompound(i)));
        }

        return new DeathLock(dimension, position, ownerId, stacks);
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putString("dimension", dimension.toString());
        tag.putInt("x", position.getX());
        tag.putInt("y", position.getY());
        tag.putInt("z", position.getZ());
        tag.putUuid("owner", ownerId);

        NbtList items = new NbtList();
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                items.add(stack.writeNbt(new NbtCompound()));
            }
        }

        tag.put("items", items);
    }

    public Identifier dimension() {
        return dimension;
    }

    public BlockPos position() {
        return position;
    }

    public UUID ownerId() {
        return ownerId;
    }

    public List<ItemStack> stacks() {
        return stacks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DeathLock) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.ownerId, that.ownerId) &&
                Objects.equals(this.stacks, that.stacks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, ownerId, stacks);
    }

    @Override
    public String toString() {
        return "DeathLock[" +
                "position=" + position + ", " +
                "ownerId=" + ownerId + ", " +
                "stacks=" + stacks + ']';
    }

}
