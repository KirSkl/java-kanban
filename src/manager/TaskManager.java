package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();
    //методы для получения списка задач
    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();
    // методы для удаления
    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();
    //методы для получения по айди
    Task getTaskById(int id);
    Epic getEpicById(int id);
    Epic getEpicByIdNoHistory(int id);
    Subtask getSubtaskById(int id);
    Subtask getSubtaskByIdNoHistory(int id);
    // методы для удаления по айди
    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);
    //методы по созданию задач
    Task createTask(Task task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);
    //методы по обновлению задач
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);
    void changeEpicStatus(Subtask subtask);
    void changeEpicTime(Epic epic);
    void addToPrioritizedTasks(Task task);
    List<Task> getPrioritizedTasks();
    List<Subtask> getSubtasksOfEpic(Epic epic);
}


