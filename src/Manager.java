import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    static int id=0;
    public static int getId() {
        return id++;
    }

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //методы для получения списка задач
    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }
    public ArrayList<Epic> getAllEpics(){
        ArrayList<Epic> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }
    public ArrayList<Subtask> getAllSubtasks(){
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
        return allSubtasks;
    }
    // методы для удаления
    public void deleteAllTasks(){
        tasks.clear();
    }
    public void deleteAllSubtasks(){
        subtasks.clear();
    }
    public void deleteAllEpics(){
        epics.clear();
    }
    //методы для получения по айди
    public Task getTaskByID(int id){
        return tasks.get(id);
    }
    public Epic getEpicByID(int id){
        return epics.get(id);
    }
    public Subtask getSubtaskByID(int id){
        return subtasks.get(id);
    }
    // методы для удаления по айди
    public void removeTaskByID(int id) {
        tasks.remove(id);
    }
    public void removeEpicByID(int id) {
        epics.remove(id);
    }
    public void removeSubtaskByID(int id) {
        subtasks.remove(id);
    }

}
