package net.bios.frenchvanilla.config;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.data.Config;
import net.bios.frenchvanilla.FrenchVanilla;

@ConfigEntries(includeAll = true)
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
    public boolean oreMiner = true;
    public int oreMinerMaxVeinSize = 1024;
    public boolean playerConfig = true;
    public boolean restoreXp = true;
    public boolean sanity = true;
    public int tasksPerTick = 128;
    public boolean teleport = true;
    public int teleportExpireTime = 10;
    public boolean timber = true;
    public boolean unpath = true;
}
