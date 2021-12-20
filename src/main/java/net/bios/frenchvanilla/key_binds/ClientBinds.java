package net.bios.frenchvanilla.key_binds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientBinds {
    public static final KeyBinding ORE_MINE = new KeyBinding(
            "key.frenchvanilla.ore_mine",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "category.frenchvanilla"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(ORE_MINE);
    }
}
