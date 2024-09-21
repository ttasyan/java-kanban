import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new Gson();

    @BeforeEach
    public void setUp() throws IOException {
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        manager.addEpic(epic);
        String epicJson = gson.toJson(epic.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        try {

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            JsonElement responseBody = JsonParser.parseString(response.body());
            assertNotNull(responseBody, "Эпики не возвращаются");
            assertEquals(1, manager.getEpics().size(), "Некорректное количество эпиков");
            assertEquals(epic.getName(), responseBody.getAsJsonObject().get("name").getAsString(), "Некорректное имя эпика");
        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        manager.addEpic(new Epic(0, "Test 1", "Testing task 1"));
        manager.addEpic(new Epic(1, "Test 2", "Testing task 2"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
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
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        manager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            System.out.println(response.body());
            JsonElement responseBody = JsonParser.parseString(response.body());
            assertNotNull(responseBody, "Эпик не возвращается");
            assertEquals(epic.getName(), responseBody.getAsJsonObject().get("name").getAsString(), "Некорректное имя эпика");
        } catch (IOException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testGetEpicsSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        manager.addEpic(epic);
        manager.addSubTask(new SubTask("Test 1", "Testing subtask 1",
                LocalDateTime.now(), Duration.ofMinutes(5), epic.getId()));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            JsonElement responseBody = JsonParser.parseString(response.body());
            assertNotNull(responseBody, "Подзадачи не возвращаются");
            assertEquals(1, manager.getSubTasksByEpicId(epic.getId()).size(), "Некорректное количество подзадач");
        } catch (IOException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Testing epic 1");
        manager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("Эпик успешно удален", response.body());

            Epic epicFromManager = manager.getEpicById(epic.getId());
            assertNull(epicFromManager, "Эпик не удален");
        } catch (IOException | InterruptedException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }
}
