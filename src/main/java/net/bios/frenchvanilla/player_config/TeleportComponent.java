package net.bios.frenchvanilla.player_config;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.util.Pair;
import net.minecraft.nbt.NbtCompound;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TeleportComponent implements Component {
    private HashMap<UUID, TeleportRequest> teleports;

    public TeleportComponent() {
        this.teleports = new HashMap<>();
    }

    // Returns true if the teleport request was added
    public boolean addTeleportRequest(UUID from, TeleportRequest request) {
        this.updateTeleports();

        if (this.teleports.containsKey(from))
            return false;

        this.teleports.put(from, request);

        return true;
    }

    public int numRequests() {
        this.updateTeleports();
        return this.teleports.size();
    }

    public Optional<Pair<UUID, TeleportRequest>> removeSingleRequest() {
        this.updateTeleports();

        if (this.numRequests() == 1) {
            UUID key = this.teleports.keySet().iterator().next();
            TeleportRequest val = this.teleports.remove(key);
            return Optional.of(new Pair<>(key, val));
        }
        return Optional.empty();
    }

    public Optional<TeleportRequest> removeRequest(UUID from) {
        this.updateTeleports();

        return Optional.ofNullable(this.teleports.remove(from));
    }

    private void updateTeleports() {
        this.teleports.entrySet().removeIf(entry ->
                ChronoUnit.SECONDS.between(entry.getValue().requestedAt(), Instant.now())
                        >= FrenchVanilla.config.teleportExpireTime.value);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (String nbtKey : tag.getKeys()) {
            UUID key = UUID.fromString(nbtKey);
            var value = TeleportRequest.fromNbt(tag.getCompound(nbtKey));
            this.teleports.put(key, value);
        }

        this.updateTeleports();
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.updateTeleports();

        for (Map.Entry<UUID, TeleportRequest> entry : this.teleports.entrySet()) {
            tag.put(entry.getKey().toString(), entry.getValue().toNbt());
        }
    }
}
