import managers.HistoryManager;
import managers.Managers;
import org.junit.Assert;
import org.junit.Test;
import tasks.Task;



public class HistoryManagerTest {
    @Test
    public void addTaskTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(1, "Task 1", "Description");
        historyManager.add(task);
        Assert.assertEquals(task, historyManager.getTasks().getFirst());
        task.setName("New name");
        Assert.assertNotEquals("Task 1", historyManager.getTasks().getFirst().getName());
    }

    @Test
    public void doubleTasksTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task(1, "Task 1", "Description 1");
        Task task2 = task1;
        historyManager.add(task1);
        historyManager.add(task2);
        Assert.assertEquals(historyManager.getTasks().getFirst(), task2);
        Assert.assertEquals(historyManager.getTasks().size(), 1);

    }

    @Test
    public void emptyHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task(1, "Task 1", "Description 1");
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        Assert.assertEquals(historyManager.getTasks().size(), 0);

    }
}
