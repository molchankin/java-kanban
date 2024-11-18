package impl;

import task.Task;

public class HistoryNode {

    public Task task;
    public HistoryNode next;
    public HistoryNode prev;

    public HistoryNode(Task task) {
        this.task = task;
        this.next = null;
        this.prev = null;
    }
}
