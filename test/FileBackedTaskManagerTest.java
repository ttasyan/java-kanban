import managers.FileBackedTaskManager;
import org.junit.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    @Test
    public void loadEmptyFileTest() throws IOException {
        File file = File.createTempFile("taskmanager", ".tmp");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.save();
        FileBackedTaskManager loadManager = new FileBackedTaskManager(file);
        loadManager.loadFromFile(file);

        assertEquals(0, loadManager.getTasks().size());
        assertEquals(0, loadManager.getSubTasks().size());
        assertEquals(0, loadManager.getEpics().size());

    }

    @Test
    public void loadAndSaveFilesTest() throws IOException {
        File file = File.createTempFile("taskmanager", ".tmp");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task task1 = new Task("task 1", "Description 1");
        Epic epic1 = new Epic("epic1", "description");
        SubTask subTask1 = new SubTask("st1.1", "subtask1.1", 1);
        SubTask subTask2 = new SubTask("st1.2", "subtask1.2", 1);

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubTask(subTask1, epic1.getId());
        manager.addSubTask(subTask2, epic1.getId());


        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(2, manager.getSubTasks().size());

        FileBackedTaskManager loadManager = new FileBackedTaskManager(file);
        loadManager.loadFromFile(file);

        assertEquals(1, loadManager.getTasks().size());
        assertEquals(1, loadManager.getEpics().size());
        assertEquals(2, loadManager.getSubTasks().size());

    }
}
