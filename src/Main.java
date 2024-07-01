public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Epic epic1 = new Epic("Уборка", "Уборка в квартире");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Помыть полы", "Использовать ведро и швабру", ProgressStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Помыть посуду", "Вся посуда собрана в раковине", ProgressStatus.IN_PROGRESS, epic1.getId());
        taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getSubtasksFromEpic(epic1.getId()));
        taskManager.deleteSubtaskById(subtask1.getId());
        System.out.println(taskManager.getSubtasksFromEpic(epic1.getId()));
        Subtask subtask3 = new Subtask("Постирать одежду", "Постирать одежду и повесить её сушиться", ProgressStatus.IN_PROGRESS, subtask2.getId(), epic1.getId());
        taskManager.updateSubtask(subtask3);
        System.out.println(taskManager.getSubtasksFromEpic(epic1.getId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getSubtasksFromEpic(123));
    }
}