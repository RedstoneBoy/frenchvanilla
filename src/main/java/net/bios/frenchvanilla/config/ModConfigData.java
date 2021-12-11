package net.bios.frenchvanilla.config;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.data.Config;
import net.bios.frenchvanilla.FrenchVanilla;

@ConfigEntries
public final class ModConfigData extends Config implements ConfigContainer, ConfigData {
    public ModConfigData() {
        super(FrenchVanilla.ID);
    }

    public boolean armorSwapping = true;
    public boolean campfireResting = true;
    public boolean carryingBucket = true;
    public boolean deathLocks = true;
    public double deathKeyUnlockDistance = 10.0;
    public boolean homes = true;
    public boolean playerConfig = true;
    public boolean restoreXp = true;
    public boolean teleport = true;
    public int teleportExpireTime = 10;
    public boolean timber = true;
    public int timberTasksPerTick = 54;
}
