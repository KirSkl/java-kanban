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

    public ArrayList<Task> getAllTasks(HashMap hashMap){// Передаю в аргумент хешмап, потому что их три
        ArrayList<Task> allTasks = new ArrayList<Task>(hashMap.values()); //привожу все к родительскому классу Task,
        return allTasks; // чтобы не писать для каждого класса свой метод. Дальше так же.
    }

    public void deleteAllTasks(HashMap hashMap){
        hashMap.clear();
    }
    public Task getByID(HashMap hashMap, int id){
        return (Task) hashMap.get(id);
    }
    public void removeByID(HashMap hashMap, int id) {
        hashMap.remove(id);
    }
}
