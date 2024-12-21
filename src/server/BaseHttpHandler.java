package server;

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
    TaskManager test = new InMemoryTaskManager();

    GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    Gson gson = gsonBuilder.create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // считываем тело запроса и преобразуем в строку
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String response = handleRequest(exchange, endpoint, body);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected String handleRequest(HttpExchange exchange, Endpoint endpoint, String body) throws IOException {
        return switch (endpoint) {
            case GET_TASKS -> handleGetTasks(exchange);
            case GET_EPICS -> handleGetEpics(exchange);
            case GET_SUBTASKS -> handleGetSubtasks(exchange);
            case GET_TASKS_ID, GET_EPICS_ID, GET_SUBTASKS_ID -> handleGetTaskById(exchange);
            case GET_EPICS_ID_SUBTUSKS -> handleGetEpicsSubsById(exchange);
            case GET_HYSTORY -> gson.toJson(test.getHistory());
            case GET_PRIORITIZED -> gson.toJson(test.getPrioritizedTasks());
            case POST_TASKS -> "";
            case POST_EPICS -> " ";
            case POST_SUBTASKS -> "  ";
            case DELETE_TASKS_ID -> handleDelTaskById(exchange);
            case DELETE_EPICS_ID -> handleDelEpicById(exchange);
            case DELETE_SUBTASKS_ID -> handleDelSubById(exchange);
            default -> "Unknown endpoint";
        };
    }

    private String handleGetTasks(HttpExchange exchange) throws IOException {
        String response;
        if (test.getTaskList() != null) {
            response = gson.toJson(test.getTaskList());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handleGetEpics(HttpExchange exchange) throws IOException {
        String response;
        if (test.getTaskList() != null) {
            response = gson.toJson(test.getEpicList());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handleGetSubtasks(HttpExchange exchange) throws IOException {
        String response;
        if (test.getTaskList() != null) {
            response = gson.toJson(test.getSubtaskList());
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

        if (test.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(test.serchTask(postId));
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

        if (test.getTaskMaster().containsKey(postId)) {
            Epic epic = (Epic) test.serchTask(postId);
            response = gson.toJson(epic.getSubMap().values());
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private String handleDelTaskById(HttpExchange exchange) throws IOException {
        String response;
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int postId = Integer.parseInt(id);

        if (test.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(test.serchTask(postId));
            test.deleteTask(postId);
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

        if (test.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(test.serchTask(postId));
            test.deleteEpic(postId);
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

        if (test.getTaskMaster().containsKey(postId)) {
            response = gson.toJson(test.serchTask(postId));
            test.deleteSubtask(postId);
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = "Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
        }
        return response;
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET":
                if (test.getTaskMaster() == null) return Endpoint.ERROR_404;
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
                if (test.getTaskMaster() == null) return Endpoint.ERROR_404;
                if (pathParts[1].equals("tasks") && pathParts.length == 2) return Endpoint.POST_TASKS;
                if (pathParts[1].equals("epics") && pathParts.length == 2) return Endpoint.POST_EPICS;
                if (pathParts[1].equals("subtasks") && pathParts.length == 2) return Endpoint.POST_SUBTASKS;
                return Endpoint.UNKNOWN;
            case "DELETE":
                if (test.getTaskMaster() == null) return Endpoint.ERROR_404;
                if (pathParts[1].equals("tasks") && pathParts.length == 3) return Endpoint.DELETE_TASKS_ID;
                if (pathParts[1].equals("epics") && pathParts.length == 3) return Endpoint.DELETE_EPICS_ID;
                if (pathParts[1].equals("subtasks") && pathParts.length == 3) return Endpoint.DELETE_SUBTASKS_ID;
                return Endpoint.UNKNOWN;
            default:
                return Endpoint.UNKNOWN;
        }
    }

//    protected void sendText(HttpExchange h, String text) throws IOException {
//        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
//        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
//        h.sendResponseHeaders(200, resp.length);
//        h.getResponseBody().write(resp);
//        h.close();
//    }
//
//    protected void sendNotFound(HttpExchange h, String text) throws IOException {
//        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
//        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
//        h.sendResponseHeaders(404, resp.length);
//        h.getResponseBody().write(resp);
//        h.close();
//    }
//
//    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
//        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
//        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
//        h.sendResponseHeaders(406, resp.length);
//        h.getResponseBody().write(resp);
//        h.close();
//    }
}
