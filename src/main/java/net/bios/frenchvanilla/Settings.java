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
}
