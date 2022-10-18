package net.bios.frenchvanilla.sanity.hallucinations;

import net.bios.frenchvanilla.sanity.Hallucination;
import net.bios.frenchvanilla.sanity.HallucinationEffect;
import net.bios.frenchvanilla.util.PlayerPacketHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RaveHEffect implements HallucinationEffect {
    public static final H HALLUCINATION = new H();

    private static final SoundEvent[] SOUNDS = new SoundEvent[]{
            SoundEvents.ENTITY_BLAZE_AMBIENT,
            SoundEvents.ENTITY_CAT_HISS,
            SoundEvents.ENTITY_COW_DEATH,
            SoundEvents.ENTITY_DONKEY_ANGRY,
            SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
            SoundEvents.ENTITY_ENDERMAN_SCREAM,
            SoundEvents.ENTITY_FOX_SCREECH,
            SoundEvents.ENTITY_GHAST_DEATH,
            SoundEvents.ENTITY_HOGLIN_ANGRY,
            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
            SoundEvents.ENTITY_PHANTOM_SWOOP,
            SoundEvents.ENTITY_PIG_DEATH,
            SoundEvents.ENTITY_PIGLIN_ANGRY,
            SoundEvents.ENTITY_SKELETON_HORSE_DEATH,
            SoundEvents.ENTITY_SKELETON_HORSE_HURT,
            SoundEvents.ENTITY_WITCH_AMBIENT,
            SoundEvents.ENTITY_WITHER_HURT,
            SoundEvents.ENTITY_WITHER_SPAWN,
            SoundEvents.ENTITY_ZOMBIE_AMBIENT
    };
    private static final int MIN_SOUNDS = 5;
    private static final int MAX_SOUNDS = 20;

    private final int numSounds;

    private ServerPlayerEntity player;
    private Random random;

    private int numSoundsPlayed;
    private int tickCounter;

    private RaveHEffect(ServerPlayerEntity player) {
        this.player = player;
        this.random = new Random();

        this.numSounds = this.random.nextInt(MAX_SOUNDS + 1 - MIN_SOUNDS) + MIN_SOUNDS;

        this.numSoundsPlayed = 0;
        this.tickCounter = 0;
    }

    @Override
    public Hallucination hallucination() {
        return HALLUCINATION;
    }

    @Override
    public boolean run() {
        if (this.numSoundsPlayed >= this.numSounds) return false;

        this.tickCounter += 1;

        if (this.tickCounter >= this.random.nextInt(13) + 2) {
            this.tickCounter = 0;
            this.numSoundsPlayed += 1;

            int soundIndex = this.random.nextInt(SOUNDS.length);
            Vec3d pos = player.getPos().add((random.nextDouble() - 0.5) * 10.0, (random.nextDouble() - 0.5) * 10.0, (random.nextDouble() - 0.5) * 10.0);
            PlayerPacketHelper.playSoundTo(player, pos, SOUNDS[soundIndex], SoundCategory.HOSTILE, 1f, 1f);
        }

        return true;
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
            return (1 - sanity) * 0.01;
        }

        @Override
        public HallucinationEffect create(ServerPlayerEntity player) {
            return new RaveHEffect(player);
        }
    }
}
