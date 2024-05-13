package model;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.List;

class TaskTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    void addNewTask() throws InvalidInputException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Statuses.NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }
}