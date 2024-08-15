package http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    public void handle(HttpExchange exchange) {
        System.out.println("Началась обработка Эпика");

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] pathParts = path.split("/");
        try {
            switch (method) {
                case "GET":

                    if (pathParts.length == 2) {
                        writeResponse(exchange, getGson().toJson(taskManager.getAllEpics()), 200);

                    } else if (pathParts.length == 3) {

                        writeResponse(exchange, getGson().toJson(taskManager.getEpicById(getIdFromPath(pathParts[2]))), 200);
                    } else if (pathParts.length > 3) {

                        writeResponse(exchange, getGson().toJson(taskManager.getEpicSubtasks(taskManager.getEpicById(getIdFromPath(pathParts[2])))), 200);
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
                    }

                    break;
                case "DELETE":

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
                    break;
            }
        } catch (Exception e) {
            exception(exchange, e);
        }
    }
}