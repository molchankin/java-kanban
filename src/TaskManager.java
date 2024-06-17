import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

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

    void deleteAllTasks() {
        tasks.clear();
    }

    ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    void addTask(Task task) {
        task.setId(getIdCounter());
        tasks.put(task.getId(), task);
    }

    void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    void addEpic(Epic epic) {
        epic.setId(getIdCounter());
        epics.put(epic.getId(), epic);
    }

    void deleteEpicById(Integer id) {
        epics.remove(id);
    }

    Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    void addSubtask(Subtask subtask) {
        subtask.setId(getIdCounter());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        universalStatusUpdate(epic);
    }

    void deleteSubtaskById(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskById(id);
        subtasks.remove(id);
    }

    void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtask();
        }
        subtasks.clear();
    }

    void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);

            if (epic != null) {
                universalStatusUpdate(epic);
            }
        }
    }

    ArrayList<Subtask> getSubtasksFromEpic(Integer epicId) {
        List<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        for (Integer subtaskId : subtasksIds) {
            subtasksInEpic.add(subtasks.get(subtaskId));
        }
        return subtasksInEpic;
    }

    ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    void deleteAllEpics() {
        epics.clear();
    }

    void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setTitle(epic.getTitle());
        }
    }

}
