package net.bios.frenchvanilla.sanity;

import net.minecraft.server.network.ServerPlayerEntity;

public interface Hallucination {
    /**
     * @return maximum sanity that the hallucination can appear at
     */
    double maxSanity();

    double triggerChance(double sanity);

    HallucinationEffect create(ServerPlayerEntity player);
}
