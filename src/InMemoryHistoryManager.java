import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> viewedTasks = new ArrayList<>();
    private final static int MAX_VIEWED= 10;

    @Override
    public List<Task> getViewedTasks() {
        return viewedTasks;
    }

    @Override
    public void addTaskToViewed(Task task) {
        if(viewedTasks.size() >= MAX_VIEWED) {
            viewedTasks.removeFirst();
        }
        viewedTasks.add(task);
    }
}