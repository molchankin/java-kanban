import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();


    public Epic(String title, String description, Integer id) {
        super(title, description, ProgressStatus.NEW, id);
    }

    public Epic(String title, String description) {
        super(title, description, ProgressStatus.NEW);
    }

    void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    public ArrayList<Integer> getSubtasksIds() {
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
}
