package net.bios.frenchvanilla.campfire_rest;

import net.bios.frenchvanilla.FrenchVanilla;
import net.bios.frenchvanilla.Texts;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UseCampfireEvent implements UseBlockCallback {
    public static void register() {
        UseBlockCallback.EVENT.register(new UseCampfireEvent());
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient
                || !FrenchVanilla.config.campfireResting.value
                || player.isSpectator()
                || hitResult.getType() != HitResult.Type.BLOCK
                || hand != Hand.MAIN_HAND
                || !player.getStackInHand(hand).isEmpty())
            return ActionResult.PASS;

        ServerWorld sworld = (ServerWorld) world;
        if (sworld.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.CAMPFIRES)) {
            BlockPos logPos = player.getBlockPos().down();
            if (sworld.getBlockState(logPos).isIn(BlockTags.LOGS)) {
                if (sworld.getTimeOfDay() % 24000L > 13000L) {
                    player.sendMessage(Texts.ALREADY_NIGHT);

                    return ActionResult.FAIL;
                } else {
                    long timeToAdd = 13000L - sworld.getTimeOfDay() % 24000L;
                    sworld.setTimeOfDay(sworld.getTimeOfDay() + timeToAdd);

                    sworld.getPlayers().forEach(p -> p.sendMessage(Text.literal("")
                            .append(player.getDisplayName())
                            .append(Texts.RESTED_AT_CAMPFIRE)));
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
