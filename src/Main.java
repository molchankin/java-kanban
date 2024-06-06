public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Уборка", "Помыть пол, помыть посуду,", ProgressStatus.NEW, taskManager.getIdCounter());
        taskManager.addTask(task);
        Task taskidaski = new Task("Уборка", "Помыть пол, помыть посуду,", ProgressStatus.IN_PROGRESS, 1);
        taskManager.updateTask(taskidaski);
    }

}
