package net.bios.frenchvanilla;

import net.bios.frenchvanilla.armor_switch.UseArmourEvent;
import net.bios.frenchvanilla.campfire_rest.UseCampfireEvent;
import net.bios.frenchvanilla.carrying_bucket.UseBucketEvent;
import net.bios.frenchvanilla.deathlock.PlayerDeathEvents;
import net.bios.frenchvanilla.deathlock.UseDeathKeyEvent;
import net.bios.frenchvanilla.ore_miner.OreBreakEvent;
import net.bios.frenchvanilla.timber.LogBreakEvent;

public class Events {
    public static void register() {
        Commands.registerEvent();
        LogBreakEvent.register();
        OreBreakEvent.register();
        PlayerDeathEvents.register();
        UseDeathKeyEvent.register();
        UseArmourEvent.register();
        UseBucketEvent.register();
        UseCampfireEvent.register();
    }
}
