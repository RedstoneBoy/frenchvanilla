package net.bios.frenchvanilla.timber;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.bios.frenchvanilla.FrenchVanilla;
import net.minecraft.nbt.NbtCompound;

import java.util.LinkedList;
import java.util.Queue;

public class TimberTaskManagerComponent implements ServerTickingComponent {
    public Queue<TimberTask> timberTasks;

    public TimberTaskManagerComponent() {
        this.timberTasks = new LinkedList<>();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }

    @Override
    public void serverTick() {
        if (!FrenchVanilla.config.timber) return;

        int tasks = 0;
        while (tasks < FrenchVanilla.config.timberTasksPerTick && !timberTasks.isEmpty()) {
            boolean remove = timberTasks.peek().run();
            if (remove) timberTasks.remove();
            tasks += 1;
        }
    }
}
