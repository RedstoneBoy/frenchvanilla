package net.bios.frenchvanilla.config;

import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.Perms;
import net.bios.frenchvanilla.config.setting.Setting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommand {
    private static ConfigSettings modConfig(ServerPlayerEntity player) {
        return FrenchVanilla.config;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var command = literal("fv_config").requires(Permissions.require(Perms.CONFIG, 4)).executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayer();

            player.sendMessage(new LiteralText("Settings:"), false);

            for (Map.Entry<String, Setting> entry : FrenchVanilla.config.settings().entrySet()) {
                player.sendMessage(ConfigCommandHelper.settingText(entry.getKey(), entry.getValue()), false);
            }

            return 1;
        });

        command = ConfigCommandHelper.configCommand(command, ConfigCommand::modConfig, FrenchVanilla.config);

        dispatcher.register(command);
    }
}
