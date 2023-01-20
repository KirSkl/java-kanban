package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList customLinkedList = new CustomLinkedList();
    @Override
    public void add(Task task) {
        if (customLinkedList.historyTasks.containsKey(task.getId())) {
            remove(task.getId());
        }
        customLinkedList.linkLast(task);
    }
    @Override
    public void remove(int id) {
        customLinkedList.removeNode(customLinkedList.historyTasks.remove(id));
    }
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

     class CustomLinkedList {
        private final Map<Integer, Node> historyTasks = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node node = new Node(task);
            if (historyTasks.isEmpty()) {
                head = node;
            } else {
                tail.next = node;
                node.prev = tail;
            }
            tail = node;
            historyTasks.put(task.getId(), node);
        }
        private List getTasks() {
            List<Task> tasks = new ArrayList<>();
            tasks.add(head.task);
            Node node = head;
            while (node.next != null) {
                node = node.next;
                tasks.add(node.task);
            }
            return tasks;
        }
        private void removeNode(Node node) {
            if (node.next != null) {
                node.next.prev = node.prev;
            } else tail = node.prev;
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
        }

        class Node {
            Task task;
            Node prev;
            Node next;

            private Node(Task task) {
                this.task = task;
            }
        }
    }
}
