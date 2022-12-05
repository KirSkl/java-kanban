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

    public ArrayList<Task> getAllTasks(HashMap hashMap){
        ArrayList<Task> allTasks = new ArrayList<Task>(hashMap.values());
        return allTasks;
    }


}
