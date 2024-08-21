package tasks;


import managers.InMemoryTaskManager;
import managers.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime;

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.status = Status.NEW;
        this.subTasksId = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasksId.add(subTask.getId());
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
