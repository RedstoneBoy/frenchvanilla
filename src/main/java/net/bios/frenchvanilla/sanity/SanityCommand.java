package net.bios.frenchvanilla.sanity;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.Perms;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.bios.frenchvanilla.Components.PLAYER_SANITY;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SanityCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var sanityCmd = literal("sanity")
                .requires(source -> FrenchVanilla.config.sanity.value)
                .executes(context -> {
                    var player = context.getSource().getPlayerOrThrow();

                    showSanity(context, player);

                    return 1;
                });

        var playerSanityCmd = argument("player", EntityArgumentType.player())
                .requires(Permissions.require(Perms.SANITY_COMMAND, 4))
                .executes(context -> {
                    var player = EntityArgumentType.getPlayer(context, "player");

                    showSanity(context, player);

                    return 1;
                });

        playerSanityCmd = playerSanityCmd.then(literal("set").then(argument("value", DoubleArgumentType.doubleArg(0.0, 1.0))
                .executes(context -> {
                    var player = EntityArgumentType.getPlayer(context, "player");
                    var value = DoubleArgumentType.getDouble(context, "value");

                    setSanity(context, player, value);

                    return 1;
                })));

        playerSanityCmd = playerSanityCmd.then(literal("effect").then(argument("heffect", IdentifierArgumentType.identifier())
                .executes(context -> {
                    var player = EntityArgumentType.getPlayer(context, "player");
                    var value = IdentifierArgumentType.getIdentifier(context, "heffect");

                    addEffect(context, player, value);

                    return 1;
                })));

        sanityCmd = sanityCmd.then(playerSanityCmd);

        dispatcher.register(sanityCmd);
    }

    private static void showSanity(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
        PlayerSanityComponent sanity = PLAYER_SANITY.get(player);

        context.getSource().sendFeedback(Text.literal("")
                .append(player.getName())
                .append(Text.literal(String.format(" sanity: %.1f%%", sanity.getSanity()))), false);
    }

    private static void setSanity(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, double value) {
        PlayerSanityComponent sanity = PLAYER_SANITY.get(player);
        double sanityDiff = value - sanity.getSanity() - sanity.sanityChange();
        sanity.addSanity(sanityDiff);
    }

    private static void addEffect(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, Identifier effect) {
        PlayerSanityComponent sanity = PLAYER_SANITY.get(player);
        var hallu = FrenchVanilla.HALLUCINATIONS.get(effect);
        if (hallu == null) {
            context.getSource().sendError(Text.literal("Effect does not exist"));
            return;
        }
        var heffect = hallu.create(player);
        sanity.halEffects().add(heffect);
    }
}
