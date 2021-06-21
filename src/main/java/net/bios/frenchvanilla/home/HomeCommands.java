package net.bios.frenchvanilla.home;

import com.mojang.brigadier.CommandDispatcher;
import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import static net.bios.frenchvanilla.Components.HOME;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HomeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("sethome").requires(source -> FrenchVanilla.config.homes).executes(context -> {
            var player = context.getSource().getPlayer();
            var homeComponent = HOME.get(player);
            homeComponent.home = new Home(player.getServerWorld().getRegistryKey().getValue(), player.getPos());

            context.getSource().sendFeedback(new LiteralText("You have a new home.")
                    .setStyle(Style.EMPTY.withColor(Formatting.AQUA)), false);

            return 1;
        }));

        dispatcher.register(literal("home").requires(source -> FrenchVanilla.config.homes).executes(context -> {
            var player = context.getSource().getPlayer();

            if (!teleportToHome(player, player)) {
                context.getSource().sendFeedback(new LiteralText("You do not have a home. Use /sethome to set a home")
                        .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
            }

            return 1;
        })
                .then(argument("player", EntityArgumentType.player()).requires(source -> source.hasPermissionLevel(2)).executes(context -> {
                    var sourcePlayer = context.getSource().getPlayer();
                    var homeTarget = EntityArgumentType.getPlayer(context, "player");
                    if (!teleportToHome(sourcePlayer, homeTarget)) {
                        context.getSource().sendFeedback(new LiteralText("This player does not have a home.")
                                .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                    }
                    return 1;
                })));
    }

    private static boolean teleportToHome(ServerPlayerEntity source, ServerPlayerEntity homeTarget) {
        var home = HOME.get(homeTarget).home;
        if (home == null) {
            return false;
        }

        for (ServerWorld serverWorld : source.getServer().getWorlds()) {
            if (serverWorld.getRegistryKey().getValue().equals(home.dimension)) {
                source.teleport(serverWorld, home.position.x, home.position.y, home.position.z, source.getYaw(), source.getPitch());
            }
        }

        return true;
    }
}
