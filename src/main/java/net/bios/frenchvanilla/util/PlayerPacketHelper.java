package net.bios.frenchvanilla.util;

import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class PlayerPacketHelper {
    public static final UUID PLAYER_HEROBRINE = UUID.fromString("0bec5711-4316-4a8f-b61b-2a119e652082");

    public static void playSoundTo(ServerPlayerEntity player, Vec3d position, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        player.networkHandler.sendPacket(
                new PlaySoundS2CPacket(sound, category, position.x, position.y, position.z, volume, pitch, 342353989534L));
    }

    public static void spawnFakePlayer(ServerPlayerEntity player, UUID playerId, Vec3d pos, float pitch, float yaw) {
        player.networkHandler.sendPacket(
                new EntitySpawnS2CPacket(Integer.MAX_VALUE, playerId, pos.x, pos.y, pos.z, pitch, yaw, EntityType.CREEPER, 0, Vec3d.ZERO, yaw));
        player.networkHandler.sendPacket(
                new EntityS2CPacket.Rotate(Integer.MAX_VALUE, (byte) yaw, (byte) pitch, true)
        );
    }
}
