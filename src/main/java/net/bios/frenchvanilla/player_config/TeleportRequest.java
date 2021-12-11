package net.bios.frenchvanilla.player_config;

import net.bios.frenchvanilla.NbtIds;
import net.minecraft.nbt.NbtCompound;

import java.time.Instant;

public record TeleportRequest(Instant requestedAt, boolean teleportToRequester) {
    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putString(NbtIds.TELEPORT_REQUESTED_AT, this.requestedAt.toString());
        compound.putBoolean(NbtIds.TELEPORT_TO_REQUESTER, this.teleportToRequester);
        return compound;
    }

    public static TeleportRequest fromNbt(NbtCompound compound) {
        Instant requestedAt = Instant.parse(compound.getString(NbtIds.TELEPORT_REQUESTED_AT));
        boolean teleportToRequester = compound.getBoolean(NbtIds.TELEPORT_TO_REQUESTER);
        return new TeleportRequest(requestedAt, teleportToRequester);
    }
}
