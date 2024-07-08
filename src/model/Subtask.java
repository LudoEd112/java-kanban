package model;

public class Subtask extends Task {
    private Epic epic;

    private final int epicId;

    public Subtask(String title, String description, Statuses status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        this.epicId = epic.id;
        this.typesOfTasks = TypesOfTasks.SUBTASK;
    }

    public Subtask(String name, String description, int epicId, Statuses status, int id) {
        super(name, description, status, id, TypesOfTasks.SUBTASK);
        this.epicId = epicId;
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
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, typesOfTasks, title, status, description, epic.getId());
    }
}