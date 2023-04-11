package tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> ids = new ArrayList<>();
    private Instant endTime = startTime.plusSeconds(getDuration().toSeconds());

    public Epic(String title, String description, StatusOfTask status, TypeOfTask type, Instant startTime,
                Duration duration) {
        super(title, description, status, type, startTime, duration);
        this.status = StatusOfTask.NEW;
    }
    public Epic(int id, String title, String description, StatusOfTask status, TypeOfTask type, Instant startTime,
                Duration duration) {
        super(id, title, description, status, type, startTime, duration);
        this.status = StatusOfTask.NEW;
    }
    public Epic(String[] lines) {
        super(lines);
    }
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
        this.ids.remove((Integer) id);
    }
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    public Instant getEndTime() {
        return endTime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;

        Epic object = (Epic) o;

        return Objects.equals(this.ids, object.ids);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ids);
    }
}
