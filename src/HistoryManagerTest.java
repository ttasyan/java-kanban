import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HistoryManagerTest {
    @Test
    public void addTaskTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(1, "Task 1", "Description");
        historyManager.add(task);
        assertEquals(task, historyManager.getHistory().get(0));
        task.setName("New name");
        assertNotEquals("Task 1", historyManager.getHistory().get(0).getName());
    }
}
