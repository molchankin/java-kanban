import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private Integer idCounter = 0;

    public Integer getIdCounter() {
        idCounter++;
        return idCounter;
    }

    void deleteAllTasks() {
        tasks.clear();
    }

    Collection<Task> getAllTasks() {
        return tasks.values();
    }

    Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    void deleteEpicById(Integer id) {
        epics.remove(id);
    }

    Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    Collection<Epic> getAllEpics() {
        return epics.values();
    }

    void deleteAllEpics() {
        epics.clear();
    }

    void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

}
