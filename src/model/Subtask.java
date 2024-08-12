package model;
import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    private final int epicId;

    public Subtask(String title, String description, Statuses status, Epic epic, Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.epic = epic;
        this.epicId = epic.id;
        this.typesOfTasks = TypesOfTasks.SUBTASK;
    }

    public Subtask(String name, String description, Statuses status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
        this.typesOfTasks = TypesOfTasks.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "EpicId=" + epicId +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "Duration: " + getDuration().toMinutes() + " mins, " +
                "StartTime: " + getStartTime() +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, typesOfTasks, title, status, description, epic.getId());
    }
}