package net.bios.frenchvanilla.sanity;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import static net.bios.frenchvanilla.Components.PLAYER_SANITY;

public class OnSanityTick implements ServerTickEvents.StartTick {
    private static final double SECOND = 20;
    private static final double MINUTE = SECOND * 60;
    private static final double DAY = MINUTE * 20;

    // DARKNESS PENALTY
    private static final double DARKNESS_PENALTY = 0.4 / DAY;

    // DIMENSION PENALTY
    private static final double DIMENSION_END_PENALTY = 0.2 / DAY;
    private static final double DIMENSION_NETHER_PENALTY = 0.2 / DAY;

    // HUNGER
    private static final double NO_HUNGER_PENALTY = 0.2 / DAY;
    private static final double LOW_HUNGER_PENALTY = 0.2 / DAY;
    private static final float LOW_HUNGER_THRESHOLD = 0.5f;
    private static final double HIGH_HUNGER_BONUS = 0.1 / DAY;
    private static final double FULL_HUNGER_BONUS = 0.1 / DAY;
    private static final float HIGH_HUNGER_THRESHOLD = 0.8f;

    // RAIN PENALTY
    private static final double RAIN_PENALTY = 0.2 / DAY;

    // SLEEP BONUS
    private static final double SLEEP_BONUS = 0.04 / SECOND;

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(new OnSanityTick());
    }

    @Override
    public void onStartTick(MinecraftServer server) {
        if (!FrenchVanilla.config.sanity.value) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PlayerSanityComponent sanity = PLAYER_SANITY.get(player);

            boolean inEnd = player.getWorld().getDimensionKey().getValue().equals(DimensionTypes.THE_END_ID);
            boolean inNether = player.getWorld().getDimensionKey().getValue().equals(DimensionTypes.THE_NETHER_ID);
            boolean inOverworld = !inEnd && !inNether;

            // DARKNESS PENALTY

            BlockPos pos = player.getBlockPos();
            if (!inOverworld) {
                if (player.getWorld().getLightLevel(pos) == 0) {
                    sanity.removeSanity(DARKNESS_PENALTY);
                }
            } else if (player.getWorld().getLightLevel(LightType.BLOCK, pos) == 0 &&
                    (player.getWorld().getLightLevel(LightType.SKY, pos) == 0 || player.getWorld().isNight())) {
                sanity.removeSanity(DARKNESS_PENALTY);
            }

            // DIMENSION PENALTY

            if (inEnd) {
                sanity.removeSanity(DIMENSION_END_PENALTY);
            } else if (inNether) {
                sanity.removeSanity(DIMENSION_NETHER_PENALTY);
            }

            // HUNGER

            float foodLevel = ((float) player.getHungerManager().getFoodLevel()) / 20.0f;
            if (foodLevel <= LOW_HUNGER_THRESHOLD) {
                sanity.removeSanity(LOW_HUNGER_PENALTY);
            }
            if (foodLevel == 0.0) {
                sanity.removeSanity(NO_HUNGER_PENALTY);
            }
            if (foodLevel >= HIGH_HUNGER_THRESHOLD) {
                sanity.addSanity(HIGH_HUNGER_BONUS);
            }
            if (foodLevel == 1.0f) {
                sanity.addSanity(FULL_HUNGER_BONUS);
            }

            // RAIN PENALTY

            if (isPlayerBeingRainedOn(player)) {
                sanity.removeSanity(RAIN_PENALTY);
            }

            // SLEEP BONUS

            if (player.isSleeping()) {
                sanity.addSanity(SLEEP_BONUS);
            }
        }
    }

    private static boolean isPlayerBeingRainedOn(ServerPlayerEntity player) {
        BlockPos blockPos = player.getBlockPos();
        return player.world.hasRain(blockPos) || player.world.hasRain(new BlockPos(blockPos.getX(), player.getBoundingBox().maxY, blockPos.getZ()));
    }
}
