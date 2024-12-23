package server.handlers;

import classes.tasks.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler {
    HttpExchange exchange;

    public TasksHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    protected void processGet(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleGetTasks(exchange);
        if (move.length == 3) response = handleGetTaskById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handlePostTask(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleDelTaskById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetTasks(HttpExchange exchange) throws IOException {
        String response;
        if (taskManager.getTaskList() != null) {
            response = gson.toJson(taskManager.getTaskList());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }


    private String handleGetTaskById(HttpExchange exchange) throws IOException {
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

    private String handlePostTask(HttpExchange exchange) throws IOException {
        String response;
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);
        try {
            taskManager.pushTask(task);
        } catch (IllegalArgumentException e) {
            response = e.getMessage();
            exchange.sendResponseHeaders(406, response.getBytes().length);
            return response;
        }
        response = task.toString();
        exchange.sendResponseHeaders(406, response.getBytes().length);

        return response;
    }


    private String handleDelTaskById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteTask(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

}
