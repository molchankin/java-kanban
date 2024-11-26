package service;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getPrioritizedTasks();

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

}
