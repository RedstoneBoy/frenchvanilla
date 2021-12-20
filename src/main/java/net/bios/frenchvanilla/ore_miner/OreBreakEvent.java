package net.bios.frenchvanilla.ore_miner;

import net.bios.frenchvanilla.Components;
import net.bios.frenchvanilla.FrenchBlockTags;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.tasks.FrenchTask;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.bios.frenchvanilla.Components.PLAYER_BIND_DATA;

public class OreBreakEvent implements PlayerBlockBreakEvents.Before {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(new OreBreakEvent());
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient
                || !FrenchVanilla.config.oreMiner.value
                || !FrenchBlockTags.ORE_MINER.contains(state.getBlock()))
            return true;

        ServerWorld serverWorld = (ServerWorld) world;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

        if (!PLAYER_BIND_DATA.get(serverPlayer).data.oreMine)
            return true;

        FrenchTask task = new OreMineTask(serverWorld, serverPlayer, pos);
        Components.TASKS.get(serverWorld).tasks.add(task);

        return true;
    }
}
