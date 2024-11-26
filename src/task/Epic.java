package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Epic(String title, String description, Integer id, ProgressStatus progressStatus, Duration duration, LocalDateTime startTime) {
        super(title, description, progressStatus, id, duration, startTime);
    }

    public Epic(String title, String description) {
        super(title, description, ProgressStatus.NEW);
    }

    public Epic(String title, String description, Integer id) {
        super(title, description, ProgressStatus.NEW, id);
    }

    public void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void deleteAllSubtask() {
        subtasksIds.clear();
        progressStatus = ProgressStatus.NEW;
    }

    public void deleteSubtaskById(Integer id) {
        subtasksIds.remove(id);
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
                "subtasksIds=" + subtasksIds +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressStatus=" + progressStatus +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Epic epic = (Epic) obj;
        return Objects.equals(title, epic.title) &&
                Objects.equals(description, epic.description) &&
                progressStatus == epic.progressStatus &&
                Objects.equals(id, epic.id);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}
