package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

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

    List<SubTask> getSubTasksByEpicId(int epicId);

    List<Task> getHistory();

    public Set<Task> getPrioritizedTasks();

    boolean isCrossingWith(Task t1, Task t2);

}
