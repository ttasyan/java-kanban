import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import managers.InMemoryTaskManager;
import managers.TaskManager;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    public static Gson gson;


    TaskManager manager;
    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }
    public static void main(String[] args) throws IOException {
        TaskManager manager = new InMemoryTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
       server.start();
    }

    public void start() throws IOException{
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.httpServer.createContext("/tasks", new TaskHandler(manager));
        this.httpServer.createContext("/epics", new EpicHandler(manager));
        this.httpServer.createContext("/subtasks", new SubTaskHandler(manager));
        this.httpServer.createContext("/history", new HistoryHandler(manager));
        this.httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        this.httpServer.start();
        gson = new Gson();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static Gson getGson() {
        return gson;
    }
}
