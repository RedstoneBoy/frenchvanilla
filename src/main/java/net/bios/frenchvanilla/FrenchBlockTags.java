package net.bios.frenchvanilla;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class FrenchBlockTags {
    private static final TagKey<Block> createTag(String id) {
        return TagKey.of(Registry.BLOCK_KEY, FrenchVanilla.identifier(id));
    }

    public static final TagKey<Block> ORE_MINER = createTag("ore_miner");
}
