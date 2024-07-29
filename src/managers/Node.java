package managers;

import tasks.Task;

public class Node<T extends Task> {
    private Task data;
    private Node<T> prev;
    private Node<T> next;

    public Task getData() {
        return data;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public Node<T> getNext() {
        return next;
    }


    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public void setData(Task data) {
        this.data = data;
    }


    public Node(Task data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
