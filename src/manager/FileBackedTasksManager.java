package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TypeOfTask.*;

import java.io.File;
//import java.io.FileWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) {
        System.out.println("1");
    }
    private Task taskFromString(String value) {
        String[] fields = value.split(",");
        if (fields[1].equals("TASK")) {
            return new Task(fields);
        } else if (fields[1].equals(("EPIC"))) {
            return new Epic(fields);
        } else {
            return new Subtask(fields);
        }
    }
    private File file;

    public FileBackedTasksManager (File file) {
        this.file = file;
    }
    static String historyToString (HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        List<String> ids = new ArrayList<>();
        for (Task task: tasks) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join( ",", ids);
    }
    private void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");
        for (Task task : getAllTasks()) {
            fileWriter.write(task.toString()+"\n");
        }
        for (Task epic : getAllEpics()) {
            fileWriter.write(epic.toString()+"\n");
        }
        for (Task subtasks : getAllSubtasks()) {
            fileWriter.write(subtasks.toString()+"\n");
        }
        try {
            fileWriter.write("\n");

        fileWriter.write(historyToString (history));
        } catch (NullPointerException e) {
            fileWriter.write("");
        }
        }
        catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic =  super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void changeEpicStatus(Subtask subtask) {
        super.changeEpicStatus(subtask);
        save();
    }
}


