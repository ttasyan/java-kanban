import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class TaskTest {
    @Test
    public void testEpicCannotAddSelfAsSubtask() {
        Epic epic = new Epic(1, "Epic 1", "Description");

        //assertFalse(epic.addSubTask(epic));
        // не получается реализовать этот метод
    }
}
