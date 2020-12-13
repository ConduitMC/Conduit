package systems.conduit.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 12/8/2020
 */
public class RunnableManager {

    @Getter private final Map<Integer, RunnableData> activeRunnables = new HashMap<>();

    private int runnableId = 0;

    public int schedule(Runnable runnable, int every) {
        this.activeRunnables.put(runnableId++, new RunnableData(runnable, every, false));
        return runnableId;
    }

    public void cancelRunnable(int id) {
        activeRunnables.remove(id);
    }

    @Getter
    @AllArgsConstructor
    public static class RunnableData {
        private final Runnable runnable;
        private final int every;

        @Setter private boolean isRunning;
    }
}
