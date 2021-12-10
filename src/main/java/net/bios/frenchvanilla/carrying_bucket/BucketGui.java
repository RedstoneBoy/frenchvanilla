package net.bios.frenchvanilla.carrying_bucket;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class BucketGui extends SimpleGui {
    private ServerPlayerEntity player;
    private ItemStack bucket;

    public BucketGui(ScreenHandlerType<?> type, ServerPlayerEntity player, ItemStack bucket) {
        super(type, player, false);
        this.player = player;
        this.bucket = bucket;
    }

    @Override
    public void onClose() {
        super.onClose();

        player.giveItemStack(this.bucket);
        if (!this.bucket.isEmpty()) {
            player.dropStack(this.bucket);
        }
    }
}
