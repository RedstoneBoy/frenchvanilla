package net.bios.frenchvanilla.config;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.bios.frenchvanilla.config.setting.BooleanSetting;
import net.bios.frenchvanilla.config.setting.DoubleSetting;
import net.bios.frenchvanilla.config.setting.IntegerSetting;
import net.bios.frenchvanilla.config.setting.Setting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.function.Function;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommandHelper {
    public static Text settingText(String settingKey, Setting setting) {
        return Text.literal("")
                .append(Text.literal(settingKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                .append(": ")
                .append(setting.text());
    }

    public static LiteralArgumentBuilder<ServerCommandSource> configCommand(LiteralArgumentBuilder<ServerCommandSource> command, Function<ServerPlayerEntity, ConfigSettings> configProvider, ConfigSettings instance) {
        for (Map.Entry<String, Setting> entry : instance.settings().entrySet()) {
            if (entry.getValue() instanceof BooleanSetting) {
                command = command.then(booleanSetting(entry.getKey(), configProvider));
            } else if (entry.getValue() instanceof DoubleSetting) {
                command = command.then(doubleSetting(entry.getKey(), configProvider));
            } else if (entry.getValue() instanceof IntegerSetting) {
                command = command.then(integerSetting(entry.getKey(), configProvider));
            }
        }

        return command;
    }

    private static LiteralArgumentBuilder<ServerCommandSource> booleanSetting(String settingKey, Function<ServerPlayerEntity, ConfigSettings> configProvider) {
        return literal(settingKey)
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    var setting = (BooleanSetting) configProvider.apply(player).settings().get(settingKey);

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                })
                .then(literal("true").executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    ConfigSettings config = configProvider.apply(player);
                    var setting = (BooleanSetting) config.settings().get(settingKey);
                    setting.value = true;
                    config.save();

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }))
                .then(literal("false").executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    ConfigSettings config = configProvider.apply(player);
                    var setting = (BooleanSetting) config.settings().get(settingKey);
                    setting.value = false;
                    config.save();

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> doubleSetting(String settingKey, Function<ServerPlayerEntity, ConfigSettings> configProvider) {
        return literal(settingKey)
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    var setting = (DoubleSetting) configProvider.apply(player).settings().get(settingKey);

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                })
                .then(argument("value", DoubleArgumentType.doubleArg()).executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    ConfigSettings config = configProvider.apply(player);
                    var setting = (DoubleSetting) config.settings().get(settingKey);
                    setting.value = DoubleArgumentType.getDouble(context, "value");
                    config.save();

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> integerSetting(String settingKey, Function<ServerPlayerEntity, ConfigSettings> configProvider) {
        return literal(settingKey)
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    var setting = (IntegerSetting) configProvider.apply(player).settings().get(settingKey);

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                })
                .then(argument("value", IntegerArgumentType.integer()).executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    ConfigSettings config = configProvider.apply(player);
                    var setting = (IntegerSetting) config.settings().get(settingKey);
                    setting.value = IntegerArgumentType.getInteger(context, "value");
                    config.save();

                    player.sendMessage(settingText(settingKey, setting), false);
                    return 1;
                }));
    }
}
