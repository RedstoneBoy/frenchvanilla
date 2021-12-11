package net.bios.frenchvanilla.player_config;

import com.mojang.brigadier.CommandDispatcher;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.config.ConfigCommandHelper;
import net.bios.frenchvanilla.config.ConfigSettings;
import net.bios.frenchvanilla.config.setting.Setting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Map;

import static net.bios.frenchvanilla.Components.PLAYER_CONFIG;
import static net.minecraft.server.command.CommandManager.literal;

public class PlayerConfigCommand {
    private static ConfigSettings playerConfig(ServerPlayerEntity player) {
        return PLAYER_CONFIG.get(player).config();
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var command = literal("my_config").requires(source -> FrenchVanilla.config.playerConfig.value).executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayer();
            PlayerConfigComponent component = PLAYER_CONFIG.get(player);

            player.sendMessage(new LiteralText("Settings:"), false);

            for (Map.Entry<String, Setting> entry : component.config().settings().entrySet()) {
                player.sendMessage(ConfigCommandHelper.settingText(entry.getKey(), entry.getValue()), false);
            }

            return 1;
        });

        command = ConfigCommandHelper.configCommand(command, PlayerConfigCommand::playerConfig, new PlayerConfig());

        dispatcher.register(command);
    }
}
