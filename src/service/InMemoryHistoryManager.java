package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Node;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> history;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getHistoryOfTasks();
    }

    private void linkLast(Task element) {
        final Node<Task> pastTail = tail;
        final Node<Task> newNode = new Node<>(pastTail, element, null);
        tail = newNode;
        history.put(element.getId(), newNode);
        if (pastTail == null)
            head = newNode;
        else
            pastTail.next = newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            node.data = null;
            if (tail == node && head == node) {
                tail = null;
                head = null;
            } else if (tail != node && head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node && head != node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    private List<Task> getHistoryOfTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> actualNode = head;
        while (actualNode != null) {
            tasks.add(actualNode.data);
            actualNode = actualNode.next;
        }
        return tasks;
    }
}
