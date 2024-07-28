package test;

import managers.HistoryManager;
import managers.Managers;
import org.junit.Assert;
import org.junit.Test;
import tasks.Task;


public class HistoryManagerTest {
    @Test
    public void addTaskTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("tasks.Task 1", "Description");
        historyManager.add(task);
        Assert.assertEquals(task, historyManager.getTasks().get(0));
        task.setName("New name");
        Assert.assertNotEquals("tasks.Task 1", historyManager.getTasks().get(0).getName());
    }
}
