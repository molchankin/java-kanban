import impl.AddTaskException;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    @Test
    public void checkAddTask() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        assertNotNull(taskManager.getTaskById(task1.getId()));
        assertEquals(taskManager.getPrioritizedTasks().size(), 1);
        assertEquals(taskManager.getPrioritizedTasks().getFirst(), task1);
    }

    @Test
    public void checkDeleteTask() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        taskManager.deleteTaskById(task1.getId());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void checkUpdateTask() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "Description2", ProgressStatus.NEW, task1.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.updateTask(task2);
        assertEquals(taskManager.getAllTasks().size(), 1);
        assertEquals(taskManager.getPrioritizedTasks().size(), 1);
        assertEquals(taskManager.getAllTasks().getFirst(), task2);
    }

    @Test
    public void checkAddEpic() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        assertNotNull(taskManager.getEpicById(epic1.getId()));
        assertEquals(taskManager.getAllEpics().getFirst(), epic1);
    }

    @Test
    public void checkDeleteEpic() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        taskManager.deleteEpicById(epic1.getId());
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    public void checkUpdateEpic() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("epic2", "Description2", epic1.getId());
        taskManager.updateEpic(epic2);
        assertEquals(taskManager.getAllEpics().size(), 1);
        assertEquals(taskManager.getAllEpics().getFirst(), epic2);
    }

    @Test
    public void checkAddSubtask() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "Description1", ProgressStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        assertNotNull(taskManager.getSubtaskById(subtask1.getId()));
        assertEquals(taskManager.getAllSubtasks().getFirst(), subtask1);
        assertEquals(taskManager.getSubtasksFromEpic(epic1.getId()).getFirst(), subtask1);
    }

    @Test
    public void checkDeleteSubtask() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "Description1", ProgressStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(epic1.getSubtasksIds().isEmpty());
    }

    @Test
    public void checkUpdateSubtask() {
        Epic epic1 = new Epic("epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "Description1", ProgressStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "Description2", ProgressStatus.DONE, subtask1.getId(), epic1.getId());
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getAllSubtasks().size(), 1);
        assertEquals(epic1.getSubtasksIds().size(), 1);
        assertEquals(taskManager.getSubtasksFromEpic(epic1.getId()).getFirst(), subtask2);
    }

    @Test
    public void checkEpicProgressStatusIsNew() {
        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description1", ProgressStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "Description2", ProgressStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(ProgressStatus.NEW, epic1.getProgressStatus());
    }

    @Test
    public void checkEpicProgressStatusIsDone() {
        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description1", ProgressStatus.DONE, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "Description2", ProgressStatus.DONE, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(ProgressStatus.DONE, epic1.getProgressStatus());
    }

    @Test
    public void checkEpicProgressStatusIfSubtasksAreNewAndDone() {
        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description1", ProgressStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "Description2", ProgressStatus.DONE, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(ProgressStatus.IN_PROGRESS, epic1.getProgressStatus());
    }

    @Test
    public void checkEpicProgressStatusIsInProgress() {
        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description1", ProgressStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "Description2", ProgressStatus.IN_PROGRESS, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(ProgressStatus.IN_PROGRESS, epic1.getProgressStatus());
    }

    @Test
    public void checkAddIntersectedTasks() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "Description2", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        assertThrows(AddTaskException.class, () -> taskManager.addTask(task2));
    }

    @Test
    public void checkAddNotIntersectedTasks() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW, Duration.ofMinutes(20), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "Description2", ProgressStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plus(Duration.ofMinutes(30)));
        assertDoesNotThrow(() -> taskManager.addTask(task2));
    }

    @Test
    public void checkHistoryIsEmpty() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW);
        taskManager.addTask(task1);
        taskManager.getTaskById(task1.getId());
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void checkHistoryIsNotDuplicated() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "Description2", ProgressStatus.NEW);
        taskManager.addTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task1.getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    private void initHistory() {
        Task task1 = new Task("task1", "Description1", ProgressStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "Description2", ProgressStatus.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("task3", "Description3", ProgressStatus.NEW);
        taskManager.addTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
    }

    @Test
    public void checkDeleteFirstFromHistory() {
        initHistory();
        taskManager.deleteTaskById(taskManager.getHistory().getFirst().getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    public void checkDeleteMiddleFromHistory() {
        initHistory();
        taskManager.deleteTaskById(taskManager.getHistory().get(1).getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    public void checkDeleteLastFromHistory() {
        initHistory();
        taskManager.deleteTaskById(taskManager.getHistory().getLast().getId());
        assertEquals(2, taskManager.getHistory().size());
    }

}
