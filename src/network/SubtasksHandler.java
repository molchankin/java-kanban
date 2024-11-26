package network;

import com.sun.net.httpserver.HttpExchange;
import impl.AddTaskException;
import impl.NotFoundException;
import service.TaskManager;
import task.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (path.equals("/subtasks")) {
                handleGetSubtasks(exchange);
            } else if (path.startsWith("/subtasks/")) {
                handleGetSubtaskById(exchange);
            }
        } else if (method.equals("POST") && path.equals("/subtasks")) {
            handleCreateOrUpdateSubtask(exchange);
        } else if (method.equals("DELETE") && path.startsWith("/subtasks/")) {
            handleDeleteSubtaskById(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()));
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        try {
            sendText(exchange, gson.toJson(taskManager.getSubtaskById(id)));
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        taskManager.deleteSubtaskById(id);
        sendText(exchange, "Подзадача удалена");
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String body = reader.lines().collect(Collectors.joining("\n"));
        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask.getId() == null) {
                taskManager.addSubtask(subtask);
            } else {
                taskManager.updateSubtask(subtask);
            }
            sendText(exchange, "Подзадача добавлена");
        } catch (AddTaskException e) {
            sendHasInteractions(exchange);
        }
    }
}
