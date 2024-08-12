import exceptions.InvalidInputException;
import model.*;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) throws InvalidInputException {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Попить", "Сок", Statuses.NEW, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 5, 21, 11, 20));
        manager.createTask(task1);
        Task task2 = new Task("Поесть", "Булочка", Statuses.IN_PROGRESS, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2026, 8, 21, 11, 45));
        manager.createTask(task2);

        Epic epic1 = new Epic("Съездить в Англию", "Лондон", Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 27, 12, 33));
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Выучить язык", "Английский", Statuses.DONE,epic1, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 27, 22, 45));
        manager.createSubTask(subtask1);

        Subtask subtask2 =  new Subtask("Накопить деньги", "10_000$",Statuses.IN_PROGRESS, epic1, Duration.of(13, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 11, 28, 5, 12));
        manager.createSubTask(subtask2);

        Epic epic2 = new Epic("Съездить в Москву", "Отдых", Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 3, 12, 16, 10));
        manager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Накопить денег", "60_000 руб.", Statuses.IN_PROGRESS, epic2, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2025, 3, 12, 16, 45));
        manager.createSubTask(subtask3);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getEpicById(3));

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println(manager.getPrioritizedTasks());
    }
}
