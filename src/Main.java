public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task(1, "task 1", "Description 1");
        Task task2 = new Task(2, "Task 2", "Description 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic(1, "epic1", "description");
        SubTask subTask1 = new SubTask(1, "st1.1", "subtask1.1", 1);
        SubTask subTask2 = new SubTask(2, "st1.2", "subtask1.2", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 3);
        taskManager.addSubTask(subTask2, 3);


        Epic epic2 = new Epic(2, "epic2", "description2");
        SubTask subTask3 = new SubTask(1, "st2.1", "subtask2.1", 2);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask3, 6);

        printAllTasks(taskManager);
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");

        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(2));

        System.out.println("История:");
         manager.getHistory();

        System.out.println("Эпики:");

        System.out.println(manager.getEpic(3));
        System.out.println(manager.getEpic(6));

        System.out.println("Подзадачи:");
        System.out.println(manager.getSubTask(4));
        System.out.println(manager.getSubTask(5));
        System.out.println(manager.getSubTask(7));


        System.out.println("История:");
        manager.getHistory();
    }
}
