package tasks;

import manager.InMemoryTaskManager;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusOfTask status;
    protected TypeOfTask type;

    public Task(String title, String description, StatusOfTask status, InMemoryTaskManager manager, TypeOfTask type) {
        this.title = title;
        this.description = description;
        this.id = manager.getId();
        this.status = status;
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setStatus(StatusOfTask status) {
        this.status = status;
    }
    public StatusOfTask getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String toString() {
        return id+","+type+","+title+","+status+","+type+","+description+",";
    }
}
