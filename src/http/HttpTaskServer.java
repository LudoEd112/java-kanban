package http;

import com.sun.net.httpserver.HttpServer;
import http.handler.EpicHandler;
import http.handler.TasksHandler;
import http.handler.SubtasksHandler;
import http.handler.HistoryHandler;
import http.handler.PrioritizedHandler;
import model.Epic;
import model.Statuses;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class HttpTaskServer {
    private static final int PORT_DEFAULT = 8080;
    private final int port;
    private HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer() {
        this(PORT_DEFAULT);
    }

    public HttpTaskServer(int port) {
        this(Managers.getDefault(), port);
    }

    public HttpTaskServer(TaskManager taskManager) {
        this(taskManager, PORT_DEFAULT);
    }

    public HttpTaskServer(TaskManager taskManager, int port) {
        this.taskManager = taskManager;
        this.port = port;
    }

    public static void main(String[] args) {
        TaskManager taskManager1 = Managers.getDefault();

        Task task = new Task("NewTask1", "NewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        taskManager1.createTask(task);

        HttpTaskServer server = new HttpTaskServer(taskManager1);
        server.start();

        Epic epic = new Epic("NewEpic1", "NewEpic1 description", Duration.of(15, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        taskManager1.createEpic(epic);

        Subtask subTask = new Subtask("NewSubtask1", "NewSubtask1 description", Statuses.NEW, epic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        taskManager1.createSubTask(subTask);

    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localHost", PORT_DEFAULT), 0);
            server.start();
            System.out.println("Сервер запущен. Порт: " + port);
            this.server.createContext("/tasks", new TasksHandler(taskManager));
            this.server.createContext("/epics", new EpicHandler(taskManager));
            this.server.createContext("/subtasks", new SubtasksHandler(taskManager));
            this.server.createContext("/history", new HistoryHandler(taskManager));
            this.server.createContext("/prioritized", new PrioritizedHandler(taskManager));

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен. Порт: " + port);
    }
}
