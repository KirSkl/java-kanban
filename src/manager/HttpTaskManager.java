package manager;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(String url) {
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
        Type taskType = new TypeToken<HashMap <Integer, Task>>() {}.getType();
        tasks = gson.fromJson(client.load("/tasks/task"), taskType);

        Type epicType = new TypeToken<HashMap <Integer, Task>>() {}.getType();
        epics = gson.fromJson(client.load("/tasks/epic"), epicType);

        Type subtaskType = new TypeToken<HashMap <Integer, Task>>() {}.getType();
        subtasks = gson.fromJson(client.load("/tasks/subtask"), subtaskType);

        Type historyType = new TypeToken<List <Task>>() {}.getType();
        ArrayList<Task> historyList = gson.fromJson(client.load("/tasks/history"), historyType);
        for (Task task: historyList) {
            history.add(task);
        }

        Type priorTaskType = new TypeToken<Set<Task>>() {}.getType();
        prioritizedTasks = gson.fromJson(client.load("/tasks"), priorTaskType);
    }

}
