package model;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Statuses status;
    protected TypesOfTasks typesOfTasks;


    public Task(String title, String description, Statuses status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.typesOfTasks = TypesOfTasks.TASK;
    }

    public Task(String title, String description, Statuses status, int id, TypesOfTasks typesOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.typesOfTasks = typesOfTasks;
    }

    public Task(String title, String description, int id, TypesOfTasks typesOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.typesOfTasks = typesOfTasks;
    }

    public Task(int id, String title, String description, TypesOfTasks typesOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.typesOfTasks = typesOfTasks;
    }

    public Task(String title, String description, Statuses status, int id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
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

    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + id +
                ", name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, typesOfTasks, title, status, description, "");
    }
}