import org.junit.Assert;
import org.junit.Test;

public class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void addTaskTest() {
        Task task = new Task( "Task 1", "Description");
        taskManager.addTask(task);
        Assert.assertEquals(task, taskManager.getTask(1));
    }

    @Test
    public void addEpicTest() {
        Epic epic = new Epic( "Epic 1", "epic description");
        taskManager.addEpic(epic);
        Assert.assertEquals(epic, taskManager.getEpic(1));
    }

    @Test
    public void addSubTaskTest() {
        SubTask subTask = new SubTask( "SubTask 1", "Subtask description", 2);
        taskManager.addSubTask(subTask, 2);
        Assert.assertEquals(subTask, taskManager.getSubTask(1));
    }

    @Test
   public void testIdCollision() {
        Task task1 = new Task("Task 1", "Description 1");
        taskManager.addTask(task1);

        Task task2 = new Task("Task 2", "Description  2");
        taskManager.addTask(task2);
        task2.setId(1);

        Assert.assertEquals(2, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTaskById(1));
    }


    @Test
    public void addTasksAndFindById() {
        Epic epic = new Epic( "Epic 1", "Description");
        SubTask subtask = new SubTask("Subtask 1", "Description", epic.getId());
        Task task = new Task( "Task 1", "Description");

        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask, epic.getId());
        taskManager.addTask(task);

        Assert.assertEquals("Epic 1", taskManager.getEpic(1).getName());
        Assert.assertEquals("Subtask 1", taskManager.getSubTask(2).getName());
        Assert.assertEquals("Task 1", taskManager.getTask(3).getName());
    }

    @Test
    public void taskIsNotChangingWhenAdded() {
        Task task = new Task( "Task 1", "Description");

        taskManager.addTask(task);

        Task addedTask = taskManager.getTask(1);

        Assert.assertEquals("Task 1", addedTask.getName());
        Assert.assertEquals("Description", addedTask.getDescription());
        Assert.assertEquals(task.getId(), addedTask.getId());

        addedTask.setName("New Name");
        addedTask.setDescription("New Description");

        Assert.assertNotEquals("Task 1", task.getName());
        Assert.assertNotEquals("Description", task.getDescription());
        Assert.assertEquals( 1, task.getId().intValue());
    }
    @Test
    public void subtaskRemovalTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description 1");
        SubTask subtask = new SubTask("Subtask 1", "Description 1.1", epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask, epic.getId());
        taskManager.deleteSubTask(subtask.getId(), epic.getId());
        Assert.assertEquals(1, epic.getSubTasksId().size());
    }

    @Test
    public void epicRemovalTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic 1", "Description 1");
        SubTask subtask = new SubTask("Subtask 1", "Description 1.1", epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask, epic.getId());
        taskManager.deleteEpic(epic.getId());
        Assert.assertEquals(0, taskManager.getSubTasks().size());
    }
}