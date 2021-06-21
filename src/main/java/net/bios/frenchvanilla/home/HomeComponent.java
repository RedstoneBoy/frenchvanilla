package net.bios.frenchvanilla.home;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class HomeComponent implements Component {
    @Nullable
    public Home home;

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("home")) {
            this.home = Home.fromNbt(tag.getCompound("home"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (home != null) {
            var compound = new NbtCompound();
            home.writeToNbt(compound);
            tag.put("home", compound);
        }
    }
}
