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
        subtasks.clear();
    }
    //методы для получения по айди
    public Task getTaskById(int id){
        return tasks.get(id);
    }
    public Epic getEpicById(int id){
        return epics.get(id);
    }
    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }
    // методы для удаления по айди
    public void removeTaskById(int id) {
        tasks.remove(id);
    }
    public void removeEpicById(int id) {
        for (Integer i : epics.get(id).ides){ //удаляем подзадачи вместе с эпиком
            subtasks.remove(i);
        }
        epics.remove(id);
    }
    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }
    //методы по созданию задач
    public void createTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void createEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    public void createTask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask); //!!!нужно реализовать передачу айди эпику, и прием айди от эпика
    }
    //методы по обновлению задач
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    public void updateTask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
    }
}
