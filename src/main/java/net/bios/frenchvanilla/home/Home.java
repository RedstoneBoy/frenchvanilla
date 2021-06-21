package net.bios.frenchvanilla.home;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class Home {
    public Identifier dimension;
    public Vec3d position;

    public Home(Identifier dimension, Vec3d position) {
        this.dimension = dimension;
        this.position = position;
    }

    public static Home fromNbt(NbtCompound tag) {
        var dimension = new Identifier(tag.getString("dimension"));
        var position = new Vec3d(
                tag.getDouble("x"),
                tag.getDouble("y"),
                tag.getDouble("z")
        );

        return new Home(dimension, position);
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putString("dimension", dimension.toString());
        tag.putDouble("x", position.getX());
        tag.putDouble("y", position.getY());
        tag.putDouble("z", position.getZ());
    }
}
