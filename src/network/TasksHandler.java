package network;

import com.sun.net.httpserver.HttpExchange;
import impl.AddTaskException;
import impl.NotFoundException;
import service.TaskManager;
import task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (path.equals("/tasks")) {
                handleGetTasks(exchange);
            } else if (path.startsWith("/tasks/")) {
                handleGetTaskById(exchange);
            }
        } else if (method.equals("POST") && path.equals("/tasks")) {
            handleCreateOrUpdateTask(exchange);
        } else if (method.equals("DELETE") && path.startsWith("/tasks/")) {
            handleDeleteTaskById(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllTasks()));
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        try {
            sendText(exchange, gson.toJson(taskManager.getTaskById(id)));
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        taskManager.deleteTaskById(id);
        sendText(exchange, "Задача удалена");
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String body = reader.lines().collect(Collectors.joining("\n"));
        try {
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() == null) {
                taskManager.addTask(task);
            } else {
                taskManager.updateTask(task);
            }
            sendText(exchange, "Задача добавлена");
        } catch (AddTaskException e) {
            sendHasInteractions(exchange);
        }
    }
}
