package server;

import com.sun.net.httpserver.HttpServer;
import classes.enums.Endpoint;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

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
        httpServer.createContext("/epics", new BaseHttpHandler());
        httpServer.createContext("/subtasks", new BaseHttpHandler());
        httpServer.createContext("/history", new BaseHttpHandler());
        httpServer.createContext("/prioritized", new BaseHttpHandler());
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
    }
}
