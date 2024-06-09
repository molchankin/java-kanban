public class Task {
    protected String title;
    protected String description;
    protected ProgressStatus progressStatus;
    protected Integer id;

    public Task(String title, String description, ProgressStatus progressStatus, Integer id) {
        this.title = title;
        this.description = description;
        this.progressStatus = progressStatus;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressStatus=" + progressStatus +
                ", id=" + id +
                '}';
    }
}
