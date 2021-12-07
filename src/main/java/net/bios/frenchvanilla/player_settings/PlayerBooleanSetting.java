package net.bios.frenchvanilla.player_settings;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerBooleanSetting implements PlayerSetting {
    private static Text TRUE = new LiteralText("ENABLED")
            .setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN));
    private static Text FALSE = new LiteralText("DISABLED")
            .setStyle(Style.EMPTY.withColor(Formatting.RED));

    private boolean defaultValue;
    public boolean value;

    public PlayerBooleanSetting(boolean defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public boolean defaultValue() {
        return this.defaultValue;
    }

    @Override
    public NbtElement toNbt() {
        return NbtByte.of(this.value);
    }

    @Override
    public void readNbt(NbtElement element) {
        this.value = ((NbtByte) element).byteValue() != 0;
    }

    @Override
    public Text text() {
        Text valText = this.value ? TRUE : FALSE;
        Text defText = this.defaultValue ? TRUE : FALSE;
        return new LiteralText("")
                .append(valText)
                .append(new LiteralText(" (default: "))
                .append(defText)
                .append(")");
    }
}
