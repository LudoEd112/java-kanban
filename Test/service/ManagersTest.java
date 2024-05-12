package service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void defaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager);
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }
    @Test
    void defaultTask() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        assertInstanceOf(InMemoryTaskManager.class, taskManager);
    }

}