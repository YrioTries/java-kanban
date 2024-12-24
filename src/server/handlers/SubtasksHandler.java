package server.handlers;

import classes.tasks.Subtask;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler {

    HttpExchange exchange;

    public SubtasksHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    protected void processGet(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleGetSubtasks(exchange);
        if (move.length == 3) response = handleGetSubById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handlePostSub(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleDelSubById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetSubtasks(HttpExchange exchange) throws IOException {
        String response;
        if (taskManager.getTaskList() != null) {
            response = gson.toJson(taskManager.getSubtaskList());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handleGetSubById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.serchTask(postId));
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handlePostSub(HttpExchange exchange) throws IOException {
        String response;
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Subtask sub = gson.fromJson(body, Subtask.class);
        try {
            taskManager.pushSub(sub);
        } catch (IllegalArgumentException e) {
            response = e.getMessage();
            exchange.sendResponseHeaders(406, response.getBytes().length);
            return response;
        }
        response = sub.toString();
        exchange.sendResponseHeaders(406, response.getBytes().length);

        return response;
    }

    private String handleDelSubById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteSubtask(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }
}
