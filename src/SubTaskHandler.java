import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.SubTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SubTaskHandler extends BaseHttpHandler {
    private Gson gson;
    public SubTaskHandler(TaskManager manager) {
        super(manager);
        this.taskManager = manager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        try {
            switch (method) {
                case "GET":
                    if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                        handleGetSubtasks(exchange);
                    } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                        int id = Integer.parseInt(pathParts[2]);
                        handleGetSubtaskById(exchange, id);
                    }
                    break;
                case "POST":
                    if (pathParts[1].equals("subtasks") && !method.equals("GET")) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                        BufferedReader br = new BufferedReader(isr);
                        String requestBody = br.lines().collect(Collectors.joining());

                        SubTask task = gson.fromJson(requestBody, SubTask.class);
                        if (pathParts.length == 2) {
                            handleAddSubtask(exchange, task);

                        } else if (pathParts.length == 3) {
                            int id = Integer.parseInt(pathParts[2]);
                            handleUpdateSubtask(exchange, id, task);
                        }
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3 && pathParts[1].equals("subtasks") && !method.equals("GET")) {
                        int id = Integer.parseInt(pathParts[2]);
                        SubTask task = taskManager.getSubTask(id);
                        int epicId = task.getEpicId();
                        handleDeleteSubtask(exchange, id, epicId);
                    }
                    break;
            }
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, "Not Found");
        }
    }

    public void handleGetSubtasks(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Началась обработка /subtasks запроса от клиента.");
            String response = taskManager.getSubTasks()
                    .stream().map(subtask -> subtask.toString()).collect(Collectors.joining("\n"));
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }


    }

    public void handleGetSubtaskById(HttpExchange exchange, int id) throws IOException {
        try {
            System.out.println("Началась обработка /subtasks запроса от клиента.");
            String response = gson.toJson(taskManager.getSubTaskById(id));
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

    }

    public void handleAddSubtask(HttpExchange exchange, SubTask task) throws IOException {
        try {
            System.out.println("Началась обработка /subtasks запроса от клиента.");
            taskManager.addSubTask(task);
            for (SubTask existingSubTask : taskManager.getSubTasks()) {
                if (taskManager.isCrossingWith(task, existingSubTask)) {
                    sendHasInteractions(exchange);
                    break;
                }
            }
            String response = "Подзадача успешно добавлена";
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void handleUpdateSubtask(HttpExchange exchange, int epicId, SubTask task) throws IOException {
        try {
            System.out.println("Началась обработка /subtasks запроса от клиента.");
            taskManager.updateSubTask(epicId, task);
            for (SubTask existingSubTask : taskManager.getSubTasks()) {
                if (taskManager.isCrossingWith(task, existingSubTask)) {
                    sendHasInteractions(exchange);
                    break;
                }
            }
            String response = "Подзадача успешно обновлена";
            exchange.sendResponseHeaders(201, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void handleDeleteSubtask(HttpExchange exchange, int id, int epicId) throws IOException {
        try {
            System.out.println("Началась обработка /subtasks запроса от клиента.");
            taskManager.deleteSubTask(id, epicId);
            String response = "Подзадача успешно удалена";
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
