package net.bios.frenchvanilla.teleport;

import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.bios.frenchvanilla.Components;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.Perms;
import net.bios.frenchvanilla.player_config.TeleportRequest;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.time.Instant;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeleportCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var tpto = literal("tpto")
                .requires(source -> FrenchVanilla.config.teleport.value && Permissions.require(Perms.TELEPORT, 2).test(source))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {
                            ServerPlayerEntity from = context.getSource().getPlayer();
                            ServerPlayerEntity to = EntityArgumentType.getPlayer(context, "player");

                            boolean requested = Components.TELEPORT.get(to)
                                    .addTeleportRequest(from.getUuid(), new TeleportRequest(Instant.now(), false));

                            if (!requested) {
                                from.sendMessage(new LiteralText("You have already sent this player a teleport request, please wait until it expires, or they accept it."), false);
                            } else {
                                from.sendMessage(new LiteralText("Sent request to teleport to ")
                                        .append(to.getName()), false);

                                to.sendMessage(new LiteralText("")
                                                .append(from.getName())
                                                .append(new LiteralText(" wants to teleport to you, type /tpa <player> to accept"))
                                        , false);
                            }

                            return 1;
                        })
                );

        var tptome = literal("tptome")
                .requires(source -> FrenchVanilla.config.teleport.value && Permissions.require(Perms.TELEPORT, 2).test(source))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {
                            ServerPlayerEntity from = context.getSource().getPlayer();
                            ServerPlayerEntity to = EntityArgumentType.getPlayer(context, "player");

                            boolean requested = Components.TELEPORT.get(to)
                                    .addTeleportRequest(from.getUuid(), new TeleportRequest(Instant.now(), true));

                            if (!requested) {
                                from.sendMessage(new LiteralText("You have already sent this player a teleport request, please wait until it expires, or they accept it."), false);
                            } else {
                                from.sendMessage(new LiteralText("Sent request to teleport ")
                                        .append(to.getName())
                                        .append(" to you"), false);

                                to.sendMessage(new LiteralText("")
                                                .append(from.getName())
                                                .append(new LiteralText(" wants you to teleport to them, type /tpa <player> to accept"))
                                        , false);
                            }

                            return 1;
                        })
                );

        var tpa = literal("tpa")
                .executes(context -> {
                    ServerPlayerEntity acceptor = context.getSource().getPlayer();

                    var teleport = Components.TELEPORT.get(acceptor);
                    teleport.removeSingleRequest().ifPresentOrElse(
                            requestEntry -> {
                                UUID requesterId = requestEntry.left;
                                TeleportRequest request = requestEntry.right;
                                ServerPlayerEntity requester = context.getSource().getServer().getPlayerManager().getPlayer(requesterId);

                                if (request.teleportToRequester()) {
                                    acceptor.teleport(requester.getWorld(), requester.getX(), requester.getY(), requester.getZ(), acceptor.getYaw(), acceptor.getPitch());
                                } else {
                                    requester.teleport(acceptor.getWorld(), acceptor.getX(), acceptor.getY(), acceptor.getZ(), requester.getYaw(), requester.getPitch());
                                }
                            },
                            () -> {
                                if (teleport.numRequests() == 0) {
                                    acceptor.sendMessage(new LiteralText("There are no teleport requests to accept"), false);
                                } else {
                                    acceptor.sendMessage(new LiteralText("You have multiple requests, use /tpa <player> to accept a specific one"), false);
                                }
                            }
                    );

                    return 1;
                })
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {
                            ServerPlayerEntity acceptor = context.getSource().getPlayer();
                            ServerPlayerEntity requester = EntityArgumentType.getPlayer(context, "player");

                            var teleport = Components.TELEPORT.get(acceptor);
                            teleport.removeRequest(requester.getUuid()).ifPresentOrElse(
                                    request -> {
                                        if (request.teleportToRequester()) {
                                            acceptor.teleport(requester.getWorld(), requester.getX(), requester.getY(), requester.getZ(), acceptor.getYaw(), acceptor.getPitch());
                                        } else {
                                            requester.teleport(acceptor.getWorld(), acceptor.getX(), acceptor.getY(), acceptor.getZ(), requester.getYaw(), requester.getPitch());
                                        }
                                    },
                                    () -> {
                                        if (teleport.numRequests() == 0) {
                                            acceptor.sendMessage(new LiteralText("There are no teleport requests to accept"), false);
                                        }
                                    }
                            );

                            return 1;
                        })
                );

        dispatcher.register(tpto);
        dispatcher.register(tptome);
        dispatcher.register(tpa);
    }
}
