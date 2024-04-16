import model.*;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Попить", "Сок", Statuses.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("поесть", "Булочка", Statuses.IN_PROGRESS);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Съездить в Англию", "Лондон");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Выучить язык", "Английский", Statuses.DONE,epic1);
        taskManager.createSubTask(subtask1);

        Subtask subtask2 =  new Subtask("Накопить деньги", "10_000$",Statuses.IN_PROGRESS, epic1);
        taskManager.createSubTask(subtask2);

        Epic epic2 = new Epic("Съездить в Москву", "Лондон");
        taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Накопить денег", "60_000 руб.", Statuses.IN_PROGRESS,epic2);
        taskManager.createSubTask(subtask3);


        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getEpicById(3));
    }
}
