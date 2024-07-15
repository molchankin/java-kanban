package service;

import impl.InMemoryHistoryManager;
import impl.InMemoryTaskManager;

public final class Managers {
    public static TaskManager getDefaults() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
