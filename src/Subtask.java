public class Subtask extends Task {

    private Integer epicId;
    public Subtask(String title, String description, ProgressStatus progressStatus, Integer id, Integer epicId) {
        super(title, description, progressStatus, id);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressStatus=" + progressStatus +
                ", id=" + id +
                '}';
    }
}
