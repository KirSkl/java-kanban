package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Util {
     public static FileBackedTasksManager loadFromFile(File file) throws IOException {
         List<String> lines = Files.readAllLines(Path.of(file.getPath()));
         int noTask = 2; //количество строк, не содержащих задач
         FileBackedTasksManager manager = new FileBackedTasksManager(file);
         Map<Integer, Task> allTasks = new HashMap<>(); //мапа-прослойка для нахождения задач для истории
         for (int i=1; i<lines.size()-noTask; i++){
            Task task = taskFromString(lines.get(i));
            allTasks.put(task.getId(), task);
            switch (lines.get(i).split(",")[1]){
                case "TASK" :
                    manager.tasks.put(task.getId(), task);
                    break;
                case "EPIC" :
                    Epic epic = epicFromString(lines.get(i));
                    manager.epics.put(epic.getId(), epic);
                    break;
                default :
                    Subtask subtask = subtaskFromString(lines.get(i));
                    manager.subtasks.put(subtask.getId(), subtask);
            }
        }
        String[] ids = lines.get(lines.size()-1).split(",");
        for (String id : ids){
            manager.history.add(allTasks.get(Integer.parseInt(id)));
        }
        return manager;
    }
    private static Task taskFromString(String value) {
        return new Task(value.split(","));
    }
    private static Epic epicFromString(String value) {
        return new Epic(value.split(","));
    }
    private static Subtask subtaskFromString(String value) {
        return new Subtask(value.split(","));
    }
    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] ids = value.split(",");
        for(String id : ids) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }
}
