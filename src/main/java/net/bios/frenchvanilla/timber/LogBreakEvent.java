package net.bios.frenchvanilla.timber;

import net.bios.frenchvanilla.Components;
import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.bios.frenchvanilla.Components.PLAYER_CONFIG;

public class LogBreakEvent implements PlayerBlockBreakEvents.Before {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(new LogBreakEvent());
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient
                || !FrenchVanilla.config.timber.value
                || !PLAYER_CONFIG.get(player).config().timber.value
                || !TreeType.isLog(state.getBlock()))
            return true;

        ServerWorld serverWorld = (ServerWorld) world;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

        TimberTask.createTimberTask(serverWorld, serverPlayer, pos, state.getBlock())
                .ifPresent(task -> Components.TASKS.get(serverWorld).tasks.add(task));

        return true;
    }
}
