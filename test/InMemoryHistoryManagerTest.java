import managers.InMemoryHistoryManager;
import org.junit.Assert;
import org.junit.Test;
import tasks.Task;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Test
    public void addNodeTest() {
        Task task = new Task("tasks.Task 1", "Description 1");
        historyManager.add(task);
        List<Task> history = historyManager.getTasks();
        assertEquals(1, history.size());
        Assert.assertEquals(task, history.get(0));
    }

    @Test
    public void removeNodeTest() {
        Task task = new Task("tasks.Task 1", "Description 1");
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> history = historyManager.getTasks();
        assertEquals(0, history.size());
    }

    @Test
    public void linkLastTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("tasks.Task 1", "Description 1");
        Task task2 = new Task("tasks.Task 2", "Description 2");
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getTasks();
        historyManager.remove(task1.getId());
        assertEquals(1, history.size());
        Assert.assertEquals(task2, history.get(0));

    }


}
