package net.bios.frenchvanilla.deathlock;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class DeathKeyItemComponent extends ItemComponent {
    public DeathKeyItemComponent(ItemStack stack) {
        super(stack);
    }

    public Optional<UUID> getLockId() {
        return Optional.ofNullable(this.getUuid("frenchvanilla:lockid"));
    }

    public void setLockId(UUID lockId) {
        this.putUuid("frenchvanilla:lockid", lockId);
    }
}
