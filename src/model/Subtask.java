package model;

public class Subtask extends Task {
    private Epic epic;

    private int epicId;


    public Subtask(String title, String description, Statuses status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, Statuses status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        this.epicId = epic.id;
    }

    public Epic getEpic() {
        return epic;
    }

    public int getEpicId(){
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
}