package server.hedlers;

import classes.enums.Endpoint;
import classes.tasks.Epic;
import classes.tasks.Subtask;
import classes.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import controllers.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager = new InMemoryTaskManager();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        //Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());
        Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());
        handleRequest(exchange, path, endpoint);
    }

    protected Endpoint getEndpoint(String path, String requestMethod) {

        return switch (requestMethod) {
            case "GET" -> Endpoint.GET;
            case "POST" -> Endpoint.POST;
            case "DELETE" -> Endpoint.DELETE;
            default -> Endpoint.UNKNOWN;
        };
    }

    protected void handleRequest(HttpExchange exchange, String path, Endpoint endpoint) throws IOException {
        switch (endpoint) {
            case GET -> processGet(exchange, path);
            case POST -> processPost(exchange, path);
            case DELETE -> processDelete(exchange, path);
            default -> handleNotFound(exchange);
        }
    }

    protected void processGet(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";

        switch (move[1]) {
            case "task" -> {
                if (move.length == 2) response = handleGetTasks(exchange);
                if (move.length == 3) response = handleGetTaskById(exchange);
            }

            case "epics" -> {
                if (move.length == 2) response = handleGetEpics(exchange);
                if (move.length == 3) response = handleGetTaskById(exchange);
                if (move.length == 4 && move[3].equals("subtasks")) response = handleGetEpicsSubsById(exchange);
            }

            case "subtasks" -> {
                if (move.length == 2) response = handleGetSubtasks(exchange);
                if (move.length == 3) response = handleGetTaskById(exchange);
            }

            case "history" -> {
                response = handleGetHistory(exchange);
            }

            case "prioritized" -> {
                response = handleGetPrioritized(exchange);
            }

            default -> {
                handleBadRequest(exchange, response);
            }
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected void processPost(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";

        switch (move[1]) {
            case "task" -> {
                if (move.length == 2) response = handlePostTask(exchange);
            }

            case "epics" -> {
                if (move.length == 2) response = handlePostEpic(exchange);
            }

            case "subtasks" -> {
                if (move.length == 2) response = handlePostSub(exchange);
            }

            default -> {
                handleBadRequest(exchange, response);
            }
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected void processDelete(HttpExchange exchange, String path) throws IOException {
        String[] move = path.split("/");
        String response = "Not Found";

        switch (move[1]) {
            case "task" -> {
                if (move.length == 2) response = handleDelTaskById(exchange);
            }

            case "epics" -> {
                if (move.length == 2) response = handleDelEpicById(exchange);
            }

            case "subtasks" -> {
                if (move.length == 2) response = handleDelSubById(exchange);
            }

            default -> {
                handleBadRequest(exchange, response);
            }
        }
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

    private String handleGetHistory(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (taskManager.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(taskManager.getHistory());
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

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleNotFound(HttpExchange exchange) throws IOException {
        String response = "Unknown request";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleBadRequest(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(400, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
