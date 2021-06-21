package net.bios.frenchvanilla;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class FrenchVanilla implements ModInitializer {
    public static final String ID = "frenchvanilla";

    public static Settings config;

    public static Identifier identifier(String value) {
        return new Identifier(ID, value);
    }

    @Override
    public void onInitialize() {
        config = new Settings();
        config.load();

        Events.register();
    }
}
