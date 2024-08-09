package tasks;

import managers.Types;

import static managers.Types.TASK;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private String description;
    private int id;
    protected Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Types getType() {
        return type;
    }

    private Types type = TASK;

    public Task(String name, String description) {
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String name, String status, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
    public LocalDateTime getEndTime() {

        return startTime.plus(duration);
    }


    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public void setDescription(String description) {
        this.description = description;
    }


}
