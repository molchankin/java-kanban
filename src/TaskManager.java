import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private Integer idCounter = 0; //счетчик уникальных ID

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

}
