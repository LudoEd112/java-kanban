import com.google.gson.Gson;
import http.HttpTaskServer;
import http.handler.BaseHttpHandler;
import model.Epic;
import model.Statuses;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubtask();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createTask(task);
        String taskJson = gson.toJson(task);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test addNewTask1", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createTask(task);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test addNewTask2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"+ task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, epic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        manager.createSubTask(subtask);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Map<Integer,Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test addNewSubtask1", tasksFromManager.get(2).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, epic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        manager.createSubTask(subtask);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Map<Integer,Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Подадачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test addNewSubtask1", tasksFromManager.get(2).getTitle(), "Некорректное имя подзадачи");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, epic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        manager.createSubTask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        Map<Integer,Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Подадачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Map<Integer,Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Подадачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test addNewEpic1", tasksFromManager.get(1).getTitle(), "Некорректное имя подзадачи");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());

        Map<Integer,Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Подадачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        Task task = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        manager.createTask(task);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 20, 10, 0));
        manager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getPrioritizedTasks();
        assertEquals(expectedTasks.size(), 2);
    }
}