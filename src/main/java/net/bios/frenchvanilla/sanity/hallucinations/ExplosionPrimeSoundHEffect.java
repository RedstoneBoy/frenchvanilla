package net.bios.frenchvanilla.sanity.hallucinations;

import net.bios.frenchvanilla.sanity.Hallucination;
import net.bios.frenchvanilla.sanity.HallucinationEffect;
import net.bios.frenchvanilla.util.PlayerPacketHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ExplosionPrimeSoundHEffect implements HallucinationEffect {
    public static final H HALLUCINATION = new H();

    private ServerPlayerEntity player;
    private Random random;

    private ExplosionPrimeSoundHEffect(ServerPlayerEntity player) {
        this.player = player;
        this.random = new Random();
    }

    @Override
    public Hallucination hallucination() {
        return HALLUCINATION;
    }

    @Override
    public boolean run() {
        Vec3d offset = player.getRotationVector().multiply(-2.0);
        Vec3d pos = player.getPos().add(offset);

        PlayerPacketHelper.playSoundTo(player, pos, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1f, 1f);
        return false;
    }

    @Override
    public void stop() {

    }

    public static class H implements Hallucination {
        @Override
        public double maxSanity() {
            return 0.5;
        }

        @Override
        public double triggerChance(double sanity) {
            return (1 - sanity) * 0.05;
        }

        @Override
        public HallucinationEffect create(ServerPlayerEntity player) {
            return new ExplosionPrimeSoundHEffect(player);
        }
    }
}
