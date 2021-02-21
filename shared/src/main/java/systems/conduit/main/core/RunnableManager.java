package systems.conduit.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Innectic
 * @since 12/8/2020
 */
public class RunnableManager {

    @Getter private final Map<Integer, RunnableData> activeRunnables = new HashMap<>();

    private int runnableId = 0;

    public int schedule(Runnable runnable, int every) {
        this.activeRunnables.put(runnableId++, new RunnableData(runnable, every, false, false));
        return runnableId;
    }

    public int scheduleAsync(Runnable runnable, int every) {
        this.activeRunnables.put(runnableId++, new RunnableData(runnable, every, true, false));
        return runnableId;
    }

    public void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }

    public void runSync(Runnable runnable) {

        runnable.run();
    }

    public void cancelRunnable(int id) {
        activeRunnables.remove(id);
    }

    @Getter
    @AllArgsConstructor
    public static class RunnableData {
        private final Runnable runnable;
        private final int every;
        private final boolean async;

        @Setter private boolean isRunning;
    }
}
