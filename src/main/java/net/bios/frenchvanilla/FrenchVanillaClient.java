package net.bios.frenchvanilla;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.fabricmc.api.ClientModInitializer;

public class FrenchVanillaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigScreenBuilder.setMain(FrenchVanilla.ID, new ClothConfigScreenBuilder());
    }
}
