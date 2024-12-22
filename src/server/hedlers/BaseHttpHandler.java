package server.hedlers;

import classes.enums.Endpoint;
import classes.tasks.Epic;
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
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String response = handleRequest(exchange, endpoint, body);

        sendResponse(exchange, response);
    }

    protected String handleRequest(HttpExchange exchange, Endpoint endpoint, String body) throws IOException {
        return switch (endpoint) {
            case GET_TASKS -> handleGetTasks(exchange);
            case GET_EPICS -> handleGetEpics(exchange);
            case GET_SUBTASKS -> handleGetSubtasks(exchange);
            case GET_TASKS_ID, GET_EPICS_ID, GET_SUBTASKS_ID -> handleGetTaskById(exchange);
            case GET_EPICS_ID_SUBTUSKS -> handleGetEpicsSubsById(exchange);
            case GET_HYSTORY -> gson.toJson(taskManager.getHistory());
            case GET_PRIORITIZED -> gson.toJson(taskManager.getPrioritizedTasks());
            case POST_TASKS -> handlePostTask(exchange, body);
            case POST_EPICS -> handlePostEpic(exchange, body);
            case POST_SUBTASKS -> handlePostSubtask(exchange, body);
            case DELETE_TASKS_ID -> handleDelTaskById(exchange);
            case DELETE_EPICS_ID -> handleDelEpicById(exchange);
            case DELETE_SUBTASKS_ID -> handleDelSubById(exchange);
            default -> "Unknown endpoint";
        };
    }

    private String handleGetTasks(HttpExchange exchange) throws IOException {
        return handleGetList(exchange, taskManager.getTaskList());
    }

    private String handleGetEpics(HttpExchange exchange) throws IOException {
        return handleGetList(exchange, taskManager.getEpicList());
    }

    private String handleGetSubtasks(HttpExchange exchange) throws IOException {
        return handleGetList(exchange, taskManager.getSubtaskList());
    }

    private String handleGetList(HttpExchange exchange, Object list) throws IOException {
        if (list != null) {
            String response = gson.toJson(list);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handleGetTaskById(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId;
        try {
            postId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return handleBadRequest(exchange, "Invalid ID format");
        }

        if (taskManager.getTaskMaster().containsKey(postId)) {
            String response = gson.toJson(taskManager.serchTask(postId));
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handleGetEpicsSubsById(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId;
        try {
            postId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return handleBadRequest(exchange, "Invalid ID format");
        }

        if (taskManager.getTaskMaster().containsKey(postId)) {
            Epic epic = (Epic) taskManager.serchTask(postId);
            String response = gson.toJson(epic.getSubMap().values());
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handleDelTaskById(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId;
        try {
            postId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return handleBadRequest(exchange, "Invalid ID format");
        }

        if (taskManager.getTaskMaster().containsKey(postId)) {
            String response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteTask(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handleDelEpicById(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId;
        try {
            postId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return handleBadRequest(exchange, "Invalid ID format");
        }

        if (taskManager.getTaskMaster().containsKey(postId)) {
            String response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteEpic(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handleDelSubById(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId;
        try {
            postId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return handleBadRequest(exchange, "Invalid ID format");
        }

        if (taskManager.getTaskMaster().containsKey(postId)) {
            String response = gson.toJson(taskManager.serchTask(postId));
            taskManager.deleteSubtask(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            return response;
        } else {
            return handleNotFound(exchange, "Not Found");
        }
    }

    private String handlePostTask(HttpExchange exchange, String body) throws IOException {
        // Реализуйте логику создания задачи здесь
        return "Task created";
    }

    private String handlePostEpic(HttpExchange exchange, String body) throws IOException {
        // Реализуйте логику создания эпика здесь
        return "Epic created";
    }

    private String handlePostSubtask(HttpExchange exchange, String body) throws IOException {
        // Реализуйте логику создания подзадачи здесь
        return "Subtask created";
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET":
                if (taskManager.getTaskMaster() == null) return Endpoint.ERROR_404;
                if (pathParts[1].equals("tasks")) {
                    if (pathParts.length == 2) return Endpoint.GET_TASKS;
                    if (pathParts.length == 3) return Endpoint.GET_TASKS_ID;
                }
                if (pathParts[1].equals("epics")) {
                    if (pathParts.length == 2) return Endpoint.GET_EPICS;
                    if (pathParts.length == 3) return Endpoint.GET_EPICS_ID;
                    if (pathParts.length == 4 && pathParts[3].equals("subtasks")) return Endpoint.GET_EPICS_ID_SUBTUSKS;
                }
                if (pathParts[1].equals("subtasks")) {
                    if (pathParts.length == 2) return Endpoint.GET_SUBTASKS;
                    if (pathParts.length == 3) return Endpoint.GET_SUBTASKS_ID;
                }
                if (pathParts[1].equals("history")) return Endpoint.GET_HYSTORY;
                if (pathParts[1].equals("prioritized")) return Endpoint.GET_PRIORITIZED;
                return Endpoint.UNKNOWN;
            case "POST":
                if (taskManager.getTaskMaster() == null) return Endpoint.ERROR_404;
                if (pathParts[1].equals("tasks") && pathParts.length == 2) return Endpoint.POST_TASKS;
                if (pathParts[1].equals("epics") && pathParts.length == 2) return Endpoint.POST_EPICS;
                if (pathParts[1].equals("subtasks") && pathParts.length == 2) return Endpoint.POST_SUBTASKS;
                return Endpoint.UNKNOWN;
            case "DELETE":
                if (taskManager.getTaskMaster() == null) return Endpoint.ERROR_404;
                if (pathParts[1].equals("tasks") && pathParts.length == 3) return Endpoint.DELETE_TASKS_ID;
                if (pathParts[1].equals("epics") && pathParts.length == 3) return Endpoint.DELETE_EPICS_ID;
                if (pathParts[1].equals("subtasks") && pathParts.length == 3) return Endpoint.DELETE_SUBTASKS_ID;
                return Endpoint.UNKNOWN;
            default:
                return Endpoint.UNKNOWN;
        }
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleNotFound(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(404, message.getBytes().length);
        return message;
    }

    private String handleBadRequest(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(400, message.getBytes().length);
        return message;
    }
}
