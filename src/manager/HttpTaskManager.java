package manager;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        this.url = url;
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }
    @Override
    public void save() {
        var tasks = gson.toJson(getAllTasks());
        client.put("/tasks/task", tasks);

        var epics = gson.toJson(getAllEpics());
        client.put("/tasks/epic", epics);

        var subtasks = gson.toJson(getAllSubtasks());
        client.put("/tasks/subtask", subtasks);

        var history = gson.toJson(getHistory());
        client.put("/tasks/history", history);

        var priorTasks = gson.toJson(getPrioritizedTasks());
        client.put("/tasks", priorTasks);
    }
    public void load() {
        var loadTasks = client.load("/tasks/task");
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasksList = gson.fromJson(loadTasks, taskType);
        for(Task task : tasksList) {
            tasks.put(task.getId(), task);
        }

        var loadEpics = client.load("/tasks/epic");
        Type epicType = new TypeToken<List<Epic>>() {}.getType();
        List<Epic> epicsList = gson.fromJson(loadEpics, epicType);
        for (Epic epic : epicsList) {
            epics.put(epic.getId(), epic);
        }

        var loadSubtasks = client.load("/tasks/subtask");
        Type subtaskType = new TypeToken<List<Subtask>>() {}.getType();
        List<Subtask> subtasksList = gson.fromJson(loadSubtasks, subtaskType);
        for (Subtask subtask : subtasksList) {
            subtasks.put(subtask.getId(), subtask);
        }

        Type historyType = new TypeToken<List<Task>>() {}.getType();
        ArrayList<Task> historyList = gson.fromJson(client.load("/tasks/history"), historyType);
        for (Task task: historyList) {
            history.add(task);
        }

        Type priorTaskType = new TypeToken<Set<Task>>() {}.getType();
        prioritizedTasks = gson.fromJson(client.load("/tasks"), priorTaskType);
    }
}
