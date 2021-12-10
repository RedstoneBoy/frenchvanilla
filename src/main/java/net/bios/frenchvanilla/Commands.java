package net.bios.frenchvanilla;

import net.bios.frenchvanilla.config.ConfigCommand;
import net.bios.frenchvanilla.deathlock.DeathsCommand;
import net.bios.frenchvanilla.home.HomeCommands;
import net.bios.frenchvanilla.player_config.PlayerConfigCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Commands {
    public static void registerEvent() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            ConfigCommand.register(dispatcher);
            DeathsCommand.register(dispatcher);
            HomeCommands.register(dispatcher);
            PlayerConfigCommand.register(dispatcher);
        });
    }
}
