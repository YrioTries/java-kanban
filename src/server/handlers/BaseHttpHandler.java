package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.InMemoryTaskManager;
import controllers.interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

public class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager = new InMemoryTaskManager();
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        getEndpoint(exchange, path, exchange.getRequestMethod());
    }

    protected void getEndpoint(HttpExchange exchange, String path, String requestMethod) throws IOException {
        switch (requestMethod) {
            case "GET" -> processGet(exchange, path);
            case "POST" -> processPost(exchange, path);
            case "DELETE" -> processDelete(exchange, path);
            default -> handleNotFound(exchange);
        };
    }

    protected void processGet(HttpExchange exchange, String path) throws IOException {
        handleBadRequest(exchange, "Not override method GET");
    }

    protected void processPost(HttpExchange exchange, String path) throws IOException {
        handleBadRequest(exchange, "Not override method POST");
    }

    protected void processDelete(HttpExchange exchange, String path) throws IOException {
        handleBadRequest(exchange, "Not override method DELETE");
    }

    protected void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected void handleNotFound(HttpExchange exchange) throws IOException {
        String response = "Unknown request";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    protected void handleBadRequest(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(400, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
