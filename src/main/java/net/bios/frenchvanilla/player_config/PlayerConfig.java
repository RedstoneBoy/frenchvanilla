package net.bios.frenchvanilla.player_config;

import net.bios.frenchvanilla.config.ConfigSettings;
import net.bios.frenchvanilla.config.setting.BooleanSetting;

public class PlayerConfig extends ConfigSettings {
    public BooleanSetting armorSwapping = new BooleanSetting(true);
    public BooleanSetting timber = new BooleanSetting(true);

    public PlayerConfig() {
        super(new PlayerConfigData());
    }

    @Override
    protected Class<?> thisClass() {
        return this.getClass();
    }
}
