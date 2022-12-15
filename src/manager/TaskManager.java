package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;

 interface TaskManager {
        int getId();
        //методы для получения списка задач
         ArrayList<Task> getAllTasks();
         ArrayList<Epic> getAllEpics();
         ArrayList<Subtask> getAllSubtasks();
        // методы для удаления
         void deleteAllTasks();
         void deleteAllSubtasks();
         void deleteAllEpics();
        //методы для получения по айди
         Task getTaskById(int id);
         Epic getEpicById(int id);
         Subtask getSubtaskById(int id);
        // методы для удаления по айди
         void removeTaskById(int id);
         void removeEpicById(int id);
         void removeSubtaskById(int id);
        //методы по созданию задач
         void createTask(Task task);
         void createEpic(Epic epic);
         void createSubtask(Subtask subtask);
        //методы по обновлению задач
         void updateTask(Task task);
         void updateEpic(Epic epic);
         void updateSubtask(Subtask subtask);
         void changeEpicStatus(Subtask subtask);
        //получение подзадач
         ArrayList<Subtask> getSubtasksOfEpic(Epic epic);
    }


