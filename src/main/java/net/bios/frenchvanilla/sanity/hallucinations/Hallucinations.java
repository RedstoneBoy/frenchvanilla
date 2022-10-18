package net.bios.frenchvanilla.sanity.hallucinations;

import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.sanity.Hallucination;

public class Hallucinations {
    public static void register() {
        registerH("explosion_prime_sound", ExplosionPrimeSoundHEffect.HALLUCINATION);
        registerH("herobrine", HerobrineHEffect.HALLUCINATION);
        registerH("rave", RaveHEffect.HALLUCINATION);
    }

    private static <H extends Hallucination> H registerH(String id, H h) {
        FrenchVanilla.HALLUCINATIONS.put(FrenchVanilla.identifier(id), h);
        return h;
    }
}
