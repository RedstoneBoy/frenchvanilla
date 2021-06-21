package net.bios.frenchvanilla.deathlock;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DeathLocksComponent implements Component {
    private final HashMap<UUID, DeathLock> deathLocks;

    private Optional<UUID> latestDeath;

    public DeathLocksComponent() {
        this.deathLocks = new HashMap<>();
        this.latestDeath = Optional.empty();
    }

    public Optional<UUID> getLatestDeath() {
        return this.latestDeath;
    }

    public void setLatestDeath(@Nullable UUID id) {
        this.latestDeath = Optional.ofNullable(id);
    }

    public Set<Map.Entry<UUID, DeathLock>> getDeathLocksWithId() {
        return this.deathLocks.entrySet();
    }

    public Collection<DeathLock> getDeathLocks() {
        return this.deathLocks.values();
    }

    public Optional<DeathLock> getDeathLockById(UUID lockId) {
        return Optional.ofNullable(this.deathLocks.get(lockId));
    }

    public UUID addDeathLock(DeathLock lock) {
        UUID id = UUID.randomUUID();
        this.deathLocks.put(id, lock);
        return id;
    }

    public void removeDeathLock(UUID lockId) {
        this.deathLocks.remove(lockId);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList locks = tag.getList("locks", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < locks.size(); i++) {
            NbtCompound entryCompound = locks.getCompound(i);
            UUID id = entryCompound.getUuid("id");
            NbtCompound lockCompound = entryCompound.getCompound("lock");
            DeathLock lock = DeathLock.readFromNbt(lockCompound);
            this.deathLocks.put(id, lock);
        }
        if (tag.contains("latest_death")) {
            this.latestDeath = Optional.of(tag.getUuid("latest_death"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList locks = new NbtList();
        for (Map.Entry<UUID, DeathLock> lockEntry : this.deathLocks.entrySet()) {
            NbtCompound lockCompound = new NbtCompound();
            lockEntry.getValue().writeToNbt(lockCompound);

            NbtCompound entryCompound = new NbtCompound();
            entryCompound.putUuid("id", lockEntry.getKey());
            entryCompound.put("lock", lockCompound);

            locks.add(entryCompound);
        }
        tag.put("locks", locks);
        this.latestDeath.ifPresent(id -> tag.putUuid("latest_death", id));
    }
}
