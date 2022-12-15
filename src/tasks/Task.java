package tasks;

import manager.InMemoryTaskManager;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusOfTask status;

    public Task(String title, String description, StatusOfTask status, InMemoryTaskManager manager) {
        this.title = title;
        this.description = description;
        this.id = manager.getId();
        this.status = status;
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
}
