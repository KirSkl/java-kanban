package Manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
        public int getId();
        public List<Task> getHistory();
        public void setHistory(Task task);
        //методы для получения списка задач
        public ArrayList<Task> getAllTasks();
        public ArrayList<Epic> getAllEpics();
        public ArrayList<Subtask> getAllSubtasks();
        // методы для удаления
        public void deleteAllTasks();
        public void deleteAllSubtasks();
        public void deleteAllEpics();
        //методы для получения по айди
        public Task getTaskById(int id);
        public Epic getEpicById(int id);
        public Subtask getSubtaskById(int id);
        // методы для удаления по айди
        public void removeTaskById(int id);
        public void removeEpicById(int id);
        public void removeSubtaskById(int id);
        //методы по созданию задач
        public void createTask(Task task);
        public void createEpic(Epic epic);
        public void createSubtask(Subtask subtask);
        //методы по обновлению задач
        public void updateTask(Task task);
        public void updateEpic(Epic epic);
        public void updateSubtask(Subtask subtask);
        public void changeEpicStatus(Subtask subtask);
        //получение подзадач
        public ArrayList<Subtask> getSubtasksOfEpic(Epic epic);
    }


