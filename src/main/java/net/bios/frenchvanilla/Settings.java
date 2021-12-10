package net.bios.frenchvanilla;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.data.Config;

@ConfigEntries
public final class Settings extends Config implements ConfigContainer {
    public Settings() {
        super(FrenchVanilla.ID);
    }

    public boolean armorSwapping = true;
    public boolean campfireResting = true;
    public boolean carryingBucket = true;
    public boolean deathLocks = true;
    public boolean restoreXp = true;
    public double deathKeyUnlockDistance = 10.0;
    public boolean homes = true;
    public boolean playerSettings = true;
    public boolean timber = true;
    public int timberTasksPerTick = 54;
}
