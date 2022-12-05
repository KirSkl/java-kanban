import java.util.ArrayList;

public class Task {
    String title;
    String description;
    int id;
    String status;

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.id = Manager.getId();
        this.status = status;
    }
}
