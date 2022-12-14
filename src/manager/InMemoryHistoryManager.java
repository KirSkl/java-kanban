package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyTasks = new ArrayList<>();
    @Override
    public void add(Task task) {
        if (historyTasks.size() > 10){
            historyTasks.remove(0);
        }
        historyTasks.add(task);
    }
    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

}
