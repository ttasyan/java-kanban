package tests;

import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HistoryManagerTest {
    @Test
    public void addTaskTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task 1", "Description");
        historyManager.add(task);
        Assert.assertEquals(task, historyManager.getTasks().get(0));
        task.setName("New name");
        Assert.assertNotEquals("Task 1", historyManager.getTasks().get(0).getName());
    }
}
