package net.bios.frenchvanilla;

public class Perms {
    private static String permission(String permission) {
        return "frenchvanilla." + permission;
    }

    private static String command(String permission) {
        return permission("command." + permission);
    }

    public static String CONFIG = permission("config");
    public static String SANITY_COMMAND = command("sanity");
    public static String TELEPORT = permission("teleport");
}
