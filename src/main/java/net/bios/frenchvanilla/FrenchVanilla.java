package net.bios.frenchvanilla;

import net.bios.frenchvanilla.event.Events;
import net.fabricmc.api.ModInitializer;

public class FrenchVanilla implements ModInitializer {
    public static final String ID = "frenchvanilla";

    @Override
    public void onInitialize() {
        Events.register();
    }
}
