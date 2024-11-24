package impl;


import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String header = "id,type,name,status,description,epic\n";
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private String toString(Task task) {
        String base = task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getProgressStatus() + "," + task.getDescription();
        if (task.getType() == TaskType.SUBTASK) {
            return base + "," + ((Subtask) task).getEpicId();
        }
        return base;
    }

    private Task fromString(String string) {
        String[] parts = string.split(",");
        if (parts.length < 5) {
            throw new ManagerSaveException("Неверный размер строки: " + string);
        }
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        switch (type) {
            case TASK:
                return new Task(parts[2], parts[4], ProgressStatus.valueOf(parts[3]), id);
            case SUBTASK:
                return new Subtask(parts[2], parts[4], ProgressStatus.valueOf(parts[3]), Integer.parseInt(parts[5]), id);
            case EPIC:
                return new Epic(parts[2], parts[4], id, ProgressStatus.valueOf(parts[3]));
            default:
                throw new ManagerSaveException("Неверный тип задачи: " + string);
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(header);
            for (Task task : tasks.values()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : epics.values()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        String text = Files.readString(file.toPath()).replaceFirst(header, "");
        String[] lines = text.split("\n");
        for (String line : lines) {
            try {
                Task task = manager.fromString(line);
                switch (task.getType()) {
                    case TASK:
                        manager.addTaskFromFile(task);
                        break;
                    case EPIC:
                        manager.addEpicFromFile((Epic) task);
                        break;
                    case SUBTASK:
                        manager.addSubtaskFromFile((Subtask) task);
                        break;
                }
                manager.setIdCounter(task.getId());
            } catch (ManagerSaveException ignored) {
            }
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }
}
