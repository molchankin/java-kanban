package impl;

import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();


    private Integer idCounter = 0;

    private Integer getIdCounter() {
        idCounter++;
        return idCounter;
    }


    private void universalStatusUpdate(Epic epic) {
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        List<Integer> subtaskIds = epic.getSubtasksIds();
        for (Integer subtaskId : subtaskIds) {
            ProgressStatus status = subtasks.get(subtaskId).getProgressStatus();
            if (status == ProgressStatus.NEW && statusDoneCounter == 0) {
                statusNewCounter++;
            } else if (status == ProgressStatus.DONE && statusNewCounter == 0) {
                statusDoneCounter++;
            } else {
                break;
            }
        }
        if (statusNewCounter == subtaskIds.size()) {
            epic.setProgressStatus(ProgressStatus.NEW);
        } else if (statusDoneCounter == subtaskIds.size()) {
            epic.setProgressStatus(ProgressStatus.DONE);
        } else {
            epic.setProgressStatus(ProgressStatus.IN_PROGRESS);
        }
    }


    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            inMemoryHistoryManager.addTaskToViewed(task);
        }
        return task;
    }

    @Override
    public void addTask(Task task) {
        task.setId(getIdCounter());
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getIdCounter());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            inMemoryHistoryManager.addTaskToViewed(epic);
        }
        return epic;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(getIdCounter());
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            universalStatusUpdate(epic);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            if (epic != null) {
                epic.deleteSubtaskById(id);
                subtasks.remove(id);
                universalStatusUpdate(epic);
            }
        }
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtask();
        }
        subtasks.clear();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskInMap = subtasks.get(subtask.getId());
        if (subtaskInMap != null) {
            if (Objects.equals(subtaskInMap.getEpicId(), subtask.getEpicId())) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    universalStatusUpdate(epic);
                }
            }
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Integer epicId) {
        List<Subtask> subtasksInEpic = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            List<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
            for (Integer subtaskId : subtasksIds) {
                subtasksInEpic.add(subtasks.get(subtaskId));
            }
        }
        return subtasksInEpic;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.addTaskToViewed(subtask);
        }
        return subtask;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setTitle(epic.getTitle());
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getViewedTasks();
    }

}
