package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Util {
    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(file.getPath()));
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file);

        for (String line : lines) {
            if (taskFromString(line).getType().equals("TASK")) {
                fbtm.tasks.put(taskFromString(line).getId(), taskFromString(line));
            } else if (taskFromString(line).getType().equals("EPIC")){
                fbtm.epics.put(taskFromString(line).getId(), taskFromString(line));
            } else {
                fbtm.subtasks.put(taskFromString(line).getId(), taskFromString(line));
            }
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
