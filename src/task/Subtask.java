package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private final Integer epicId;

    public Subtask(String title, String description, ProgressStatus progressStatus, Integer id, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, progressStatus, id, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, ProgressStatus progressStatus, Integer epicId) {
        super(title, description, progressStatus);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, ProgressStatus progressStatus, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, progressStatus, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, ProgressStatus progressStatus, Integer id, Integer epicId) {
        super(title, description, progressStatus, id);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "Task.Subtask{" +
                "epicId=" + epicId +
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
        Subtask subtask = (Subtask) obj;
        return Objects.equals(title, subtask.title) &&
                Objects.equals(description, subtask.description) &&
                progressStatus == subtask.progressStatus &&
                Objects.equals(id, subtask.id) &&
                Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
