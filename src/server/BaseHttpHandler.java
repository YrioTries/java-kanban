package server;

import classes.enums.Endpoint;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // считываем тело запроса и преобразуем в строку
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String response = handleRequest(exchange, endpoint, body);

        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected String handleRequest(HttpExchange exchange, Endpoint endpoint, String body) {
        // Обработка запроса в зависимости от эндпоинта
        switch (endpoint) {
            case GET_TASKS:
                return handleGetTasks();
            case GET_TASKS_ID:
                return handleGetTaskById(exchange);
            case POST_TASKS:
                return handlePostTasks(body);
            // Добавьте обработку для других эндпоинтов
            default:
                return "Unknown endpoint";
        }
    }

    private String handleGetTasks() {
        // Логика для получения всех задач
        return "Get all tasks";
    }

    private String handleGetTaskById(HttpExchange exchange) {
        // Логика для получения задачи по ID
        String id = exchange.getRequestURI().getPath().split("/")[2];
        return "Get task by ID: " + id;
    }

    private String handlePostTasks(String body) {
        // Логика для создания новой задачи
        return "Create task with body: " + body;
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET":
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
                if (pathParts[1].equals("tasks") && pathParts.length == 2) return Endpoint.POST_TASKS;
                if (pathParts[1].equals("epics") && pathParts.length == 2) return Endpoint.POST_EPICS;
                if (pathParts[1].equals("subtasks") && pathParts.length == 2) return Endpoint.POST_SUBTASKS;
                return Endpoint.UNKNOWN;
            case "DELETE":
                if (pathParts[1].equals("tasks") && pathParts.length == 3) return Endpoint.DELETE_TASKS_ID;
                if (pathParts[1].equals("epics") && pathParts.length == 3) return Endpoint.DELETE_EPICS_ID;
                if (pathParts[1].equals("subtasks") && pathParts.length == 3) return Endpoint.DELETE_SUBTASKS_ID;
                return Endpoint.UNKNOWN;
            default:
                return Endpoint.UNKNOWN;
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
