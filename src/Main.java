public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Уборка", "Помыть пол и посуду,", ProgressStatus.NEW, taskManager.getIdCounter());
        taskManager.addTask(task1);
        Task task2 = new Task("Уборка", "Помыть пол и посуду,", ProgressStatus.IN_PROGRESS, task1.getId());
        taskManager.updateTask(task2);
        Epic epic1 = new Epic("Генеральная уборка", "Уборка", taskManager.getIdCounter());
        Subtask subtask1ForEpic1 = new Subtask("Помыть окна","Использовать моющее средство и тряпку", ProgressStatus.NEW, taskManager.getIdCounter(), epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("Помыть пол", "Использовать ведро с водой и швабру", ProgressStatus.IN_PROGRESS, taskManager.getIdCounter(), epic1.getId());
        taskManager.addEpic(epic1);
        epic1.addSubtask(subtask1ForEpic1);
        epic1.addSubtask(subtask2ForEpic1);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
    }

}
