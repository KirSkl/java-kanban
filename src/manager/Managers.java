package manager;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();//я так понимаю, нормальную реализацию мы в след раз будем
        return taskManager; // делать
    }
    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
