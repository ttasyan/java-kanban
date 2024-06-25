import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    ArrayList<Integer> epicSubTasks = new ArrayList<>();
    int taskId = 1;

    public TaskManager() {
        this.taskId = 1;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    public void addTask(Task task) {
        task.setId(taskId);
        taskId++;
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(taskId);
        taskId++;
        epics.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask, Epic epic) {
        subTask.setId(taskId);
        taskId++;
        subTasks.put(subTask.getId(), subTask);
        epic.getSubTasksId().add(subTask.getId());
        epic.setStatus(Status.IN_PROGRESS);


    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics(int id) {
        ArrayList<Integer> subtaskIds = epics.get(id).getSubTasksId();
        for (Integer subtaskId : subtaskIds) {
            subTasks.remove(subtaskId);
        }
        epics.remove(id);

    }

    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            epic.setStatus(Status.NEW);
        }
        subTasks.clear();

    }

    public void printTask() {
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    public void printEpic() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }

    }

    public void printSubTask() {
        for (SubTask st : subTasks.values()) {
            System.out.println(st.toString());
        }

    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        subTasks.remove(id);
        updateEpicStatus();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }


    public void updateTask(int newId, Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(int newId, Epic e) {
        epics.put(e.getId(), e);

    }

    public void updateSubTask(int newId, SubTask st) {
        subTasks.put(st.getId(), st);

    }

    public void updateEpicStatus() {
        for (Epic epic : epics.values()) {
            if (!epic.getSubTasksId().isEmpty()) {
                ArrayList<Integer> subTasksId = epic.getSubTasksId();
                ArrayList<SubTask> subTasksTemp = new ArrayList<>();
                for (Integer st : subTasksId) {
                    subTasksTemp.add(subTasks.get(st));
                }

                boolean checkStatusNew = true;
                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.NEW))) {
                        checkStatusNew = false;
                    }
                }
                if (checkStatusNew) {
                    epic.setStatus(Status.NEW);
                }

                boolean checkStatusDone = true;
                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.DONE))) {
                        checkStatusDone = false;
                    }
                }
                if (checkStatusDone) {
                    epic.setStatus(Status.DONE);
                }

                if ( !checkStatusNew && !checkStatusDone) {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTaskId : epic.getSubTasksId()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask != null) { // check for null values
                subTasks.add(subTask);
            }
        }
        return subTasks;
    }
}
