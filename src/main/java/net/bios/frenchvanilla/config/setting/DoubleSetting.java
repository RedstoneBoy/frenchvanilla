package net.bios.frenchvanilla.config.setting;

import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;

public class DoubleSetting implements Setting {
    private double defaultValue;
    public double value;

    public DoubleSetting(double defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public double defaultValue() {
        return this.defaultValue;
    }

    @Override
    public NbtElement toNbt() {
        return NbtDouble.of(this.value);
    }

    @Override
    public void readNbt(NbtElement element) {
        this.value = ((NbtDouble) element).doubleValue();
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
        this.value = (Double) obj;
    }
}
