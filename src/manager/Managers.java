package manager;



import com.google.gson.Gson;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }
    public static HistoryManager getDefaultHistory() { //здесь тоже
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }
    public static Gson getGson() {
        return new Gson();
    }
}
