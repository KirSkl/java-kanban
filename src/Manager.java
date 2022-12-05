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
        for(Epic epic: epics.values()) { //удалить все айди из эпиков
            epic.ides.clear();
        }

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
        Integer idSubtask=id;
        epics.get(subtasks.get(id).getIdEpic()).ides.remove(idSubtask);//удаляю айди субтаска из эпика
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
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getIdEpic()).ides.add(subtask.getId());//сообщаю эпику айдти субтаска
    }
    //методы по обновлению задач
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
    }

    //получение подзадач
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){ //можно также использовать айди эпика
        ArrayList<Subtask> subtasksOfEpics = new ArrayList<>();
        for (Integer id : epic.ides){
            subtasksOfEpics.add(subtasks.get(id));
        }
        return subtasksOfEpics;
    }
}
