package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Statuses status;
    protected TypesOfTasks typesOfTasks;
    private Duration duration;
    private LocalDateTime startTime = LocalDateTime.now();
    protected LocalDateTime endTime;


    public Task(String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.typesOfTasks = TypesOfTasks.TASK;
    }

    public Task(String title, String description, Statuses status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.typesOfTasks = TypesOfTasks.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String title, String description,TypesOfTasks typesOfTasks, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.typesOfTasks = typesOfTasks;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public Statuses getStatus() {
        return status;
    }

    public TypesOfTasks getTypesOfTasks() {
        return typesOfTasks;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + id +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "Duration: " + getDuration().toMinutes() + " mins, " +
                "StartTime: " + getStartTime() +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, typesOfTasks, title, status, description, "");
    }
}