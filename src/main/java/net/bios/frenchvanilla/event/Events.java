package net.bios.frenchvanilla.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Events {
    public static void register() {
        UseArmour.EVENT.register(new UseArmour());
        UseBlockCallback.EVENT.register(new UseCampfire());
    }
}
