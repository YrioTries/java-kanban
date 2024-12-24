package server.handlers;

import classes.tasks.Epic;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {

    HttpExchange exchange;

    public EpicsHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    protected void processGet(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleGetEpics(exchange);
        if (move.length == 3) response = handleGetEpicById(exchange);
        if (move.length == 4 && move[3].equals("subtasks")) response = handleGetEpicsSubsById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handlePostEpic(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";
        if (move.length == 2) response = handleDelEpicById(exchange);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetEpics(HttpExchange exchange) throws IOException {
        String response;
        if (taskManager.getTaskList() != null) {
            response = gson.toJson(taskManager.getEpicList());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handleGetEpicById(HttpExchange exchange) throws IOException {
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

    private String handleGetEpicsSubsById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            Epic epic = (Epic) taskManager.serchTask(postId);
            response = gson.toJson(epic.getSubMap().values());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handlePostEpic(HttpExchange exchange) throws IOException {
        String response;
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Epic epic = gson.fromJson(body, Epic.class);
        try {
            taskManager.pushEpic(epic);
        } catch (IllegalArgumentException e) {
            response = e.getMessage();
            exchange.sendResponseHeaders(406, response.getBytes().length);
            return response;
        }
        response = epic.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        return response;
    }

    private String handleDelEpicById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteEpic(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }


}
