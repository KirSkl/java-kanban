package tasks;

import Manager.InMemoryTaskManager;
import Manager.StatusOfTask;

public class Subtask extends Task {
    private int idEpic;
    public Subtask(String title, String description, StatusOfTask status, int idEpic, InMemoryTaskManager manager) {
        super(title, description, status, manager);
        this.idEpic=idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }
}
