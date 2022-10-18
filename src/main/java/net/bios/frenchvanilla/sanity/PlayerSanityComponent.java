package net.bios.frenchvanilla.sanity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.bios.frenchvanilla.NbtIds;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class PlayerSanityComponent implements Component {
    private double sanity;
    private double sanityChange;

    public double effectCooldown;

    private final List<HallucinationEffect> halEffects;

    public PlayerSanityComponent() {
        this.sanity = 1.0;
        this.sanityChange = 0.0;

        this.effectCooldown = 5.0;

        this.halEffects = new ArrayList<>();
    }

    public double sanityChange() {
        return this.sanityChange;
    }

    public double getSanity() {
        return this.sanity;
    }

    private void setSanity(double sanity) {
        if (sanity > 1.0) sanity = 1.0;
        if (sanity < 0.0) sanity = 0.0;

        this.sanity = sanity;
    }

    public void addSanity(double amount) {
        this.sanityChange += amount;
    }

    public void removeSanity(double amount) {
        this.addSanity(-amount);
    }

    public List<HallucinationEffect> halEffects() {
        return this.halEffects;
    }

    void updateSanity() {
        this.setSanity(this.getSanity() + this.sanityChange);
        this.sanityChange = 0;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.sanity = tag.getDouble(NbtIds.SANITY);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble(NbtIds.SANITY, this.sanity);
    }
}
