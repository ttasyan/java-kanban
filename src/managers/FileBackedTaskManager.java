package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File filename;

    public FileBackedTaskManager(File filename) {
        this.filename = filename;
    }

    public static void main(String[] args) throws IOException {
        File file = File.createTempFile("taskmanager", ".tmp");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task task1 = new Task("task 1", "Description 1");
        Task task2 = new Task("task 2", "Description 2");
        Epic epic1 = new Epic("epic1", "description");
        SubTask subTask1 = new SubTask("st1.1", "subtask1.1", epic1.getId());
        SubTask subTask2 = new SubTask("st1.2", "subtask1.2", epic1.getId());

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubTask(subTask1, epic1.getId());
        manager.addSubTask(subTask2, epic1.getId());
        System.out.println("Epics:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }


        FileBackedTaskManager newManager = new FileBackedTaskManager(file);
        newManager.loadFromFile(file);
        System.out.println("Tasks:");
        for (Task task : newManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Epics:");
        for (Epic epic : newManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("SubTasks:");
        for (SubTask subtask : newManager.getSubTasks()) {
            System.out.println(subtask);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("id,type,name,status,description,epic");
            writer.write("\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (SubTask subtask : getSubTasks()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(new ManagerSaveException("Произошла ошибка во время записи файла", e));
        }
    }

    public void loadFromFile(File file) throws IOException {
        Files.readString(file.toPath());
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);
                if (task instanceof Epic) {
                    this.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    this.addSubTask((SubTask) task, ((SubTask) task).getEpicId());
                } else {
                    this.addTask(task);
                }

            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(new ManagerSaveException("Произошла ошибка во время чтения файла", e));
        }
    }

    @Override
    public void addSubTask(SubTask subtask, int epicId) {
        super.addSubTask(subtask, epicId);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks(int epicId) {
        super.deleteSubTasks(epicId);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id, int epicId) {
        super.deleteSubTask(id, epicId);
        save();
    }

    @Override
    public void updateTask(int newId, Task task) {
        super.updateTask(newId, task);
        save();
    }

    @Override
    public void updateEpic(int newId, Epic e) {
        super.updateEpic(newId, e);
        save();
    }

    @Override
    public void updateSubTask(int epicId, SubTask st) {
        super.updateSubTask(epicId, st);
        save();

    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }


    public String toString(Task task) {
        String type;
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof SubTask) {
            type = "SUBTASK";
        } else {
            type = "TASK";
        }

        String result = String.format("%d,%s,%s,%s,%s,",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription()
        );
        if (task.getType() == Types.SUBTASK) {
            SubTask subTask = (SubTask) task;
            return result + subTask.getEpicId();
        } else {
            return result + " ";
        }

    }

    public Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];


        if (type.equals("TASK")) {
            Task task = new Task(id, name, status, description);
            return task;
        } else if (type.equals("EPIC")) {
            Epic task = new Epic(id, name, status, description);
            return task;
        } else if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(parts[5]);
            SubTask task = new SubTask(id, name, status, description, epicId);
            return task;
        } else {
            throw new RuntimeException("Произошла ошибка");
        }
    }

}
