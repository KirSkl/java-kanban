package tasks;

import manager.Manager;
import java.util.ArrayList;

public class Epic extends Task {
    public Epic(String title, String description, String status, Manager manager) {
        super(title, description, status, manager);
        this.status = "NEW";
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
