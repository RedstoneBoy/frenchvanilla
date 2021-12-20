package net.bios.frenchvanilla.key_binds;

import net.bios.frenchvanilla.FrenchVanilla;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import static net.bios.frenchvanilla.Components.PLAYER_BIND_DATA;

public class C2SBindDataPacket {
    private static final Identifier PACKET_ID = FrenchVanilla.identifier("bind_data");

    public static void send(BindData data) {
        ClientPlayNetworking.send(PACKET_ID, PacketByteBufs.create().writeNbt(data.toNbt()));
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            NbtCompound nbt = buf.readNbt();
            if (nbt != null) {
                PLAYER_BIND_DATA.get(player).update(BindData.fromNbt(nbt));
            }
        });
    }
}
