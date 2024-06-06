public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String title, String description, ProgressStatus progressStatus, Integer id, Integer epicId) {
        super(title, description, progressStatus, id);
        this.epicId = epicId;
    }
}
