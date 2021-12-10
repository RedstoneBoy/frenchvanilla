package net.bios.frenchvanilla.player_config;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.bios.frenchvanilla.config.setting.Setting;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Map;

public class PlayerConfigComponent implements Component {
    private PlayerConfig config;

    public PlayerConfig config() {
        return this.config;
    }

    public PlayerConfigComponent() {
        this.config = new PlayerConfig();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (Map.Entry<String, Setting> setting : this.config.settings().entrySet()) {
            NbtElement element = tag.get(setting.getKey());
            if (element != null) {
                setting.getValue().readNbt(element);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        for (Map.Entry<String, Setting> setting : this.config.settings().entrySet()) {
            tag.put(setting.getKey(), setting.getValue().toNbt());
        }
    }
}
