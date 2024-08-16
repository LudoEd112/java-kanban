package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.InvalidInputException;
import exceptions.NotFoundException;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка Эпика");
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            switch (method) {
                case "GET":
                    try {
                        if (pathParts.length == 2) {
                            writeResponse(exchange, getGson().toJson(taskManager.getAllEpics()), 200);

                        } else if (pathParts.length == 3) {

                            writeResponse(exchange, getGson().toJson(taskManager.getEpicById(getIdFromPath(pathParts[2]))), 200);
                        } else if (pathParts.length > 3) {

                            writeResponse(exchange, getGson().toJson(taskManager.getEpicSubtasks(taskManager.getEpicById(getIdFromPath(pathParts[2])))), 200);
                        }
                    } catch (InvalidInputException e) {
                            writeResponse(exchange, "Ошибка при чтении тела запроса", 400);
                        }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = getGson().fromJson(body, Epic.class);
                    if (epic.getEpicSubtasks() == null) {
                        epic.setEpicSubtasks(new ArrayList<>());
                    }
                    try {
                        taskManager.getEpicById(epic.getId());
                    } catch (NotFoundException e) {
                        taskManager.createEpic(epic);
                        writeResponse(exchange, "Эпик создан", 201);
                    } catch (InvalidInputException e) {
                        writeResponse(exchange, "Ошибка при чтении тела запроса", 400);
                    }

                    break;
                case "DELETE":
                    try {
                        int id = getIdFromPath(pathParts[2]);
                        if (id == -1) {
                            writeResponse(exchange, "Такого эпика нет", 404);
                        } else {
                            Epic epic2 = taskManager.getEpicById(id);
                            List<Subtask> listSubTasks = taskManager.getEpicSubtasks(epic2);
                            listSubTasks.clear();
                            taskManager.removeEpicById(id);
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