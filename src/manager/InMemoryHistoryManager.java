package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyTasks = new HashMap<>();
    Node head;
    Node tail;
    private void linkLast(Task task) {
        Node node = new Node(task);
        if (historyTasks.isEmpty()) {
            head = node;
        } else {
            node.prev = tail;
        }
        tail = node;
        historyTasks.put(task.getId(), node);
    }
    class Node {
        Task task;
        Node prev;
        Node next;
        private Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }

    @Override
    public void add(Task task) {
        if (historyTasks.size() > 10) {
            historyTasks.remove(0);
        }
        historyTasks.add(task);
    }

    @Override
    public void remove(int id) {
        //пока здесь возможен только цикл, пробегающий по всему списку
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }
}
