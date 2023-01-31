package tasks;

import manager.InMemoryTaskManager;

import static tasks.TypeOfTask.TASK;

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
    public Task(String[] lines) {
        id = Integer.parseInt(lines[0]);
        type = TypeOfTask.valueOf(lines[1]);
        title = lines[2];
        status = StatusOfTask.valueOf(lines[3]);
        description = lines[4];
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
        return id+","+type+","+title+","+status+","+description+",";
    }
}
