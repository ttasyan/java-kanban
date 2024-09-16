import com.sun.net.httpserver.HttpExchange;
import managers.Managers;
import managers.TaskManager;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(Managers.getDefault());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Началась обработка /prioritized запроса от клиента.");
            String responseBody = taskManager.getPrioritizedTasks()
                    .stream().map(task -> task.toString()).collect(Collectors.joining("\n"));
            sendText(exchange, responseBody);
        } catch (
                NoSuchElementException e) {
            sendNotFound(exchange, "Not Found");
        }
    }
}
