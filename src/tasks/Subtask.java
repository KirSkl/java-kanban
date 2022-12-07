package tasks;

import manager.Manager;

public class Subtask extends Task {



    private int idEpic;
    public Subtask(String title, String description, String status, int idEpic, Manager manager) {
        super(title, description, status, manager);
        this.idEpic=idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }
}
