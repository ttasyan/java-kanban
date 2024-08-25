package tasks;

import managers.Types;


import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;
    private Duration duration;
    private LocalDateTime startTime;
    private Status status = Status.NEW;

    public Types getType() {
        return type;
    }

    private Types type = Types.SUBTASK;


    public Duration getDuration() {
        return duration;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }


    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }



    public int getEpicId() {

        return epicId;
    }

    @Override
    public String toString() {
        return "tasks.SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }
}
