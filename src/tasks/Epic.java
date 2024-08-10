package tasks;

import managers.Types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();
    private Types type = Types.EPIC;

    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
        this.subTasksId = new ArrayList<>();
    }

    public Epic(int id, String name, String status, String description) {
        super(id, name, status, description);
    }

    public Types getType() {
        return type;
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

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }
}
