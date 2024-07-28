package service;

import model.Task;
import model.Epic;
import model.Subtask;
import model.Statuses;
import exceptions.InvalidInputException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    void AddAndGetTasks() throws InvalidInputException {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW);
        taskManager.createTask(task1);
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic1);
        Subtask subtask1 = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description",
                Statuses.DONE,epic1);
        taskManager.createTask(subtask1);

        assertEquals(3, taskManager.getAllTasks().size());

        assertEquals(task1, taskManager.getTaskById(1));
        assertEquals(epic1, taskManager.getTaskById(2));
        assertEquals(subtask1, taskManager.getTaskById(3));
    }

    @Test
    void GivenAndGeneratedIdDontConflicted(){
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW);
        taskManager.createTask(task2);
        task2.setId(1);
        assertEquals(2,taskManager.getAllTasks().size());
    }

    @Test
    void immutabilityOfTasks() throws InvalidInputException {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Statuses.NEW);
        taskManager.createTask(task);
        Task task2 = taskManager.getTaskById(task.getId());

        assertEquals(task, task2);
    }
}