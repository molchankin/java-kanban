package impl;

import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();


    private Integer idCounter = 0;

    protected void setIdCounter(Integer idCounter) {
        this.idCounter = Math.max(this.idCounter, idCounter);
    }

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
        durationUpdate(epic);
    }

    private void durationUpdate(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtasksIds();
        if (subtaskIds.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }
        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStartTime = LocalDateTime.MAX;
        LocalDateTime latestEndTime = LocalDateTime.MIN;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStartTime() == null || subtask.getDuration() == null) {
                continue;
            }
            totalDuration = totalDuration.plus(subtask.getDuration());
            if (subtask.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(latestEndTime)) {
                latestEndTime = subtask.getEndTime();
            }
        }
        epic.setDuration(totalDuration);
        if (earliestStartTime == LocalDateTime.MAX) {
            epic.setStartTime(null);
        } else {
            epic.setStartTime(earliestStartTime);
        }
        if (latestEndTime == LocalDateTime.MIN) {
            epic.setEndTime(null);
        } else {
            epic.setEndTime(latestEndTime);
        }
    }

    protected void addTaskFromFile(Task task) {
        tasks.put(task.getId(), task);
        addTaskToPrioritizedTasks(task);
    }

    protected void addEpicFromFile(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void addSubtaskFromFile(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        addTaskToPrioritizedTasks(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addTaskToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private boolean checkIntersection(Task task) {
        if (task.getStartTime() == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(prioritizedTask -> !Objects.equals(task.getId(), prioritizedTask.getId()))
                .anyMatch(prioritizedTask ->
                        !(task.getEndTime().isBefore(prioritizedTask.getStartTime()) || task.getStartTime().isAfter(prioritizedTask.getEndTime()))
                );
    }

    private void removeTaskFromHistoryAndPrioritizedTasks(Task task) {
        inMemoryHistoryManager.remove(task.getId());
        prioritizedTasks.remove(task);
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(this::removeTaskFromHistoryAndPrioritizedTasks);
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
            return task;
        }
        throw new NotFoundException();
    }

    @Override
    public void addTask(Task task) {
        if (checkIntersection(task)) {
            throw new AddTaskException("Время задачи уже занято. Измените время задачи.");
        }
        task.setId(getIdCounter());
        tasks.put(task.getId(), task);
        addTaskToPrioritizedTasks(task);
    }

    @Override
    public void deleteTaskById(Integer id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        if (checkIntersection(task)) {
            throw new AddTaskException("Время задачи уже занято. Измените время задачи.");
        }
        if (tasks.containsKey(task.getId())) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            addTaskToPrioritizedTasks(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (checkIntersection(epic)) {
            throw new AddTaskException("Время задачи уже занято. Измените время задачи.");
        }
        epic.setId(getIdCounter());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubtasksIds().forEach(subtaskId -> {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            });
            epics.remove(id);
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            inMemoryHistoryManager.addTaskToViewed(epic);
            return epic;
        }
        throw new NotFoundException();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            throw new AddTaskException("Время задачи уже занято. Измените время задачи.");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(getIdCounter());
            subtasks.put(subtask.getId(), subtask);
            addTaskToPrioritizedTasks(subtask);
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
                prioritizedTasks.remove(subtasks.get(id));
                subtasks.remove(id);
                inMemoryHistoryManager.remove(id);
                universalStatusUpdate(epic);
            }
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(this::removeTaskFromHistoryAndPrioritizedTasks);
        epics.values().forEach(Epic::deleteAllSubtask);
        subtasks.clear();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            throw new AddTaskException("Время задачи уже занято. Измените время задачи.");
        }
        Subtask subtaskInMap = subtasks.get(subtask.getId());
        if (subtaskInMap != null) {
            if (Objects.equals(subtaskInMap.getEpicId(), subtask.getEpicId())) {
                prioritizedTasks.remove(subtaskInMap);
                subtasks.put(subtask.getId(), subtask);
                addTaskToPrioritizedTasks(subtask);
                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    universalStatusUpdate(epic);
                }
            }
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Integer epicId) {
        if (!epics.containsKey(epicId)) {
            return new ArrayList<>();
        }
        return epics.get(epicId).getSubtasksIds().stream().map(subtasks::get).toList();
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
        epics.values().stream().map(Epic::getId).forEach(inMemoryHistoryManager::remove);
        epics.values().stream()
                .flatMap(epic -> getSubtasksFromEpic(epic.getId()).stream())
                .forEach(this::removeTaskFromHistoryAndPrioritizedTasks);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.addTaskToViewed(subtask);
            return subtask;
        }
        throw new NotFoundException();
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
