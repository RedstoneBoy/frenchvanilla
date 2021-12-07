package net.bios.frenchvanilla.player_settings;

import java.util.HashMap;
import java.util.Map;

public class PlayerSettings {
    public PlayerBooleanSetting armorSwapping = new PlayerBooleanSetting(true);
    public PlayerBooleanSetting timber = new PlayerBooleanSetting(true);

    public Map<String, PlayerSetting> settings() {
        Map<String, PlayerSetting> settings = new HashMap<>();

        settings.put("armorSwapping", armorSwapping);
        settings.put("timber", timber);

        return settings;
    }
}
