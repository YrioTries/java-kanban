package server.hedlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {
    HttpExchange exchange;

    public TasksHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

            @Override
            protected void processGet(HttpExchange exchange, String path) throws IOException {

            }

            @Override
            protected void processPost(HttpExchange exchange, String path) throws IOException {


            }

            @Override
            protected void processDelete(HttpExchange exchange, String path) throws IOException {

            }
}
