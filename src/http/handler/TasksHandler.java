package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.InvalidInputException;
import exceptions.NotFoundException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TasksHandler extends BaseHttpHandler {


    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка Задачи");
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            switch (method) {
                case "GET":
                    try {
                        if (pathParts.length == 2) {
                            writeResponse(exchange, getGson().toJson(taskManager.getAllTasks()), 200);
                        } else if (pathParts.length == 3) {
                            writeResponse(exchange, getGson().toJson(taskManager.getTaskById(getIdFromPath(pathParts[2]))), 200);
                        }
                    } catch (InvalidInputException e) {
                        writeResponse(exchange, "Ошибка при чтении тела запроса", 400);
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = getGson().fromJson(body, Task.class);
                    try {
                        taskManager.getTaskById(task.getId());
                        taskManager.updateTask(task);
                        writeResponse(exchange, "Задача обновлена", 201);
                    } catch (NotFoundException e) {
                        taskManager.createTask(task);
                        writeResponse(exchange, "Задача создана", 201);
                    } catch (InvalidInputException e) {
                        writeResponse(exchange, "Ошибка при чтении тела запроса", 400);
                    }
                    break;
                case "DELETE":
                    try {
                        int id = getIdFromPath(pathParts[2]);
                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.removeTaskById(id);
                            sendResponseCode(exchange);
                        }
                    } catch (InvalidInputException e) {
                        writeResponse(exchange, "Ошибка при чтении тела запроса", 400);
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            writeResponse(exchange, "Ошибка ввода-вывода", 500);
        }
    }
}