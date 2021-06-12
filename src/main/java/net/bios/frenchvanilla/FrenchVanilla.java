package net.bios.frenchvanilla;

import net.bios.frenchvanilla.event.Events;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class FrenchVanilla implements ModInitializer {
    public static final String ID = "frenchvanilla";

    public static final Identifier identifier(String value) {
        return new Identifier(ID, value);
    }

    @Override
    public void onInitialize() {
        Events.register();
    }
}
