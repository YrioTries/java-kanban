package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;

    public static void main(String[] args, HttpExchange exchange, int port) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);

        httpServer.createContext("/tasks", new TasksHandler(exchange));
        httpServer.createContext("/epics", new EpicsHandler(exchange));
        httpServer.createContext("/subtask", new SubtasksHandler(exchange));
        httpServer.createContext("/history", new HistoryHandler(exchange));
        httpServer.createContext("/prioritized", new PrioritizedHandler(exchange));

        httpServer.start();

        System.out.println("Starting server on port " + port);
    }

    public static void stop() {
        httpServer.stop(0);
    }
}
