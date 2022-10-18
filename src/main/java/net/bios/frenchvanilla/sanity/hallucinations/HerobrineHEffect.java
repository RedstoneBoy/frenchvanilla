package net.bios.frenchvanilla.sanity.hallucinations;

import net.bios.frenchvanilla.sanity.Hallucination;
import net.bios.frenchvanilla.sanity.HallucinationEffect;
import net.bios.frenchvanilla.util.PlayerPacketHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class HerobrineHEffect implements HallucinationEffect {
    public static final H HALLUCINATION = new H();

    private ServerPlayerEntity player;
    private Random random;

    private State state;

    private BlockPos pos;

    private HerobrineHEffect(ServerPlayerEntity player) {
        this.player = player;
        this.random = new Random();

        this.state = State.SUMMON;

        this.pos = null;
    }

    @Override
    public Hallucination hallucination() {
        return HALLUCINATION;
    }

    @Override
    public boolean run() {
        if (this.state == State.SUMMON) {
            this.run_summon();
        }

        // State.FINISHED
        return false;
    }

    private void run_summon() {
        // FIND POSITION

        double angle = this.random.nextDouble() * 2 * Math.PI;
        double distance = 30;

        BlockPos potentialPos = player.getBlockPos().add(
                distance * Math.sin(angle),
                0,
                distance * Math.cos(angle)
        );

        ServerWorld world = player.getWorld();

        int yOffset = 0;
        while (!world.isAir(potentialPos) || !world.isAir(potentialPos.up())) {
            potentialPos = potentialPos.up();
            yOffset += 1;

            if (yOffset > distance * 2) {
                this.state = State.FINISHED;
                return;
            }
        }

        this.pos = potentialPos;

        Vec3d soundOffset = new Vec3d(player.getX(), player.getY(), player.getZ())
                .subtract(pos.getX(), pos.getY(), pos.getZ())
                .multiply(-1)
                .normalize()
                .multiply(10);

        Vec3d soundPos = player.getPos().add(soundOffset);

        // FIND ROTATION

        float pitch, yaw;
        {
            double d = player.getX() - pos.getX();
            double e = player.getY() - pos.getY();
            double f = player.getZ() - pos.getZ();
            double g = Math.sqrt(d * d + f * f);
            pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875D)));
            yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F);
        }

        // player.getWorld().spawnEntity()

        // SUMMON

        // this.pos = new BlockPos(player.getBlockPos());

        PlayerPacketHelper.playSoundTo(player, soundPos, SoundEvents.ENTITY_GHAST_HURT, SoundCategory.HOSTILE, 1f, 1f);

        PlayerPacketHelper.spawnFakePlayer(player, PlayerPacketHelper.PLAYER_HEROBRINE, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), pitch, yaw);

        this.state = State.FINISHED;
    }

    @Override
    public void stop() {

    }

    private enum State {
        SUMMON,
        FINISHED;
    }

    public static class H implements Hallucination {
        @Override
        public double maxSanity() {
            return 0.3;
        }

        @Override
        public double triggerChance(double sanity) {
            return (1 - sanity) * 0.005;
        }

        @Override
        public HallucinationEffect create(ServerPlayerEntity player) {
            return new HerobrineHEffect(player);
        }
    }
}