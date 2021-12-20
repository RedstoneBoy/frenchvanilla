package net.bios.frenchvanilla.timber;

import net.bios.frenchvanilla.tasks.FrenchTask;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimberTask implements FrenchTask {
    private final ServerWorld world;
    private final ServerPlayerEntity player;
    private final BlockPos origin;

    private final ItemStack tool;
    private final TreeType treeType;

    private final Stack<Runnable> tasks;

    private final HashSet<BlockPos> logs;
    private final HashSet<BlockPos> leaves;

    public static Optional<TimberTask> createTimberTask(ServerWorld world, ServerPlayerEntity player, BlockPos origin, Block originBlock) {
        ItemStack tool = player.getMainHandStack();
        return Arrays.stream(TreeType.values())
                .filter(treeType -> treeType.logType.equals(originBlock))
                .findFirst()
                .map(treeType -> new TimberTask(world, player, origin, tool, treeType));
    }

    private TimberTask(ServerWorld world, ServerPlayerEntity player, BlockPos origin, ItemStack tool, TreeType treeType) {
        this.world = world;
        this.player = player;
        this.origin = origin;
        this.tool = tool;
        this.treeType = treeType;

        this.tasks = new Stack<>();
        this.tasks.push(() -> this.validateLeaves(origin));

        this.logs = new HashSet<>();
        this.leaves = new HashSet<>();
    }

    private void startFindTree() {
        this.tasks.clear();
        this.logs.clear();

        this.tasks.push(this::destroyTree);
        this.tasks.push(this::startFindLeaves);
        this.tasks.push(() -> this.findLogs(this.origin));
    }

    private void validateLeaves(BlockPos at) {
        this.logs.add(at);

        for (int xo = -1; xo <= 1; xo++) {
            for (int yo = -1; yo <= 1; yo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    if (xo == 0 && yo == 0 && zo == 0) continue;

                    BlockPos adjBlockPos = at.add(xo, yo, zo);
                    BlockState blockState = world.getBlockState(adjBlockPos);

                    if (this.treeType.leafTypes.contains(blockState.getBlock())
                            && (!this.treeType.hasProperties || !blockState.get(Properties.PERSISTENT))) {
                        this.startFindTree();
                        return;
                    } else if (!this.logs.contains(adjBlockPos)
                            && this.treeType.logType.equals(blockState.getBlock())) {
                        tasks.push(() -> this.validateLeaves(adjBlockPos));
                    }
                }
            }
        }
    }

    private void findLogs(BlockPos at) {
        this.logs.add(at);

        for (int xo = -1; xo <= 1; xo++) {
            for (int yo = -1; yo <= 1; yo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    if (xo == 0 && yo == 0 && zo == 0) continue;

                    BlockPos adjBlockPos = at.add(xo, yo, zo);
                    BlockState blockState = world.getBlockState(adjBlockPos);

                    if (!this.logs.contains(adjBlockPos)
                            && this.treeType.logType.equals(blockState.getBlock())) {
                        tasks.push(() -> this.findLogs(adjBlockPos));
                    } else if (!this.leaves.contains(adjBlockPos)
                            && this.treeType.leafTypes.contains(blockState.getBlock())) {
                        this.leaves.add(adjBlockPos);
                    }
                }
            }
        }
    }

    private void startFindLeaves() {
        for (BlockPos leafPos : this.leaves) {
            int distance = this.treeType.hasProperties ? world.getBlockState(leafPos).get(Properties.DISTANCE_1_7) : 1;
            this.tasks.push(() -> this.findLeaves(leafPos, Math.min(7, distance + 1)));
        }
    }

    private void findLeaves(BlockPos at, int minDistance) {
        for (int xo = -1; xo <= 1; xo++) {
            for (int yo = -1; yo <= 1; yo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    if (xo == 0 && yo == 0 && zo == 0) continue;

                    BlockPos adjBlockPos = at.add(xo, yo, zo);

                    if (this.logs.contains(adjBlockPos) || this.leaves.contains(adjBlockPos)) continue;

                    BlockState blockState = world.getBlockState(adjBlockPos);

                    if (this.treeType.leafTypes.contains(blockState.getBlock())
                            && (!this.treeType.hasProperties || (blockState.get(Properties.DISTANCE_1_7) >= minDistance && !blockState.get(Properties.PERSISTENT)))) {
                        this.leaves.add(adjBlockPos);
                        int nextDistance = this.treeType.hasProperties ? Math.min(7, blockState.get(Properties.DISTANCE_1_7) + 1) : 1;
                        this.tasks.push(() -> this.findLeaves(adjBlockPos, nextDistance));
                    }
                }
            }
        }
    }

    private void destroyTree() {
        if (!player.getMainHandStack().equals(this.tool)) {
            // The player switched their tool, nothing happens
            return;
        }

        ArrayList<ItemStack> drops = new ArrayList<>();

        AtomicBoolean toolBroken = new AtomicBoolean(false);

        for (BlockPos logPos : this.logs) {
            List<ItemStack> newDrops = Block.getDroppedStacks(world.getBlockState(logPos), world, logPos, world.getBlockEntity(logPos), player, tool);
            drops.addAll(newDrops);
            world.breakBlock(logPos, false, player);
            player.getMainHandStack().damage(1, player, (e) -> toolBroken.set(true));
            if (toolBroken.get()) {
                return;
            }
        }

        for (BlockPos leafPos : this.leaves) {
            List<ItemStack> newDrops = Block.getDroppedStacks(world.getBlockState(leafPos), world, leafPos, world.getBlockEntity(leafPos), player, ItemStack.EMPTY);
            drops.addAll(newDrops);
            world.breakBlock(leafPos, false, player);
        }

        for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;

            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack, 0.0, 0.0, 0.0);
            itemEntity.resetPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }

    // Returns true when finished
    @Override
    public boolean run() {
        if (!tasks.empty())
            tasks.pop().run();

        return tasks.empty();
    }
}
