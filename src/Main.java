public class Main {

    public static void main(String[] args) {
    TaskManager taskManager = new TaskManager();

        Task task1 = new Task( 1,"task 1", "Description 1");
        Task task2 = new Task( 2,"Task 2", "Description 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic(1,"epic1", "description");
        SubTask subTask1 = new SubTask( 1,"st1.1", "subtask1.1", 1);
        SubTask subTask2 = new SubTask( 2,"st1.2", "subtask1.2", 1);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, epic1);
        taskManager.addSubTask(subTask2, epic1);


        Epic epic2 = new Epic( 2,"epic2", "description2");
        SubTask subTask3 = new SubTask(1,"st2.1","subtask2.1", 2);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask3, epic2);

        System.out.println("printing tasks");
        taskManager.printTask();

        System.out.println("printing epics");
        taskManager.printEpic();

        System.out.println("printing sub tasks");
        taskManager.printSubTask();

        task1.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.DONE);

        System.out.println("printing tasks again");
        taskManager.printTask();

        System.out.println("printing epics again");
        taskManager.printEpic();

        System.out.println("printing sub tasks again");
        taskManager.printSubTask();

        taskManager.deleteSubTask(4);
        taskManager.deleteSubTask(5);
        taskManager.deleteEpic(3);

        System.out.println("printing tasks again and again");
        taskManager.printTask();

        System.out.println("printing epics again and again");
        taskManager.printEpic();

        System.out.println("printing sub tasks again and again");
        taskManager.printSubTask();

    }
}
