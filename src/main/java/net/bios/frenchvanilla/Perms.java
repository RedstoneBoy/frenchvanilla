package net.bios.frenchvanilla;

public class Perms {
    private static String permission(String permission) {
        return "frenchvanilla." + permission;
    }

    public static String CONFIG = permission("config");
    public static String TELEPORT = permission("teleport");
}
