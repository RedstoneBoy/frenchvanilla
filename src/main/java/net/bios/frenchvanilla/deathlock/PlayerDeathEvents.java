package net.bios.frenchvanilla.deathlock;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

import java.util.Arrays;
import java.util.UUID;

import static net.bios.frenchvanilla.Components.DEATH_LOCKS;

public class PlayerDeathEvents implements ServerPlayerEvents.AllowDeath, ServerPlayerEvents.AfterRespawn {
    public static void register() {
        ServerPlayerEvents.ALLOW_DEATH.register(new PlayerDeathEvents());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerDeathEvents());
    }

    @Override
    public boolean allowDeath(ServerPlayerEntity player, DamageSource damageSource, float damageAmount) {
        if (!FrenchVanilla.config.deathLocks
                || player.getServerWorld().getServer().getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
            return true;

        DeathLocksComponent locks = DEATH_LOCKS.get(player);
        BlockPos position = player.getBlockPos();
        if (position.getY() < player.getServerWorld().getBottomY()) {
            position = position.withY(player.getServerWorld().getBottomY());
        } else if (position.getY() > player.getServerWorld().getTopY()) {
            position = position.withY(player.getServerWorld().getTopY());
        }

        ItemStack[] stacks = new ItemStack[player.getInventory().size()];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = player.getInventory().getStack(i);
        }

        UUID latestDeath = locks.addDeathLock(new DeathLock(player.getServerWorld().getRegistryKey().getValue(), position, player.getUuid(), Arrays.asList(stacks)));
        locks.setLatestDeath(latestDeath);

        player.getInventory().clear();

        return true;
    }

    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (!FrenchVanilla.config.deathLocks
                || newPlayer.getServerWorld().getServer().getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
            return;

        DeathLocksComponent locks = DEATH_LOCKS.get(newPlayer);
        if (locks.getLatestDeath().isPresent()) {
            ItemStack keyStack = DeathKeyItemHelper.createDeathKey(locks.getLatestDeath().get());
            locks.setLatestDeath(null);
            newPlayer.giveItemStack(keyStack);
        }
    }
}