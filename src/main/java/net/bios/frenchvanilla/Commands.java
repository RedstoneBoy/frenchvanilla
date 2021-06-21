package net.bios.frenchvanilla;

import net.bios.frenchvanilla.deathlock.DeathsCommand;
import net.bios.frenchvanilla.home.HomeCommands;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Commands {
    public static void registerEvent() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            DeathsCommand.register(dispatcher);
            HomeCommands.register(dispatcher);
        });
    }
}
