package net.bios.frenchvanilla.deathlock;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

import static net.bios.frenchvanilla.Components.DEATH_KEY;
import static net.bios.frenchvanilla.Components.DEATH_LOCKS;

public class UseDeathKeyEvent implements UseItemCallback {
    public static void register() {
        UseItemCallback.EVENT.register(new UseDeathKeyEvent());
    }

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (!FrenchVanilla.config.deathLocks.value
                || world.isClient
                || !DeathKeyItemHelper.isDeathKey(player.getStackInHand(hand)))
            return TypedActionResult.pass(ItemStack.EMPTY);

        DeathKeyItemComponent deathKey = DEATH_KEY.get(player.getStackInHand(hand));

        if (deathKey.getLockId().isPresent()) {
            UUID lockId = deathKey.getLockId().get();

            DeathLocksComponent locks = DEATH_LOCKS.get(player);
            Optional<DeathLock> lockOptional = locks.getDeathLockById(lockId);
            if (lockOptional.isPresent()) {
                DeathLock lock = lockOptional.get();

                if (player.getEntityWorld().getRegistryKey().getValue().equals(lock.dimension())
                        && lock.position().isWithinDistance(player.getBlockPos(), FrenchVanilla.config.deathKeyUnlockDistance.value)) {
                    // Unlock
                    player.sendMessage(new LiteralText("Unlocking...").setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);

                    if (FrenchVanilla.config.restoreXp.value) {
                        player.addExperienceLevels(lock.xpLevel());
                    }

                    // TODO: Inventory merging in creative
                    for (ItemStack stack : lock.stacks()) {
                        player.getInventory().insertStack(stack);
                    }
                    int i = 0;
                    while (i < lock.stacks().size()) {
                        if (lock.stacks().get(i).isEmpty()) {
                            lock.stacks().remove(i);
                        } else {
                            i += 1;
                        }
                    }

                    // Remove lock and key if all items are removed
                    if (lock.stacks().size() == 0) {
                        player.sendMessage(new LiteralText("All items recovered!").setStyle(Style.EMPTY.withColor(Formatting.BLUE)), false);

                        player.setStackInHand(hand, ItemStack.EMPTY);

                        locks.removeDeathLock(lockId);

                        return TypedActionResult.consume(ItemStack.EMPTY);
                    } else {
                        player.sendMessage(new LiteralText("Your inventory is full!"), false);
                        return TypedActionResult.success(ItemStack.EMPTY);
                    }
                } else {
                    // Give lock position
                    BlockPos pos = lock.position();
                    player.sendMessage(new LiteralText("The lock is in " + lock.dimension() + " at X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ()), false);

                    return TypedActionResult.success(ItemStack.EMPTY);
                }
            } else {
                // Key has an invalid lock id
                player.sendMessage(new LiteralText("This is an invalid key, removing it...")
                        .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                player.setStackInHand(hand, ItemStack.EMPTY);

                return TypedActionResult.consume(ItemStack.EMPTY);
            }
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }
}
