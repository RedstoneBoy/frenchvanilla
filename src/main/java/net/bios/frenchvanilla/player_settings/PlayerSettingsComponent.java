package net.bios.frenchvanilla.player_settings;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Map;

public class PlayerSettingsComponent implements Component {
    private PlayerSettings settings;

    public PlayerSettings settings() {
        return this.settings;
    }

    public PlayerSettingsComponent() {
        this.settings = new PlayerSettings();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (Map.Entry<String, PlayerSetting> setting : this.settings.settings().entrySet()) {
            NbtElement element = tag.get(setting.getKey());
            if (element != null) {
                setting.getValue().readNbt(element);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        for (Map.Entry<String, PlayerSetting> setting : this.settings.settings().entrySet()) {
            tag.put(setting.getKey(), setting.getValue().toNbt());
        }
    }
}
