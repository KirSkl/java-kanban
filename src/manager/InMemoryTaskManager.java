package manager;

import exceptions.IntersectionException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import static tasks.StatusOfTask.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id=0;

    public int getId() {
        return ++id;
    }

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        for (int id : tasks.keySet()) {
            history.remove(id);
            prioritizedTasks.removeIf(t -> t.getId() == id);
        }
        tasks.clear();
    }
    @Override
    public void deleteAllSubtasks() {
        for (int id : subtasks.keySet()) {
            history.remove(id);
            prioritizedTasks.removeIf(t -> t.getId() == id);
        }
        subtasks.clear();
        for(Epic epic: epics.values()) { //удалить все айди из эпиков
            epic.clearIds();
            epic.setStatus(NEW);
        }
    }
    @Override
    public void deleteAllEpics() {
        for (int id : epics.keySet()) {
            history.remove(id);
        }
        for (int id : subtasks.keySet()) {
            history.remove(id);
            prioritizedTasks.removeIf(t -> t.getId() == id);
        }
        epics.clear();
        subtasks.clear();
    }
    //методы для получения по айди
    @Override
    public Task getTaskById(int id) {
        try {
            history.add(tasks.get(id));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return tasks.get(id);
    }
    @Override
    public Epic getEpicById(int id) {
        try {
            history.add(epics.get(id));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return epics.get(id);
    }
    @Override
    public Epic getEpicByIdNoHistory(int id) {
        return epics.get(id);
    }
    @Override
    public Subtask getSubtaskById(int id) {
        try {
            history.add(subtasks.get(id));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return subtasks.get(id);
    }
    @Override
    public Subtask getSubtaskByIdNoHistory(int id) {
        return subtasks.get(id);
    }
    // методы для удаления по айди
    @Override
    public void removeTaskById(int id) {
        if(getHistory().contains(tasks.get(id))) {
            history.remove(id);
        }
        tasks.remove(id);
        prioritizedTasks.removeIf(t -> t.getId() == id);
    }
    @Override
    public void removeEpicById(int id) {
        try {
            for (Integer i : epics.get(id).getIds()) { //удаляем подзадачи вместе с эпиком
                subtasks.remove(i);
                prioritizedTasks.removeIf(t -> t.getId() == id);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        if(getHistory().contains(epics.get(id))) {
            history.remove(id);
        }
        epics.remove(id);
    }
    @Override
    public void removeSubtaskById(int id) {
        try {
            epics.get(subtasks.get(id).getIdEpic()).removeFromIds(id);
            changeEpicStatus(subtasks.get(id));//удаляю айди субтаска из эпика
            changeEpicTime(getEpicByIdNoHistory(subtasks.get(id).getIdEpic()));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        history.remove(id);
        subtasks.remove(id);
        prioritizedTasks.removeIf(t -> t.getId() == id);
    }
    //методы по созданию задач
    @Override
    public Task createTask(Task task) {
        task.setId(getId());
        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
        return task;
    }
    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getId());
        epics.put(epic.getId(), epic);
        return epic;
    }
    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getId());
        subtasks.put(subtask.getId(), subtask);
        addToPrioritizedTasks(subtask);
        epics.get(subtask.getIdEpic()).setIds(subtask.getId());//сообщаю эпику айдти субтаска
        changeEpicStatus(subtask);
        changeEpicTime(getEpicByIdNoHistory(subtask.getIdEpic()));
        return subtask;
    }
    //методы по обновлению задач
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
    }
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        addToPrioritizedTasks(subtask);
        changeEpicStatus(subtask);
        changeEpicTime(getEpicByIdNoHistory(subtask.getIdEpic()));
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
    @Override
    public void changeEpicTime(Epic epic) {
        long duration = 0;

        for (int id : epic.getIds()) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getStartTime().isBefore(epic.getStartTime())) {
                epic.setStartTime(subtask.getStartTime());
            }
            if (subtask.getEndTime().isAfter(epic.getEndTime())) {
                epic.setEndTime(subtask.getEndTime());
            }
            duration += subtask.getDuration();
        }
        epic.setDuration(duration);
    }
    @Override
    public void addToPrioritizedTasks(Task task) {
        try {
            checkIntersections(task);
            prioritizedTasks.add(task);
        } catch (IntersectionException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
    //получение подзадач
    @Override
    public List<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpics = new ArrayList<>();
        for (Integer iD : epic.getIds()) {
            subtasksOfEpics.add(subtasks.get(iD));
        }
        return subtasksOfEpics;
    }
    public void checkIntersections(Task task) {
        List<Task> priorTasks = new ArrayList<>();
        for (Task priorTask : priorTasks) {
            if (!task.getEndTime().isBefore(priorTask.getStartTime()) ||
                    !task.getStartTime().isAfter((priorTask.getEndTime()))) {
                throw new IntersectionException ("Ошибка добавления/обновления задачи. Обнаружено пересечение " +
                        "во времени выполнения с " + priorTask);
            }
        }
    }
}

