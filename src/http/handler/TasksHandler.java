package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.Task;
import service.TaskManager;

import java.nio.charset.StandardCharsets;


public class TasksHandler extends BaseHttpHandler {


    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Началась обработка Задачи");
        try (exchange) {

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            try {
                switch (method) {
                    case "GET":
                        if (pathParts.length == 2) {
                            writeResponse(exchange, getGson().toJson(taskManager.getAllTasks()), 200);
                        } else if (pathParts.length == 3) {
                            writeResponse(exchange, getGson().toJson(taskManager.getTaskById(getIdFromPath(pathParts[2]))), 200);
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
                        }
                        break;
                    case "DELETE":
                        int id = getIdFromPath(pathParts[2]);

                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.removeTaskById(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception exception) {
                exception(exchange, exception);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}