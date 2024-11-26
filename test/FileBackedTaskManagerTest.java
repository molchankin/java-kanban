import impl.FileBackedTaskManager;
import impl.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void checkFromStringException() {
        assertThrows(ManagerSaveException.class, () -> taskManager.fromString("Сломанная строка"));
    }

    @Test
    public void checkFromStringDoesNotThrowException() {
        assertDoesNotThrow(() -> taskManager.fromString("1,TASK,task1,NEW,description1,PT30M,null"));
    }

    @Test
    public void checkEmptyFileLoad() throws IOException {
        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    public void checkFileSaveAndLoad() throws IOException {
        Task task3 = new Task("UpdatedTask1", "UpdatedDescription1", ProgressStatus.IN_PROGRESS);
        Task task2 = new Task("UpdatedTask2", "UpdatedDescription2", ProgressStatus.NEW);
        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.addTask(task3);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        TaskManager anotherTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(anotherTaskManager.getAllTasks().contains(task3));
        assertTrue(anotherTaskManager.getAllTasks().contains(task2));
        assertTrue(anotherTaskManager.getAllEpics().contains(epic1));
    }
}
