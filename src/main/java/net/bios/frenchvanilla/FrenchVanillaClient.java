package net.bios.frenchvanilla;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.bios.frenchvanilla.key_binds.BindData;
import net.bios.frenchvanilla.key_binds.ClientBinds;
import net.bios.frenchvanilla.key_binds.ClientBindsHandler;
import net.fabricmc.api.ClientModInitializer;

public class FrenchVanillaClient implements ClientModInitializer {
    public static BindData BIND_DATA = new BindData();
    public static BindData OLD_BIND_DATA = new BindData();

    @Override
    public void onInitializeClient() {
        ConfigScreenBuilder.setMain(FrenchVanilla.ID, new ClothConfigScreenBuilder());

        ClientBinds.register();
        ClientBindsHandler.register();
        S2CPackets.register();
    }
}
