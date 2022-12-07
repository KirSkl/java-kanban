package tasks;

import manager.Manager;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected String status;

    public Task(String title, String description, String status, Manager manager) {
        this.title = title;
        this.description = description;
        this.id = manager.getId();
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
