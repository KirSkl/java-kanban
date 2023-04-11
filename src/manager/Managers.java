package manager;

import com.google.gson.Gson;

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
    public static HttpTaskManager getHttpTaskManager() {
        return new HttpTaskManager("http://localhost:8078");
    }
    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
    public static Gson getGson() {
        return new Gson();
    }
}
