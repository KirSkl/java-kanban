package tasks;

import manager.InMemoryTaskManager;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String title, String description, StatusOfTask status, int idEpic, TypeOfTask type,
                       Instant startTime, long duration) {
        super(title, description, status, type, startTime, duration);
        this.idEpic = idEpic;
    }
    public Subtask(int id, String title, String description, StatusOfTask status, int idEpic, TypeOfTask type,
                       Instant startTime, long duration) {
        super(id, title, description, status, type, startTime, duration );
        this.idEpic = idEpic;
    }
    public Subtask(String[] fields) {
        super(fields);
        idEpic = Integer.parseInt(fields[8]);
    }
    public int getIdEpic() {
        return idEpic;
    }
    @Override
    public String toString() {
        return super.toString() + idEpic;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return Objects.equals(this.idEpic, subtask.idEpic);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}
