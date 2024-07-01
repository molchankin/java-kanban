import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();
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

    public void deleteAllTasks() {
        tasks.clear();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(getIdCounter());
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void addEpic(Epic epic) {
        epic.setId(getIdCounter());
        epics.put(epic.getId(), epic);
    }

    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(getIdCounter());
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            universalStatusUpdate(epic);
        }
    }

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

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtask();
        }
        subtasks.clear();
    }

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

    public ArrayList<Subtask> getSubtasksFromEpic(Integer epicId) {
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
            for (Integer subtaskId : subtasksIds) {
                subtasksInEpic.add(subtasks.get(subtaskId));
            }
        }
        return subtasksInEpic;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setTitle(epic.getTitle());
        }
    }

}
