package net.bios.frenchvanilla.deathlock;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.UUID;

import static net.bios.frenchvanilla.Components.DEATH_KEY;

public class DeathKeyItemHelper {
    public static ItemStack createDeathKey(UUID lockId) {
        ItemStack keyStack = new ItemStack(Items.ARROW, 1);
        DEATH_KEY.get(keyStack).setLockId(lockId);
        keyStack.setCustomName(new LiteralText("Death Key")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
        return keyStack;
    }

    public static boolean isDeathKey(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.ARROW)
            return false;

        return DEATH_KEY.get(stack).getLockId().isPresent();
    }
}
