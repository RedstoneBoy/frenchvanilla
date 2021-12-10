package net.bios.frenchvanilla.config;

import net.bios.frenchvanilla.config.setting.Setting;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigSettings {
    private ConfigData configData;

    protected ConfigSettings(ConfigData configData) {
        this.configData = configData;
    }

    public final void load() {
        this.configData.load();

        Class<?> dataClass = this.configData.getClass();
        for (Field settingField : this.thisClass().getDeclaredFields()) {
            try {
                Object settingObject = settingField.get(this);
                if (settingObject instanceof Setting setting) {
                    Field dataField = dataClass.getDeclaredField(settingField.getName());
                    if (setting.getValue().getClass().isAssignableFrom(dataField.getType())) {
                        setting.setValue(dataField.get(this.configData));
                    }
                }
            } catch (Exception ex) {
                System.out.println("Failed to load setting '" + settingField.getName() + "':");
                System.out.println(ex);
                continue;
            }
        }
    }

    public final void save() {
        Class<?> dataClass = this.configData.getClass();
        for (Field settingField : this.thisClass().getDeclaredFields()) {
            try {
                Object settingObject = settingField.get(this);
                if (settingObject instanceof Setting setting) {
                    Field dataField = dataClass.getDeclaredField(settingField.getName());
                    dataField.set(this.configData, setting.getValue());
                }
            } catch (Exception ex) {
                System.out.println("Failed to save setting '" + settingField.getName() + "':");
                System.out.println(ex);

                continue;
            }
        }

        this.configData.save();
    }

    public final Map<String, Setting> settings() {
        HashMap<String, Setting> settings = new HashMap<>();
        for (Field settingField : this.thisClass().getDeclaredFields()) {
            try {
                Object settingObject = settingField.get(this);
                if (settingObject instanceof Setting setting) {
                    settings.put(settingField.getName(), setting);
                }
            } catch (IllegalAccessException ex) {
                continue;
            }
        }
        return settings;
    }

    protected abstract Class<?> thisClass();
}
