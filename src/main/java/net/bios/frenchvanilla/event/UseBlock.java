package net.bios.frenchvanilla.event;

import net.bios.frenchvanilla.SleepUtil;
import net.bios.frenchvanilla.Texts;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class UseBlock implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        if (hitResult.getType() != HitResult.Type.BLOCK) return ActionResult.PASS;
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (!player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;

        ServerWorld sworld = (ServerWorld) world;
        if (sworld.getBlockState(hitResult.getBlockPos()).getBlock().isIn(BlockTags.CAMPFIRES)) {
            BlockPos bedPos = SleepUtil.findBedNextTo(sworld, hitResult.getBlockPos());
            if (bedPos != null && sworld.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                if (sworld.getTimeOfDay() % 24000L > 13000L) {
                    player.sendSystemMessage(new TranslatableText(Texts.ALREADY_NIGHT), player.getUuid());
                } else {
                    long timeToAdd = 13000L - sworld.getTimeOfDay() % 24000L;
                    sworld.setTimeOfDay(sworld.getTimeOfDay() + timeToAdd);

                    sworld.getPlayers().forEach(p -> p.sendSystemMessage(new TranslatableText(Texts.RESTED_AT_CAMPFIRE, player.getDisplayName()), p.getUuid()));
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
