package net.bios.frenchvanilla.config.setting;

import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

public interface Setting {
    NbtElement toNbt();

    void readNbt(NbtElement element);

    Text text();

    Object getValue();

    void setValue(Object obj)
            throws ClassCastException;
}
