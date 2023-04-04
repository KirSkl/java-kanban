package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() { //здесь тоже
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }
}
