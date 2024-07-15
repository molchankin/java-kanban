import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtasksIds = new ArrayList<>();


    public Epic(String title, String description, Integer id) {
        super(title, description, ProgressStatus.NEW, id);
    }

    public Epic(String title, String description) {
        super(title, description, ProgressStatus.NEW);
    }

    void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    void deleteAllSubtask() {
        subtasksIds.clear();
        progressStatus = ProgressStatus.NEW;
    }

    void deleteSubtaskById(Integer id) {
        subtasksIds.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
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


}
