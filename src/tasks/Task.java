package tasks;

import managers.Types;

import static managers.Types.TASK;

public class Task {

    private String description;
    private int id;
    protected Status status;

    public Types getType() {
        return type;
    }

    private Types type = TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
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
