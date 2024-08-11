

package model;

import exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicAndSubtaskTest {
    private static TaskManager taskManager;
    private static Epic epic;

    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefault();
        epic = new Epic("Test addNewEpic", "Test addNewEpic description", Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 21, 11, 10));
        taskManager.createEpic(epic);
    }

    @Test
    void addNewEpic() throws InvalidInputException {

        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final Map<Integer,Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпик.");
        assertEquals(epic, epics.get(1), "Эпики не совпадают.");
    }

    @Test
    void addNewSubtask(){
        Subtask subtask1 = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description",
                Statuses.DONE,epic, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 21, 11, 30));
        taskManager.createSubTask(subtask1);

        Subtask subtask2 =  new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description",
                Statuses.IN_PROGRESS, epic, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 21, 11, 45));
        taskManager.createSubTask(subtask2);

        final Map<Integer,Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество Эпик.");

        final int Subtask1epicId = subtask1.getEpicId();
        final int Subtask2epicId = subtask2.getEpicId();
        assertEquals(Subtask1epicId, Subtask2epicId, "Подзадачи принадлежат разным эпикам");


    }
}

