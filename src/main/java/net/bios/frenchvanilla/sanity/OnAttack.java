package net.bios.frenchvanilla.sanity;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.bios.frenchvanilla.Components.PLAYER_SANITY;

public class OnAttack implements AttackEntityCallback {
    private static final double FIGHT_DRAGON_PENALTY = 0.02;
    private static final double FIGHT_MONSTER_PENALTY = 0.01;
    private static final double FIGHT_WITHER_PENALTY = 0.05;

    public static void register() {
        AttackEntityCallback.EVENT.register(new OnAttack());
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (world.isClient
                || !FrenchVanilla.config.sanity.value
                || player.isSpectator())
            return ActionResult.PASS;

        PlayerSanityComponent sanity = PLAYER_SANITY.get(player);
        if (entity instanceof EnderDragonEntity) {
            sanity.removeSanity(FIGHT_DRAGON_PENALTY);
        } else if (entity instanceof WitherEntity) {
            sanity.removeSanity(FIGHT_WITHER_PENALTY);
        } else if (entity instanceof HostileEntity) {
            sanity.removeSanity(FIGHT_MONSTER_PENALTY);
        }

        return ActionResult.PASS;
    }
}
