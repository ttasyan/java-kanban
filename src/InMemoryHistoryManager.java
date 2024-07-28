import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    Map<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        if (history.containsValue(node)) {
            removeNode(node);
        }
        Node newNode = new Node(task);
        linkLast(newNode);
        history.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
            history.remove(id);
        }
    }


    private void linkLast(Node node) {
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> arrayHistory = new ArrayList<>();
        for (Node node : history.values()) {
            arrayHistory.add(node.data);
        }

        return arrayHistory;

    }

    public void removeNode(Node node) {
        history.remove(node.data.getId());
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

    }

}
