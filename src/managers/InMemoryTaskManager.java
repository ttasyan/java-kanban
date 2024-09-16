package managers;

import tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private HistoryManager historyManager;

    Set<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getStartTime().compareTo(t2.getStartTime());
        }
    });
    private int taskId = 1;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }


    @Override
    public void addTask(Task task) {
        task.setId(taskId);
        taskId++;
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {

            if (prioritizedTasks.stream()
                    .anyMatch(prioritizedTask -> isCrossingWith(prioritizedTask, task)
                            || (isCrossingWith(task, prioritizedTask)))) {
                System.out.println("Задача пересекается с другой.");
            } else {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(taskId);
        taskId++;
        epics.put(epic.getId(), epic);

    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(taskId);
        taskId++;
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.getSubTasksId().add(subTask.getId());
            updateEpicStatus(subTask.getEpicId());
        }
        if (subTask.getStartTime() != null) {
            if (prioritizedTasks.stream()
                    .anyMatch(prioritizedTask -> isCrossingWith(prioritizedTask, subTask)
                            || (isCrossingWith(subTask, prioritizedTask)))) {
                System.out.println("Подзадача пересекается с другой.");
            } else {
                prioritizedTasks.add(subTask);
                epic.getEndTime();
            }
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();

    }

    @Override
    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            for (SubTask subTask : subTasks.values()) {
                epic.getSubTasksId().clear();
                updateEpicStatus(subTask.getEpicId());
            }
        }
        subTasks.clear();

    }

    @Override
    public void printTask() {
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    @Override
    public void printEpic() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }

    }

    @Override
    public void printSubTask() {
        for (SubTask st : subTasks.values()) {
            System.out.println(st.toString());
        }

    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        epics.get(id).getSubTasksId().stream()
                .peek(subTaskId -> subTasks.remove(subTaskId))
                .peek(historyManager::remove)
                .forEach(subTaskId -> {
                });
        epics.remove(id);
        historyManager.remove(id);


    }

    @Override
    public void deleteSubTask(int id, int epicId) {
        subTasks.remove(id);
        updateEpicStatus(epicId);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }


    @Override
    public void updateTask(int id, Task task) {
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            if (prioritizedTasks.stream()
                    .anyMatch(prioritizedTask -> isCrossingWith(prioritizedTask, task)
                            || (isCrossingWith(task, prioritizedTask)))) {
                System.out.println("Подзадача пересекается с другой.");
            } else {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateEpic(int id, Epic e) {
        epics.put(id, e);

    }

    @Override
    public void updateSubTask(int epicId, SubTask st) {
        subTasks.put(epicId, st);
        updateEpicStatus(epicId);
        if (st.getStartTime() != null) {
            if (prioritizedTasks.stream()
                    .anyMatch(prioritizedTask -> isCrossingWith(prioritizedTask, st)
                            || (isCrossingWith(st, prioritizedTask)))) {
                System.out.println("Подзадача пересекается с другой.");
            } else {
                prioritizedTasks.add(st);
                getEpicById(epicId).getEndTime();
            }
        }
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.getSubTasksId().isEmpty()) {
            List<Integer> subTasksId = epic.getSubTasksId();
            ArrayList<SubTask> subTasksTemp = new ArrayList<>();
            for (Integer st : subTasksId) {
                subTasksTemp.add(subTasks.get(st));
            }
            if (subTasksTemp.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            } else {

                boolean checkStatusNew = true;
                boolean checkStatusDone = true;
                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.NEW))) {
                        checkStatusNew = false;
                    }
                }

                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.DONE))) {
                        checkStatusDone = false;

                    }
                }
                if (checkStatusNew && !checkStatusDone) {
                    epic.setStatus(Status.NEW);
                } else if (checkStatusDone && !checkStatusNew) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubTasksId().stream()
                .map(subTaskId -> subTasks.get(subTaskId))
                .filter(subTaskId -> subTaskId != null)
                .peek(historyManager::add)
                .collect(Collectors.toList());

    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        for (Task node : historyManager.getTasks()) {
            history.add(node);
        }
        return history;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public boolean isCrossingWith(Task task1, Task task2) {
        return isCrossing(task1, task2);
    }

    private boolean isCrossing(Task task1, Task task2) {
        return task1.getEndTime().isAfter(task2.getStartTime()) && task1.getStartTime().isBefore(task2.getEndTime());
    }
}


