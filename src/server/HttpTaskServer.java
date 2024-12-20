package server;

import com.sun.net.httpserver.HttpServer;
import classes.enums.Endpoint;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureEndpoints();
        httpServer.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(160);
        }
    }

    private void configureEndpoints() {
        httpServer.createContext("/tasks", new BaseHttpHandler());
        httpServer.createContext("/tasks/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);
            new BaseHttpHandler().handle(exchange);
        });
        httpServer.createContext("/epics", new BaseHttpHandler());
        httpServer.createContext("/epics/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);
            new BaseHttpHandler().handle(exchange);
        });
        httpServer.createContext("/epics//subtasks", new BaseHttpHandler());
        httpServer.createContext("/subtasks", new BaseHttpHandler());
        httpServer.createContext("/subtasks/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);
            new BaseHttpHandler().handle(exchange);
        });
        httpServer.createContext("/history", new BaseHttpHandler());
        httpServer.createContext("/prioritized", new BaseHttpHandler());
    }
}
