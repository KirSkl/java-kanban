package manager;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    String path;
    public FileBackedTasksManager (String path) {
        this.path = path;
    }

    private void save(){}
}
