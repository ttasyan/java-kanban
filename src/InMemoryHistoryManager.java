import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> history = new ArrayList<>();
    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (history.size() > 10) {
            history.remove(0);
        }
        for (Task h : history) {
            System.out.println(h);;
        }
        return history;
    }
}
