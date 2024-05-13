package service;

import model.Statuses;
import model.Task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    void SavingPreviousVersionAndData() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Statuses.NEW);
        taskManager.createTask(task1);

        historyManager.add(task1);

        Task task2 = new Task("Task 2", "Description 2", Statuses.NEW);
        taskManager.createTask(task2);

        historyManager.add(task2);

        assertEquals(historyManager.getHistory().getFirst(), task1);
        assertEquals(historyManager.getHistory().get(1), task2);


        assertEquals(historyManager.getHistory().getFirst().getStatus(), task1.getStatus());


    }
}