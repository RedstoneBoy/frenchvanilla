package net.bios.frenchvanilla.sanity;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.bios.frenchvanilla.Components.PLAYER_SANITY;

public class OnSanityEndTick implements ServerTickEvents.EndTick {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(new OnSanityEndTick());
    }

    private Random random;

    private int tickCounter;

    public OnSanityEndTick() {
        this.random = new Random();

        this.tickCounter = 0;
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if (!FrenchVanilla.config.sanity.value) return;

        this.tickCounter += 1;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PlayerSanityComponent sanity = PLAYER_SANITY.get(player);
            var halEffects = sanity.halEffects();

            if (player.isSneaking()) {
                System.out.println("SANITY: " + sanity.getSanity());
            }

            // TODO: Use sanityChange here

            sanity.updateSanity();

            double sanityVal = sanity.getSanity();

            int i = 0;
            while (i < halEffects.size()) {
                var effect = halEffects.get(i);
                if (!effect.run()) {
                    halEffects.remove(i);
                } else if (effect.hallucination().maxSanity() < sanityVal) {
                    effect.stop();
                    halEffects.remove(i);
                } else {
                    i += 1;
                }
            }

            if (this.tickCounter < 20) {
                continue;
            }

            if (sanity.effectCooldown > 0) {
                sanity.effectCooldown -= 1;
                continue;
            }

            sanity.effectCooldown = 0;

            // Remove bias for hallucinations appearing earlier in the iterator
            List<Hallucination> chosen = new ArrayList<>();

            for (Hallucination h : FrenchVanilla.HALLUCINATIONS.values()) {
                if (h.maxSanity() < sanityVal) continue;
                double halChance = h.triggerChance(sanityVal);
                halChance = Math.max(0.0, Math.min(1.0, halChance));
                double roll = random.nextDouble();
                if (roll <= halChance) {
                    chosen.add(h);
                }
            }

            if (!chosen.isEmpty()) {
                int index = random.nextInt(chosen.size());
                Hallucination h = chosen.get(index);
                var effect = h.create(player);
                sanity.halEffects().add(effect);

                sanity.effectCooldown = 10.0;
            }
        }

        if (this.tickCounter >= 20) {
            this.tickCounter = 0;
        }
    }
}
