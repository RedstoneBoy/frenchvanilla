package net.bios.frenchvanilla.key_binds;

import net.bios.frenchvanilla.NbtIds;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class BindData {
    public boolean oreMine;

    public void copyFrom(BindData bindData) {
        this.oreMine = bindData.oreMine;
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putBoolean(NbtIds.KEY_ORE_MINE, oreMine);
        return compound;
    }

    public static BindData fromNbt(NbtCompound compound) {
        BindData data = new BindData();

        data.oreMine = compound.getBoolean(NbtIds.KEY_ORE_MINE);

        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BindData bindData = (BindData) o;
        return oreMine == bindData.oreMine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(oreMine);
    }
}
