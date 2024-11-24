package task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasksIds = new ArrayList<>();


    public Epic(String title, String description, Integer id, ProgressStatus progressStatus) {
        super(title, description, progressStatus, id);
    }

    public Epic(String title, String description) {
        super(title, description, ProgressStatus.NEW);
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
