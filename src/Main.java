public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Уборка", "Полная уборка квартиры");
        Subtask subtask1 = new Subtask("Помыть пол", "Использовать швабру и ведро", ProgressStatus.NEW, epic1.getId());
        Task subtask2 = new Subtask("a", "b", ProgressStatus.IN_PROGRESS, epic1.getId());
        Task task1 = new Task("Успеть сдать 5 проект", "Вся ночь впереди", ProgressStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addTask(subtask2);

        TaskManager.printAllTasks(taskManager);
    }
}