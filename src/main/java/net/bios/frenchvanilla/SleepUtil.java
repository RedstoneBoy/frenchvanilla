package net.bios.frenchvanilla;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class SleepUtil {
    @Nullable
    public static BlockPos findBedNextTo(World world, BlockPos pos) {
        BlockPos[] positions = new BlockPos[]{pos.north(), pos.south(), pos.east(), pos.west()};
        for (BlockPos neighbour : positions) {
            if (world.getBlockState(neighbour).isIn(BlockTags.BEDS)) {
                return neighbour;
            }
        }

        return null;
    }
}
