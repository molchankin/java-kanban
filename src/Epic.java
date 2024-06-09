import java.util.HashMap;


public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;


    public Epic(String title, String description, Integer id) {
        super(title, description, ProgressStatus.NEW, id);
        subtasks = new HashMap<>();
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        universalStatusUpdate();
    }
    
    void deleteAllSubtask() {
        subtasks.clear();
        progressStatus = ProgressStatus.NEW;
    }
    
    void deleteSubtaskById(Integer id) {
        subtasks.remove(id);
        universalStatusUpdate();
    }

    private void universalStatusUpdate() {
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        for (Subtask st : subtasks.values()) {
            if (st.getProgressStatus() == ProgressStatus.NEW && statusDoneCounter == 0) {
                statusNewCounter++;
            } else if (st.getProgressStatus() == ProgressStatus.DONE && statusNewCounter == 0) {
                statusDoneCounter++;
            } else {
                break;
            }
        }
        if (statusNewCounter == subtasks.size()) {
            progressStatus = ProgressStatus.NEW;
        } else if (statusDoneCounter == subtasks.size()) {
            progressStatus = ProgressStatus.DONE;
        } else {
            progressStatus = ProgressStatus.IN_PROGRESS;
        }
    } 
    
    Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }
    
    void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(),subtask);
        universalStatusUpdate();
    }
    
    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressStatus=" + progressStatus +
                ", id=" + id +
                '}';
    }
}
