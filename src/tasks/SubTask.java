package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public Duration getDuration() {
        return duration;
    }

    private Duration duration;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    private LocalDateTime startTime;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);
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
}
