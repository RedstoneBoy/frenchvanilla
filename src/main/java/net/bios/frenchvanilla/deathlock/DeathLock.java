package net.bios.frenchvanilla.deathlock;

import net.bios.frenchvanilla.NbtIds;
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
    private final int xpLevel;

    public DeathLock(Identifier dimension, BlockPos position, UUID ownerId,
                     Collection<ItemStack> stacks, int xpLevel) {
        this.dimension = dimension;
        this.position = position;
        this.ownerId = ownerId;
        this.stacks = new ArrayList<>(stacks);
        this.xpLevel = xpLevel;
    }

    public static DeathLock readFromNbt(NbtCompound tag) {
        Identifier dimension = new Identifier(tag.getString(NbtIds.DEATH_LOCK_DIMENSION));
        BlockPos position = new BlockPos(
                tag.getInt(NbtIds.DEATH_LOCK_X),
                tag.getInt(NbtIds.DEATH_LOCK_Y),
                tag.getInt(NbtIds.DEATH_LOCK_Z)
        );
        UUID ownerId = tag.getUuid(NbtIds.DEATH_LOCK_OWNER);
        NbtList items = tag.getList(NbtIds.DEATH_LOCK_ITEMS, NbtElement.COMPOUND_TYPE);
        ArrayList<ItemStack> stacks = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            stacks.add(i, ItemStack.fromNbt(items.getCompound(i)));
        }
        int xpLevel = tag.getInt(NbtIds.DEATH_LOCK_LEVEL);

        return new DeathLock(dimension, position, ownerId, stacks, xpLevel);
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putString(NbtIds.DEATH_LOCK_DIMENSION, dimension.toString());
        tag.putInt(NbtIds.DEATH_LOCK_X, position.getX());
        tag.putInt(NbtIds.DEATH_LOCK_Y, position.getY());
        tag.putInt(NbtIds.DEATH_LOCK_Z, position.getZ());
        tag.putUuid(NbtIds.DEATH_LOCK_OWNER, ownerId);

        NbtList items = new NbtList();
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                items.add(stack.writeNbt(new NbtCompound()));
            }
        }

        tag.put(NbtIds.DEATH_LOCK_ITEMS, items);

        tag.putInt(NbtIds.DEATH_LOCK_LEVEL, xpLevel);
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

    public int xpLevel() {
        return xpLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeathLock deathLock = (DeathLock) o;
        return xpLevel == deathLock.xpLevel && dimension.equals(deathLock.dimension) && position.equals(deathLock.position) && ownerId.equals(deathLock.ownerId) && stacks.equals(deathLock.stacks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, position, ownerId, stacks, xpLevel);
    }

    @Override
    public String toString() {
        return "DeathLock{" +
                "dimension=" + dimension +
                ", position=" + position +
                ", ownerId=" + ownerId +
                ", stacks=" + stacks +
                ", xpLevel=" + xpLevel +
                '}';
    }
}
