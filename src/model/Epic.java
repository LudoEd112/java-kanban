package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> epicSubtasks;

    public Epic(String title, String description) {
        super(title, description, Statuses.NEW);
        epicSubtasks = new ArrayList<>();
        this.typesOfTasks = TypesOfTasks.EPIC;
    }

    public Epic(String title, String description, int id) {
        super(id, title, description, TypesOfTasks.EPIC);
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
                '}';
    }
}