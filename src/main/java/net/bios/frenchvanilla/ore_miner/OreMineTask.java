package net.bios.frenchvanilla.ore_miner;

import net.bios.frenchvanilla.tasks.FrenchTask;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class OreMineTask implements FrenchTask {
    private final ServerWorld world;
    private final ServerPlayerEntity player;
    private final BlockPos origin;
    private final Block originalBlock;

    private final ItemStack tool;

    private final HashSet<BlockPos> marked;

    private final Stack<Runnable> tasks;

    public OreMineTask(ServerWorld world, ServerPlayerEntity player, BlockPos origin) {
        this.world = world;
        this.player = player;
        this.origin = origin;
        this.tool = player.getMainHandStack();
        this.originalBlock = world.getBlockState(origin).getBlock();

        this.marked = new HashSet<>();

        this.tasks = new Stack<>();
        this.tasks.add(() -> this.findAndBreak(origin));
    }

    private void findAndBreak(BlockPos at) {
        this.tasks.add(() -> this.breakBlock(at));

        for (int xo = -1; xo <= 1; xo++) {
            for (int yo = -1; yo <= 1; yo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    if (xo == 0 && yo == 0 && zo == 0) continue;

                    BlockPos adjBlockPos = at.add(xo, yo, zo);

                    if (this.marked.contains(adjBlockPos)) continue;
                    this.marked.add(adjBlockPos);

                    Block block = world.getBlockState(adjBlockPos).getBlock();

                    if (this.originalBlock.equals(block)) {
                        this.tasks.add(() -> this.findAndBreak(adjBlockPos));
                    }
                }
            }
        }
    }

    private void breakBlock(BlockPos at) {
        if (!player.getMainHandStack().equals(this.tool)) {
            return;
        }

        AtomicBoolean toolBroken = new AtomicBoolean(false);

        List<ItemStack> drops = Block.getDroppedStacks(
                world.getBlockState(at), world, at, world.getBlockEntity(at), player, tool);
        world.breakBlock(at, false, player);
        player.getMainHandStack().damage(1, player, (e) -> {
        });

        for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;

            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack, 0.0, 0.0, 0.0);
            itemEntity.resetPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }

    @Override
    public boolean run() {
        if (!this.tasks.empty()) {
            this.tasks.pop().run();
        }

        return this.tasks.empty();
    }
}
