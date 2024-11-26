package network;

import com.sun.net.httpserver.HttpExchange;
import impl.AddTaskException;
import impl.NotFoundException;
import service.TaskManager;
import task.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (path.equals("/epics")) {
                handleGetEpics(exchange);
            } else if (path.endsWith("/subtasks")) {
                handleGetSubtasksByEpicId(exchange);
            } else if (path.startsWith("/epics/")) {
                handleGetEpicById(exchange);
            }
        } else if (method.equals("POST") && path.equals("/epics")) {
            handleCreateOrUpdateEpic(exchange);
        } else if (method.equals("DELETE") && path.startsWith("/epics/")) {
            handleDeleteEpicById(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllEpics());
        sendText(exchange, response);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        try {
            sendText(exchange, gson.toJson(taskManager.getEpicById(id)));
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }

    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        taskManager.deleteEpicById(id);
        sendText(exchange, "Задача удалена");
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String body = reader.lines().collect(Collectors.joining("\n"));
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic.getId() == null) {
                taskManager.addEpic(epic);
            } else {
                taskManager.updateEpic(epic);
            }
            sendText(exchange, "Эпик добавлен");
        } catch (AddTaskException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleGetSubtasksByEpicId(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getTaskId(exchange);
        if (idOpt.isEmpty()) {
            sendNotFound(exchange);
            return;
        }
        int id = idOpt.get();
        sendText(exchange, gson.toJson(taskManager.getSubtasksFromEpic(id)));
    }
}
