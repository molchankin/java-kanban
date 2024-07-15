import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void deleteAllTasks();

    List<Task> getAllTasks();

    Task getTaskById(Integer id);

    void addTask(Task task);

    void deleteTaskById(Integer id);

    void updateTask(Task task);

    void addEpic(Epic epic);

    void deleteEpicById(Integer id);

    Epic getEpicById(Integer id);

    void addSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);

    void deleteAllSubtasks();

    void updateSubtask(Subtask subtask);

    List<Subtask> getSubtasksFromEpic(Integer epicId);

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    void deleteAllEpics();

    Subtask getSubtaskById(Integer id);

    void updateEpic(Epic epic);

    List<Task> getHistory();
    static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksFromEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
