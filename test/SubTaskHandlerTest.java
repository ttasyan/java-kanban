import com.google.gson.*;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    ;
    HttpTaskServer taskServer;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    @BeforeEach
    public void setUp() throws IOException {
        taskServer = new HttpTaskServer(manager);
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "TestEpic 1", "Testing subtask");
        SubTask task = new SubTask("Test 1", "Testing subtask 1", LocalDateTime.now(), Duration.ofMinutes(5), epic.getId());
        manager.addEpic(epic);
        manager.addSubTask(task);

        String taskJson = gson.toJson(task.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            List<SubTask> tasksFromManager = manager.getSubTasks();

            assertNotNull(tasksFromManager, "Сабтаск не возвращается");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество сабтасков");
            assertEquals("Test 1", tasksFromManager.get(0).getName(), "Некорректное имя сабтаска");
        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "TestEpic 1", "Testing subtask");
        manager.addEpic(epic);
        manager.addSubTask(new SubTask("Test 1", "Testing subtask 1", LocalDateTime.now(), Duration.ofMinutes(5), epic.getId()));
        manager.addSubTask(new SubTask("Test 2", "Testing subtask 2", LocalDateTime.now().plus(Duration.ofMinutes(10)), Duration.ofMinutes(5), epic.getId()));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        SubTask subTask = new SubTask(1, "Test Epic", "Testing epic 1", epic.getId());
        manager.addEpic(epic);
        manager.addSubTask(subTask);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTask.getId()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            JsonElement responseBody = JsonParser.parseString(response.body());
            assertNotNull(responseBody, "Сабтаск не возвращается");
            assertEquals(subTask.getName(), responseBody.getAsJsonObject().get("name").getAsString(), "Некорректное имя сабтаска");
        } catch (IOException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "TestEpic 1", "Testing subtask");
        SubTask task = new SubTask("Test 1", "Testing subtask 1", LocalDateTime.now(), Duration.ofMinutes(5), epic.getId());
        manager.addEpic(epic);
        manager.addSubTask(task);
        task.setName("New name");
        String taskJson = gson.toJson(task.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + task.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            List<SubTask> tasksFromManager = manager.getSubTasks();

            assertNotNull(tasksFromManager, "Сабтаск не возвращается");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество сабтасков");
            assertEquals("New name", tasksFromManager.get(0).getName(), "Некорректное имя сабтаска");
        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        SubTask subTask = new SubTask(1, "Test Epic", "Testing epic 1", epic.getId());
        manager.addEpic(epic);
        manager.addSubTask(subTask);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTask.getId()))
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            SubTask subTaskFromManager = manager.getSubTaskById(subTask.getId());
            assertNull(subTaskFromManager, "Сабтаск не удален");
        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }
}
