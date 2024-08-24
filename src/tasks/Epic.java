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
    private TaskManager taskManager;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }


    private LocalDateTime startTime;


    public Epic(int id, String name, String description, LocalDateTime startTime) {
        super(id, name, description);
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = Duration.between(startTime, getEndTime());
        this.subTasksId = new ArrayList<>();
        this.taskManager = new InMemoryTaskManager();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Types getType() {
        return type;
    }

    public void addSubTask(SubTask subTask) {
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
        List<SubTask> subTasks = new ArrayList<>();
        for (int id : subTasksId) {
            SubTask subTask = taskManager.getSubTaskById(id);
            subTasks.add(subTask);
        }
        if (subTasks.isEmpty()) {
            return startTime.plus(Duration.ZERO);
        }

        for (SubTask st : subTasks) {
            duration = duration.plus(st.getDuration());
        }
        return startTime.plus(duration);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

}
