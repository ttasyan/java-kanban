package managers;

import tasks.Task;

public class Node<T extends Task> {
    Task data;
    Node<T> prev;
    Node<T> next;

    public Node(Task data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
