package net.bios.frenchvanilla.player_settings;

import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;

public interface PlayerSetting {
    NbtElement toNbt();

    void readNbt(NbtElement element);

    Text text();
}
