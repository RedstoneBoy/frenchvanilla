package net.bios.frenchvanilla.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Events {
    public static void register() {
        AttackBarrel.EVENT.register(new AttackBarrel());
        PlayerBlockBreakEvents.AFTER.register(new BreakBarrel());
        UseArmour.EVENT.register(new UseArmour());
        UseBlockCallback.EVENT.register(new UseBarrel());
        UseBlockCallback.EVENT.register(new UseCampfire());
    }
}
