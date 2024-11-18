package impl;

import service.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, HistoryNode> nodesById = new HashMap<>();
    private final HistoryLinkedList viewedTasks = new HistoryLinkedList();

    @Override
    public List<Task> getViewedTasks() {
        return viewedTasks.getTasks();
    }

    @Override
    public void addTaskToViewed(Task task) {
        removeNode(nodesById.get(task.getId()));
        viewedTasks.linkLast(task);
        nodesById.put(task.getId(), viewedTasks.tail);
    }

    @Override
    public void remove(int id) {
        removeNode(nodesById.get(id));
    }

    @Override
    public void removeNode(HistoryNode node) {
        if (node == null) {
            return;
        }
        HistoryNode prev = node.prev;
        if (prev != null) {
            prev.next = node.next;
        }

        HistoryNode next = node.next;
        if (prev != null) {
            next.prev = node.prev;
        }
        nodesById.remove(node.task.getId());
    }

    public static class HistoryLinkedList {
        public HistoryNode head;
        public HistoryNode tail;
        private int size = 0;

        public void linkLast(Task task) {
            HistoryNode node = new HistoryNode(task);
            if (tail == null) {
                head = node;
            } else {
                tail.next = node;
                node.prev = tail;
            }
            tail = node;
            size++;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> result = new ArrayList<>();
            HistoryNode current = head;
            while (current != null) {
                result.add(current.task);
                current = current.next;
            }
            return result;
        }


    }

}