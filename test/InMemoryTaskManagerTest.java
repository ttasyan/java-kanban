import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.Assert;
import org.junit.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = new InMemoryTaskManager();


    @Test
    public void addTaskTest() {
        Task task = new Task("tasks.Task 1", "Description", LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.addTask(task);
        Assert.assertEquals(task, taskManager.getTask(1));
    }

    @Test
    public void addEpicTest() {
        Epic epic = new Epic(1, "tasks.Epic 1", "epic description", LocalDateTime.now());
        taskManager.addEpic(epic);
        Assert.assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    public void addSubTaskTest() {
        SubTask subTask = new SubTask("tasks.SubTask 1", "Subtask description", LocalDateTime.now(), Duration.ofMinutes(5), 2);
        taskManager.addSubTask(subTask);
        Assert.assertEquals(subTask, taskManager.getSubTask(1));
    }

    @Test
    public void testIdCollision() {
        Task task1 = new Task("tasks.Task 1", "Description 1", LocalDateTime.now(), Duration.ofMinutes(5));
        taskManager.addTask(task1);

        Task task2 = new Task("tasks.Task 2", "Description  2", LocalDateTime.now().plus(Duration.ofMinutes(16)), Duration.ofMinutes(7));
        taskManager.addTask(task2);
        task2.setId(1);

        Assert.assertEquals(2, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTaskById(1));
    }


    @Test
    public void addTasksAndFindById() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Description", LocalDateTime.now());
        SubTask subtask = new SubTask("Subtask 1", "Description", LocalDateTime.now(), Duration.ofMinutes(15), epic.getId());
        Task task = new Task("tasks.Task 1", "Description", LocalDateTime.now().plus(Duration.ofMinutes(16)), Duration.ofMinutes(15));

        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask);
        taskManager.addTask(task);

        Assert.assertEquals("tasks.Epic 1", taskManager.getEpic(1).getName());
        Assert.assertEquals("Subtask 1", taskManager.getSubTask(2).getName());
        Assert.assertEquals("tasks.Task 1", taskManager.getTask(3).getName());
    }

    @Test
    public void taskIsNotChangingWhenAdded() {
        Task task = new Task("tasks.Task 1", "Description", LocalDateTime.now(), Duration.ofMinutes(15));

        taskManager.addTask(task);

        Task addedTask = taskManager.getTask(1);

        Assert.assertEquals("tasks.Task 1", addedTask.getName());
        Assert.assertEquals("Description", addedTask.getDescription());
        Assert.assertEquals(task.getId(), addedTask.getId());

        addedTask.setName("New Name");
        addedTask.setDescription("New Description");

        Assert.assertNotEquals("tasks.Task 1", task.getName());
        Assert.assertNotEquals("Description", task.getDescription());
        Assert.assertEquals(1, task.getId().intValue());
    }

    @Test
    public void subtaskRemovalTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "tasks.Epic 1", "Description 1", LocalDateTime.now());
        SubTask subtask = new SubTask("Subtask 1", "Description 1.1", LocalDateTime.now(), Duration.ofMinutes(15), epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask);
        taskManager.deleteSubTask(subtask.getId(), epic.getId());
        Assert.assertEquals(1, epic.getSubTasksId().size());
    }

    @Test
    public void epicRemovalTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "tasks.Epic 1", "Description 1", LocalDateTime.now());
        SubTask subtask = new SubTask("Subtask 1", "Description 1.1", LocalDateTime.now(), Duration.ofMinutes(15), epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask);
        taskManager.deleteEpic(epic.getId());
        Assert.assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    public void epicGetStatusNewTest() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Description 1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask(2, "SubTask 1", "Description 1.1", epic.getId());
        SubTask subTask2 = new SubTask(3, "SubTask 2", "Description 1.2", epic.getId());
        epic.getSubTasksId().add(subTask1.getId());
        epic.getSubTasksId().add(subTask2.getId());

        Assert.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void epicGetStatusInProgressTest() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Description 1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask(2, "SubTask 1", "Description 1.1", epic.getId());
        SubTask subTask2 = new SubTask(3, "SubTask 2", "Description 1.2", epic.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        epic.getSubTasksId().add(subTask1.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        epic.getSubTasksId().add(subTask2.getId());
        taskManager.updateEpicStatus(epic.getId());

        Assert.assertEquals(Status.IN_PROGRESS, epic.getStatus());

    }

    @Test
    public void epicGetStatusMixedTest() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Description 1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask(2, "SubTask 1", "Description 1.1", epic.getId());
        SubTask subTask2 = new SubTask(3, "SubTask 2", "Description 1.2", epic.getId());
        subTask1.setStatus(Status.DONE);
        epic.getSubTasksId().add(subTask1.getId());
        subTask2.setStatus(Status.NEW);
        epic.getSubTasksId().add(subTask2.getId());

        taskManager.updateEpicStatus(epic.getId());

        Assert.assertEquals(Status.IN_PROGRESS, epic.getStatus());

    }

    @Test
    public void testIsCrossing() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), Duration.ofHours(1));
        Task task2 = new Task("Task 2", "Description 2", task1.getStartTime().plusMinutes(59), Duration.ofHours(1));
        Assert.assertTrue(taskManager.isCrossingWith(task1, task2));
    }
}