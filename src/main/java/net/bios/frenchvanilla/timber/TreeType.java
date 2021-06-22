package net.bios.frenchvanilla.timber;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Arrays;
import java.util.HashSet;

public enum TreeType {
    OAK(Blocks.OAK_LOG, true, Blocks.OAK_LEAVES),
    BIRCH(Blocks.BIRCH_LOG, true, Blocks.BIRCH_LEAVES),
    SPRUCE(Blocks.SPRUCE_LOG, true, Blocks.SPRUCE_LEAVES),
    JUNGLE(Blocks.JUNGLE_LOG, true, Blocks.JUNGLE_LEAVES),
    DARK_OAK(Blocks.DARK_OAK_LOG, true, Blocks.DARK_OAK_LEAVES),
    ACACIA(Blocks.ACACIA_LOG, true, Blocks.ACACIA_LEAVES),
    CRIMSON(Blocks.CRIMSON_STEM, false, Blocks.NETHER_WART_BLOCK, Blocks.SHROOMLIGHT),
    WARPED(Blocks.WARPED_STEM, false, Blocks.WARPED_WART_BLOCK, Blocks.SHROOMLIGHT);

    public final Block logType;
    public final HashSet<Block> leafTypes;
    public final boolean hasProperties;

    TreeType(Block logType, boolean hasProperties, Block... leafTypes) {
        this.logType = logType;
        this.leafTypes = new HashSet<>(Arrays.asList(leafTypes));
        this.hasProperties = hasProperties;
    }

    public static boolean isLog(Block block) {
        return block.equals(Blocks.OAK_LOG)
                || block.equals(Blocks.BIRCH_LOG)
                || block.equals(Blocks.SPRUCE_LOG)
                || block.equals(Blocks.JUNGLE_LOG)
                || block.equals(Blocks.DARK_OAK_LOG)
                || block.equals(Blocks.ACACIA_LOG)
                || block.equals(Blocks.CRIMSON_STEM)
                || block.equals(Blocks.WARPED_STEM);
    }
}
