import java.util.ArrayList;

public interface NewTaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask, int epicId);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks(int epicId);

    void printTask();

    void printEpic();

    void printSubTask();

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id, int epicId);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void updateTask(int newId, Task task);

    void updateEpic(int newId, Epic e);

    void updateSubTask(int epicId, SubTask st);

    void updateEpicStatus(int epicId);

    ArrayList<SubTask> getSubTasksByEpicId(int epicId);

    void getHistory();

}
