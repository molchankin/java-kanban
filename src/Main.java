import service.Managers;
import service.TaskManager;
import task.Epic;
import task.ProgressStatus;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaults();

        Task task1 = new Task("Сдать пятый проект", "Осталось мало времени", ProgressStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск1", "Описание сабтаск1", ProgressStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask2 = new Subtask("Сабтаск2", "Описание сабтаск2", ProgressStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask3 = new Subtask("Сабтаск3", "Описание сабтаск3", ProgressStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask4 = new Subtask("Сабтаск4", "Описание сабтаск4", ProgressStatus.IN_PROGRESS, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        printAllTasks(taskManager);
        taskManager.deleteSubtaskById(subtask3.getId());
        taskManager.getTaskById(task1.getId());
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task subtask : manager.getAllSubtasks()) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}