package net.bios.frenchvanilla.home;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.bios.frenchvanilla.NbtIds;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class HomeComponent implements Component {
    @Nullable
    public Home home;

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(NbtIds.HOME)) {
            this.home = Home.fromNbt(tag.getCompound(NbtIds.HOME));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (home != null) {
            var compound = new NbtCompound();
            home.writeToNbt(compound);
            tag.put(NbtIds.HOME, compound);
        }
    }
}
