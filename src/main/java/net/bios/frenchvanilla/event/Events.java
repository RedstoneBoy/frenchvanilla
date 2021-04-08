package net.bios.frenchvanilla.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Events {
    public static void register() {
        UseBlockCallback.EVENT.register(new UseBlock());
    }
}
