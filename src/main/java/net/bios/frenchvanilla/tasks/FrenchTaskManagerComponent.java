package net.bios.frenchvanilla.tasks;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.nbt.NbtCompound;

import java.util.LinkedList;
import java.util.Queue;

public class FrenchTaskManagerComponent implements ServerTickingComponent {
    public Queue<FrenchTask> tasks;

    public FrenchTaskManagerComponent() {
        this.tasks = new LinkedList<>();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }

    @Override
    public void serverTick() {
        int numTasks = 0;
        while (numTasks < FrenchVanilla.config.tasksPerTick.value && !this.tasks.isEmpty()) {
            boolean remove = this.tasks.peek().run();
            if (remove) this.tasks.remove();
            numTasks += 1;
        }
    }
}
