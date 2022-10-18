package net.bios.frenchvanilla;

import net.bios.frenchvanilla.config.ModConfig;
import net.bios.frenchvanilla.sanity.Hallucination;
import net.bios.frenchvanilla.sanity.hallucinations.Hallucinations;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class FrenchVanilla implements ModInitializer {
    public static final String ID = "frenchvanilla";

    public static final HashMap<Identifier, Hallucination> HALLUCINATIONS = new HashMap<>();

    public static ModConfig config;

    public static Identifier identifier(String value) {
        return new Identifier(ID, value);
    }

    @Override
    public void onInitialize() {
        config = new ModConfig();
        config.load();

        C2SPackets.register();
        Events.register();
        Hallucinations.register();
    }
}
