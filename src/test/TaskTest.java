package test;

import impl.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    public void tasksEqualToEachOther() {
        TaskManager taskManager = Managers.getDefaults();
        Task task = new Task("task", "Description", ProgressStatus.NEW, 1);
        Task task2 = new Task("task", "Description", ProgressStatus.NEW, 1);
        assertEquals(task, task2);
    }

    @Test
    public void epicsEqualToEachOther() {
        Epic epic = new Epic("Task.Epic", "Description");
        Epic epic2 = new Epic("Task.Epic", "Description");

        assertEquals(epic, epic2);
    }

    @Test
    public void subtaskEqualToEachOther() {
        TaskManager taskManager = Managers.getDefaults();
        Epic epic = new Epic("Task.Epic", "Description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Task.Subtask", "Description", ProgressStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Task.Subtask", "Description", ProgressStatus.NEW, epic.getId());
        assertEquals(subtask, subtask2);
    }

    @Test
    public void taskManagerAlwaysReturnsInitializedManager() {
        TaskManager taskManager = Managers.getDefaults();
        Assertions.assertNotNull(taskManager);
    }

    @Test
    public void checkAddTask() {
        TaskManager taskManager = Managers.getDefaults();
        Task task = new Task("task", "Description", ProgressStatus.NEW);
        Task task2 = new Task("task", "Description", ProgressStatus.NEW);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        assertEquals(task, taskManager.getTaskById(task.getId()));
        assertEquals(task2, taskManager.getTaskById(task2.getId()));
    }

    @Test
    public void checkTaskConstancy() {
        TaskManager taskManager = Managers.getDefaults();
        Task task1 = new Task("Task1", "Description1", ProgressStatus.NEW);
        taskManager.addTask(task1);
        Task updatedTask = taskManager.getTaskById(task1.getId());
        assertEquals(task1.getDescription(), updatedTask.getDescription());
        assertEquals(task1.getTitle(), updatedTask.getTitle());
    }

    @Test
    public void taskIdConflict() {
        TaskManager taskManager = Managers.getDefaults();
        Task task1 = new Task("Task1", "Description1", ProgressStatus.NEW);
        Task task2 = new Task("Task2", "Description2", ProgressStatus.NEW);
        Task task3 = new Task("Task3", "Description3", ProgressStatus.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        Assertions.assertNotEquals(task1.getId(), task2.getId());
        Assertions.assertNotEquals(task1.getId(), task3.getId());
    }

    @Test
    public void checkClearTaskMap() {
        TaskManager taskManager = Managers.getDefaults();
        Task task1 = new Task("Task1", "Description1", ProgressStatus.NEW);
        Task task2 = new Task("Task2", "Description2", ProgressStatus.NEW);
        Task task3 = new Task("Task3", "Description3", ProgressStatus.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void checkClearEpicMap() {
        TaskManager taskManager = Managers.getDefaults();
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        Epic epic3 = new Epic("Epic3", "Description3");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void checkClearSubtaskMap() {
        TaskManager taskManager = Managers.getDefaults();
        Epic epic = new Epic("Task.Epic", "Description");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1", "Description1", ProgressStatus.NEW, 0);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", ProgressStatus.NEW, 0);
        Subtask subtask3 = new Subtask("Subtask3", "Description3", ProgressStatus.NEW, 0);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    public void taskAddedByHistoryManager() {
        TaskManager taskManager = Managers.getDefaults();
        Task task1 = new Task("Task1", "Description1", ProgressStatus.NEW);
        taskManager.addTask(task1);
        taskManager.getTaskById(task1.getId());
        Task task3 = new Task("UpdatedTask1", "UpdatedDescription1", ProgressStatus.IN_PROGRESS);
        taskManager.updateTask(task3);
        assertEquals(task1, taskManager.getHistory().getFirst());
    }

    @Test
    public void checkEmptyFileLoad() throws IOException {
        File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(fileBackedTaskManager.getAllTasks().isEmpty());
        assertTrue(fileBackedTaskManager.getAllEpics().isEmpty());
        assertTrue(fileBackedTaskManager.getAllSubtasks().isEmpty());
    }

    @Test
    public void checkFileSaveAndLoad() throws IOException {
        Task task3 = new Task("UpdatedTask1", "UpdatedDescription1", ProgressStatus.IN_PROGRESS);
        Task task2 = new Task("UpdatedTask2", "UpdatedDescription2", ProgressStatus.NEW);
        Epic epic1 = new Epic("Epic1", "Description1");
        TaskManager taskManager = Managers.getFileBackedTaskManager();
        taskManager.addTask(task3);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        TaskManager anotherTaskManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));
        assertTrue(anotherTaskManager.getAllTasks().contains(task3));
        assertTrue(anotherTaskManager.getAllTasks().contains(task2));
        assertTrue(anotherTaskManager.getAllEpics().contains(epic1));
    }

}