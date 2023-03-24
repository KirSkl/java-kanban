package tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusOfTask status;
    protected TypeOfTask type;
    protected Instant startTime;
    protected Duration duration;

    public Task(String title, String description, StatusOfTask status, TypeOfTask type, Instant startTime,
                    Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Task(int id, String title, String description, StatusOfTask status, TypeOfTask type, Instant startTime,
                    Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Task(String[] lines) {
        id = Integer.parseInt(lines[0]);
        type = TypeOfTask.valueOf(lines[1]);
        title = lines[2];
        status = StatusOfTask.valueOf(lines[3]);
        description = lines[4];
        startTime = Instant.parse(lines[5]);
        duration = Duration.parse(lines[6]);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Instant getStartTime() {
        return startTime;
    }
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
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
        return id + "," + type + "," + title + "," + status + "," + description + "," + startTime + "," + duration
                + "," + getEndTime() + ",";
    }
    public TypeOfTask getType() {
        return type;
    }
    public Instant getEndTime() {
        return startTime.plusSeconds(duration.toSeconds());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Task object = (Task) o;

        return Objects.equals(this.title, object.title)
                && Objects.equals(this.description, object.description)
                && Objects.equals(this.id, object.id)
                && Objects.equals(this.status, object.status);
    }
    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }
}