public class Task {
    private String title;
    private String description;
    private ProgressStatus progressStatus;
    private Integer id;

    public Task(String title, String description, ProgressStatus progressStatus, Integer id) {
        this.title = title;
        this.description = description;
        this.progressStatus = progressStatus;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
