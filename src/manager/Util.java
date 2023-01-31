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
    static public FileBackedTasksManager loadFromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(file.getPath()));
        int noTask = 2; //количество строк, не содержащих задач
        FileBackedTasksManager fBTM = new FileBackedTasksManager(file);
        Map<Integer, Task> allTasks = new HashMap<>(); //мапа-прослойка для нахождения задач для истории
        for (int i=1; i<lines.size()-noTask; i++){
            allTasks.put(taskFromString(lines.get(i)).getId(), taskFromString(lines.get(i)));
            String[] fields = lines.get(i).split(",");
            if (fields[1].equals("TASK")){
                fBTM.tasks.put(taskFromString(lines.get(i)).getId(), taskFromString(lines.get(i)));
            } else if (fields[1].equals("EPIC")) {
                fBTM.epics.put(epicFromString(lines.get(i)).getId(), epicFromString(lines.get(i)));
            } else {
                fBTM.subtasks.put(subtaskFromString(lines.get(i)).getId(), subtaskFromString(lines.get(i)));
            }
        }
        String[] ids = lines.get(lines.size()-1).split(",");
        for (String id : ids){
            fBTM.history.add(allTasks.get(Integer.parseInt(id)));
        }
        return fBTM;
    }
    private static Task taskFromString(String value) {
        String[] fields = value.split(",");
        return new Task(fields);
    }
    private static Epic epicFromString(String value) {
        String[] fields = value.split(",");
        return new Epic(fields);
    }
    private static Subtask subtaskFromString(String value) {
        String[] fields = value.split(",");
        return new Subtask(fields);
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
