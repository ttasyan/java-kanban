import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson;
    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
        gson = new Gson();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addTask(task1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement history = JsonParser.parseString(response.body());
        assertEquals(1, manager.getPrioritizedTasks().size(), "Некорректная история");
    }

}
