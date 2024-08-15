package http.handler;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;


public class PrioritizedHandler extends BaseHttpHandler {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            try {
                if (exchange.getRequestMethod().equals("GET")) {
                    writeResponse(exchange, getGson().toJson(taskManager.getPrioritizedTasks()), 200);
                } else
                    writeResponse(exchange, "Ошибка при обработке запроса", 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
