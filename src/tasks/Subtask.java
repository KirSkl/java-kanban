package tasks;

import manager.InMemoryTaskManager;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String title, String description, StatusOfTask status, int idEpic, InMemoryTaskManager manager,
                   TypeOfTask type) {
        super(title, description, status, manager, type);
        this.idEpic=idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }
    @Override
    public String toString() {
        return super.toString()+","+idEpic;
    }
}
