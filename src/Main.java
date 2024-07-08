import model.*;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) throws InvalidInputException {
        TaskManager manager = Managers.getDefault();


        Task task1 = new Task("Попить", "Сок", Statuses.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Поесть", "Булочка", Statuses.IN_PROGRESS);
        manager.createTask(task2);
        manager.addHistory();

        Epic epic1 = new Epic("Съездить в Англию", "Лондон");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Выучить язык", "Английский", Statuses.DONE,epic1);
        manager.createSubTask(subtask1);

        Subtask subtask2 =  new Subtask("Накопить деньги", "10_000$",Statuses.IN_PROGRESS, epic1);
        manager.createSubTask(subtask2);

        Epic epic2 = new Epic("Съездить в Москву", "Отдых");
        manager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Накопить денег", "60_000 руб.", Statuses.IN_PROGRESS,epic2);
        manager.createSubTask(subtask3);


        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getEpicById(3));

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
