package tasks;


import managers.InMemoryTaskManager;
import managers.TaskManager;
import managers.Types;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();

    private Duration duration;
    private Types type = Types.EPIC;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }


    private LocalDateTime startTime;


    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.status = Status.NEW;
        try {
            this.duration = Duration.between(startTime, getEndTime());
        } catch (NullPointerException e) {
            duration = null;
        }
        this.subTasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String status, String description) {
        super(id, name, status, description);
    }

    public Types getType() {
        return type;
    }

    public void addSubTask(SubTask subTask) {
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.updateEpicStatus(getId());
        subTasksId.add(subTask.getId());
        getEndTime();
    }


    @Override
    public String toString() {
        return "Epics{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasksId=" + subTasksId +
                '}';
    }

    public LocalDateTime getEndTime() {
        TaskManager taskManager = new InMemoryTaskManager();
        List<SubTask> subTasks = new ArrayList<>();
        for (int id : subTasksId) {
            SubTask subTask = taskManager.getSubTaskById(id);
            subTasks.add(subTask);
        }
        if (subTasks.isEmpty()) {
            startTime = null;
            return null;
        }
        startTime = subTasks.getFirst().getStartTime();

        for (SubTask st : subTasks) {
            duration.plus(st.getDuration());
        }
        return startTime.plus(duration);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }


}
