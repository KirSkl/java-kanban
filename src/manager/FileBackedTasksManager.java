package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tasks.StatusOfTask.*;
import static tasks.TypeOfTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File file = new File("src/resources/defaultSave.csv");

    public FileBackedTasksManager (File file) {
        this.file = file;
    }
    public FileBackedTasksManager() {}
    public void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic,\n");
            for(Task task : getAllTasks()){
                fileWriter.write(task.toString()+"\n");
            }
            for(Task epic : getAllEpics()){
                fileWriter.write(epic.toString()+"\n");
            }
            for(Task subtasks : getAllSubtasks()){
                fileWriter.write(subtasks.toString()+"\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(history));
        }
        catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
    public static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        int noHistory = 2; //количество строк, не содержащих задач
        Map<Integer, Task> allTasks = new HashMap<>(); //мапа-прослойка для нахождения задач для истории
        int countTasks = 0;
        try {
            List<String> lines = Files.readAllLines(Path.of(file.getPath()));
            for (int i = 1; i < lines.size(); i++) {
                if(lines.get(i).isEmpty()) {
                    break;
                }
                Task task = taskFromString(lines.get(i));
                allTasks.put(task.getId(), task);
                String[] fields = lines.get(i).split(",");
                switch (fields[1]) {
                    case "TASK" :
                        manager.tasks.put(task.getId(), task);
                        countTasks++;
                    case "EPIC" :
                        Epic epic = epicFromString(lines.get(i));
                        manager.epics.put(epic.getId(), epic);
                        countTasks++;
                    case "SUBTASK" :
                        Subtask subtask = subtaskFromString(lines.get(i));
                        manager.subtasks.put(subtask.getId(), subtask);
                        manager.getEpicByIdNoHistory(subtask.getIdEpic()).setIds(subtask.getId());
                        countTasks++;
                    default :
                        System.out.println("Тип задачи не распознан");
                }
            }
            if (lines.size()-countTasks-noHistory > 0) {
                String[] ids = lines.get(lines.size() - 1).split(",");
                for (String id : ids) {
                    manager.history.add(allTasks.get(Integer.parseInt(id)));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
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
    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] ids = value.split(",");
        for(String id : ids) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }
    private static String historyToString (HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        List<String> ids = new ArrayList<>();
        for (Task task: tasks) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
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
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }
    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }
    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
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
    public void setFile(File file) {
        this.file=file;
    }

    public static void main(String[] args) throws IOException {
        //просто тесты
        /*File file = new File("test.csv");
        FileBackedTasksManager manager1 = new FileBackedTasksManager(file);

        tasks.Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, TASK);
        manager1.createTask(task);
        tasks.Task task1 = new tasks.Task("Вторая задача", "интересная", NEW, TASK);
        manager1.createTask(task1);
        tasks.Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, EPIC);
        manager1.createEpic(epic);
        tasks.Subtask subtask = new tasks.Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), SUBTASK);

        manager1.createSubtask(subtask);
        tasks.Subtask subtask1 = new tasks.Subtask("Вторая подзадача", "Скучная",
                IN_PROGRESS, epic.getId(), SUBTASK);

        manager1.createSubtask(subtask1);
        manager1.getTaskById(0);
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());
        System.out.println(manager1.getSubtaskById(subtask1.getId()).getTitle());
        System.out.println(manager1.getSubtaskById(subtask.getId()).getTitle());
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());

        if (!manager1.getHistory().isEmpty()) {
            for (int i = 0; i < manager1.getHistory().size(); i++) {


                System.out.println(manager1.getHistory().get(i).getTitle());
            }
        }
        FileBackedTasksManager manager2 = loadFromFile(manager1.file);
        manager2.getSubtaskById(3);
        System.out.println(manager2.getHistory());
        manager2.save(); */
    }
}


