package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicAndSubtaskTest {
    private static TaskManager taskManager;
    private static Epic epic;

    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefault();
        epic = new Epic("Test addNewEpic", "Test addNewEpic description");
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
                Statuses.DONE,epic);
        taskManager.createSubTask(subtask1);

        Subtask subtask2 =  new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description",
                Statuses.IN_PROGRESS, epic);
        taskManager.createSubTask(subtask2);

        final Map<Integer,Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество Эпик.");

        final int Subtask1epicId = subtask1.getEpicId();
        final int Subtask2epicId = subtask2.getEpicId();
        assertEquals(Subtask1epicId, Subtask2epicId, "Подзадачи принадлежат разным эпикам");


    }

    @Test
    void EpicAsSubtask() {
        Subtask subtask = new Subtask(epic.title, epic.description,epic.status, epic);
        taskManager.createSubTask(subtask);

        assertFalse(epic.getEpicSubtasks().contains(epic));
    }

    @Test
    void SubtaskAsEpic() {
        Subtask subtask = new Subtask(epic.title, epic.description,epic.status, epic);
        taskManager.createSubTask(subtask);
        taskManager.updateEpic(subtask.getEpic());

        assertFalse(epic.getEpicSubtasks().contains(epic));
    }
}