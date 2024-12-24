package server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class PrioritizedHandler extends BaseHttpHandler {

    HttpExchange exchange;

    public PrioritizedHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    protected void processGet(HttpExchange exchange, String path) throws IOException {
        String response = handleGetPrioritized(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetPrioritized(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }
}
