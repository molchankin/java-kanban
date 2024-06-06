import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description, ProgressStatus progressStatus, Integer id) {
        super(title, description, progressStatus, id);
    }
}
