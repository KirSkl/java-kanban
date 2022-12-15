package tasks;

import manager.InMemoryTaskManager;

import java.util.ArrayList;

public class Epic extends Task {
    public Epic(String title, String description, StatusOfTask status, InMemoryTaskManager manager) {
        super(title, description, status, manager);
        this.status = StatusOfTask.NEW;
    }

    private ArrayList<Integer> ids = new ArrayList<>();

    public void setIds(int id) {
        this.ids.add(id);
    }
    public ArrayList<Integer> getIds() {
        return ids;
    }
    public void clearIds(){
        ids.clear();
    }
    public void removeFromIds(int id){
        this.ids.remove(id);
    }
}
