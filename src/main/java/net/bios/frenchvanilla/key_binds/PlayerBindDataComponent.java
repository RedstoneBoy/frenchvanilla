package net.bios.frenchvanilla.key_binds;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.bios.frenchvanilla.NbtIds;
import net.minecraft.nbt.NbtCompound;

public class PlayerBindDataComponent implements Component {
    public BindData data;
    public BindData oldData;

    public PlayerBindDataComponent() {
        this.data = new BindData();
        this.oldData = new BindData();
    }

    public void update(BindData newData) {
        this.oldData = this.data;
        this.data = newData;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound dataTag = tag.getCompound(NbtIds.BIND_DATA);
        if (dataTag != null) {
            this.data = BindData.fromNbt(dataTag);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put(NbtIds.BIND_DATA, this.data.toNbt());
    }
}
