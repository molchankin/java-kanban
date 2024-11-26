package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();


    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length < 3) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(pathParts[2]));
    }

    protected void sendText(HttpExchange exchange, String responseString) throws IOException {
        byte[] response = responseString.getBytes();
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] notFoundResponse = "Не найдено".getBytes();
        exchange.sendResponseHeaders(404, notFoundResponse.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(notFoundResponse);
        }
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] hasInteractionsResponse = "Задача пересекается с уже существующими".getBytes();
        exchange.sendResponseHeaders(404, hasInteractionsResponse.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(hasInteractionsResponse);
        }
    }
}
