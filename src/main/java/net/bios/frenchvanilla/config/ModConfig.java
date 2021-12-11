package net.bios.frenchvanilla.config;

import net.bios.frenchvanilla.config.setting.BooleanSetting;
import net.bios.frenchvanilla.config.setting.DoubleSetting;
import net.bios.frenchvanilla.config.setting.IntegerSetting;

public class ModConfig extends ConfigSettings {
    public BooleanSetting armorSwapping = new BooleanSetting(true);
    public BooleanSetting campfireResting = new BooleanSetting(true);
    public BooleanSetting carryingBucket = new BooleanSetting(true);
    public BooleanSetting deathLocks = new BooleanSetting(true);
    public DoubleSetting deathKeyUnlockDistance = new DoubleSetting(10.0);
    public BooleanSetting homes = new BooleanSetting(true);
    public BooleanSetting playerConfig = new BooleanSetting(true);
    public BooleanSetting restoreXp = new BooleanSetting(true);
    public BooleanSetting teleport = new BooleanSetting(true);
    public IntegerSetting teleportExpireTime = new IntegerSetting(10);
    public BooleanSetting timber = new BooleanSetting(true);
    public IntegerSetting timberTasksPerTick = new IntegerSetting(54);

    public ModConfig() {
        super(new ModConfigData());
    }

    @Override
    protected Class<?> thisClass() {
        return this.getClass();
    }
}
