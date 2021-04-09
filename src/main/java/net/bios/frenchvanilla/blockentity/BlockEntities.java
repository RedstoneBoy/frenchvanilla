package net.bios.frenchvanilla.blockentity;

import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntities {
    public static BlockEntityType<MassBarrelBlockEntity> MASS_BARREL_BLOCK_ENTITY;

    public static void register() {
        MASS_BARREL_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                FrenchVanilla.identifier("mass_barrel"),
                BlockEntityType.Builder.create(MassBarrelBlockEntity::new, Blocks.BARREL).build(null));
    }
}
