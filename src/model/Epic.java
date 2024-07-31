package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> epicSubtasks;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, Statuses.NEW, duration, startTime);
        epicSubtasks = new ArrayList<>();
        this.typesOfTasks = TypesOfTasks.EPIC;
    }

    public List<Integer> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(List<Integer> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "EpicId=" + id +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "Duration: " + getDuration().toMinutes() + " mins, " +
                "StartTime: " + getStartTime() +
                '}';
    }
}