package net.bios.frenchvanilla.player_settings;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

import static net.bios.frenchvanilla.Components.PLAYER_SETTINGS;
import static net.minecraft.server.command.CommandManager.literal;

public class PlayerSettingsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var command = literal("my_settings").requires(source -> FrenchVanilla.config.playerSettings).executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayer();
            PlayerSettingsComponent component = PLAYER_SETTINGS.get(player);

            player.sendMessage(new LiteralText("Settings:"), false);

            for (Map.Entry<String, PlayerSetting> entry : component.settings().settings().entrySet()) {
                player.sendMessage(settingText(entry.getKey(), entry.getValue()), false);
            }

            return 1;
        });

        for (Map.Entry<String, PlayerSetting> entry : new PlayerSettings().settings().entrySet()) {
            if (entry.getValue() instanceof PlayerBooleanSetting) {
                command = command.then(booleanSetting(entry.getKey()));
            }
        }

        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> booleanSetting(String settingKey) {
        return literal(settingKey)
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    var setting = (PlayerBooleanSetting) PLAYER_SETTINGS.get(player).settings().settings().get(settingKey);

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                })
                .then(literal("true").executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    var setting = (PlayerBooleanSetting) PLAYER_SETTINGS.get(player).settings().settings().get(settingKey);
                    setting.value = true;

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }))
                .then(literal("false").executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    var setting = (PlayerBooleanSetting) PLAYER_SETTINGS.get(player).settings().settings().get(settingKey);
                    setting.value = false;

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }));
    }

    private static Text settingText(String settingKey, PlayerSetting setting) {
        return new LiteralText("")
                .append(new LiteralText(settingKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                .append(new LiteralText(": "))
                .append(setting.text());
    }
}
