package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tasks.StatusOfTask.*;
import static tasks.TypeOfTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager { //метод loadFromFile я перенес в класс Util, нужно
                                                                  //вернуть сюда?
    private final File file;

    public FileBackedTasksManager (File file) {
        this.file = file;
    }
    public static FileBackedTasksManager loadFromFile(File file) throws IOException { //вот так еще можно сделать...
        return Util.loadFromFile(file);
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
            for(Task task : getAllTasks()){
            fileWriter.write(task.toString()+"\n");
            }
            for(Task epic : getAllEpics()){
            fileWriter.write(epic.toString()+"\n");
            }
            for(Task subtasks : getAllSubtasks()){
            fileWriter.write(subtasks.toString()+"\n");
            }
            try {
                fileWriter.write("\n");
                fileWriter.write(historyToString (history));
            } catch (NullPointerException e) { //может выбросить исключение, если задачи были созданы, но не просмотрены
                fileWriter.write("");      // и история пустая. Это не правильно?
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

    public static void main(String[] args) throws IOException {
        //просто тесты
        File file = new File("test.csv");
        FileBackedTasksManager manager1 = new FileBackedTasksManager(file);

        tasks.Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, manager1, TASK);
        manager1.createTask(task);
        tasks.Task task1 = new tasks.Task("Вторая задача", "интересная", NEW, manager1, TASK);
        manager1.createTask(task1);
        tasks.Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, manager1, EPIC);
        manager1.createEpic(epic);
        tasks.Subtask subtask = new tasks.Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), manager1, SUBTASK);

        manager1.createSubtask(subtask);
        tasks.Subtask subtask1 = new tasks.Subtask("Вторая подзадача", "Скучная",
                IN_PROGRESS, epic.getId(), manager1, SUBTASK);

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
        FileBackedTasksManager manager2 = Util.loadFromFile(manager1.file);
        manager2.getSubtaskById(3);
        System.out.println(manager2.getHistory());
        manager2.save();
    }
}


