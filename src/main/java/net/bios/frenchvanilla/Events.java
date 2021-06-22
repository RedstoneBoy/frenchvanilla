package net.bios.frenchvanilla;

import net.bios.frenchvanilla.armor_switch.UseArmourEvent;
import net.bios.frenchvanilla.campfire_rest.UseCampfireEvent;
import net.bios.frenchvanilla.deathlock.PlayerDeathEvents;
import net.bios.frenchvanilla.deathlock.UseDeathKeyEvent;
import net.bios.frenchvanilla.timber.LogBreakEvent;

public class Events {
    public static void register() {
        Commands.registerEvent();
        LogBreakEvent.register();
        PlayerDeathEvents.register();
        UseDeathKeyEvent.register();
        UseArmourEvent.register();
        UseCampfireEvent.register();
    }
}
