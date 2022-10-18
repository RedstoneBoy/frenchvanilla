package net.bios.frenchvanilla;

import net.bios.frenchvanilla.config.ConfigCommand;
import net.bios.frenchvanilla.deathlock.DeathsCommand;
import net.bios.frenchvanilla.home.HomeCommands;
import net.bios.frenchvanilla.player_config.PlayerConfigCommand;
import net.bios.frenchvanilla.sanity.SanityCommand;
import net.bios.frenchvanilla.teleport.TeleportCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Commands {
    public static void registerEvent() {
        CommandRegistrationCallback.EVENT.register((dispatcher, access, env) -> {
            ConfigCommand.register(dispatcher);
            DeathsCommand.register(dispatcher);
            HomeCommands.register(dispatcher);
            PlayerConfigCommand.register(dispatcher);
            SanityCommand.register(dispatcher);
            TeleportCommands.register(dispatcher);
        });
    }
}
