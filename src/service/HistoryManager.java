package service;

import task.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getViewedTasks();

    void addTaskToViewed(Task task);

    void remove(int id);

}
