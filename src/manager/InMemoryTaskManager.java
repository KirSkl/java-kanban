package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static tasks.StatusOfTask.*;

public class InMemoryTaskManager implements TaskManager {
    private int id=0;
    public int getId() {
        return id++;
    }

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager history = new InMemoryHistoryManager();

    @Override
    public List<Task> getHistory()  {
        return history.getHistory();
    }
    //методы для получения списка задач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }
    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
    @Override
    // методы для удаления
    public void deleteAllTasks() {
        tasks.clear();
    }
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for(Epic epic: epics.values()) { //удалить все айди из эпиков
            epic.clearIds();
            epic.setStatus(NEW);
        }    }
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }
    //методы для получения по айди
    @Override
    public Task getTaskById(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Epic getEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Subtask getSubtaskById(int id) {
        history.add(subtasks.get(id));
        return subtasks.get(id);
    }
    // методы для удаления по айди
    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }
    @Override
    public void removeEpicById(int id) {
        for (Integer i : epics.get(id).getIds()) { //удаляем подзадачи вместе с эпиком
            subtasks.remove(i);
        }
        epics.remove(id);
    }
    @Override
    public void removeSubtaskById(int id) {
        epics.get(subtasks.get(id).getIdEpic()).removeFromIds(id);
        changeEpicStatus(subtasks.get(id));//удаляю айди субтаска из эпика
        subtasks.remove(id);
    }
    //методы по созданию задач
    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getIdEpic()).setIds(subtask.getId());//сообщаю эпику айдти субтаска
        changeEpicStatus(subtask);
    }
    //методы по обновлению задач
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask);
    }
    @Override
    public void changeEpicStatus(Subtask subtask) {
        for (Integer iD : epics.get(subtask.getIdEpic()).getIds()) {
            if (subtasks.get(iD).getStatus() == NEW) {
                epics.get(subtask.getIdEpic()).setStatus(NEW);
            } else {
                epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                break;
            }
        }
        if (epics.get(subtask.getIdEpic()).getStatus() == NEW) {
            return;
        }
        for (Integer iD : epics.get(subtask.getIdEpic()).getIds()) {
            if (subtasks.get(iD).getStatus() == DONE) {
                epics.get(subtask.getIdEpic()).setStatus(DONE);
            } else {
                epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                break;
            }
        }
    }
    //получение подзадач
    @Override
    public List<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpics = new ArrayList<>();
        for (Integer iD : epic.getIds()){
            subtasksOfEpics.add(subtasks.get(iD));
        }
        return subtasksOfEpics;
    }
}
