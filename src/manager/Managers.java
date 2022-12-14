package manager;

public class Managers {

    public static TaskManager getDefault() { //пока не очень понятно, для чего это и как должно выглядеть
        return new InMemoryTaskManager();    //и работать
    }
    public static HistoryManager getDefaultHistory() { //здесь тоже
        return new InMemoryHistoryManager();
    }
}
