package net.bios.frenchvanilla;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public class FrenchBlockTags {
    private static final Tag<Block> createTag(String id) {
        return TagFactory.BLOCK.create(FrenchVanilla.identifier(id));
    }

    public static final Tag<Block> ORE_MINER = createTag("ore_miner");
}
