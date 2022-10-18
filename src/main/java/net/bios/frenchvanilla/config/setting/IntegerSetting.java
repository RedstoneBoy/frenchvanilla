package net.bios.frenchvanilla.config.setting;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;

public class IntegerSetting implements Setting {
    private int defaultValue;
    public int value;

    public IntegerSetting(int defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public int defaultValue() {
        return this.defaultValue;
    }

    @Override
    public NbtElement toNbt() {
        return NbtInt.of(this.value);
    }

    @Override
    public void readNbt(NbtElement element) {
        this.value = ((NbtInt) element).intValue();
    }

    @Override
    public Text text() {
        return Text.literal("")
                .append(Double.toString(this.value))
                .append(" (default: ")
                .append(Double.toString(this.defaultValue))
                .append(")");
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object obj) throws ClassCastException {
        this.value = (Integer) obj;
    }
}
