package net.bios.frenchvanilla.key_binds;

import net.bios.frenchvanilla.FrenchVanillaClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientBindsHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            FrenchVanillaClient.OLD_BIND_DATA.copyFrom(FrenchVanillaClient.BIND_DATA);

            FrenchVanillaClient.BIND_DATA.oreMine = ClientBinds.ORE_MINE.isPressed();

            if (!FrenchVanillaClient.OLD_BIND_DATA.equals(FrenchVanillaClient.BIND_DATA)) {
                C2SBindDataPacket.send(FrenchVanillaClient.BIND_DATA);
            }
        });
    }
}
