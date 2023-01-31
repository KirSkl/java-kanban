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
    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(file.getPath()));
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file);
        Map<Integer, Task> allTasks = new HashMap<>(); //мапа-прослойка для нахождения задач для истории
        for (String line : lines){
            allTasks.put(taskFromString(line).getId(), taskFromString(line));
        }
        for (Task task : allTasks.values()) {
            if (task.getType().equals("TASK")) {
                fbtm.tasks.put(task.getId(), task);
            } else if (task.getType().equals("EPIC")){
                fbtm.epics.put(task.getId(), task);
            } else {
                fbtm.subtasks.put(task.getId(), task);
            }
        }
        String[] ids = lines.get(lines.size()-1).split(",");
        for (String id : ids){
        fbtm.history.add(allTasks.get(Integer.parseInt(id)));
        }
        return fbtm;
    }
    private static Task taskFromString(String value) {
        String[] fields = value.split(",");
        if (fields[1].equals("TASK")) {
            return new Task(fields);
        } else if (fields[1].equals(("EPIC"))) {
            return new Epic(fields);
        } else {
            return new Subtask(fields);
        }
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
