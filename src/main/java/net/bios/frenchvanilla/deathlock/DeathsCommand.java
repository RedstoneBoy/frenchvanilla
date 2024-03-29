package net.bios.frenchvanilla.deathlock;

import com.mojang.brigadier.CommandDispatcher;
import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.UUID;

import static net.bios.frenchvanilla.Components.DEATH_LOCKS;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DeathsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("deaths").requires(source -> FrenchVanilla.config.deathLocks.value).executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            DeathLocksComponent locks = DEATH_LOCKS.get(player);

            player.sendMessage(Text.literal("Deaths:"), false);

            for (Map.Entry<UUID, DeathLock> entry : locks.getDeathLocksWithId()) {
                UUID lockId = entry.getKey();
                DeathLock lock = entry.getValue();
                player.sendMessage(Text.literal(lock.dimension().toString())
                        .setStyle(Style.EMPTY.withColor(Formatting.AQUA))
                        .append(Text.literal(" (" + lock.position().getX() + ", " + lock.position().getY() + ", " + lock.position().getZ() + ")")
                                .setStyle(Style.EMPTY.withColor(Formatting.GREEN))
                        )
                        .append(Text.literal(" [Get Key]")
                                .setStyle(Style.EMPTY
                                        .withColor(Formatting.LIGHT_PURPLE)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deaths key " + lockId.toString()))
                                )
                        ), false);
            }

            return 1;
        }).then(literal("key")
                .then(argument("id", UuidArgumentType.uuid()).executes(context -> {
                    UUID lockId = UuidArgumentType.getUuid(context, "id");
                    DeathLocksComponent deathLocksComponent = DEATH_LOCKS.get(context.getSource().getPlayerOrThrow());
                    if (deathLocksComponent.getDeathLockById(lockId).isPresent()) {
                        ItemStack keyStack = DeathKeyItemHelper.createDeathKey(lockId);
                        context.getSource().getPlayerOrThrow().giveItemStack(keyStack);
                    } else {
                        context.getSource().sendFeedback(Text.literal("No death lock with that UUID exists"), false);
                    }

                    return 1;
                }))
        ));
    }
}
